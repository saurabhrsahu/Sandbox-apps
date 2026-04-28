from __future__ import annotations

import csv
import io

from fastapi import APIRouter, Depends, Response
from sqlalchemy import select
from sqlalchemy.orm import Session, selectinload

from pulseboard.api.deps import get_db
from pulseboard.db.models import Task

router = APIRouter(prefix="/reports", tags=["Reports"])


@router.get("/tasks.csv")
def tasks_csv(db: Session = Depends(get_db)) -> Response:
    stmt = (
        select(Task)
        .options(selectinload(Task.owner), selectinload(Task.tags))
        .order_by(Task.id.asc())
    )
    tasks = db.scalars(stmt).unique().all()

    buffer = io.StringIO()
    writer = csv.writer(buffer)
    writer.writerow(
        [
            "id",
            "title",
            "status",
            "owner_email",
            "owner_name",
            "tags",
            "due_at",
            "created_at",
        ],
    )
    for t in tasks:
        tag_names = ",".join(sorted(tag.name for tag in t.tags))
        writer.writerow(
            [
                t.id,
                t.title,
                t.status.value,
                t.owner.email if t.owner else "",
                t.owner.full_name if t.owner else "",
                tag_names,
                t.due_at.isoformat() if t.due_at else "",
                t.created_at.isoformat() if t.created_at else "",
            ],
        )

    return Response(
        content=buffer.getvalue(),
        media_type="text/csv; charset=utf-8",
        headers={"Content-Disposition": 'attachment; filename="tasks.csv"'},
    )
