package com.hoon.calendardiaryapp.data.database.diary

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @param date : type = "yyyy-MM-dd"
 * reference util.Constants DATE_STRING_PATTERN
 */
@Entity
data class DiaryEntity(
    @PrimaryKey val date: String,
    @ColumnInfo val title: String,
    @ColumnInfo val imageUri: String,
    @ColumnInfo val contents: String
)