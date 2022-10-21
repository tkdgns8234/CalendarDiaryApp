package com.hoon.calendardiaryapp.data.database

import androidx.room.*

@Dao
interface DiaryDao {
    @Query("SELECT * FROM DiaryEntity")
    fun getAll(): List<DiaryEntity>

    @Query("SELECT * FROM DiaryEntity WHERE date = :date")
    fun getDiaryEntity(date: String): DiaryEntity?

    /**
     * ~년 ~월에 해당하는 다이어리 정보를 return
     * @param dateYearMonth : 'yyyy-mm' format
     */
    @Query("SELECT * FROM DiaryEntity WHERE date LIKE :dateYearMonth || '%'")
    fun getDiaryEntityInMonth(dateYearMonth: String): List<DiaryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(diaryEntity: DiaryEntity)

    @Delete
    fun delete(diaryEntity: DiaryEntity)
}