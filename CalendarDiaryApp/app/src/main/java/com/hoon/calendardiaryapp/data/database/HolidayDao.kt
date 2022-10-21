package com.hoon.calendardiaryapp.data.database

import androidx.room.*

@Dao
interface HolidayDao {

    /**
     * YearEntity
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertYear(year: YearEntity)

    @Delete
    fun deleteYear(year: YearEntity)

    @Query("SELECT * from YearEntity WHERE year = :year")
    fun getYear(year: String): YearEntity?

    /**
     * DayEntity
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDate(date: DayEntity)

    @Delete
    fun deleteDate(date: DayEntity)

    @Query("SELECT * FROM DayEntity WHERE year = :year")
    fun getDays(year: String) : List<DayEntity>

    /**
     * @param year: 'yyyy' format
     */
    @Transaction
    @Query("SELECT * FROM YearEntity WHERE year = :year")
    fun getYearWithDays(year: String) : YearWithDays?

}