package com.eljabali.joggingapplicationandroid.map.view

import com.eljabali.joggingapplicationandroid.map.viewmodel.MapsViewState

interface MapsViewListener {
    fun setMapsViewState(mapsViewState: MapsViewState)
    fun monitorMapsViewState()
}