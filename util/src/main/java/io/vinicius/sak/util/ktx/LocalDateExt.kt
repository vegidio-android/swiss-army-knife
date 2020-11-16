package io.vinicius.sak.util.ktx

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

/**
 * Convert a {LocalDate} object to {Date}
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toDate(zoneId: String = "UTC"): Date = Date.from(this.atStartOfDay(ZoneId.of(zoneId)).toInstant())