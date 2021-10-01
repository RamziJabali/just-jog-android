package com.eljabali.joggingapplicationandroid.mainview

import android.graphics.drawable.ColorDrawable
import com.eljabali.joggingapplicationandroid.recyclerview.RecyclerViewProperties
import java.time.ZonedDateTime
import java.util.*

data class ViewState(
    val listOfSpecificDates: List<RecyclerViewProperties> = emptyList(),
    val listOfColoredDates: List<ColoredDates> = emptyList(),
)

data class ColoredDates(
    val date: Date,
    val colorDrawable: ColorDrawable
)



