package com.hoon.calendardiaryapp.data.repository

import com.hoon.calendardiaryapp.data.database.diary.DiaryDao
import com.hoon.calendardiaryapp.data.database.holiday.HolidayDao
import com.hoon.calendardiaryapp.data.database.YearEntity
import com.hoon.calendardiaryapp.data.model.DiaryModel
import com.hoon.calendardiaryapp.data.model.HolidayModel
import com.hoon.calendardiaryapp.extensions.getYearString
import com.hoon.calendardiaryapp.extensions.toEntity
import com.hoon.calendardiaryapp.extensions.toModel
import com.hoon.calendardiaryapp.util.Constants.DATE_STRING_PATTERN
import com.hoon.calendardiaryapp.util.Constants.DATE_STRING_YEAR_MONTH_PATTERN
import com.hoon.calendardiaryapp.util.DateUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.*

class DatabaseRepositoryImpl(
    private val holidayDao: HolidayDao,
    private val diaryDao: DiaryDao,
    private val ioDispatcher: CoroutineDispatcher
) : DatabaseRepository {

    override suspend fun getHolidaysInfo(date: Date) = withContext(ioDispatcher) {
        holidayDao.getYearWithDays(date.getYearString())
            ?.days
            ?.map {
                it.toModel()
            }
    }

    override suspend fun updateHolidaysInfo(date: Date, holidayModels: List<HolidayModel>) =
        withContext(ioDispatcher) {
            holidayDao.insertYear(YearEntity(date.getYearString()))
            holidayModels.forEach {
                holidayDao.insertDate(it.toEntity(date.getYearString()))
            }
        }

    override suspend fun getDiaryContents(date: Date): DiaryModel? = withContext(ioDispatcher) {
        val dateString = DateUtil.dateToString(date, DATE_STRING_PATTERN)
        val diaryEntity = diaryDao.getDiaryEntity(dateString)
        diaryEntity?.toModel()
    }

    override suspend fun getDiaryContentsInMonth(date: Date): List<DiaryModel>? = withContext(ioDispatcher){
        val yearMonthString = DateUtil.dateToString(date, DATE_STRING_YEAR_MONTH_PATTERN)
        diaryDao.getDiaryEntityInMonth(yearMonthString).map { it.toModel() }
    }

    override suspend fun insertDiaryContents(diaryModel: DiaryModel) = withContext(ioDispatcher) {
        diaryDao.insert(diaryModel.toEntity())
    }

    override suspend fun deleteDiaryContents(diaryModel: DiaryModel) {
        diaryDao.delete(diaryModel.toEntity())
    }

}