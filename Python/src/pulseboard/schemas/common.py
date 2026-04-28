from __future__ import annotations

from pydantic import BaseModel, ConfigDict, Field


class PaginationParams(BaseModel):
    model_config = ConfigDict(extra="forbid")

    skip: int = Field(default=0, ge=0, le=1_000_000)
    limit: int = Field(default=50, ge=1, le=200)
