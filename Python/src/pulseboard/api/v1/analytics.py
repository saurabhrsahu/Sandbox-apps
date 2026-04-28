from __future__ import annotations

from fastapi import APIRouter, Depends, Query
from sqlalchemy.orm import Session

from pulseboard.api.deps import get_db
from pulseboard.schemas.analytics import AnalyticsSummaryOut, OwnerWorkloadOut
from pulseboard.services.analytics_service import dashboard_summary, tasks_by_owner

router = APIRouter(prefix="/analytics", tags=["Analytics"])


@router.get("/summary", response_model=AnalyticsSummaryOut)
def analytics_summary(db: Session = Depends(get_db)) -> AnalyticsSummaryOut:
    data = dashboard_summary(db)
    return AnalyticsSummaryOut(**data)


@router.get("/workload", response_model=list[OwnerWorkloadOut])
def analytics_workload(
    db: Session = Depends(get_db),
    limit: int = Query(default=20, ge=1, le=100),
) -> list[OwnerWorkloadOut]:
    rows = tasks_by_owner(db, limit=limit)
    return [OwnerWorkloadOut(**r) for r in rows]
