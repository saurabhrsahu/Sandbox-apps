from __future__ import annotations

from fastapi import APIRouter

from pulseboard.api.v1 import analytics, reports, tags, tasks, users

api_router = APIRouter()
api_router.include_router(users.router)
api_router.include_router(tasks.router)
api_router.include_router(tags.router)
api_router.include_router(analytics.router)
api_router.include_router(reports.router)
