package com.eljabali.joggingapplicationandroid.home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.eljabali.joggingapplicationandroid.R
import com.eljabali.joggingapplicationandroid.calendar.jogsummaries.JogSummariesFragment
import com.eljabali.joggingapplicationandroid.statistics.view.StatisticsFragment
import com.eljabali.joggingapplicationandroid.util.PermissionUtil
import com.eljabali.joggingapplicationandroid.util.TAG
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.roomorama.caldroid.CaldroidFragment
import com.roomorama.caldroid.CaldroidListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel
import zoneddatetime.ZonedDateTimes
import java.util.*

class HomeActivity : AppCompatActivity() {

    companion object {
        const val STOP_SERVICE_KEY = "STOP_SERVICE_KEY"
        private const val CAL_TAG = "CaldroidFragment.TAG"
        private const val FINE_LOCATION_RQ = 101
        private const val COARSE_LOCATION_RQ = 102
    }

    private var shouldStopService = false
    private var homeViewState: HomeViewState = HomeViewState()
    private val homeViewModel: HomeViewModel by viewModel()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val bottomNavigationBarView: BottomNavigationView by lazy { findViewById(R.id.bottom_navigation) }
    private val statisticsFragment: StatisticsFragment by lazy {
        checkForPermissions(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            "Fine Location",
            FINE_LOCATION_RQ
        )
        checkForPermissions(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            "Coarse Location",
            COARSE_LOCATION_RQ
        )
        StatisticsFragment.newInstance(shouldStopService)
    }
    private val jogSummariesFragment: JogSummariesFragment by lazy { JogSummariesFragment.newInstance() }
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
        setContentView(R.layout.activity_home)
        shouldStopService = intent.getBooleanExtra(STOP_SERVICE_KEY, false)
        setupBottomNavigation()
        setupFragments()
        setupCalendarListener()
        monitorCalendarViewState()
    }

    private fun monitorCalendarViewState() {
        homeViewModel.viewStateObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { viewState ->
                    this.homeViewState = viewState
                    setNewViewState(viewState)
                },
                { error -> Log.e(HomeViewModel.TAG, error.localizedMessage, error) })
            .addTo(compositeDisposable)
    }

    private fun setNewViewState(homeViewState: HomeViewState) {
        homeViewState.listOfColoredDates.forEach { date ->
            caldroidFragment.setBackgroundDrawableForDate(date.colorDrawable, date.date)
        }
        jogSummariesFragment.updateListOfProperties(homeViewState.listOfSpecificDates)
        caldroidFragment.refreshView()
    }

    private fun setupFragments() {
        supportFragmentManager.beginTransaction()
            .add(R.id.frameLayout, caldroidFragment, CAL_TAG)
            .hide(caldroidFragment)
            .commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.recycler_view_frame_layout, jogSummariesFragment, JogSummariesFragment.TAG)
            .hide(jogSummariesFragment)
            .commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.frameLayout, statisticsFragment, StatisticsFragment.TAG)
            .commit()
    }

    private fun setupBottomNavigation() {
        bottomNavigationBarView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.statistics_page -> {
                    statisticsFragment.refreshPage()
                    supportFragmentManager.beginTransaction()
                        .hide(caldroidFragment)
                        .hide(jogSummariesFragment)
                        .show(statisticsFragment)
                        .commit()
                    true
                }
                R.id.calendar_page -> {
                    homeViewModel.getAllDates()
                    supportFragmentManager.beginTransaction()
                        .hide(statisticsFragment)
                        .show(caldroidFragment)
                        .commit()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupCalendarListener() {
        caldroidFragment.caldroidListener = object : CaldroidListener() {
            override fun onSelectDate(date: Date, view: View?) {
                supportFragmentManager.beginTransaction()
                    .show(jogSummariesFragment)
                    .commit()
                homeViewModel.getAllJogSummariesAtSpecificDate(date)
            }
        }
    }

    private fun checkForPermissions(permission: String, name: String, requestCode: Int) {
        when {
            PermissionUtil.isGranted(this, permission) -> Log.i(CAL_TAG, "Permission $name Granted")
            shouldShowRequestPermissionRationale(permission) -> showDialog(
                permission,
                name,
                requestCode
            )
            else -> PermissionUtil.request(this, arrayOf(permission), requestCode)
        }
    }

    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val dialog = AlertDialog.Builder(this)
            .setMessage("Permission to access your $name is required to use this app")
            .setTitle("Permission Required")
            .setPositiveButton("OK") { dialog, which ->
                PermissionUtil.request(this, arrayOf(permission), requestCode)
            }
            .create()
        dialog.show()
    }
}