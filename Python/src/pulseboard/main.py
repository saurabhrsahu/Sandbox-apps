from __future__ import annotations

from contextlib import asynccontextmanager
from datetime import UTC, datetime
from typing import Any

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field
from sqlalchemy import create_engine
from sqlalchemy.engine import make_url
from sqlalchemy.orm import sessionmaker

from pulseboard import __version__
from pulseboard.api.v1.router import api_router
from pulseboard.config import Settings, get_settings
from pulseboard.db.base import Base
from pulseboard.db.seed import seed_if_empty


class HealthResponse(BaseModel):
    status: str = Field(description="Service health status")
    timestamp: str = Field(description="UTC ISO-8601 timestamp")


class InfoResponse(BaseModel):
    name: str
    version: str
    environment: str
    docs: str
    api: str


def _ensure_sqlite_parent_dir(url: str) -> None:
    try:
        parsed = make_url(url)
    except Exception:
        return
    if parsed.get_dialect().name != "sqlite":
        return
    database = parsed.database
    if not database or database == ":memory:":
        return
    from pathlib import Path

    Path(database).expanduser().resolve().parent.mkdir(parents=True, exist_ok=True)


def create_app(settings: Settings | None = None) -> FastAPI:
    settings = settings or get_settings()
    _ensure_sqlite_parent_dir(settings.database_url)

    connect_args: dict[str, Any] = {}
    if settings.database_url.startswith("sqlite"):
        connect_args["check_same_thread"] = False

    engine = create_engine(
        settings.database_url,
        pool_pre_ping=True,
        connect_args=connect_args,
        future=True,
    )
    session_factory = sessionmaker(
        bind=engine,
        autoflush=False,
        autocommit=False,
        expire_on_commit=False,
        future=True,
    )

    @asynccontextmanager
    async def lifespan(_app: FastAPI):
        Base.metadata.create_all(bind=engine)
        db = session_factory()
        try:
            seed_if_empty(db)
            db.commit()
        finally:
            db.close()
        yield

    app = FastAPI(
        title=settings.app_name,
        description=(
            "Multi-module REST API with SQLAlchemy, audit logging, analytics, and CSV export. "
            "Built as a larger sandbox app for CI/CD and containerization practice."
        ),
        version=__version__,
        lifespan=lifespan,
        docs_url="/docs",
        redoc_url="/redoc",
    )

    app.state.settings = settings
    app.state.engine = engine
    app.state.session_factory = session_factory

    app.add_middleware(
        CORSMiddleware,
        allow_origins=settings.cors_origins,
        allow_credentials=True,
        allow_methods=["*"],
        allow_headers=["*"],
    )

    @app.get("/", response_model=dict[str, Any], tags=["Meta"])
    def root() -> dict[str, Any]:
        return {
            "service": settings.app_name,
            "version": __version__,
            "message": f"Running. API at {settings.api_v1_prefix}. Open /docs.",
            "api_version": "v1",
        }

    @app.get("/health", response_model=HealthResponse, tags=["Meta"])
    def health() -> HealthResponse:
        return HealthResponse(
            status="ok",
            timestamp=datetime.now(UTC).isoformat().replace("+00:00", "Z"),
        )

    @app.get("/api/info", response_model=InfoResponse, tags=["Meta"])
    def info() -> InfoResponse:
        return InfoResponse(
            name=settings.app_name,
            version=__version__,
            environment=settings.app_env,
            docs="/docs",
            api=settings.api_v1_prefix,
        )

    app.include_router(api_router, prefix=settings.api_v1_prefix)
    return app
