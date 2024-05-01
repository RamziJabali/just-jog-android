package ramzi.eljabali.justjog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import com.jaikeerthick.composable_graphs.composables.line.model.LineData
import ramzi.eljabali.justjog.ui.design.JustJogTheme
import ramzi.eljabali.justjog.ui.views.CalendarPage
import ramzi.eljabali.justjog.ui.views.JoggingFAB
import ramzi.eljabali.justjog.ui.views.StatisticsPage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        val data = listOf(
            LineData(x = "Mon", y = 40),
            LineData(x = "Tues", y = 60),
            LineData(x = "Wed", y = 70),
            LineData(x = "Thurs", y = 120),
            LineData(x = "Fri", y = 80),
            LineData(x = "Sat", y = 60),
            LineData(x = "Sun", y = 150),
        )
        setContent {
            JustJogTheme(true) {
                Scaffold(
//                bottomBar = bottomBar,
//                snackbarHost = snackbarHost,
                    floatingActionButton = { JoggingFAB() },
                    floatingActionButtonPosition = FabPosition.EndOverlay,
                ) { it
//                    StatisticsPage(
//                        motivationalQuote = "Awareness is the only density, the only guarantee of affirmation.",
//                        data = data
//                    )
                    CalendarPage()
                }
            }
        }
    }
}
