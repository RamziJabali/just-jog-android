package com.eljabali.joggingapplicationandroid.map.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.eljabali.joggingapplicationandroid.R
import com.eljabali.joggingapplicationandroid.map.viewmodel.MapsViewModel
import com.eljabali.joggingapplicationandroid.util.DateFormat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import localdate.extensions.parseLocalDate
import localdate.extensions.print
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        private const val ZOOM_LEVEL = 13f
        private const val RUN_ID = "RUN_ID"
        private const val DATE_ID = "DATE_ID"

        fun newInstance(context: Context, localDate: LocalDate, runID: Int): Intent =
            Intent(context, MapsActivity::class.java).apply {
                putExtra(RUN_ID, runID)
                putExtra(DATE_ID, localDate.print(DateFormat.YYYY_MM_DD.format))
            }
    }

    private lateinit var map: GoogleMap
    private val mapsViewModel: MapsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupObserver() {
        setupViewModel()
        val mapsViewStateObserver = Observer<MapsViewState> { mapsViewState ->
            setMapsViewState(mapsViewState)
        }
        mapsViewModel.mapsLiveData.observe(this, mapsViewStateObserver)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        setupViewModel()
        setupObserver()
    }

    private fun setupViewModel() {
        val runID = intent.getIntExtra(RUN_ID, -1)
        val localDate = intent.getStringExtra(DATE_ID)?.parseLocalDate() ?: throw RuntimeException()
        mapsViewModel.getAllJogsAtSpecificID(runID = runID)
    }

    private fun setMapsViewState(mapsViewState: MapsViewState) {
        map.apply {
            addPolyline(
                PolylineOptions()
                    .clickable(false)
                    .color(R.color.light_blue)
                    .addAll(mapsViewState.listOfLatLng)
            )
            addMarker(
                MarkerOptions()
                    .position(mapsViewState.listOfLatLng[0])
                    .title(getString(R.string.start))
            )
            addMarker(
                MarkerOptions()
                    .position(mapsViewState.listOfLatLng[mapsViewState.listOfLatLng.lastIndex])
                    .title(getString(R.string.end))
            )
            animateCamera(
                CameraUpdateFactory.newLatLngZoom(mapsViewState.listOfLatLng[0], ZOOM_LEVEL)
            )
        }
    }
}