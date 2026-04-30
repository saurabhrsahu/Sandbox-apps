package com.sandbox.helix.http

import cats.effect.IO

import io.circe.Json
import io.circe.syntax.*
import org.http4s.*
import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.dsl.io.*

import com.sandbox.helix.domain.*
import com.sandbox.helix.store.HelixBoard

object HelixHttp:

  def router(board: HelixBoard[IO]): HttpRoutes[IO] = HttpRoutes.of[IO] {

    case GET -> Root =>
      Ok(Json.obj(
        "service"     -> Json.fromString("helix-dispatch"),
        "version"     -> Json.fromString("1.0"),
        "summary"     -> Json.fromString("priority job queue · worker tags · in-memory board"),
        "defaultPort" -> Json.fromInt(8090),
      ))

    case GET -> Root / "health" =>
      Ok(Json.obj("status" -> Json.fromString("ok")))

    case GET -> Root / "api" / "v1" / "workers" =>
      board.workers.flatMap(w => Ok(w.asJson))

    case GET -> Root / "api" / "v1" / "jobs" =>
      board.jobs.flatMap(js => Ok(js.asJson))

    case GET -> Root / "api" / "v1" / "jobs" / rawId =>
      rawId.toLongOption match
        case None =>
          BadRequest(Json.obj("detail" -> Json.fromString("job id must be numeric")))
        case Some(jobId) =>
          board.job(jobId).flatMap {
            case None    => NotFound()
            case Some(j) => Ok(j.asJson)
          }

    case GET -> Root / "api" / "v1" / "radar" =>
      board.radar.flatMap(r => Ok(r.asJson))

    case req @ POST -> Root / "api" / "v1" / "jobs" =>
      req.as[SubmitJob].flatMap(board.submit).flatMap(rec => Created(rec.asJson))

    case POST -> Root / "api" / "v1" / "dispatch" / "sweep" =>
      board.sweepDispatch.flatMap(s => Ok(s.asJson))

    case POST -> Root / "api" / "v1" / "jobs" / rawId / "complete" =>
      rawId.toLongOption match
        case None =>
          BadRequest(Json.obj("detail" -> Json.fromString("job id must be numeric")))
        case Some(jobId) =>
          board.complete(jobId).flatMap {
            case Left(err) if err.code == "not_found" =>
              NotFound()
            case Left(err) =>
              BadRequest(
                Json.obj("code" -> Json.fromString(err.code), "detail" -> Json.fromString(err.detail)),
              )
            case Right(rec) =>
              Ok(rec.asJson)
          }
  }
