package com.sandbox.aurora

import cats.effect.{IO, IOApp}

import com.comcast.ip4s.*
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.Logger

import com.sandbox.aurora.store.AuroraLedger
import com.sandbox.aurora.http.AuroraHttp

object Main extends IOApp.Simple:

  override def run: IO[Unit] =
    AuroraLedger.bloom[IO].flatMap { ledger =>
      val app = Logger.httpApp[IO](logHeaders = false, logBody = false)(
        AuroraHttp.router(ledger).orNotFound,
      )
      IO.println("Aurora Atelier • http://127.0.0.1:8088 • /health") *>
        EmberServerBuilder
          .default[IO]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8088")
          .withHttpApp(app)
          .build
          .use(_ => IO.never)
    }
