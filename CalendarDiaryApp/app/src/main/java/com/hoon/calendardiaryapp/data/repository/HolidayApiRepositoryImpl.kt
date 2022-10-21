package com.hoon.calendardiaryapp.data.repository

import com.hoon.calendardiaryapp.data.api.HolidayApiService
import com.hoon.calendardiaryapp.data.model.HolidayModel
import com.hoon.calendardiaryapp.extensions.getYearString
import com.hoon.calendardiaryapp.extensions.toModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.*

class HolidayApiRepositoryImpl(
    private val holidayApiService: HolidayApiService,
    private val ioDispatcher: CoroutineDispatcher
) : HolidayApiRepository {

    companion object {
        const val HOLIDAY_TYPE = "Public"
    }

    override suspend fun getHolidaysFromYear(date: Date): List<HolidayModel>? =
        withContext(ioDispatcher) {
            holidayApiService.searchHolidays(date.getYearString())
                .body()
                ?.filter {
                    // types 에 Public 을 포함한 공휴일만 표시
                    it.types.contains(HOLIDAY_TYPE)
                }?.map { it.toModel() }
        }
}