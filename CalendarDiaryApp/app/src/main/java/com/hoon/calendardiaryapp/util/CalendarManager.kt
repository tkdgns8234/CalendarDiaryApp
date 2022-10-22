package com.hoon.calendardiaryapp.util

import java.util.*

class CalendarManager {

    companion object {
        const val DAYS_OF_WEEK = 7
    }

    val calendar: Calendar by lazy { Calendar.getInstance().apply { this.time = Date() } }
    var days = arrayListOf<Date>()

    fun initCalendarManager(refreshCallback: (Calendar) -> Unit) {
        makeDays(refreshCallback)
    }

    fun changeToPrevMonth(refreshCallback: (Calendar) -> Unit) {
        if (calendar.get(Calendar.MONTH) == 0) { // 1월인 경우
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1)
            calendar.set(Calendar.MONTH, Calendar.DECEMBER)
        } else {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1)
        }
        makeDays(refreshCallback)
    }

    fun changeToNextMonth(refreshCallback: (Calendar) -> Unit) {
        if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER) { // 12월인 경우
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1)
            calendar.set(Calendar.MONTH, 0)
        } else {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1)
        }
        makeDays(refreshCallback)
    }

    fun moveToTargetDate(year: Int, month: Int, refreshCallback: (Calendar) -> Unit) {
        val isValidRange = month in (1..12)
        if (isValidRange.not()) return

        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1) // 1월 = 0
        makeDays(refreshCallback)
    }

    private fun makeDays(refreshCallback: (Calendar) -> Unit) {
        days.clear()
        calendar.set(Calendar.DATE, 1)

        val currentMonthMaxDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val prevMonthOffset = calendar.get(Calendar.DAY_OF_WEEK) - 1 // 캘린더의 1일이 무슨 요일인지에 따라 값이 달라짐
        val nextMonthOffset = DAYS_OF_WEEK - (prevMonthOffset + currentMonthMaxDate) % DAYS_OF_WEEK

        makePrevMonthOffset(calendar.clone() as Calendar, prevMonthOffset)
        makeCurrentMonth(calendar)

        if (nextMonthOffset != DAYS_OF_WEEK) {
            makeNextMonthOffset(calendar.clone() as Calendar, nextMonthOffset)
        }

        refreshCallback(calendar)
    }

    private fun makeCurrentMonth(calendar: Calendar) {
        for (i in 1..calendar.getActualMaximum(Calendar.DATE)) {
            calendar.set(Calendar.DATE, i)
            days.add(calendar.time)
        }
    }

    private fun makePrevMonthOffset(calendar: Calendar, prevMonthTailOffset: Int) {
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1) // 이전 월로 이동
        val maxDate = calendar.getActualMaximum(Calendar.DATE) // 이전 월의 max date
        var maxOffsetDate = maxDate - prevMonthTailOffset //

        for (i in 1..prevMonthTailOffset) {
            calendar.set(Calendar.DATE, ++maxOffsetDate)
            days.add(calendar.time)
        }
    }

    private fun makeNextMonthOffset(calendar: Calendar, nextMonthHeadOffset: Int) {
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1) // 다음 월로 이동
        var date = 1

        for (i in 1..nextMonthHeadOffset) {
            calendar.set(Calendar.DATE, date++)
            this.days.add(calendar.time)
        }
    }
}