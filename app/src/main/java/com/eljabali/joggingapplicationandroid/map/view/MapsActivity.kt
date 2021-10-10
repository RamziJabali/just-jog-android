package com.eljabali.joggingapplicationandroid.map.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.eljabali.joggingapplicationandroid.R
import com.eljabali.joggingapplicationandroid.util.DateFormat
import com.eljabali.joggingapplicationandroid.map.viewmodel.MapsViewModel
import com.eljabali.joggingapplicationandroid.map.viewmodel.MapsViewState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import localdate.extensions.parseLocalDate
import localdate.extensions.print
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.RuntimeException
import java.time.LocalDate

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, MapsViewListener {
    companion object {
        const val MA_TAG = "MapsActivity"
        const val ZOOM_LEVEL = 13f
        const val RUN_ID = "com.eljabali.joggingapplicationandroid.map.mapsview"
        const val DATE_ID = "GOOGLYMOOGLY"

        fun newInstance(context: Context, localDate: LocalDate, runID: Int): Intent =
            Intent(context, MapsActivity::class.java).apply {
                putExtra(RUN_ID, runID)
                putExtra(DATE_ID, localDate.print(DateFormat.YYYY_MM_DD.format))
            }
    }

    private lateinit var mMap: GoogleMap
    private val mapsViewModel: MapsViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        monitorMapsViewState()
        val runID = intent.getIntExtra(RUN_ID, -1)
        val localDate = intent.getStringExtra(DATE_ID)?.parseLocalDate() ?: throw RuntimeException()

        mapsViewModel.getAllJogsAtSpecificDate(runID = runID, localDate = localDate)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    override fun setMapsViewState(mapsViewState: MapsViewState) {
        mapsViewState.listOfLatLng.forEach { latLng ->
            mMap.addMarker(MarkerOptions().position(latLng))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL))
        }
    }

    override fun monitorMapsViewState() {
        compositeDisposable.add(
            mapsViewModel.mapsViewStateObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { viewState ->
                        setMapsViewState(viewState)
                    },
                    { error -> Log.e(MA_TAG, error.localizedMessage, error) })
        )
    }
}