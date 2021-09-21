package com.eljabali.joggingapplicationandroid.mainviewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.eljabali.joggingapplicationandroid.mainview.ViewState
import com.eljabali.joggingapplicationandroid.usecase.UseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class ViewModel(application: Application, private val useCase: UseCase) :
    AndroidViewModel(application) {

    companion object {
        const val VM_TAG = "ViewModel"
    }

    private var viewState = ViewState()
    private var compositeDisposable = CompositeDisposable()

    val viewStateObservable = BehaviorSubject.create<ViewState>()


    fun getAllEntries() {
        compositeDisposable.add(
            useCase.getAllJogs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { listOfModifiedJogInformation ->
                        viewState =
                            viewState.copy(listOfModifiedDates = listOfModifiedJogInformation)
                        invalidateView()
                    },
                    { error -> Log.e(VM_TAG, error.localizedMessage, error) }
                ))
    }

    private fun invalidateView() {
        viewStateObservable.onNext(viewState)
    }

}