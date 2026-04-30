package com.sandbox.aurora.store

import scala.annotation.tailrec
import cats.effect.Concurrent
import cats.effect.Ref
import cats.syntax.all.*

import com.sandbox.aurora.domain.*

final class AuroraLedger[F[_]](private val cell: Ref[F, LedgerState])(using Concurrent[F]):

  def artisansBeam: F[Seq[ArtisanPortrait]] =
    cell.get.map(_.artisansByKey.values.toSeq.sortBy(-_.spotlight))

  def constellation: F[HaloObservation] =
    cell.get.map(s =>
      HaloObservation(s.skuByKey.size, s.artisansByKey.size, s.haloBudgetRemain),
    )

  def haloSkies(
      lineage: Option[String],
      q: Option[String],
      artisan: Option[Long],
  ): F[Seq[HaloSku]] =
    cell.get.map: ledger =>
      val tagChoice = lineage.flatMap(raw => CraftLineage.all.find(_.tag == raw)).toSet
      val artisanFilter = artisan.map(ArtisanId.apply)
      ledger.skuByKey.values.toSeq
        .filter(sku =>
          artisanFilter.forall(_ == sku.artisanId) &&
            (tagChoice.isEmpty || sku.facets.exists(tagChoice.contains)),
        )
        .filter(sku =>
          q.forall(term =>
            val t = term.toLowerCase
            sku.title.toLowerCase.contains(t) || sku.vignette.toLowerCase.contains(t),
          ),
        )
        .sortBy(sku => (-sku.luminousScore, sku.title))

  def capturePalette(proposal: PaletteProposal): F[Either[AuroraError, CommissionTicket]] =
    cell.modify: ledger =>
      validate(proposal, ledger) match
        case Left(err)      => (ledger, Left(err))
        case Right(ticket) =>
          (
            ledger.copy(
              tickets = ledger.tickets :+ ticket,
              haloBudgetRemain = ledger.haloBudgetRemain - ticket.luminousBlend,
              seqPointer = ticket.id,
            ),
            Right(ticket),
          )

  def ledgerPanorama: F[LedgerPanorama] =
    cell.get.map: s =>
      val mean =
        if s.tickets.isEmpty then 0d
        else s.tickets.map(_.luminousBlend).sum / s.tickets.size.toDouble
      LedgerPanorama(s.tickets, s.artisansByKey.size, s.skuByKey.size, mean)

  def bundleWeave(targets: Set[CraftLineage]): F[BundleSuggestion] =
    cell.get.map: ledger =>
      val ranked = ledger.skuByKey.values.toVector
        .map(sku => sku -> sku.auraRank(targets))
        .sortBy(_._2)(Ordering[Double].reverse)
        .take(3)
      val blend =
        if ranked.isEmpty then 0d
        else
          val denom = math.max(ranked.size, 1).toDouble
          CraftLineage.blend(ranked.flatMap { case (sku, score) =>
            sku.facets.map(l => l -> (score / denom))
          })
      BundleSuggestion(targets.toList, ranked.map(_._1.id), blend)

final case class HaloObservation(
    activeSkus: Int,
    livingArtisans: Int,
    haloBudget: Double,
) derives io.circe.Codec.AsObject

object AuroraLedger:

  def bloom[F[_]: Concurrent]: F[AuroraLedger[F]] =
    Ref.of(LedgerState.fresh).map(AuroraLedger(_))

private def validate(
    proposal: PaletteProposal,
    ledger: LedgerState,
): Either[AuroraError, CommissionTicket] =
  if proposal.lines.isEmpty then Left(AuroraError.EmptyPalette())
  else if proposal.lines.size > 16 then Left(AuroraError.PaletteOverload(proposal.lines.size))
  else
    val loads = proposal.lines.map(pl => pl.skuId -> pl.quantity).toVector
    loads.find(_._2 <= 0) match
      case Some(_) => Left(AuroraError.Malformed("quantities must stay positive"))
      case None =>
        ledger.luminousMass(loads) match
          case Left(err) => Left(err)
          case Right(mass) =>
            resolveSkus(loads, ledger, Vector.empty).map { loaded =>

              val captured =
                loaded.map { case (sku, qty) => CapturedLine(sku.id, qty, sku.haloMinor) }
              val sumCents = captured.foldLeft(0)((a, l) => a + l.unitMinor.raw * l.quantity)
              val sumMinor = MinorUsd.cents(sumCents)
              val pulse = sumMinor.raw match
                case n if n > 400000 => CommissionPulse.ShipsToParlor
                case n if n > 120000 => CommissionPulse.AureoleReview
                case n if n > 40000  => CommissionPulse.ForgingLoom
                case _               => CommissionPulse.Conceived
              CommissionTicket(
                ledger.seqPointer + 1,
                captured,
                sumMinor,
                mass,
                pulse,
              )
            }

@tailrec
private def resolveSkus(
    rem: Vector[(SkuId, Int)],
    ledger: LedgerState,
    acc: Vector[(HaloSku, Int)],
): Either[AuroraError, Vector[(HaloSku, Int)]] =
  if rem.isEmpty then Right(acc)
  else
    val (skuId, qty) = rem.head
    ledger.skuByKey.get(skuId) match
      case None       => Left(AuroraError.UnknownSku(skuId.underlying))
      case Some(sku) => resolveSkus(rem.tail, ledger, acc :+ (sku -> qty))
