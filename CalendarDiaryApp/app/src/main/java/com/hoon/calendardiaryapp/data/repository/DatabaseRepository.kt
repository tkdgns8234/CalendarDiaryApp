package com.hoon.calendardiaryapp.data.repository

import com.hoon.calendardiaryapp.data.model.HolidayModel

interface DatabaseRepository {
    suspend fun getHolidayInfo(year: String): List<HolidayModel>?

    suspend fun updateHolidayInfo(year: String, holidayModels: List<HolidayModel>)
}