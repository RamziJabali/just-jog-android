package com.eljabali.joggingapplicationandroid.usecase

import android.graphics.drawable.ColorDrawable
import com.google.android.gms.maps.model.LatLng
import java.util.*

data class JogDateInformation(
    val date: Date,
    val runNumber: Int,
    val backgroundColor: ColorDrawable,
    val time: Double,
    val latitudeLongitude: LatLng
)