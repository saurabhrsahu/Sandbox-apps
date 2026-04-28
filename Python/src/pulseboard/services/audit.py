from __future__ import annotations

from sqlalchemy.orm import Session

from pulseboard.db.models import AuditAction, AuditLog


def record_audit(
    db: Session,
    *,
    entity_type: str,
    entity_id: int,
    action: AuditAction,
    detail: str | None = None,
) -> AuditLog:
    row = AuditLog(
        entity_type=entity_type,
        entity_id=entity_id,
        action=action,
        detail=detail,
    )
    db.add(row)
    return row
