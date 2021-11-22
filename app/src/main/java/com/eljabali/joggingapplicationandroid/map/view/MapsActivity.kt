package com.eljabali.joggingapplicationandroid.map.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.eljabali.joggingapplicationandroid.R
import com.eljabali.joggingapplicationandroid.map.viewmodel.MapsViewModel
import com.eljabali.joggingapplicationandroid.map.viewmodel.MapsViewState
import com.eljabali.joggingapplicationandroid.util.DateFormat
import com.eljabali.joggingapplicationandroid.util.TAG
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
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
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setupViewModel()
        monitorMapsViewState()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    private fun setupViewModel() {
        val runID = intent.getIntExtra(RUN_ID, -1)
        val localDate = intent.getStringExtra(DATE_ID)?.parseLocalDate() ?: throw RuntimeException()
        mapsViewModel.getAllJogsAtSpecificDate(runID = runID, localDate = localDate)
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

    private fun monitorMapsViewState() {
        mapsViewModel.mapsViewStateObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { viewState ->
                    setMapsViewState(viewState)
                },
                { error -> Log.e(TAG, error.localizedMessage, error) })
            .addTo(compositeDisposable)
    }
}