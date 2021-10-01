package com.eljabali.joggingapplicationandroid.mapsview

import com.eljabali.joggingapplicationandroid.mapsviewmodel.MapsViewState

interface MapsViewListener {
    fun setMapsViewState(mapsViewState: MapsViewState)
    fun monitorMapsViewState()
}