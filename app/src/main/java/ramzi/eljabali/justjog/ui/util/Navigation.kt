package ramzi.eljabali.justjog.ui.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jaikeerthick.composable_graphs.composables.line.model.LineData
import ramzi.eljabali.justjog.ui.views.JustJogCalendarView
import ramzi.eljabali.justjog.ui.views.StatisticsPage

@Composable
fun JustJogNavigation(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination = StatisticsScreen) {
        composable<StatisticsScreen> {
            StatisticsPage(
                motivationalQuote = "Awareness is the only density, the only guarantee of affirmation.",
                data = listOf(
                    LineData(x = "Mon", y = 40),
                    LineData(x = "Tues", y = 60),
                    LineData(x = "Wed", y = 70),
                    LineData(x = "Thurs", y = 120),
                    LineData(x = "Fri", y = 80),
                    LineData(x = "Sat", y = 60),
                    LineData(x = "Sun", y = 150),
                ),
            )
        }
        composable<CalendarScreen> {
            JustJogCalendarView()
        }
    }
}