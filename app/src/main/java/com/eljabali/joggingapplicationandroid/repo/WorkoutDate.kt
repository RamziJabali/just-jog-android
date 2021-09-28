package com.eljabali.joggingapplicationandroid.repo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "user_workout_schedule")
data class WorkoutDate(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "total_run_number")
    val totalRuns: Int,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "hour")
    val hours: Int,
    @ColumnInfo(name = "minute")
    val minutes: Int,
    @ColumnInfo(name = "second")
    val seconds: Int,
    @ColumnInfo(name = "daily_run_number")
    val runNumber: Int,
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double
)
