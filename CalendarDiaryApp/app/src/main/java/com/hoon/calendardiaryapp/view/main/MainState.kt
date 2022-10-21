package com.hoon.calendardiaryapp.view.main

import com.hoon.calendardiaryapp.data.model.DiaryModel
import com.hoon.calendardiaryapp.data.model.HolidayModel
import java.util.*

sealed class MainState {
    object UnInitialized : MainState()

    sealed class Loading : MainState() {
        object Start : Loading()
        object End : Loading()
    }

    sealed class GetHolidaysFromYear : MainState() {
        data class Success(val list: List<HolidayModel>) : GetHolidaysFromYear()
        object Fail : GetHolidaysFromYear()
    }

    sealed class GetDiaryContents : MainState() {
        data class Success(val diaryModel: DiaryModel) : GetDiaryContents()
        data class Fail(val date: Date) : GetDiaryContents()
    }

    data class UpdateDiaryWrittenList(val list: List<DiaryModel>) : MainState()

    data class Error(
        val message: String
    ) : MainState()
}
