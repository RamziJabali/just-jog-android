package com.eljabali.joggingapplicationandroid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.eljabali.joggingapplicationandroid.usecase.UseCase

class ViewModel(application: Application, private val UseCase: UseCase): AndroidViewModel(application) {

    public fun onAppStart(){

    }
}