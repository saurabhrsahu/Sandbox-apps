from __future__ import annotations

from datetime import datetime

from pydantic import BaseModel, ConfigDict, Field

from pulseboard.db.models import TaskStatus
from pulseboard.schemas.tag import TagOut


class TaskCreate(BaseModel):
    model_config = ConfigDict(extra="forbid")

    title: str = Field(min_length=1, max_length=300)
    description: str | None = None
    owner_id: int = Field(ge=1)
    status: TaskStatus = TaskStatus.TODO
    due_at: datetime | None = None
    tags: list[str] = Field(default_factory=list)


class TaskUpdate(BaseModel):
    model_config = ConfigDict(extra="forbid")

    title: str | None = Field(default=None, min_length=1, max_length=300)
    description: str | None = None
    owner_id: int | None = Field(default=None, ge=1)
    status: TaskStatus | None = None
    due_at: datetime | None = None
    tags: list[str] | None = None


class TaskOut(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: int
    title: str
    description: str | None
    status: TaskStatus
    owner_id: int
    due_at: datetime | None
    created_at: datetime
    updated_at: datetime
    tags: list[TagOut] = Field(default_factory=list)
