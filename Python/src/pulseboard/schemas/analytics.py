from __future__ import annotations

from pydantic import BaseModel, ConfigDict


class AnalyticsSummaryOut(BaseModel):
    model_config = ConfigDict(extra="forbid")

    users_total: int
    tasks_total: int
    tasks_by_status: dict[str, int]
    tags_total: int
    audit_events_total: int


class OwnerWorkloadOut(BaseModel):
    model_config = ConfigDict(extra="forbid")

    owner_name: str
    task_count: int
