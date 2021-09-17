package com.eljabali.joggingapplicationandroid.statisticsview

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.eljabali.joggingapplicationandroid.R
import com.eljabali.joggingapplicationandroid.statisticsviewmodel.StatisticsViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class JogStatisticsFragment : Fragment(), ViewListener {

    companion object {
        const val TAG = "Statistics Fragment"
        fun newInstance() = JogStatisticsFragment()
    }

    private val statisticsViewModel: StatisticsViewModel by lazy { StatisticsViewModel() }
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val dateTextView: TextView by lazy { requireView().findViewById(R.id.date) }
    private val timeTextView: TextView by lazy { requireView().findViewById(R.id.time) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_jog_statistics, container, false)
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG,"onResume()")
        onLaunch()
    }

    override fun monitorStatisticsViewState() {
        compositeDisposable.add(
            statisticsViewModel.observableStatisticsViewState
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { statisticsViewState -> setNewViewState(statisticsViewState) },
                    { error -> Log.e(TAG, error.localizedMessage, error) }
                )
        )
    }

    private fun onLaunch() {
        statisticsViewModel.onFragmentLaunch()
        monitorStatisticsViewState()
    }

    override fun setNewViewState(statisticsViewState: StatisticsViewState) {
        dateTextView.text = statisticsViewState.date
        timeTextView.text = statisticsViewState.time
    }
}