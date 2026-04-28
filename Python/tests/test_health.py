from __future__ import annotations

from fastapi.testclient import TestClient


def test_health(client: TestClient) -> None:
    response = client.get("/health")
    assert response.status_code == 200
    data = response.json()
    assert data["status"] == "ok"
    assert "timestamp" in data


def test_api_info(client: TestClient) -> None:
    response = client.get("/api/info")
    assert response.status_code == 200
    data = response.json()
    assert data["api"] == "/api/v1"
