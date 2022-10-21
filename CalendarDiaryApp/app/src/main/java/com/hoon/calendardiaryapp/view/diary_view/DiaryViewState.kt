package com.hoon.calendardiaryapp.view.diary_view

import com.hoon.calendardiaryapp.data.model.DiaryModel

sealed class DiaryViewState {
    object UnInitialized : DiaryViewState()

    sealed class UpdateDiaryContents : DiaryViewState() {
        data class Success(val diaryModel: DiaryModel) : UpdateDiaryContents()
        object Fail : UpdateDiaryContents()
    }
}