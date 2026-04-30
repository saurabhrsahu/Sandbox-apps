package com.sandbox.aurora.store

import cats.syntax.all.*

import com.sandbox.aurora.Seed
import com.sandbox.aurora.domain.*

/** Ledger snapshot — halo budget erodes as palettes burn luminous mass */
final private[store] case class LedgerState(
    artisansByKey: Map[ArtisanId, ArtisanPortrait],
    skuByKey: Map[SkuId, HaloSku],
    tickets: Vector[CommissionTicket],
    haloBudgetRemain: Double,
    seqPointer: Long,
)

private[store] object LedgerState:

  val fresh: LedgerState =
    val artisans = Seed.artisans.map(a => a.id -> a).toMap
    val skus = Seed.catalog.map(h => h.id -> h).toMap
    LedgerState(artisans, skus, Vector.empty, Seed.haloReserve, 900_400L)

  extension (ledger: LedgerState)
    private[store] def luminousMass(lines: Iterable[(SkuId, Int)]): Either[AuroraError, Double] =
      val totalE = lines.toVector.foldLeft[Either[AuroraError, Double]](Right(0d)) {
        case (accE, (skuId, qty)) =>
          accE.flatMap(acc =>
            ledger.skuByKey.get(skuId) match
              case None       => Left(AuroraError.UnknownSku(skuId.underlying))
              case Some(sku) =>
                Right(acc + sku.luminousScore * qty.toDouble * 1.12),
          )
      }
      totalE.flatMap: total =>
        if total <= ledger.haloBudgetRemain then Right(total)
        else Left(AuroraError.HaloInsufficient(total, ledger.haloBudgetRemain))
