package com.hoon.calendardiaryapp.data.repository

import com.hoon.calendardiaryapp.data.model.HolidayModel
import java.util.*

interface HolidayApiRepository {
    suspend fun getHolidaysFromYear(date: Date): List<HolidayModel>?
}