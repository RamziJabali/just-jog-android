package com.eljabali.joggingapplicationandroid.mainview

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.eljabali.joggingapplicationandroid.usecase.ModifiedJogDateInformation
import java.util.*

data class ViewState(
    val listOfModifiedDates: List<ModifiedJogDateInformation> = emptyList(),
    val listOfColoredDates: List<ColoredDates> = emptyList(),
)

data class ColoredDates(
    val date: Date,
    val colorDrawable: ColorDrawable
)


