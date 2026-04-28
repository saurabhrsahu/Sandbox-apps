from __future__ import annotations

from sqlalchemy import select
from sqlalchemy.orm import Session

from pulseboard.db.models import AuditAction, User
from pulseboard.services.audit import record_audit


class UserService:
    def __init__(self, db: Session):
        self.db = db

    def list_users(self, *, skip: int = 0, limit: int = 50, active_only: bool = False) -> list[User]:
        stmt = select(User).order_by(User.created_at.desc()).offset(skip).limit(limit)
        if active_only:
            stmt = stmt.where(User.is_active.is_(True))
        return list(self.db.scalars(stmt).unique().all())

    def get_user(self, user_id: int) -> User | None:
        return self.db.get(User, user_id)

    def get_by_email(self, email: str) -> User | None:
        return self.db.scalar(select(User).where(User.email == email.lower().strip()))

    def create_user(self, *, email: str, full_name: str, is_active: bool = True) -> User:
        user = User(email=email.lower().strip(), full_name=full_name.strip(), is_active=is_active)
        self.db.add(user)
        self.db.flush()
        record_audit(
            self.db,
            entity_type="user",
            entity_id=user.id,
            action=AuditAction.CREATE,
            detail=email,
        )
        return user

    def update_user(
        self,
        user_id: int,
        *,
        full_name: str | None = None,
        is_active: bool | None = None,
    ) -> User | None:
        user = self.get_user(user_id)
        if user is None:
            return None
        if full_name is not None:
            user.full_name = full_name.strip()
        if is_active is not None:
            user.is_active = is_active
        self.db.flush()
        record_audit(
            self.db,
            entity_type="user",
            entity_id=user.id,
            action=AuditAction.UPDATE,
            detail=full_name,
        )
        return user

    def delete_user(self, user_id: int) -> bool:
        user = self.get_user(user_id)
        if user is None:
            return False
        record_audit(
            self.db,
            entity_type="user",
            entity_id=user_id,
            action=AuditAction.DELETE,
            detail=user.email,
        )
        self.db.delete(user)
        return True
