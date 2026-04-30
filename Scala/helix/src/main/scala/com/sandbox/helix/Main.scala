package com.sandbox.helix

import cats.effect.{IO, IOApp}

import com.comcast.ip4s.*
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.Logger

import com.sandbox.helix.store.HelixBoard
import com.sandbox.helix.http.HelixHttp

object Main extends IOApp.Simple:

  override def run: IO[Unit] =
    HelixBoard.spawn[IO].flatMap { board =>
      val routes = HelixHttp.router(board).orNotFound
      val app = Logger.httpApp[IO](false, false)(routes)
      IO.println("Helix Dispatch • http://127.0.0.1:8090 • /health") *>
        EmberServerBuilder
          .default[IO]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8090")
          .withHttpApp(app)
          .build
          .use(_ => IO.never)
    }
