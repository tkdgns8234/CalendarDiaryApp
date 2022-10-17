package com.hoon.calendardiaryapp.view.diary_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hoon.calendardiaryapp.BaseViewModel

class DiaryViewViewModel : BaseViewModel() {
    private val _diaryViewStateLiveData = MutableLiveData<DiaryViewState>(DiaryViewState.UnInitialized)
    val diaryViewStateLiveData: LiveData<DiaryViewState> get() = _diaryViewStateLiveData

    private fun setState(state: DiaryViewState) {
        _diaryViewStateLiveData.value = state
    }
}