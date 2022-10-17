package com.hoon.calendardiaryapp.view.diary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hoon.calendardiaryapp.BaseViewModel

class DiaryViewModel : BaseViewModel() {
    private val _diaryStateLiveData = MutableLiveData<DiaryState>(DiaryState.UnInitialized)
    val diaryStateLiveData: LiveData<DiaryState> get() = _diaryStateLiveData

    private fun setState(state: DiaryState) {
        _diaryStateLiveData.value = state
    }
}