package ramzi.eljabali.justjog.usecase

import com.google.android.gms.maps.model.LatLng
import java.time.ZonedDateTime

data class ModifiedJogEntry(
    val jogSummaryId: Int,
    val dateTime: ZonedDateTime,
    val latLng: LatLng
)
