package com.hoon.calendardiaryapp.view.main

sealed class MainState {
    object UnInitialized : MainState()
    object Loading : MainState()
}
