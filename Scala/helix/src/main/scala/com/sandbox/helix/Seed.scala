package com.sandbox.helix

import com.sandbox.helix.domain.*

object Seed:

  val roster: Vector[WorkerNode] = Vector(
    WorkerNode(WorkerId(101L), "atlas-east-1", List("cpu", "batch", "io"), maxConcurrent = 4, inFlight = 0),
    WorkerNode(WorkerId(102L), "atlas-west-2", List("gpu", "cpu", "stream"), maxConcurrent = 2, inFlight = 0),
    WorkerNode(WorkerId(103L), "nova-edge-7", List("io", "latency", "batch"), maxConcurrent = 6, inFlight = 0),
    WorkerNode(WorkerId(104L), "polar-batch", List("batch", "cold", "archive"), maxConcurrent = 8, inFlight = 0),
  )
