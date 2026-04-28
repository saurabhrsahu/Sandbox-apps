from __future__ import annotations

from fastapi.testclient import TestClient


def test_seed_users_and_create_task(client: TestClient) -> None:
    users_response = client.get("/api/v1/users")
    assert users_response.status_code == 200
    users = users_response.json()
    assert len(users) >= 1
    user_id = users[0]["id"]

    task_payload = {
        "title": "Integration test task",
        "description": "Created by pytest",
        "owner_id": user_id,
        "status": "todo",
        "tags": ["qa"],
    }
    task_response = client.post("/api/v1/tasks", json=task_payload)
    assert task_response.status_code == 201
    task = task_response.json()
    assert task["title"] == task_payload["title"]
    assert any(t["name"] == "qa" for t in task["tags"])


def test_analytics_summary(client: TestClient) -> None:
    response = client.get("/api/v1/analytics/summary")
    assert response.status_code == 200
    data = response.json()
    assert "tasks_by_status" in data
    assert data["users_total"] >= 1
