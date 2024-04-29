package ramzi.eljabali.justjog.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.layout.VerticalAlignmentLine
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
import ramzi.eljabali.justjog.ui.design.JustJogTheme
import ramzi.eljabali.justjog.ui.design.Spacing
import ramzi.eljabali.justjog.ui.design.Typography
import ramzi.eljabali.justjog.ui.design.errorContainerDarkHighContrast
import ramzi.eljabali.justjog.ui.design.primaryContainerDarkHighContrast
import ramzi.eljabali.justjog.ui.design.primaryTextColor
import ramzi.eljabali.justjog.ui.design.secondaryTextColor


/*
TODO:
 Wire everything together when the time comes:
 state management etc
*/
@Composable
fun StatisticsPage(motivationalQuote: String, data: List<LineData>) {
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.Horizontal.s, vertical = Spacing.Vertical.m),
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
                text = stringResource(R.string.just_jog),
                style = Typography.titleSmall,
                color = secondaryTextColor,
                textAlign = TextAlign.Center,

                )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = motivationalQuote,
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
                data = data,
                style = LineGraphStyle(
                    visibility = LineGraphVisibility(
                        isYAxisLabelVisible = true,
                        isGridVisible = true,
                        isCrossHairVisible = true
                    ),
                    yAxisLabelPosition = LabelPosition.LEFT,
                    colors = LineGraphColors(
                        lineColor = primaryContainerDarkHighContrast,
                        pointColor = errorContainerDarkHighContrast,
                        xAxisTextColor = primaryTextColor,
                        yAxisTextColor = primaryTextColor
                    )

                ),
                onPointClick = { value: LineData ->
                    // do something with value
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
                    text = "6 Runs",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Spacing.Vertical.s),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "24.81 Miles",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Spacing.Vertical.s),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "26m 55s",
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
                Text(text = stringResource(R.string.average_per_run),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Spacing.Vertical.s),
                    textAlign = TextAlign.Center)
                Text(text = "3.54 Miles",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Spacing.Vertical.s),
                    textAlign = TextAlign.Center)
                Text(text = "3:00 Min/Mil",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Spacing.Vertical.s),
                    textAlign = TextAlign.Center)
                Text(
                    text = "3m 50s",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Spacing.Vertical.s, bottom = Spacing.Vertical.s),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0)
@Composable
fun PreviewStatisticsPage() {
    val data = listOf(
        LineData(x = "Mon", y = 40),
        LineData(x = "Tues", y = 60),
        LineData(x = "Wed", y = 70),
        LineData(x = "Thurs", y = 120),
        LineData(x = "Fri", y = 80),
        LineData(x = "Sat", y = 60),
        LineData(x = "Sun", y = 150),
    )
    JustJogTheme(true) {
        StatisticsPage("Try your best until you succeed - RJ!", data)
    }
}