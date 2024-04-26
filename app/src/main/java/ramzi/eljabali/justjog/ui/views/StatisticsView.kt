package ramzi.eljabali.justjog.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.jaikeerthick.composable_graphs.composables.line.LineGraph
import com.jaikeerthick.composable_graphs.composables.line.model.LineData
import ramzi.eljabali.justjog.ui.design.CardElevation
import ramzi.eljabali.justjog.ui.design.CardSize
import ramzi.eljabali.justjog.ui.design.Spacing
import ramzi.eljabali.justjog.ui.design.Typography
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
                    .height(CardSize.xl)
                    .padding(vertical = Spacing.Vertical.s),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = CardElevation.default
                )
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Just Jog",
                    style = Typography.headlineSmall,
                    color = secondaryTextColor,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = motivationalQuote,
                    style = Typography.headlineMedium,
                    color = primaryTextColor,
                    textAlign = TextAlign.Center
                )
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
                        .padding(horizontal = Spacing.Horizontal.s, vertical = Spacing.Vertical.s),
                    data = data,
                    onPointClick = { value: LineData ->
                        // do something with value
                    },
                )
            }
        }
    }


@Preview(showBackground = true)
@Composable
fun PreviewStatisticsPage() {
    val data = listOf(LineData(x = "Sun", y = 200), LineData(x = "Mon", y = 40))
    StatisticsPage("Try your best until you succeed - RJ!", data)
}