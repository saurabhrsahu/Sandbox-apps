from __future__ import annotations

import pytest
from fastapi.testclient import TestClient

from pulseboard.config import Settings
from pulseboard.main import create_app


@pytest.fixture()
def client(tmp_path) -> TestClient:
    db_path = tmp_path / "test.db"
    settings = Settings(database_url=f"sqlite:///{db_path.as_posix()}")
    app = create_app(settings)
    with TestClient(app) as test_client:
        yield test_client
