package com.eljabali.joggingapplicationandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val bottomNavigationBarView: BottomNavigationView by lazy { findViewById(R.id.bottom_navigation) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationSetup()
    }

    private fun bottomNavigationSetup() {
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
}