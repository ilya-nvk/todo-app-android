package com.ilyanvk.todoapp.ui

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object StringConverter {
    const val YEAR = 31_536_000_000
    const val DAY = 86_400_000
    const val HOUR = 3_600_000
    const val MINUTE = 60_000

    fun Long.toDateTimeString(): String {
        val date = Date(this - TimeZone.getDefault().rawOffset)
        val pattern =
            if (System.currentTimeMillis() < this && this < System.currentTimeMillis() + YEAR) {
                "dd MMMM, HH:mm"
            } else {
                "dd MMMM yyyy, HH:mm"
            }
        val format = SimpleDateFormat(pattern, Locale.getDefault())
        return format.format(date)
    }
}