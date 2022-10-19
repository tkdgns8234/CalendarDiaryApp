package com.hoon.calendardiaryapp.module

import com.hoon.calendardiaryapp.data.database.DiaryAppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val DatabaseModule = module {

    single { DiaryAppDatabase.build(androidApplication()) }
    single { get<DiaryAppDatabase>().holidayDao() }
}