package com.sandbox.aurora.domain

import io.circe.Codec.AsObject

/** Request body — craft lineage slug list for bundle weaving */
final case class TagBundleQuery(tags: List[String]) derives AsObject
