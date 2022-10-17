package com.hoon.calendardiaryapp.view.diary_view

sealed class DiaryViewState {
    object UnInitialized : DiaryViewState()
    object Loading : DiaryViewState()
}