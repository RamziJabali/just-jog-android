package com.eljabali.joggingapplicationandroid.calendar.recyclerviewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.subjects.BehaviorSubject

class RecyclerViewModel : ViewModel() {
    private var recyclerViewState: RecyclerViewState = RecyclerViewState()
    val recyclerViewStateObservable = BehaviorSubject.create<RecyclerViewState>()

}