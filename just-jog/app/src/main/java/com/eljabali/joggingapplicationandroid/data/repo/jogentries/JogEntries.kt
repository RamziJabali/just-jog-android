package com.eljabali.joggingapplicationandroid.data.repo.jogentries

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jog_entries")
data class JogEntries(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "jog_summary_id")
    val jogSummaryID: Int,
    @ColumnInfo(name = "date_time")
    val dateTime: String,
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double
)
