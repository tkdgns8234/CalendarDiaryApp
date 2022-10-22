package com.hoon.calendardiaryapp.view.diary_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoon.calendardiaryapp.BaseViewModel
import com.hoon.calendardiaryapp.data.repository.DatabaseRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class DiaryViewViewModel(
    private val databaseRepository: DatabaseRepository
) : BaseViewModel() {

    private val _diaryViewStateLiveData = MutableLiveData<DiaryViewState>(DiaryViewState.UnInitialized)
    val diaryViewStateLiveData: LiveData<DiaryViewState> get() = _diaryViewStateLiveData

    fun fetchData(date: Date) = viewModelScope.launch {
        val diaryModel = databaseRepository.getDiaryContents(date)

        if (diaryModel != null) {
            setState(DiaryViewState.GetDiaryContents.Success(diaryModel))
        } else {
            setState(DiaryViewState.GetDiaryContents.Fail)
        }
    }

    fun deleteDiaryContent(date: Date) = GlobalScope.launch {
        val diaryModel = databaseRepository.getDiaryContents(date)
        diaryModel?.let {
            databaseRepository.deleteDiaryContents(it)
        }
    }

    private fun setState(state: DiaryViewState) {
        _diaryViewStateLiveData.value = state
    }
}