package com.hoon.calendardiaryapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hoon.calendardiaryapp.BaseViewModel

class MainViewModel : BaseViewModel() {
    private val _mainStateLiveData = MutableLiveData<MainState>(MainState.UnInitialized)
    val mainStateLiveData: LiveData<MainState> get() = _mainStateLiveData

    private fun setState(state: MainState) {
        _mainStateLiveData.value = state
    }
}