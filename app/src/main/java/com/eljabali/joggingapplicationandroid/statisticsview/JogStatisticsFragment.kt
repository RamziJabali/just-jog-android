package com.eljabali.joggingapplicationandroid.statisticsview

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.eljabali.joggingapplicationandroid.R
import com.eljabali.joggingapplicationandroid.statisticsviewmodel.StatisticsViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel
import zoneddatetime.extensions.print
import java.time.ZonedDateTime

class JogStatisticsFragment : Fragment(), ViewListener {

    companion object {
        const val TAG = "Statistics Fragment"
        fun newInstance() = JogStatisticsFragment()
    }

    private val statisticsViewModel: StatisticsViewModel by viewModel()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val dateTextView: TextView by lazy { requireView().findViewById(R.id.date) }
    private val timeTextView: TextView by lazy { requireView().findViewById(R.id.time) }
    private val jogEntryTextView: TextView by lazy { requireView().findViewById(R.id.jog_entry) }
    private val startRunButton: Button by lazy { requireView().findViewById(R.id.start_run) }
    private val deleteAllRunsButton: Button by lazy { requireView().findViewById(R.id.delete_all_runs) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_jog_statistics, container, false)
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume()")
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

    override fun setNewViewState(statisticsViewState: StatisticsViewState) {
        dateTextView.text = statisticsViewState.date
        timeTextView.text = statisticsViewState.time
    }

    private fun onLaunch() {
        monitorStatisticsViewState()
        statisticsViewModel.onFragmentLaunch()
//        statisticsViewModel.getAllJogs()

        startRunButton.setOnClickListener {
            statisticsViewModel.addJog()
        }

        deleteAllRunsButton.setOnClickListener {
            statisticsViewModel.deleteAll()
        }
    }
}