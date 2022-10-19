package com.hoon.calendardiaryapp.extensions

import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun Date.getMonthString() = DateFormat.format("MM", this).toString()

fun Date.getDayString() = DateFormat.format("dd", this).toString()

fun Date.formatString(): String {
    val format = SimpleDateFormat("yyyy-MM-dd")
    return format.format(this)
}
