package com.eljabali.joggingapplicationandroid.home

import android.graphics.drawable.ColorDrawable
import com.eljabali.joggingapplicationandroid.calendar.jogsummaries.JogSummaryProperties
import java.util.*

data class HomeViewState(
        val listOfSpecificDates: List<JogSummaryProperties> = emptyList(),
        val listOfColoredDates: List<ColoredDates> = emptyList(),
)

data class ColoredDates(
        val date: Date,
        val colorDrawable: ColorDrawable
)



