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
import com.eljabali.joggingapplicationandroid.util.getTag
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel

class JogStatisticsFragment : Fragment(), ViewListener {

    companion object {
        val TAG: String = JogStatisticsFragment.getTag()
        fun newInstance() = JogStatisticsFragment()
    }

    private val statisticsViewModel: StatisticsViewModel by viewModel()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var binding: StatisticsFragmentBinding

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
        onLaunch()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun monitorStatisticsViewState() {
        statisticsViewModel.observableStatisticsViewState
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { statisticsViewState -> setNewViewState(statisticsViewState) },
                { error -> Log.e(TAG, error.localizedMessage, error) }
            )
            .addTo(compositeDisposable)
    }

    override fun setNewViewState(statisticsViewState: StatisticsViewState) {
        with(binding) {
            date.text = statisticsViewState.dateToday
            time.text = statisticsViewState.timeNow
            averageWeeklyMilage.text = statisticsViewState.weeklyAverageDistance
            jogEntry.text = statisticsViewState.todayLastJogDistance
        }
    }

    private fun onLaunch() {
        monitorStatisticsViewState()
        statisticsViewModel.onFragmentLaunch()
        with (binding) {
            startRun.setOnClickListener {
                activity?.startService(Intent(requireContext(), ForegroundService::class.java))
            }
            deleteAllRuns.setOnClickListener {
                statisticsViewModel.deleteAll()
            }
        }
        val homeActivity = activity as HomeActivity
        if (homeActivity.stopService) {
            homeActivity.stopService(Intent(requireContext(), ForegroundService::class.java))
        }
    }
}