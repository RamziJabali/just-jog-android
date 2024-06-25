package ramzi.eljabali.justjog.ui.views

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jaikeerthick.composable_graphs.composables.bar.BarGraph
import com.jaikeerthick.composable_graphs.composables.bar.model.BarData
import com.jaikeerthick.composable_graphs.composables.bar.style.BarGraphColors
import com.jaikeerthick.composable_graphs.composables.bar.style.BarGraphStyle
import com.jaikeerthick.composable_graphs.composables.bar.style.BarGraphVisibility
import com.jaikeerthick.composable_graphs.style.LabelPosition
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.Line
import ramzi.eljabali.justjog.ui.design.Spacing
import ramzi.eljabali.justjog.ui.design.primaryTextColor

@Composable
fun MockView() {
    LineChart(
        modifier = Modifier.fillMaxSize().padding(horizontal = 22.dp),
        data = listOf(
            Line(
                label = "Windows",
                values = listOf(28.0,41.0,5.0,10.0,35.0),
                color = SolidColor(Color(0xFF23af92)),
                firstGradientFillColor = Color(0xFF2BC0A1).copy(alpha = .5f),
                secondGradientFillColor = Color.Transparent,
                strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                gradientAnimationDelay = 1000,
                drawStyle = DrawStyle.Stroke(width = 2.dp),
            )
        ),
        animationMode = AnimationMode.Together(delayBuilder = {
            it * 500L
        }),
    )
}
@Composable
fun MockView2(){
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

@Composable
@Preview(backgroundColor = 0xFF121212)
fun MockViewPreview() {
    MockView()
//    MockView2()
}