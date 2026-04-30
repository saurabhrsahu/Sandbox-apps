# Aurora Atelier

A **Scala 3** sandbox: a “luminous retail” API with curated artisans, halo SKUs, palette capture, bundles, and panorama reports. Layered as **domain → store → http** for teaching or as a seed for larger services.

## Stack

- Scala 3.4 · Cats Effect 3 · http4s (Ember) · Circe  
- In-memory `Ref` ledger with rich seed data  

## Run

JDK 17+ and sbt required.

```powershell
cd Scala/aurora
sbt compile
sbt run
```

Listens on **http://127.0.0.1:8088/**. If sbt reports a server lock, use `sbt --no-server run`.

### Sample calls

```powershell
curl -s http://127.0.0.1:8088/health

curl -s http://127.0.0.1:8088/api/v1/artisans

curl -s "http://127.0.0.1:8088/api/v1/catalog?lineage=metallurgy"

curl -s -X POST http://127.0.0.1:8088/api/v1/bundles/weave -H "Content-Type: application/json" -d "{\"tags\":[\"metallurgy\",\"luminary\"]}"

curl -s -X POST http://127.0.0.1:8088/api/v1/palettes -H "Content-Type: application/json" -d "{\"lines\":[{\"skuId\":9001,\"quantity\":1}]}"
```

## Source map

- `domain/` — lineages, opaque ids, money (minor USD), errors  
- `store/` — halo budget, catalog search, palettes, bundle weaving  
- `http/` — JSON routes  
- `Seed.scala` — artisans + SKUs  
- `Main.scala` — Ember + logger  

Designed to sit beside PulseBoard (`Python/`) and Nexus/Bloom (`Javascript/`) as a narrative-heavy, non-trivial Scala slice.
