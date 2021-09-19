package com.eljabali.joggingapplicationandroid.usecase

import android.graphics.drawable.ColorDrawable
import androidx.room.ColumnInfo

data class ColoredJogDate(
    val date: Long,
    val runNumber: Int,
    val backgroundColor: ColorDrawable,
    val time: Double,
    val latitude: Double,
    val longitude: Double
)