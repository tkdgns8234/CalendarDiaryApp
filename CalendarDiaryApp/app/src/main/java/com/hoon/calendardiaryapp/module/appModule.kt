package com.hoon.calendardiaryapp.module

import com.hoon.calendardiaryapp.data.repository.DatabaseRepository
import com.hoon.calendardiaryapp.data.repository.DatabaseRepositoryImpl
import com.hoon.calendardiaryapp.data.repository.HolidayApiRepository
import com.hoon.calendardiaryapp.data.repository.HolidayApiRepositoryImpl
import com.hoon.calendardiaryapp.view.diary.DiaryViewModel
import com.hoon.calendardiaryapp.view.diary_view.DiaryViewViewModel
import com.hoon.calendardiaryapp.view.main.MainViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Dispatchers.IO }

    single<HolidayApiRepository> { HolidayApiRepositoryImpl(get(), get()) }
    single<DatabaseRepository> { DatabaseRepositoryImpl(get(), get(), get()) }

    viewModel { MainViewModel(get(), get()) }
    viewModel { DiaryViewModel(get()) }
    viewModel { DiaryViewViewModel(get()) }
}