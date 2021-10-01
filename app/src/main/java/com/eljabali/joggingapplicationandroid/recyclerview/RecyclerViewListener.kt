package com.eljabali.joggingapplicationandroid.recyclerview

import com.eljabali.joggingapplicationandroid.recyclerviewmodel.RecyclerViewState

interface RecyclerViewListener {
    fun setRecyclerViewState(recyclerViewState: RecyclerViewState)
    fun monitorRecyclerViewState()
}