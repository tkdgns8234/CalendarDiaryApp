package com.hoon.calendardiaryapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DiaryDao {
    @Query("SELECT * FROM DiaryEntity")
    fun getAll(): List<DiaryEntity>

    @Query("SELECT * FROM DiaryEntity WHERE date = :date")
    fun getDiaryEntity(date: String): DiaryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(diaryEntity: DiaryEntity)
}