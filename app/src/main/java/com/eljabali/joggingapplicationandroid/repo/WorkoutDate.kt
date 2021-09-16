package com.eljabali.joggingapplicationandroid.repo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "user_workout_schedule")
data class WorkoutDate(
    @PrimaryKey @ColumnInfo(name = "run_number") val runNumber: Int,
    @ColumnInfo(name = "did_user_attend_date") val didUserAttend: Boolean,
    @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "time") val time: Double,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double
    )
