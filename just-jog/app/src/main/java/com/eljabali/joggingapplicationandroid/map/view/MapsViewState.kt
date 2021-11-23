package com.eljabali.joggingapplicationandroid.map.view

import com.google.android.gms.maps.model.LatLng

data class MapsViewState(
    val listOfLatLng: List<LatLng> = emptyList(),
)