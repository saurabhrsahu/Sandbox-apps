from __future__ import annotations

from datetime import UTC, datetime, timedelta

from sqlalchemy import select
from sqlalchemy.orm import Session

from pulseboard.db.models import Tag, Task, TaskStatus, User


def seed_if_empty(db: Session) -> None:
    if db.scalar(select(User).limit(1)) is not None:
        return

    u1 = User(
        email="alice@example.com",
        full_name="Alice Engineer",
        is_active=True,
    )
    u2 = User(
        email="bob@example.com",
        full_name="Bob Ops",
        is_active=True,
    )
    db.add_all([u1, u2])
    db.flush()

    tags = {
        "backend": Tag(name="backend"),
        "frontend": Tag(name="frontend"),
        "devops": Tag(name="devops"),
        "docs": Tag(name="docs"),
    }
    db.add_all(tags.values())
    db.flush()

    soon = datetime.now(UTC) + timedelta(days=3)

    tasks = [
        Task(
            title="Harden CI pipeline",
            description="Add lint, typecheck, and integration test job matrix.",
            status=TaskStatus.IN_PROGRESS,
            owner_id=u1.id,
            due_at=soon,
            tags=[tags["devops"], tags["backend"]],
        ),
        Task(
            title="Ship OpenAPI client SDK",
            description="Generate TypeScript client and publish internally.",
            status=TaskStatus.TODO,
            owner_id=u1.id,
            tags=[tags["frontend"], tags["backend"]],
        ),
        Task(
            title="Write runbooks",
            description="Deployment, rollback, and on-call basics.",
            status=TaskStatus.TODO,
            owner_id=u2.id,
            tags=[tags["docs"], tags["devops"]],
        ),
    ]
    db.add_all(tasks)
    # commit happens in lifespan caller
