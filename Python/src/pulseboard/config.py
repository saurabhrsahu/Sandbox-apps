from __future__ import annotations

import os
from functools import lru_cache

from pydantic import AliasChoices, Field
from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    model_config = SettingsConfigDict(
        env_prefix="PULSEBOARD_",
        env_file=".env",
        extra="ignore",
    )

    app_name: str = Field(default="PulseBoard API")
    app_env: str = Field(
        default="development",
        validation_alias=AliasChoices("PULSEBOARD_APP_ENV", "APP_ENV"),
    )
    database_url: str = Field(
        default_factory=lambda: os.environ.get(
            "DATABASE_URL",
            "sqlite:///./data/pulseboard.db",
        )
    )
    api_v1_prefix: str = "/api/v1"
    cors_origins: list[str] = Field(default_factory=lambda: ["*"])


@lru_cache
def get_settings() -> Settings:
    return Settings()
