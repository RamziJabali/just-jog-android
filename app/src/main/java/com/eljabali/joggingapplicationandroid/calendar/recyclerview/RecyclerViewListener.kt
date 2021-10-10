package com.eljabali.joggingapplicationandroid.calendar.recyclerview

import com.eljabali.joggingapplicationandroid.calendar.recyclerviewmodel.RecyclerViewState

interface RecyclerViewListener {
    fun setRecyclerViewState(recyclerViewState: RecyclerViewState)
    fun monitorRecyclerViewState()
}