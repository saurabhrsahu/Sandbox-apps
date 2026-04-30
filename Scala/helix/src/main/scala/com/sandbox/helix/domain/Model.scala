package com.sandbox.helix.domain

import io.circe.*

opaque type WorkerId = Long

object WorkerId:
  def apply(v: Long): WorkerId = v
  extension (w: WorkerId) def underlying: Long = w
  given Codec[WorkerId] =
    Codec.from(Decoder.decodeLong.map(apply(_)), Encoder.encodeLong.contramap(_.underlying))

opaque type JobId = Long

object JobId:
  def apply(v: Long): JobId = v
  extension (j: JobId) def underlying: Long = j
  given Codec[JobId] =
    Codec.from(Decoder.decodeLong.map(apply(_)), Encoder.encodeLong.contramap(_.underlying))

enum JobStatus derives Codec.AsObject:
  case Queued, Running, Completed, Failed

final case class WorkerNode(
    id: WorkerId,
    hostname: String,
    tags: List[String],
    maxConcurrent: Int,
    /** Active jobs pinned to this node */
    inFlight: Int,
) derives Codec.AsObject:

  val headroom: Int = math.max(0, maxConcurrent - inFlight)
  val tagSet: Set[String] = tags.map(_.trim.toLowerCase).toSet

  def satisfies(required: Iterable[String]): Boolean =
    val need = required.map(_.trim.toLowerCase).toSet
    need.subsetOf(tagSet)

final case class JobRecord(
    id: JobId,
    title: String,
    priority: Int,
    requiredTags: List[String],
    status: JobStatus,
    pinnedWorkerId: Option[WorkerId],
) derives Codec.AsObject

final case class SubmitJob(
    title: String,
    priority: Int,
    requiredTags: List[String],
) derives Codec.AsObject

final case class HelixRadar(
    jobsQueued: Int,
    jobsRunning: Int,
    jobsCompleted: Int,
    workersIdle: Int,
    workersBusy: Int,
) derives Codec.AsObject

final case class DispatchSweep(moved: Int, snapshot: HelixRadar) derives Codec.AsObject
