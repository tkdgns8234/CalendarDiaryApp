package com.hoon.calendardiaryapp.view.diary

sealed class DiaryState {
    object UnInitialized : DiaryState()
    object Loading : DiaryState()
}
