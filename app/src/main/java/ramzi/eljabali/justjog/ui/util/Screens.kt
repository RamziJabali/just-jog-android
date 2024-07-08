package ramzi.eljabali.justjog.ui.util

import kotlinx.serialization.Serializable

@Serializable
object StatisticsScreen {
    const val Route = "ramzi.eljabali.justjog.ui.util.StatisticsScreen"
}

@Serializable
object CalendarScreen {
    const val Route = "ramzi.eljabali.justjog.ui.util.CalendarScreen"
}

@Serializable
data class MapsScreen(
    val jogId: Int
)

