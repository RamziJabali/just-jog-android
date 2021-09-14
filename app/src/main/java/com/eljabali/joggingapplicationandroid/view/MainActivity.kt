package com.eljabali.joggingapplicationandroid.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.eljabali.joggingapplicationandroid.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.roomorama.caldroid.CaldroidFragment
import com.roomorama.caldroid.CaldroidListener
import java.util.*

class MainActivity : AppCompatActivity() {

    private val bottomNavigationBarView: BottomNavigationView by lazy { findViewById(R.id.bottom_navigation) }

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
        setupCalendar()
    }

    private fun setupBottomNavigation() {
        bottomNavigationBarView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.statistics_page -> {
                    // Respond to navigation item 1 click
                    Toast.makeText(this, "MusicPage selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.calendar_page -> {
                    // Respond to navigation item 2 click
                    Toast.makeText(this, "SettingPage selected", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        bottomNavigationBarView.setOnItemReselectedListener { item ->
            when (item.itemId) {
                R.id.statistics_page -> {
                    Toast.makeText(this, "MusicPage reselected", Toast.LENGTH_SHORT).show()
                }
                R.id.calendar_page -> {
                    Toast.makeText(this, "SettingPage reselected", Toast.LENGTH_SHORT).show()
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
        supportFragmentManager.beginTransaction()
            .replace(R.id.calendarView, caldroidFragment)
            .commit()
    }
}