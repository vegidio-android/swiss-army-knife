package io.vinicius.sak.util.ktx

import java.time.ZoneId
import java.util.Date

fun Date.toLocalDate(zoneId: String = "UTC") = this.toInstant().atZone(ZoneId.of(zoneId)).toLocalDate()