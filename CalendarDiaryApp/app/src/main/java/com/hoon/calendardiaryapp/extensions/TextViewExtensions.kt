package com.hoon.calendardiaryapp.extensions

import android.content.Context
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun TextView.setColor(context: Context, @ColorRes id: Int) {
    this.setTextColor(ContextCompat.getColor(context, id))
}