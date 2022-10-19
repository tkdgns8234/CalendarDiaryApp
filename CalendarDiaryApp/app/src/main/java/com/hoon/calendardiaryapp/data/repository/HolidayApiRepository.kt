package com.hoon.calendardiaryapp.data.repository

import com.hoon.calendardiaryapp.data.model.HolidayModel

interface HolidayApiRepository {
    suspend fun getHolidaysFromYear(year: String): List<HolidayModel>?
}