package com.eljabali.joggingapplicationandroid.calendar.mainview

interface ViewListener {
    fun setNewViewState(viewState: ViewState)
    fun monitorCalendarViewState()
}