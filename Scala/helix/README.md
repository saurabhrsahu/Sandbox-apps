# Helix Dispatch

Second **Scala 3** sandbox: tagged **workers**, **priority jobs**, and explicit **dispatch sweeps** on an in-memory board (`Ref`). Complements **Aurora** (retail ledger) with routing / capacity mechanics.

## Stack

Cats Effect 3 · http4s Ember · Circe · logback (same versions as `Scala/aurora/build.sbt`).

## Run

```powershell
cd Scala/helix
sbt --no-server run
```

Listens on **http://127.0.0.1:8090/** (Aurora uses 8088).

## API sketch

| Method | Path | Purpose |
|--------|------|---------|
| GET | `/api/v1/workers` | seeded fleet + headroom |
| GET | `/api/v1/jobs` | all jobs |
| GET | `/api/v1/jobs/{id}` | one job |
| POST | `/api/v1/jobs` | body `title`, `priority`, `requiredTags` → queued |
| POST | `/api/v1/dispatch/sweep` | greedy assign by priority / tags / headroom |
| POST | `/api/v1/jobs/{id}/complete` | finish running job, free worker slot |
| GET | `/api/v1/radar` | queue / running / completed counts |

## Example

```powershell
curl -s http://127.0.0.1:8090/api/v1/workers

curl -s -X POST http://127.0.0.1:8090/api/v1/jobs -H "Content-Type: application/json" -d "{\"title\":\"infer\",\"priority\":40,\"requiredTags\":[\"gpu\"]}"
curl -s -X POST http://127.0.0.1:8090/api/v1/dispatch/sweep
curl -s http://127.0.0.1:8090/api/v1/jobs
```
