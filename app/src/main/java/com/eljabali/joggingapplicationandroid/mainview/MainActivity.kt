package com.eljabali.joggingapplicationandroid.mainview

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.eljabali.joggingapplicationandroid.R
import com.eljabali.joggingapplicationandroid.statisticsview.JogStatisticsFragment
import com.eljabali.joggingapplicationandroid.mainviewmodel.ViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.roomorama.caldroid.CaldroidFragment
import com.roomorama.caldroid.CaldroidListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel
import zoneddatetime.ZonedDateTimes
import java.util.Date

class MainActivity : AppCompatActivity(), ViewListener {

    companion object {
        const val CAL_TAG = "CaldroidFragment"
        const val MVM_TAG = "Main ViewModel"
    }

    private val viewModel: ViewModel by viewModel()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val bottomNavigationBarView: BottomNavigationView by lazy { findViewById(R.id.bottom_navigation) }
    private val statisticsFragment: JogStatisticsFragment by lazy { JogStatisticsFragment.newInstance() }
    private val caldroidFragment: CaldroidFragment by lazy {
        val today = ZonedDateTimes.today
        CaldroidFragment().apply {
            arguments = Bundle().apply {
                putInt(CaldroidFragment.MONTH, today.monthValue)
                putInt(CaldroidFragment.YEAR, today.year)
                putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, false)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBottomNavigation()
        setupPages()
        setupCalendar()
        monitorCalendarViewState()
    }

    override fun setNewViewState(viewState: ViewState) {
        viewState.listOfDates.forEach { date ->
            caldroidFragment.setBackgroundDrawableForDate(date.colorDrawable, date.date)
        }
        caldroidFragment.refreshView()
    }

    override fun monitorCalendarViewState() {
        compositeDisposable.add(
            viewModel.viewStateObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { viewState -> setNewViewState(viewState) },
                    { error -> Log.e(MVM_TAG, error.localizedMessage, error) })
        )
    }


    private fun setupPages() {
        supportFragmentManager.beginTransaction()
            .add(R.id.frameLayout, caldroidFragment, CAL_TAG)
            .hide(caldroidFragment)
            .commit()

        supportFragmentManager.beginTransaction()
            .add(R.id.frameLayout, statisticsFragment, JogStatisticsFragment.TAG)
            .commit()
    }

    private fun setupBottomNavigation() {
        bottomNavigationBarView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.statistics_page -> {
                    supportFragmentManager.beginTransaction()
                        .hide(caldroidFragment)
                        .show(statisticsFragment)
                        .commit()
                    true
                }
                R.id.calendar_page -> {
                    viewModel.getAllEntries()
                    supportFragmentManager.beginTransaction()
                        .hide(statisticsFragment)
                        .show(caldroidFragment)
                        .commit()
                    true
                }
                else -> false
            }
        }

        bottomNavigationBarView.setOnItemReselectedListener { item ->
            when (item.itemId) {
                R.id.statistics_page -> {
                    Toast.makeText(this, "StatsPage reselected", Toast.LENGTH_SHORT).show()
                }
                R.id.calendar_page -> {
                    Toast.makeText(this, "CalendarPage reselected", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupCalendar() {
        caldroidFragment.caldroidListener = object : CaldroidListener() {
            override fun onSelectDate(date: Date, view: View?) {

            }
        }
    }
}