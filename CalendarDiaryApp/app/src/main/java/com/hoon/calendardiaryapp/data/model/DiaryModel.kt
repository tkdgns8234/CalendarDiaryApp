package com.hoon.calendardiaryapp.data.model

import java.util.*

data class DiaryModel(
    val date: Date,
    val title: String,
    val imageUri: String,
    val contents: String
)