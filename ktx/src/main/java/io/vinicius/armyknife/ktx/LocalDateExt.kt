package io.vinicius.armyknife.ktx

import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

/**
 * Convert a {LocalDate} object to {Date}
 */
fun LocalDate.toDate(zoneId: String = "UTC") = Date.from(this.atStartOfDay(ZoneId.of(zoneId)).toInstant())