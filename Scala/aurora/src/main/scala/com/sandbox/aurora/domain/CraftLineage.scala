package com.sandbox.aurora.domain

import io.circe.Codec

/** Material families surfaced in palettes and curator scoring. */
enum CraftLineage(val tag: String, val luminousWeight: Double):
  case Metallurgy extends CraftLineage("metallurgy", 0.32)
  case Horology extends CraftLineage("horology", 0.28)
  case Ceramics extends CraftLineage("ceramics", 0.2)
  case Luminary extends CraftLineage("luminary", 0.2)
  case Composite extends CraftLineage("composite", 1.0)

object CraftLineage:
  val all: IndexedSeq[CraftLineage] = values.toIndexedSeq

  private val byTag: Map[String, CraftLineage] =
    all.iterator.map(l => l.tag -> l).toMap

  given Codec[CraftLineage] =
    Codec.from(
      io.circe.Decoder.decodeString.emap(s =>
        byTag.get(s).toRight(s"unknown craft lineage: $s")),
      io.circe.Encoder.encodeString.contramap(_.tag),
    )

  /** Weighted centroid of lineage weights. */
  def blend(weights: Iterable[(CraftLineage, Double)]): Double =
    val ws = weights.filter(_._2 > 0).toSeq
    if ws.isEmpty then 0d
    else
      val denom = ws.map(_._2).sum.max(1e-6)
      ws.map { case (l, w) => l.luminousWeight * w }.sum / denom

/** Fulfillment choreography for ledger narration */
enum CommissionPulse derives Codec:
  case Conceived, ForgingLoom, AureoleReview, ShipsToParlor
