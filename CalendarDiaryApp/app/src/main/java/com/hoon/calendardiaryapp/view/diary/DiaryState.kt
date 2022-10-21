package com.hoon.calendardiaryapp.view.diary

import com.hoon.calendardiaryapp.data.model.DiaryModel
import com.hoon.calendardiaryapp.data.model.HolidayModel
import com.hoon.calendardiaryapp.view.main.MainState

sealed class DiaryState {
    object UnInitialized : DiaryState()

    sealed class Loading : DiaryState() {
        object Start: Loading()
        object End: Loading()
    }

    sealed class Success : DiaryState() {
        data class FetchData(val diaryModel: DiaryModel) : Success()
    }
}
