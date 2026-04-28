from __future__ import annotations

from sqlalchemy import func, select
from sqlalchemy.orm import Session, selectinload

from pulseboard.db.models import AuditAction, Tag, Task, TaskStatus
from pulseboard.services.audit import record_audit


class TaskService:
    def __init__(self, db: Session):
        self.db = db

    def get_task(self, task_id: int, *, load_relations: bool = True) -> Task | None:
        stmt = select(Task).where(Task.id == task_id)
        if load_relations:
            stmt = stmt.options(selectinload(Task.tags), selectinload(Task.owner))
        return self.db.scalar(stmt)

    def list_tasks(
        self,
        *,
        skip: int = 0,
        limit: int = 50,
        owner_id: int | None = None,
        status: TaskStatus | None = None,
        tag: str | None = None,
        q: str | None = None,
    ) -> list[Task]:
        stmt = select(Task).options(selectinload(Task.tags), selectinload(Task.owner))
        if owner_id is not None:
            stmt = stmt.where(Task.owner_id == owner_id)
        if status is not None:
            stmt = stmt.where(Task.status == status)
        if q:
            stmt = stmt.where(Task.title.ilike(f"%{q}%"))
        if tag:
            stmt = stmt.join(Task.tags).where(func.lower(Tag.name) == tag.strip().lower())
        stmt = stmt.order_by(Task.created_at.desc()).offset(skip).limit(limit)
        return list(self.db.scalars(stmt).unique().all())

    def create_task(
        self,
        *,
        title: str,
        description: str | None,
        owner_id: int,
        status: TaskStatus = TaskStatus.TODO,
        due_at=None,
        tag_names: list[str] | None = None,
    ) -> Task:
        task = Task(
            title=title.strip(),
            description=description,
            owner_id=owner_id,
            status=status,
            due_at=due_at,
        )
        if tag_names:
            task.tags = self._resolve_tags(tag_names)
        self.db.add(task)
        self.db.flush()
        record_audit(
            self.db,
            entity_type="task",
            entity_id=task.id,
            action=AuditAction.CREATE,
            detail=title,
        )
        return task

    def update_task(
        self,
        task_id: int,
        *,
        title: str | None = None,
        description: str | None = None,
        status: TaskStatus | None = None,
        owner_id: int | None = None,
        due_at=None,
        tag_names: list[str] | None = None,
    ) -> Task | None:
        task = self.get_task(task_id)
        if task is None:
            return None
        if title is not None:
            task.title = title.strip()
        if description is not None:
            task.description = description
        if status is not None:
            task.status = status
        if owner_id is not None:
            task.owner_id = owner_id
        if due_at is not None:
            task.due_at = due_at
        if tag_names is not None:
            task.tags = self._resolve_tags(tag_names)
        self.db.flush()
        record_audit(
            self.db,
            entity_type="task",
            entity_id=task.id,
            action=AuditAction.UPDATE,
            detail=title or "",
        )
        return task

    def delete_task(self, task_id: int) -> bool:
        task = self.get_task(task_id, load_relations=False)
        if task is None:
            return False
        record_audit(
            self.db,
            entity_type="task",
            entity_id=task_id,
            action=AuditAction.DELETE,
            detail=task.title,
        )
        self.db.delete(task)
        return True

    def _resolve_tags(self, names: list[str]) -> list[Tag]:
        resolved: list[Tag] = []
        for raw in names:
            name = raw.strip().lower()
            if not name:
                continue
            tag = self.db.scalar(select(Tag).where(func.lower(Tag.name) == name))
            if tag is None:
                tag = Tag(name=name)
                self.db.add(tag)
                self.db.flush()
            resolved.append(tag)
        return resolved
