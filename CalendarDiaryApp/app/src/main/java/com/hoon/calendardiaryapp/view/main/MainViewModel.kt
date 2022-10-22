package com.hoon.calendardiaryapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoon.calendardiaryapp.BaseViewModel
import com.hoon.calendardiaryapp.data.repository.DatabaseRepository
import com.hoon.calendardiaryapp.data.repository.HolidayApiRepository
import com.hoon.calendardiaryapp.extensions.getYearString
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(
    private val holidayApiRepository: HolidayApiRepository,
    private val databaseRepository: DatabaseRepository
) : BaseViewModel() {

    private val _mainStateLiveData = MutableLiveData<MainState>(MainState.UnInitialized)
    val mainStateLiveData: LiveData<MainState> get() = _mainStateLiveData

    fun getHolidaysFromYear(date: Date) = viewModelScope.launch {
        setState(MainState.Loading.Start)
        // db에 holiday 정보가 저장되어있는지 확인
        var holidays = databaseRepository.getHolidaysInfo(date)

        if (holidays == null) {
            // holiday 정보가 없으면 rest api 를 통해 holiday list 를 가져온다
            holidays = holidayApiRepository.getHolidaysFromYear(date)
            holidays?.let {
                // db update
                databaseRepository.updateHolidaysInfo(date, holidays)
            }
        }

        if (holidays == null) {
            setState(MainState.GetHolidaysFromYear.Fail)
        } else {
            setState(MainState.GetHolidaysFromYear.Success(holidays))
        }
        setState(MainState.Loading.End)
    }

    fun getDiaryContents(date: Date) = viewModelScope.launch {
        val diaryModel = databaseRepository.getDiaryContents(date)

        if (diaryModel != null) {
            setState(MainState.GetDiaryContents.Success(diaryModel))
        } else {
            setState(MainState.GetDiaryContents.Fail(date))
        }
    }

    fun getDiaryContentsInMonth(date: Date) = viewModelScope.launch {
        val diaryList = databaseRepository.getDiaryContentsInMonth(date)
        diaryList?.let {
            setState(MainState.UpdateDiaryWrittenList(it))
        }
    }

    private fun setState(state: MainState) {
        _mainStateLiveData.value = state
    }

}