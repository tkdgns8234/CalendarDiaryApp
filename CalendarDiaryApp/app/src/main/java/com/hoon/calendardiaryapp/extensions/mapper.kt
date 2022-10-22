package com.hoon.calendardiaryapp.extensions

import com.hoon.calendardiaryapp.data.api.response.HolidayResponseItem
import com.hoon.calendardiaryapp.data.database.diary.DiaryEntity
import com.hoon.calendardiaryapp.data.database.holiday.DayEntity
import com.hoon.calendardiaryapp.data.model.DiaryModel
import com.hoon.calendardiaryapp.data.model.HolidayModel
import com.hoon.calendardiaryapp.util.Constants.DATE_STRING_PATTERN
import com.hoon.calendardiaryapp.util.DateUtil

fun HolidayResponseItem.toModel(): HolidayModel {
    return HolidayModel(date = this.date)
}

fun DayEntity.toModel(): HolidayModel {
    return HolidayModel(date = this.day)
}

fun HolidayModel.toEntity(year: String): DayEntity {
    return DayEntity(
        id = null,
        year = year,
        day = this.date
    )
}

fun DiaryEntity.toModel(): DiaryModel {
    return DiaryModel(
        DateUtil.stringToDate(this.date, DATE_STRING_PATTERN),
        this.title,
        this.imageUri,
        this.contents
    )
}

fun DiaryModel.toEntity(): DiaryEntity {
    return DiaryEntity(
        DateUtil.dateToString(this.date, DATE_STRING_PATTERN),
        this.title,
        this.imageUri,
        this.contents
    )
}