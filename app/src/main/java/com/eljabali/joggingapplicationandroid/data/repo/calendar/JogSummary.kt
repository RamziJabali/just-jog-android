package com.eljabali.joggingapplicationandroid.data.repo.calendar

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.PrimaryKey

@Entity(tableName = "jog_summary")
data class JogSummary(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "jog_id")
    val jogId: Int,
    @ColumnInfo(name = "jog_start_date")
    val jogStartDate: String,
)