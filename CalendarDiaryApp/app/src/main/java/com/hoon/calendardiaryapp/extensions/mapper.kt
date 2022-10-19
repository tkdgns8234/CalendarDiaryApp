package com.hoon.calendardiaryapp.extensions

import com.hoon.calendardiaryapp.data.api.response.HolidayResponseItem
import com.hoon.calendardiaryapp.data.database.DayEntity
import com.hoon.calendardiaryapp.data.model.HolidayModel

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

fun List<HolidayModel>.toEntity(year: String): List<DayEntity> = map { it.toEntity(year) }