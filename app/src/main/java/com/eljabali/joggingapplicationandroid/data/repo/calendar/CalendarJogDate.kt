package com.eljabali.joggingapplicationandroid.data.repo.calendar

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calendar_jog_dates")
data class CalendarJogDate(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    val totalRuns: Int,
    @ColumnInfo(name = "date")
    val jogDate: String,
    @ColumnInfo(name = "jog_color")
    val jogColor: Int
)