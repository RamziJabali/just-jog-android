package com.eljabali.joggingapplicationandroid.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.eljabali.joggingapplicationandroid.JogStatisticsFragment
import com.eljabali.joggingapplicationandroid.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.roomorama.caldroid.CaldroidFragment
import com.roomorama.caldroid.CaldroidListener
import java.util.*

class MainActivity : AppCompatActivity() {

    private val bottomNavigationBarView: BottomNavigationView by lazy { findViewById(R.id.bottom_navigation) }

    private val statisticsFragment: JogStatisticsFragment by lazy {  JogStatisticsFragment.newInstance() }
    private val caldroidFragment: CaldroidFragment by lazy {
        val calendar = Calendar.getInstance()
        CaldroidFragment().apply {
            arguments = Bundle().apply {
                putInt(CaldroidFragment.MONTH, calendar.get(Calendar.MONTH) + 1)
                putInt(CaldroidFragment.YEAR, calendar.get(Calendar.YEAR))
                putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, false)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBottomNavigation()
        setupStatisticsPage()
        setupCalendar()
    }

    private fun setupStatisticsPage() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout,statisticsFragment)
            .commit()
    }

    private fun setupBottomNavigation() {
        bottomNavigationBarView.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.statistics_page -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, statisticsFragment)
                        .addToBackStack("StatisticsFragment")
                        .commit()
                    true
                }

                R.id.calendar_page -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, caldroidFragment,"CaldroidFragment")
                        .addToBackStack("CaldroidFragment")
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
                    // Respond to navigation item 2 reselection
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