from __future__ import annotations

from sqlalchemy import func, select
from sqlalchemy.orm import Session

from pulseboard.db.models import AuditLog, Tag, Task, TaskStatus, User


def dashboard_summary(db: Session) -> dict:
    users_total = db.scalar(select(func.count()).select_from(User)) or 0
    tasks_total = db.scalar(select(func.count()).select_from(Task)) or 0

    by_status: dict[str, int] = {}
    for status in TaskStatus:
        c = db.scalar(select(func.count()).select_from(Task).where(Task.status == status)) or 0
        by_status[status.value] = int(c)

    tags_total = db.scalar(select(func.count()).select_from(Tag)) or 0
    audit_recent = db.scalar(select(func.count()).select_from(AuditLog)) or 0

    return {
        "users_total": int(users_total),
        "tasks_total": int(tasks_total),
        "tasks_by_status": by_status,
        "tags_total": int(tags_total),
        "audit_events_total": int(audit_recent),
    }


def tasks_by_owner(db: Session, limit: int = 20) -> list[dict]:
    stmt = (
        select(User.full_name, func.count(Task.id))
        .join(Task, Task.owner_id == User.id, isouter=True)
        .group_by(User.id, User.full_name)
        .order_by(func.count(Task.id).desc())
        .limit(limit)
    )
    rows = db.execute(stmt).all()
    return [{"owner_name": name, "task_count": int(count)} for name, count in rows]
