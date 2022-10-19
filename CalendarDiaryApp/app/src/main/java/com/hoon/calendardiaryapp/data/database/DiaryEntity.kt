package com.hoon.calendardiaryapp.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @param date : type = "yyyy-MM-dd"
 * reference util.Constants DB_DIARY_ENTITY_PK_PATTERN
 */
@Entity
data class DiaryEntity(
    @PrimaryKey val date: String,
    @ColumnInfo val title: String,
    @ColumnInfo val imageUri: String,
    @ColumnInfo val contents: String
)