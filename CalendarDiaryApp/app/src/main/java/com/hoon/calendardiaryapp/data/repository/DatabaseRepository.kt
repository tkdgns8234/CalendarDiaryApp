package com.hoon.calendardiaryapp.data.repository

import com.hoon.calendardiaryapp.data.model.DiaryModel
import com.hoon.calendardiaryapp.data.model.HolidayModel
import java.util.*

interface DatabaseRepository {
    suspend fun getHolidaysInfo(date: Date): List<HolidayModel>?

    suspend fun updateHolidaysInfo(date: Date, holidayModels: List<HolidayModel>)

    suspend fun getDiaryContents(date: Date): DiaryModel?

    suspend fun getDiaryContentsInMonth(date: Date): List<DiaryModel>?

    suspend fun insertDiaryContents(diaryModel: DiaryModel)

    suspend fun deleteDiaryContents(diaryModel: DiaryModel)
}