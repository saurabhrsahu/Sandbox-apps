package com.sandbox.helix.store

import scala.annotation.tailrec

import cats.effect.Concurrent
import cats.effect.Ref
import cats.syntax.all.*

import com.sandbox.helix.Seed
import com.sandbox.helix.domain.*

private case class BoardSnapshot(
    workers: Map[WorkerId, WorkerNode],
    jobs: Map[JobId, JobRecord],
    nextJobSeq: Long,
)

object BoardSnapshot:
  val boot: BoardSnapshot =
    BoardSnapshot(Seed.roster.map(w => w.id -> w).toMap, Map.empty, 77_100L)

private object DispatchLogic:

  def pick(candidates: Iterable[WorkerNode], job: JobRecord): Option[WorkerNode] =
    candidates
      .filter(w => w.headroom > 0 && w.satisfies(job.requiredTags))
      .toVector
      .sortBy(w => (w.inFlight, w.id.underlying))
      .headOption

  @tailrec
  def drain(board: BoardSnapshot, moved: Int = 0): (BoardSnapshot, Int) =
    val queue =
      board.jobs.values
        .filter(_.status == JobStatus.Queued)
        .toVector
        .sortBy(j => (-j.priority, j.id.underlying))
    if queue.isEmpty then (board, moved)
    else
      val assignment =
        queue.foldLeft[Option[(JobRecord, WorkerNode)]](None) { (acc, job) =>
          acc.orElse(pick(board.workers.values, job).map(w => job -> w))
        }
      assignment match
        case None =>
          (board, moved)
        case Some((job, worker)) =>
          val w1 = worker.copy(inFlight = worker.inFlight + 1)
          val j1 = job.copy(status = JobStatus.Running, pinnedWorkerId = Some(worker.id))
          val next = board.copy(
            workers = board.workers + (worker.id -> w1),
            jobs = board.jobs + (job.id -> j1),
          )
          drain(next, moved + 1)

final class HelixBoard[F[_]](private val cell: Ref[F, BoardSnapshot])(using Concurrent[F]):

  def workers: F[Vector[WorkerNode]] =
    cell.get.map(_.workers.values.toVector.sortBy(_.id.underlying))

  def jobs: F[Vector[JobRecord]] =
    cell.get.map(_.jobs.values.toVector.sortBy(_.id.underlying))

  def job(jobId: Long): F[Option[JobRecord]] =
    cell.get.map(_.jobs.get(JobId(jobId)))

  def radarOf(s: BoardSnapshot): HelixRadar =
    HelixRadar(
      jobsQueued = s.jobs.values.count(_.status == JobStatus.Queued),
      jobsRunning = s.jobs.values.count(_.status == JobStatus.Running),
      jobsCompleted = s.jobs.values.count(_.status == JobStatus.Completed),
      workersIdle = s.workers.values.count(_.headroom > 0),
      workersBusy = s.workers.values.count(_.inFlight > 0),
    )

  def radar: F[HelixRadar] =
    cell.get.map(radarOf)

  def submit(body: SubmitJob): F[JobRecord] =
    cell.modify: s =>
      val id = JobId(s.nextJobSeq)
      val tags =
        body.requiredTags.map(_.trim.toLowerCase).filter(_.nonEmpty).distinct
      val rec =
        JobRecord(id, body.title.trim, body.priority, tags, JobStatus.Queued, None)
      (
        s.copy(jobs = s.jobs + (id -> rec), nextJobSeq = id.underlying + 1),
        rec,
      )

  def sweepDispatch: F[DispatchSweep] =
    cell.modify: s =>
      val (next, moved) = DispatchLogic.drain(s)
      (next, DispatchSweep(moved, radarOf(next)))

  /** Mark running job complete and free worker capacity. */
  def complete(jobId: Long): F[Either[HelixError, JobRecord]] =
    cell.modify { s =>
      val jid = JobId(jobId)
      s.jobs.get(jid) match
        case None =>
          (s, Left(HelixError.UnknownJob(jobId)))
        case Some(job) if job.status != JobStatus.Running =>
          (
            s,
            Left(
              HelixError.IllegalTransition(
                "only running jobs finalize — sweep queue first.",
              ),
            ),
          )
        case Some(job) =>
          job.pinnedWorkerId match
            case None =>
              (
                s,
                Left(
                  HelixError.IllegalTransition("job missing worker pin — inconsistent board."),
                ),
              )
            case Some(wid) =>
              s.workers.get(wid) match
                case None =>
                  (s, Left(HelixError.UnknownWorker(wid.underlying)))
                case Some(worker) =>
                  val w2 = worker.copy(inFlight = math.max(0, worker.inFlight - 1))
                  val j2 = job.copy(status = JobStatus.Completed)
                  val next = s.copy(
                    workers = s.workers + (wid -> w2),
                    jobs = s.jobs + (jid -> j2),
                  )
                  (next, Right(j2))
    }

object HelixBoard:

  def spawn[F[_]: Concurrent]: F[HelixBoard[F]] =
    Ref.of(BoardSnapshot.boot).map(HelixBoard(_))
