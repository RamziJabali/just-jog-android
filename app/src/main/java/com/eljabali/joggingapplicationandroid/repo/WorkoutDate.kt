package com.eljabali.joggingapplicationandroid.repo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "user_workout_schedule")
data class WorkoutDate(
    @PrimaryKey @ColumnInfo(name = "date_time") val dateTime: String,
    @ColumnInfo(name = "run_number") val runNumber: Int,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double
    )
