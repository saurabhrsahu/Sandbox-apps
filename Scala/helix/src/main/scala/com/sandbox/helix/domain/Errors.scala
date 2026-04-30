package com.sandbox.helix.domain

sealed trait HelixError:
  def code: String
  def detail: String

object HelixError:
  final case class UnknownJob(jobId: Long) extends HelixError:
    val code = "not_found"
    def detail = s"No job keyed $jobId."

  final case class UnknownWorker(workerId: Long) extends HelixError:
    val code = "not_found"
    def detail = s"No worker keyed $workerId."

  final case class IllegalTransition(what: String) extends HelixError:
    val code = "transition"
    def detail = what

  final case class NoCapacity(hint: String) extends HelixError:
    val code = "capacity"
    def detail = hint

  final case class MalformedBody(reason: String) extends HelixError:
    val code = "bad_request"
    def detail = reason
