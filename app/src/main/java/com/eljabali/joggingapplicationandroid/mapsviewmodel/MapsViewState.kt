package com.eljabali.joggingapplicationandroid.mapsviewmodel

import com.google.android.gms.maps.model.LatLng

data class MapsViewState(
    val listOfLatLng: List<LatLng> = emptyList()
)