package com.hoon.calendardiaryapp.view.diary

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoon.calendardiaryapp.BaseViewModel
import com.hoon.calendardiaryapp.data.model.DiaryModel
import com.hoon.calendardiaryapp.data.repository.DatabaseRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class DiaryViewModel(
    private val databaseRepository: DatabaseRepository
) : BaseViewModel() {
    private val _diaryStateLiveData = MutableLiveData<DiaryState>(DiaryState.UnInitialized)
    val diaryStateLiveData: LiveData<DiaryState> get() = _diaryStateLiveData

    fun fetchData(date: Date) = viewModelScope.launch {
        setState(DiaryState.Loading.Start)

        val diaryModel = databaseRepository.getDiaryContents(date)
        diaryModel?.let {
            setState(DiaryState.Success.FetchData(it))
        }

        delay(200)
        setState(DiaryState.Loading.End)
    }

    fun saveDiaryDataInDB(diaryModel: DiaryModel) = viewModelScope.launch {
        databaseRepository.insertDiaryContents(diaryModel)
    }

    private fun setState(state: DiaryState) {
        _diaryStateLiveData.value = state
    }
}