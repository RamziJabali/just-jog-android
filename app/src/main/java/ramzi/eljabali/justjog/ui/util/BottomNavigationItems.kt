package ramzi.eljabali.justjog.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AreaChart
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.outlined.AreaChart
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavigationItems(
    val title: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
) {
    STATISTICS("Statistics", Icons.Filled.AreaChart, Icons.Outlined.AreaChart),
    CALENDAR("Calendar", Icons.Filled.CalendarToday, Icons.Outlined.CalendarToday)
}