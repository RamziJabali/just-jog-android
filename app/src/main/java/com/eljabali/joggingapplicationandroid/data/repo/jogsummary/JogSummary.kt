package com.eljabali.joggingapplicationandroid.data.repo.jogsummary

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jog_summary")
data class JogSummary(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "start_date")
    val startDate: String,
)