package com.hoon.calendardiaryapp

import android.app.Application
import com.hoon.calendardiaryapp.module.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class CalendarDiaryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger(Level.ERROR) // koin의 로그 레벨을 지정
            } else {
                androidLogger(Level.NONE)
            }
            androidContext(this@CalendarDiaryApplication) // context 등록
            modules(appModule)
        }
    }
}