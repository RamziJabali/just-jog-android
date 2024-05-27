package ramzi.eljabali.justjog.usecase

import java.time.Duration
import java.time.ZonedDateTime

data class ModifiedJogSummary(
    val jogId: Int,
    val startDate: ZonedDateTime,
    val duration: Duration,
    val totalDistance: Double
)
