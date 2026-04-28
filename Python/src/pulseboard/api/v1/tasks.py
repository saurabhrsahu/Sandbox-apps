from __future__ import annotations

from fastapi import APIRouter, Depends, HTTPException, Query, status
from sqlalchemy.orm import Session

from pulseboard.api.deps import get_db
from pulseboard.db.models import TaskStatus
from pulseboard.schemas.task import TaskCreate, TaskOut, TaskUpdate
from pulseboard.services.task_service import TaskService
from pulseboard.services.user_service import UserService

router = APIRouter(prefix="/tasks", tags=["Tasks"])


@router.get("", response_model=list[TaskOut])
def list_tasks(
    db: Session = Depends(get_db),
    skip: int = Query(default=0, ge=0),
    limit: int = Query(default=50, ge=1, le=200),
    owner_id: int | None = Query(default=None, ge=1),
    status_filter: TaskStatus | None = Query(default=None, alias="status"),
    tag: str | None = Query(default=None, max_length=80),
    q: str | None = Query(default=None, max_length=200),
) -> list[TaskOut]:
    svc = TaskService(db)
    tasks = svc.list_tasks(
        skip=skip,
        limit=limit,
        owner_id=owner_id,
        status=status_filter,
        tag=tag,
        q=q,
    )
    return [TaskOut.model_validate(t) for t in tasks]


@router.post("", response_model=TaskOut, status_code=status.HTTP_201_CREATED)
def create_task(payload: TaskCreate, db: Session = Depends(get_db)) -> TaskOut:
    users = UserService(db)
    if users.get_user(payload.owner_id) is None:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Owner does not exist")
    svc = TaskService(db)
    task = svc.create_task(
        title=payload.title,
        description=payload.description,
        owner_id=payload.owner_id,
        status=payload.status,
        due_at=payload.due_at,
        tag_names=payload.tags,
    )
    task = svc.get_task(task.id)  # reload with relations
    return TaskOut.model_validate(task)


@router.get("/{task_id}", response_model=TaskOut)
def get_task(task_id: int, db: Session = Depends(get_db)) -> TaskOut:
    svc = TaskService(db)
    task = svc.get_task(task_id)
    if task is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Task not found")
    return TaskOut.model_validate(task)


@router.patch("/{task_id}", response_model=TaskOut)
def update_task(task_id: int, payload: TaskUpdate, db: Session = Depends(get_db)) -> TaskOut:
    users = UserService(db)
    if payload.owner_id is not None and users.get_user(payload.owner_id) is None:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Owner does not exist")
    svc = TaskService(db)
    task = svc.update_task(
        task_id,
        title=payload.title,
        description=payload.description,
        status=payload.status,
        owner_id=payload.owner_id,
        due_at=payload.due_at,
        tag_names=payload.tags,
    )
    if task is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Task not found")
    task = svc.get_task(task_id)
    return TaskOut.model_validate(task)


@router.delete("/{task_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_task(task_id: int, db: Session = Depends(get_db)) -> None:
    svc = TaskService(db)
    if not svc.delete_task(task_id):
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Task not found")
