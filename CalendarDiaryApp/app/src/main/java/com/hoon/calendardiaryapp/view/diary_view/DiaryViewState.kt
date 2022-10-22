package com.hoon.calendardiaryapp.view.diary_view

import com.hoon.calendardiaryapp.data.model.DiaryModel

sealed class DiaryViewState {
    object UnInitialized : DiaryViewState()

    sealed class GetDiaryContents : DiaryViewState() {
        data class Success(val diaryModel: DiaryModel) : GetDiaryContents()
        object Fail : GetDiaryContents()
    }
}