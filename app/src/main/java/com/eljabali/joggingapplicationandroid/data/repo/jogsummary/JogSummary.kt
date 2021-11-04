package com.eljabali.joggingapplicationandroid.data.repo.jogsummary

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Duration

@Entity(tableName = "jog_summary")
data class JogSummary(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "start_date_time")
    val startDate: String,
    @ColumnInfo(name = "duration_seconds")
    val totalJogDuration: Long,
    @ColumnInfo(name = "distance_miles")
    val totalJogDistance: Double,

)