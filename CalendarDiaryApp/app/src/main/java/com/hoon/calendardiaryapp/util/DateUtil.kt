package com.hoon.calendardiaryapp.util

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    fun stringToDate(dateString: String, pattern: String): Date {
        try {
            val format = SimpleDateFormat(pattern)
            return format.parse(dateString)
        } catch (e: Exception) {
            return Date()
        }
    }

    fun dateToString(date: Date, pattern: String): String {
        val format = SimpleDateFormat(pattern)
        return format.format(date)
    }

    fun dateToString(context: Context, date: Date, pattern: String): String {
        val format = SimpleDateFormat(pattern, Locale(LocaleHelper.getPersistedLocale(context)))
        return format.format(date)
    }
}