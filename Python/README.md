# PulseBoard API (Python)

Larger **FastAPI + SQLAlchemy** sample service for **CI/CD**, **Docker**, and deployment practice. It includes multiple routers, a small domain model, audit logging, analytics endpoints, and CSV export.

## Features

- **REST API v1** under `/api/v1`
  - **Users**: CRUD
  - **Tasks**: CRUD, filters (`status`, `owner_id`, `tag`, search `q`)
  - **Tags**: list / create
  - **Analytics**: summary + per-owner workload
  - **Reports**: `GET /api/v1/reports/tasks.csv`
- **Meta**: `GET /`, `GET /health`, `GET /api/info`
- **OpenAPI**: `GET /docs`, `GET /redoc`
- **SQLite** by default (file under `./data/`), auto **seed data** on first boot
- **CORS** enabled (configurable)

## Requirements

- Python **3.10+** (3.12 recommended)

## Run locally

From this folder (`Python/`):

```powershell
python -m venv .venv
.\.venv\Scripts\Activate.ps1
pip install -r requirements.txt
$env:PYTHONPATH = "src"
$env:APP_ENV = "development"
uvicorn pulseboard.main:create_app --factory --reload --host 127.0.0.1 --port 8000
```

Then open `http://127.0.0.1:8000/docs`.

### Configuration

| Variable | Purpose |
|----------|---------|
| `DATABASE_URL` | SQLAlchemy URL (default `sqlite:///./data/pulseboard.db`) |
| `APP_ENV` | Shown in `/api/info` |
| `PULSEBOARD_*` | See `pulseboard/config.py` |

### Tests

```powershell
$env:PYTHONPATH = "src"
pytest
```

## Docker

```powershell
docker build -t pulseboard-api .
docker run --rm -p 8000:8000 -e APP_ENV=production pulseboard-api
```

## Layout

```text
src/pulseboard/
  api/           HTTP layer (v1 routers, dependency injection)
  db/            SQLAlchemy models, seed data
  schemas/       Pydantic request/response models
  services/      Domain services and audit helper
tests/           pytest + TestClient
requirements.txt
Dockerfile
```
