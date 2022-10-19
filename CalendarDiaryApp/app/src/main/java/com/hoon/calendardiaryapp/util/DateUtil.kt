package com.hoon.calendardiaryapp.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    fun parseDate(dateString: String, pattern: String): Date {
        try {
            val format = SimpleDateFormat(pattern)
            return format.parse(dateString)
        } catch (e: Exception) {
            return Date()
        }
    }

    fun formatDate(date: Date, pattern: String): String {
        val format = SimpleDateFormat(pattern)
        return format.format(date)
    }
}