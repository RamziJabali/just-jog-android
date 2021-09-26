package com.eljabali.joggingapplicationandroid.mainview

import android.graphics.drawable.ColorDrawable
import com.eljabali.joggingapplicationandroid.recyclerview.RecyclerViewProperties
import com.eljabali.joggingapplicationandroid.usecase.ModifiedJogDateInformation
import java.util.*

data class ViewState(
    val listOfSpecificDates: List<ModifiedJogDateInformation> = emptyList(),
    val listOfColoredDates: List<ColoredDates> = emptyList(),
    val listOfRecyclerViewProperties: List<RecyclerViewProperties> = emptyList()
)

data class ColoredDates(
    val date: Date,
    val colorDrawable: ColorDrawable
)


