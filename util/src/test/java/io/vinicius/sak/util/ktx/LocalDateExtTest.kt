package io.vinicius.sak.util.ktx

import android.os.Build
import org.junit.FixMethodOrder
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.LocalDate
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class LocalDateExtTest
{
    fun `Convert Date to LocalDate`()
    {
        val tzUTC = TimeZone.getTimeZone("UTC")
        val calendar = GregorianCalendar.getInstance(tzUTC).apply {
            set(1980, 10, 6, 0, 0, 0)
        }

        val localDate1 = calendar.time.toLocalDate("UTC")
        val localDate2 = LocalDate.of(1980, 10, 6)
    }
}