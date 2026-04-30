package com.sandbox.aurora

import com.sandbox.aurora.domain.*

object Seed:
  val haloReserve: Double = 8.4

  val artisans: Seq[ArtisanPortrait] = Seq(
    ArtisanPortrait(
      ArtisanId(2001L),
      "Lys Ibarra",
      "Oaxaca · Parhelion studio",
      "Every seam is a halo thread — nothing ships without singing to the metal.",
      6.2,
      List(CraftLineage.Metallurgy, CraftLineage.Ceramics),
    ),
    ArtisanPortrait(
      ArtisanId(2002L),
      "Noor El-Amin",
      "Lisbon tidal atelier",
      "We braid photons into bone-white porcelain for slow mornings.",
      5.8,
      List(CraftLineage.Ceramics, CraftLineage.Horology),
    ),
    ArtisanPortrait(
      ArtisanId(2003L),
      "Mika Verdant",
      "Reykjavík blue hour lab",
      "Lumina lineages need glacial patience — I translate pressure into glow.",
      5.1,
      List(CraftLineage.Luminary, CraftLineage.Composite),
    ),
  )

  val catalog: Seq[HaloSku] = Seq(
    HaloSku(
      SkuId(9001L),
      ArtisanId(2001L),
      "Helio arc cuff",
      "Ripple-forged brass with obsidian inlay and a micro aurora seam.",
      luminousScore = 0.81,
      haloMinor = MinorUsd.cents(42_800),
      facets = List(CraftLineage.Metallurgy, CraftLineage.Composite),
    ),
    HaloSku(
      SkuId(9002L),
      ArtisanId(2001L),
      "Sundial torque ring",
      "Interlocking tension bands inspired by armillary spheres.",
      luminousScore = 0.74,
      haloMinor = MinorUsd.cents(18_200),
      facets = List(CraftLineage.Metallurgy, CraftLineage.Horology),
    ),
    HaloSku(
      SkuId(9003L),
      ArtisanId(2002L),
      "Lumen breakfast set",
      "Porcelain triptych with kiln-fused selenite veil.",
      luminousScore = 0.77,
      haloMinor = MinorUsd.cents(9_600),
      facets = List(CraftLineage.Ceramics, CraftLineage.Horology),
    ),
    HaloSku(
      SkuId(9004L),
      ArtisanId(2002L),
      "Tide stemware pair",
      "Hand-ground stems with moiré optics that answer candlelight.",
      luminousScore = 0.69,
      haloMinor = MinorUsd.cents(6_400),
      facets = List(CraftLineage.Ceramics, CraftLineage.Composite),
    ),
    HaloSku(
      SkuId(9005L),
      ArtisanId(2003L),
      "Aurora tether lamp",
      "Composite sail cloth diffuses edge light into a slow breathing gradient.",
      luminousScore = 0.88,
      haloMinor = MinorUsd.cents(28_900),
      facets = List(CraftLineage.Luminary, CraftLineage.Composite),
    ),
    HaloSku(
      SkuId(9006L),
      ArtisanId(2003L),
      "Parhelion signal chain",
      "Modular glass nodes for spatial sound — warm, never clinical.",
      luminousScore = 0.71,
      haloMinor = MinorUsd.cents(15_300),
      facets = List(CraftLineage.Composite, CraftLineage.Horology),
    ),
  )
