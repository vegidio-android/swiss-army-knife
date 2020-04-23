package io.vinicius.sak.util.ktx

import android.os.Build
import org.junit.FixMethodOrder
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.GregorianCalendar
import java.util.TimeZone

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class DateExtTest
{
    fun `1- Create a Date object`()
    {
        val tzUTC = TimeZone.getTimeZone("UTC")
        val calendar = GregorianCalendar.getInstance(tzUTC).apply {
            set(1980, 10, 6, 0, 0, 0)
        }

        val date1 = calendar.time

        calendar.time
    }
}