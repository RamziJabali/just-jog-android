package ramzi.eljabali.justjog.ui.views

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.jaikeerthick.composable_graphs.composables.bar.BarGraph
import com.jaikeerthick.composable_graphs.composables.bar.model.BarData
import com.jaikeerthick.composable_graphs.composables.bar.style.BarGraphColors
import com.jaikeerthick.composable_graphs.composables.bar.style.BarGraphStyle
import com.jaikeerthick.composable_graphs.composables.bar.style.BarGraphVisibility
import com.jaikeerthick.composable_graphs.style.LabelPosition
import com.mapbox.geojson.Point
import com.mapbox.maps.Image
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotation
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyle
import com.mapbox.maps.extension.style.image.image
import ramzi.eljabali.justjog.R
import ramzi.eljabali.justjog.ui.design.Spacing
import ramzi.eljabali.justjog.ui.design.primaryTextColor

@Composable
fun MockView2() {
    BarGraph(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.Horizontal.s, vertical = Spacing.Vertical.m),
        data = listOf(
            BarData(x = "Mon", y = 0),
            BarData(x = "Tues", y = 9),
            BarData(x = "Wed", y = 0),
            BarData(x = "Thurs", y = 3.5F),
            BarData(x = "Fri", y = 0),
            BarData(x = "Sat", y = 0),
            BarData(x = "Sun", y = 0),
        ),
        style = BarGraphStyle(
            visibility = BarGraphVisibility(
                isYAxisLabelVisible = true,
                isGridVisible = true,
                isXAxisLabelVisible = true
            ),
            yAxisLabelPosition = LabelPosition.LEFT,
            colors = BarGraphColors(
                xAxisTextColor = primaryTextColor,
                yAxisTextColor = primaryTextColor
            )
        ),
    )
}

@OptIn(MapboxExperimental::class)
@Composable
fun Test() {
    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        mapViewportState = MapViewportState().apply {
            setCameraOptions {
                zoom(10.0)
                center(Point.fromLngLat(-98.0, 39.5))
                pitch(0.0)
                bearing(0.0)
            }
        },
        style = {
            PolylineAnnotation(
                listOf(
                    Point.fromLngLat(-98.0, 39.5),
                    Point.fromLngLat(-97.0, 38.5),
                    Point.fromLngLat(-96.0, 37.5),
                    Point.fromLngLat(-95.0, 36.5),
                    Point.fromLngLat(-94.0, 39.5)
                )
            )

            MapboxStandardStyle()
        }
    )
}

@Composable
@Preview(backgroundColor = 0xFF121212)
fun MockViewPreview() {
    Test()
}