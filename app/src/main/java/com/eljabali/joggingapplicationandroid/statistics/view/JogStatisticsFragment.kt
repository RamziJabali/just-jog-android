package com.eljabali.joggingapplicationandroid.statistics.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.eljabali.joggingapplicationandroid.calendar.mainview.HomeActivity
import com.eljabali.joggingapplicationandroid.databinding.StatisticsFragmentBinding
import com.eljabali.joggingapplicationandroid.services.ForegroundService
import com.eljabali.joggingapplicationandroid.statistics.viewmodel.StatisticsViewModel
import com.eljabali.joggingapplicationandroid.util.TAG
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel

class JogStatisticsFragment : Fragment() {

    companion object {
        private const val SHOULD_STOP_SERVICE_KEY = "SHOULD_STOP_SERVICE_KEY"

        fun newInstance(shouldStopService: Boolean) = JogStatisticsFragment().apply {
            arguments = Bundle().apply {
                putBoolean(SHOULD_STOP_SERVICE_KEY, shouldStopService)
            }
        }
    }

    private val statisticsViewModel: StatisticsViewModel by viewModel()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var binding: StatisticsFragmentBinding
    private var shouldStopService = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shouldStopService = arguments?.getBoolean(SHOULD_STOP_SERVICE_KEY) ?: false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = StatisticsFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume()")
        statisticsViewModel.onFragmentLaunch()
        monitorStatisticsViewState()
        setClickListeners()
        if (shouldStopService) {
            val homeActivity = activity as HomeActivity
            homeActivity.stopService(Intent(requireContext(), ForegroundService::class.java))
        }
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    private fun monitorStatisticsViewState() {
        statisticsViewModel.observableStatisticsViewState
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { statisticsViewState -> setNewViewState(statisticsViewState) },
                { error -> Log.e(TAG, error.localizedMessage, error) }
            )
            .addTo(compositeDisposable)
    }

    private fun setClickListeners() {
        with (binding) {
            startRunButton.setOnClickListener {
                activity?.startService(Intent(requireContext(), ForegroundService::class.java))
            }
            deleteAllRunsButton.setOnClickListener {
                statisticsViewModel.deleteAll()
            }
        }
    }

    private fun setNewViewState(statisticsViewState: StatisticsViewState) {
        with(binding) {
            date.text = statisticsViewState.dateToday
            time.text = statisticsViewState.timeNow
            averageWeeklyMilage.text = statisticsViewState.weeklyAverageDistance
            jogEntry.text = statisticsViewState.todayLastJogDistance
        }
    }
}