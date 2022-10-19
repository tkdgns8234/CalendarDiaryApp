package com.hoon.calendardiaryapp.data.repository

import com.hoon.calendardiaryapp.data.database.HolidayDao
import com.hoon.calendardiaryapp.data.database.YearEntity
import com.hoon.calendardiaryapp.data.model.HolidayModel
import com.hoon.calendardiaryapp.extensions.toEntity
import com.hoon.calendardiaryapp.extensions.toModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DatabaseRepositoryImpl(
    private val holidayDao: HolidayDao,
    private val ioDispatcher: CoroutineDispatcher
) : DatabaseRepository {

    override suspend fun getHolidayInfo(year: String) = withContext(ioDispatcher) {
        holidayDao.getYearWithDays(year)
            ?.days
            ?.map {
                it.toModel()
            }
    }

    override suspend fun updateHolidayInfo(year: String, holidayModels: List<HolidayModel>) =
        withContext(ioDispatcher) {
            holidayDao.insertYear(YearEntity(year))
            holidayModels.forEach {
                holidayDao.insertDate(it.toEntity(year))
            }
        }
}