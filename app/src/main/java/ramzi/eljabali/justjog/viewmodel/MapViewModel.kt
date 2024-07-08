package ramzi.eljabali.justjog.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Point
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ramzi.eljabali.justjog.usecase.JogUseCase
import ramzi.eljabali.justjog.viewstate.MapViewState

class MapViewModel(private val jogUseCase: JogUseCase) : ViewModel() {
    private val _mapViewState = MutableStateFlow(MapViewState(listOf()))
    val mapViewState = _mapViewState.asStateFlow()

    fun getJogs(jogSummaryId: Int) {
        viewModelScope.launch {
            val listOfJogEntries =
                jogUseCase.getJogEntriesFromJogSummaryId(jogSummaryId).first()
            Log.i("MapViewModel", "getJogs: $listOfJogEntries")
            val listOfPoints = listOfJogEntries.map {
                Point.fromLngLat(it.latLng.longitude, it.latLng.latitude)
            }
            Log.i("MapViewModel", "convertedJogs: $listOfPoints")
            _mapViewState.value = MapViewState(listOfPoints)
        }
    }
}