package com.eljabali.joggingapplicationandroid.data.usecase

import com.google.android.gms.maps.model.LatLng
import java.time.ZonedDateTime

data class ModifiedJogDateInformation(
    val dateTime: ZonedDateTime,
    val runNumber: Int,
    val latitudeLongitude: LatLng,
)