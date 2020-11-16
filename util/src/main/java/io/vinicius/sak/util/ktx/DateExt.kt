package io.vinicius.sak.util.ktx

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
fun Date.toLocalDate(zoneId: String = "UTC"): LocalDate = this.toInstant().atZone(ZoneId.of(zoneId)).toLocalDate()