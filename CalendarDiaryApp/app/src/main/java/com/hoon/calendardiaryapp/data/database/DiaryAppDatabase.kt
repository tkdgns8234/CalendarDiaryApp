package com.hoon.calendardiaryapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hoon.calendardiaryapp.data.database.diary.DiaryDao
import com.hoon.calendardiaryapp.data.database.holiday.HolidayDao
import com.hoon.calendardiaryapp.data.database.diary.DiaryEntity
import com.hoon.calendardiaryapp.data.database.holiday.DayEntity
import com.hoon.calendardiaryapp.data.database.holiday.YearEntity

@Database(entities = [YearEntity::class, DayEntity::class, DiaryEntity::class], version = 1)
abstract class DiaryAppDatabase : RoomDatabase() {
    abstract fun holidayDao(): HolidayDao
    abstract fun diaryDao(): DiaryDao

    companion object {
        const val DB_NAME = "DiaryAppDatabase"

        fun build(context: Context): DiaryAppDatabase {
            return Room.databaseBuilder(context, DiaryAppDatabase::class.java, DB_NAME).build()
        }
    }
}