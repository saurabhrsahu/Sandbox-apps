package com.sandbox.aurora.http

import cats.effect.IO
import cats.syntax.all.*

import io.circe.Json
import io.circe.syntax.*
import org.http4s.*
import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.dsl.io.*
import org.http4s.dsl.io.OptionalQueryParamDecoderMatcher

import com.sandbox.aurora.domain.*
import com.sandbox.aurora.store.*

object AuroraHttp:

  private object LineageQ extends OptionalQueryParamDecoderMatcher[String]("lineage")
  private object ArtisanQ extends OptionalQueryParamDecoderMatcher[Long]("artisan")
  private object QueryQ extends OptionalQueryParamDecoderMatcher[String]("q")

  def router(led: AuroraLedger[IO]): HttpRoutes[IO] = HttpRoutes.of[IO] {

    case GET -> Root =>
      Ok(
        Json.obj(
          "service" -> Json.fromString("aurora-atelier"),
          "version" -> Json.fromString("1.0"),
          "summary" -> Json.fromString("luminous retail ledger sandbox — Scala 3 · http4s · Circe · Cats Effect"),
          "links" -> Json.obj(
            "catalog"  -> Json.fromString("/api/v1/catalog"),
            "artisans" -> Json.fromString("/api/v1/artisans"),
            "palette"  -> Json.fromString("POST /api/v1/palettes"),
          ),
        ),
      )

    case GET -> Root / "health" =>
      Ok(Json.obj("status" -> Json.fromString("ok")))

    case GET -> Root / "api" / "v1" / "halo" =>
      led.constellation.flatMap(obs => Ok(obs.asJson))

    case GET -> Root / "api" / "v1" / "artisans" =>
      led.artisansBeam.flatMap(a => Ok(a.asJson))

    case GET -> Root / "api" / "v1" / "reports" / "panorama" =>
      led.ledgerPanorama.flatMap(r => Ok(r.asJson))

    case GET -> Root / "api" / "v1" / "catalog"
        :? LineageQ(lineage)
        +& ArtisanQ(artisan)
        +& QueryQ(qText) =>
      led.haloSkies(lineage, qText, artisan).flatMap(rows => Ok(rows.asJson))

    case req @ POST -> Root / "api" / "v1" / "palettes" =>
      for
        palette <- req.as[PaletteProposal]
        result <- led.capturePalette(palette)
        resp <- result match
          case Left(err) =>
            BadRequest(Json.obj(
              "code"   -> Json.fromString(err.code),
              "detail" -> Json.fromString(err.detail),
            ))
          case Right(ticket) => Created(ticket.asJson)
      yield resp

    case req @ POST -> Root / "api" / "v1" / "bundles" / "weave" =>
      req.as[TagBundleQuery].flatMap { bundle =>
        val lineage =
          bundle.tags.distinct.flatMap(t => CraftLineage.all.find(_.tag == t.toLowerCase)).toSet
        led.bundleWeave(lineage).flatMap(b => Ok(b.asJson))
      }
  }
