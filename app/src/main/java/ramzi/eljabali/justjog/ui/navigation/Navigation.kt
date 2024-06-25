package ramzi.eljabali.justjog.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ramzi.eljabali.justjog.ui.util.CalendarScreen
import ramzi.eljabali.justjog.ui.util.StatisticsScreen
import ramzi.eljabali.justjog.ui.views.JustJogCalendarView
import ramzi.eljabali.justjog.ui.views.StatisticsPage
import ramzi.eljabali.justjog.viewmodel.StatisticsViewModel


@Composable
fun JustJogNavigation(
    navController: NavHostController,
    statisticsViewModel: StatisticsViewModel,
    askForPermission: (List<String>, (String, Boolean) -> Unit) -> Unit,
    shouldShowRequestPermissionRationale: (String) -> Boolean,
    openSettings: () -> Unit,
) {
    NavHost(navController = navController, startDestination = StatisticsScreen) {
        composable<StatisticsScreen> {
            statisticsViewModel.onLaunch()
            StatisticsPage(
                statisticsViewModel.statisticsViewState.collectAsStateWithLifecycle(),
                statisticsViewModel::onFabClicked,
                statisticsViewModel::dismissDialog,
                statisticsViewModel::onPermissionResult,
                shouldShowRequestPermissionRationale,
                openSettings,
                askForPermission
            )
        }
        composable<CalendarScreen> {
            JustJogCalendarView()
        }
    }
}