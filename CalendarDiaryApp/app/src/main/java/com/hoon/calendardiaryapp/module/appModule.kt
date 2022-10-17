package com.hoon.calendardiaryapp.module

import com.hoon.calendardiaryapp.view.main.MainViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Dispatchers.IO }

    viewModel { MainViewModel() }
}