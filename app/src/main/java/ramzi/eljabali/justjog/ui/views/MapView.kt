package ramzi.eljabali.justjog.ui.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotation
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyle
import ramzi.eljabali.justjog.viewstate.MapViewState

@OptIn(MapboxExperimental::class)
@Composable
fun MapView(mapViewState: State<MapViewState>, navController: NavController) {
    if (mapViewState.value.listOfPoints.isNotEmpty()) {
        MapboxMap(
            modifier = Modifier.fillMaxSize(),
            mapViewportState = MapViewportState().apply {
                setCameraOptions {
                    zoom(15.0)
                    center(mapViewState.value.listOfPoints[0])
                    pitch(0.0)
                    bearing(0.0)
                }
            },
            style = {
                PolylineAnnotation(
                    mapViewState.value.listOfPoints
                )
                MapboxStandardStyle()
            }
        )
    }
}