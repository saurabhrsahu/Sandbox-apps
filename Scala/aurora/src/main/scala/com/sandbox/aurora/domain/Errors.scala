package com.sandbox.aurora.domain

sealed trait AuroraError:
  def code: String
  def detail: String
  override def toString = s"$code · $detail"

object AuroraError:
  sealed abstract class Ledger(val detail: String) extends AuroraError:
    override val code = "ledger"

  final case class UnknownArtisan(id: Long) extends Ledger(s"No artisan keyed $id in constellation.")
  final case class UnknownSku(id: Long) extends Ledger(s"SKU $id not anchored in halo catalog.")

  sealed abstract class Policy(val detail: String) extends AuroraError:
    override val code = "policy"

  final case class PaletteOverload(lines: Int) extends Policy(s"Palettes capped at sixteen lines — incoming $lines.")
  final case class HaloInsufficient(need: Double, have: Double) extends Policy(s"Halo budget $have below requisite $need.")
  final case class EmptyPalette() extends Policy("Palettes ship with ≥ one SKU.")

  final case class Malformed(reason: String) extends AuroraError:
    override val code = "bad_request"
    override val detail = reason
