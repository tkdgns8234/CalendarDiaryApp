package com.hoon.calendardiaryapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoon.calendardiaryapp.BaseViewModel
import com.hoon.calendardiaryapp.data.repository.DatabaseRepository
import com.hoon.calendardiaryapp.data.repository.HolidayApiRepository
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(
    private val holidayApiRepository: HolidayApiRepository,
    private val databaseRepository: DatabaseRepository
) : BaseViewModel() {

    private val _mainStateLiveData = MutableLiveData<MainState>(MainState.UnInitialized)
    val mainStateLiveData: LiveData<MainState> get() = _mainStateLiveData

    /**
     * @param year : string format "yyyy"
     */
    fun getHolidaysFromYear(year: String) = viewModelScope.launch {
        setState(MainState.Loading.Start)
        // db에 holiday 정보가 저장되어있는지 확인
        var holidays = databaseRepository.getHolidayInfo(year)

        if (holidays == null) {
            // holiday 정보가 없으면 rest api 를 통해 holiday list 를 가져온다
            holidays = holidayApiRepository.getHolidaysFromYear(year)
            holidays?.let {
                // db update
                databaseRepository.updateHolidayInfo(year, holidays)
            }
        }

        if (holidays == null) {
            setState(MainState.Error(ERROR_MSG_LOAD_FAILED))
        } else {
            setState(MainState.Success.GetHolidaysFromYear(holidays))
        }
        setState(MainState.Loading.End)
    }

    fun updateDiaryTitleAndImage(date: Date) = viewModelScope.launch {
        databaseRepository.getDiaryContents(date)
    }

    private fun setState(state: MainState) {
        _mainStateLiveData.value = state
    }

    companion object {
        const val TAG = "MainViewModel"
        const val ERROR_MSG_LOAD_FAILED = "공휴일 정보를 불러오는데 실패하였습니다."
    }
}