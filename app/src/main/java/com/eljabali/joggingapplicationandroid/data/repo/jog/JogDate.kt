package com.eljabali.joggingapplicationandroid.data.repo.jog

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_jog_schedule")
data class JogDate(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    val totalRuns: Int,
    @ColumnInfo(name = "date_time")
    val dateTime: String,
    @ColumnInfo(name = "daily_id")
    val runNumber: Int,
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double
)
