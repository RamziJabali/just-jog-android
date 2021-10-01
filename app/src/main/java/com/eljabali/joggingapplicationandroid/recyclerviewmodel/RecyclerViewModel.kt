package com.eljabali.joggingapplicationandroid.recyclerviewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.subjects.BehaviorSubject
import localdate.extensions.parseLocalDate

class RecyclerViewModel : ViewModel() {
    private var recyclerViewState: RecyclerViewState = RecyclerViewState()
    val recyclerViewStateObservable = BehaviorSubject.create<RecyclerViewState>()

}