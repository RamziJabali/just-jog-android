package ramzi.eljabali.justjog.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ramzi.eljabali.justjog.ui.util.CalendarScreen
import ramzi.eljabali.justjog.ui.util.StatisticsScreen
import ramzi.eljabali.justjog.ui.views.JustJogCalendarView
import ramzi.eljabali.justjog.ui.views.StatisticsPage
import ramzi.eljabali.justjog.viewmodel.StatisticsViewModel


@Composable
fun JustJogNavigation(
    navController: NavHostController,
    statisticsViewModel: StatisticsViewModel,
) {
    NavHost(navController = navController, startDestination = StatisticsScreen) {
        composable<StatisticsScreen> {
            statisticsViewModel.onLaunch()
            StatisticsPage(statisticsViewModel.statisticsViewState.collectAsStateWithLifecycle(), statisticsViewModel::onFabClicked)
        }
        composable<CalendarScreen> {
            JustJogCalendarView()
        }
    }
}