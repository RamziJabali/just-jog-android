package ramzi.eljabali.justjog.ui.views

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.jaikeerthick.composable_graphs.composables.line.LineGraph
import com.jaikeerthick.composable_graphs.composables.line.model.LineData
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphColors
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphStyle
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphVisibility
import com.jaikeerthick.composable_graphs.style.LabelPosition
import ramzi.eljabali.justjog.R
import ramzi.eljabali.justjog.ui.design.CardElevation
import ramzi.eljabali.justjog.ui.design.CardSize
import ramzi.eljabali.justjog.ui.design.FabElevation
import ramzi.eljabali.justjog.ui.design.FloatingActionButton
import ramzi.eljabali.justjog.ui.design.JustJogTheme
import ramzi.eljabali.justjog.ui.design.Spacing
import ramzi.eljabali.justjog.ui.design.Typography
import ramzi.eljabali.justjog.ui.design.errorDark
import ramzi.eljabali.justjog.ui.design.primaryDark
import ramzi.eljabali.justjog.ui.design.primaryTextColor
import ramzi.eljabali.justjog.ui.design.secondaryTextColor
import ramzi.eljabali.justjog.viewstate.StatisticsViewState


/*
TODO:
 Wire everything together when the time comes:
 state management etc
*/
@Composable
fun StatisticsPage(statisticsViewState: State<StatisticsViewState>) {
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.Surrounding.s),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(CardSize.xxl)
                .padding(vertical = Spacing.Vertical.s),
            elevation = CardDefaults.cardElevation(
                defaultElevation = CardElevation.default
            )
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Spacing.Vertical.xs),
                text = stringResource(R.string.app_name),
                style = Typography.titleSmall,
                color = secondaryTextColor,
                textAlign = TextAlign.Center,

                )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = statisticsViewState.value.quote,
                    style = Typography.titleLarge,
                    color = primaryTextColor,
                    textAlign = TextAlign.Center
                )
            }
        }
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = CardElevation.default
            )
        ) {
            LineGraph(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.Horizontal.s, vertical = Spacing.Vertical.m),
                data = statisticsViewState.value.lineDataList,
                style = LineGraphStyle(
                    visibility = LineGraphVisibility(
                        isYAxisLabelVisible = true,
                        isGridVisible = true,
                        isCrossHairVisible = true,
                        isXAxisLabelVisible = true
                    ),
                    yAxisLabelPosition = LabelPosition.LEFT,
                    colors = LineGraphColors(
                        lineColor = primaryDark,
                        pointColor = errorDark,
                        xAxisTextColor = primaryTextColor,
                        yAxisTextColor = primaryTextColor
                    )
                ),
                onPointClick = { value: LineData ->
                    // do something with value
                    Log.i("lineGraph", "Point clicked: $value")
                },
            )
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = Spacing.Vertical.s),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth(fraction = .5f)
                    .padding(end = Spacing.Horizontal.xs),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = CardElevation.default
                ),
            ) {
                Text(
                    text = stringResource(R.string.this_week),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Spacing.Vertical.s),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = statisticsViewState.value.weeklyStatisticsBreakDown[0],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Spacing.Vertical.s),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = statisticsViewState.value.weeklyStatisticsBreakDown[1],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Spacing.Vertical.s),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = statisticsViewState.value.weeklyStatisticsBreakDown[2],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Spacing.Vertical.s, bottom = Spacing.Vertical.s),
                    textAlign = TextAlign.Center
                )
            }

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Spacing.Horizontal.xs),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = CardElevation.default
                )
            ) {
                Text(
                    text = stringResource(R.string.average_per_run),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Spacing.Vertical.s),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = statisticsViewState.value.perJogStatisticsBreakDown[0],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Spacing.Vertical.s),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = statisticsViewState.value.perJogStatisticsBreakDown[1],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Spacing.Vertical.s),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = statisticsViewState.value.perJogStatisticsBreakDown[2],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Spacing.Vertical.s, bottom = Spacing.Vertical.s),
                    textAlign = TextAlign.Center
                )

            }
        }
    }
}

@Composable
fun JoggingFAB(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = {
            Log.i("fab", "Floating Action Button Pressed")
            onClick()
        },
        modifier = Modifier.size(FloatingActionButton.default),
        shape = CircleShape,
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = FabElevation.default)
    ) {
        Icon(painterResource(id = R.mipmap.just_jog_icon_foreground), "Floating action button.")
    }
}

@Preview(showBackground = true, backgroundColor = 0)
@Composable
fun PreviewStatisticsPage() {
    val data = listOf(
        LineData(x = "Mon", y = 0),
        LineData(x = "Tues", y = 0),
        LineData(x = "Wed", y = 0),
        LineData(x = "Thurs", y = 0),
        LineData(x = "Fri", y = 2.59999999),
        LineData(x = "Sat", y = 0),
        LineData(x = "Sun", y = 0),
    )
    JustJogTheme(true) {
        LineGraph(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.Horizontal.s, vertical = Spacing.Vertical.m),
            data = data,
            style = LineGraphStyle(
                visibility = LineGraphVisibility(
                    isYAxisLabelVisible = true,
                    isGridVisible = true,
                    isCrossHairVisible = true,
                    isXAxisLabelVisible = true
                ),
                yAxisLabelPosition = LabelPosition.LEFT,
                colors = LineGraphColors(
                    lineColor = primaryDark,
                    pointColor = errorDark,
                    xAxisTextColor = primaryTextColor,
                    yAxisTextColor = primaryTextColor
                )
            ),
            onPointClick = { value: LineData ->
                // do something with value
                Log.i("lineGraph", "Point clicked: $value")
            },
        )
    }
}