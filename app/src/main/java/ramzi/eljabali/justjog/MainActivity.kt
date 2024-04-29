package ramzi.eljabali.justjog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jaikeerthick.composable_graphs.composables.line.model.LineData
import ramzi.eljabali.justjog.ui.design.JustJogTheme
import ramzi.eljabali.justjog.ui.views.CalendarPage
import ramzi.eljabali.justjog.ui.views.StatisticsPage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JustJogTheme(true) {
                val data = listOf(
                    LineData(x = "Mon", y = 40),
                    LineData(x = "Tues", y = 60),
                    LineData(x = "Wed", y = 70),
                    LineData(x = "Thurs", y = 120),
                    LineData(x = "Fri", y = 80),
                    LineData(x = "Sat", y = 60),
                    LineData(x = "Sun", y = 150),
                )
//                StatisticsPage("Try your best until you succeed - RJ!", data)
                CalendarPage()
            }
        }
    }
}
