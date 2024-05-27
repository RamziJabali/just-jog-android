package ramzi.eljabali.justjog.repository.room.jogentries

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("jog_entries")
data class JogEntry(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo("jog_summary_id")
    val jogSummaryId: Int,
    @ColumnInfo("date_time")
    val dateTime: String,
    @ColumnInfo("latitude")
    val latitude: Double,
    @ColumnInfo("longitude")
    val longitude: Double,
)