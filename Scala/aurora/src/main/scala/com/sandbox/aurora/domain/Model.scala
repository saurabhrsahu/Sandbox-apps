package com.sandbox.aurora.domain

import io.circe.*

opaque type ArtisanId = Long

object ArtisanId:
  def apply(raw: Long): ArtisanId = raw
  extension (aid: ArtisanId)
    def underlying: Long = aid
  given Codec[ArtisanId] =
    Codec.from(Decoder.decodeLong.map(apply), Encoder.encodeLong.contramap(_.underlying))

opaque type SkuId = Long

object SkuId:
  def apply(raw: Long): SkuId = raw
  extension (sid: SkuId)
    def underlying: Long = sid
  given Codec[SkuId] =
    Codec.from(Decoder.decodeLong.map(apply), Encoder.encodeLong.contramap(_.underlying))

opaque type MinorUsd <: Int = Int

object MinorUsd:
  def cents(n: Int): MinorUsd = math.max(n, 0)

  extension (m: MinorUsd)
    /** Underlying cents (minor units). */
    def raw: Int = m
    def toMajor: Double = raw.toDouble / 100d
    def +(o: MinorUsd): MinorUsd = m + o
    /** Line total at unit price × quantity */
    def *(qty: Int): MinorUsd = MinorUsd.cents(raw * math.max(qty, 0))

  given Codec[MinorUsd] =
    Codec.from(Decoder.decodeInt.map(cents), Encoder.encodeInt.contramap(_.raw))

final case class ArtisanPortrait(
    id: ArtisanId,
    displayName: String,
    city: String,
    mantra: String,
    spotlight: Double,
    lineages: List[CraftLineage],
) derives Codec.AsObject

final case class HaloSku(
    id: SkuId,
    artisanId: ArtisanId,
    title: String,
    vignette: String,
    luminousScore: Double,
    haloMinor: MinorUsd,
    facets: List[CraftLineage],
) derives Codec.AsObject

object HaloSku:
  extension (sku: HaloSku)
    /** Search bloom — luminous base plus lineage overlap */
    def auraRank(forTags: Set[CraftLineage]): Double =
      sku.luminousScore + sku.facets.view.toSet.intersect(forTags).size * 0.05

final case class PaletteLine(
    skuId: SkuId,
    quantity: Int,
) derives Codec.AsObject

final case class PaletteProposal(
    requestId: Option[String],
    lines: Vector[PaletteLine],
) derives Codec.AsObject

final case class CapturedLine(
    skuId: SkuId,
    quantity: Int,
    unitMinor: MinorUsd,
) derives Codec.AsObject

final case class CommissionTicket(
    id: Long,
    lines: Vector[CapturedLine],
    capturedMinorSum: MinorUsd,
    luminousBlend: Double,
    pulse: CommissionPulse,
) derives Codec.AsObject

final case class LedgerPanorama(
    tickets: Seq[CommissionTicket],
    artisanCount: Int,
    skuCount: Int,
    luminousMean: Double,
) derives Codec.AsObject

final case class BundleSuggestion(
    lineages: List[CraftLineage],
    skuIds: Vector[SkuId],
    luminousBlend: Double,
) derives Codec.AsObject
