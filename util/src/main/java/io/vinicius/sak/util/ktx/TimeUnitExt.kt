package io.vinicius.sak.util.ktx

import android.icu.util.TimeUnit

fun TimeUnit.toSeconds(value: Long): Long
{
    val numeric = when (this) {
        TimeUnit.SECOND -> 1
        TimeUnit.MINUTE -> 60
        TimeUnit.HOUR -> 3_600
        TimeUnit.DAY -> 86_400
        TimeUnit.WEEK -> 604_800
        TimeUnit.MONTH -> 2_592_000
        TimeUnit.YEAR -> 31_536_000
        else -> 0
    }

    return numeric * value
}