package com.hoon.calendardiaryapp.data.database

import androidx.room.*

@Entity
data class YearEntity(
    @PrimaryKey val year: String
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = YearEntity::class,
            parentColumns = arrayOf("year"),
            childColumns = arrayOf("year"),
            onDelete = ForeignKey.CASCADE // 부모 삭제 시 자식도 삭제
        )
    ]
)
data class DayEntity(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo val year: String,
    @ColumnInfo val day: String
)

data class YearWithDays(
    @Embedded val year: YearEntity,
    @Relation(
        parentColumn = "year",
        entityColumn = "year"
    )
    val days: List<DayEntity>
)