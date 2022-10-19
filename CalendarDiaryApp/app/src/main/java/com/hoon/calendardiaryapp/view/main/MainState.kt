package com.hoon.calendardiaryapp.view.main

import com.hoon.calendardiaryapp.data.model.HolidayModel

sealed class MainState {
    object UnInitialized : MainState()

    sealed class Loading : MainState() {
        object Start: Loading()
        object End: Loading()
    }

    sealed class Success : MainState() {
        data class GetHolidaysFromYear(val list: List<HolidayModel>) : Success()
    }

    data class Error(
        val message: String
    ) : MainState()
}
