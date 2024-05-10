package ramzi.eljabali.justjog.room.jogsummarytemp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jog_summary_temp")
data class JogSummaryTemp(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "current_date_time")
    val currentDateTime: String,
    @ColumnInfo(name = "duration_seconds")
    val totalJogDuration: Long,
    @ColumnInfo(name = "distance_miles")
    val totalJogDistance: Double,
    @ColumnInfo(name = "current_latitude")
    val latitude: Double,
    @ColumnInfo(name = "current_longitude")
    val longitude: Double
)
