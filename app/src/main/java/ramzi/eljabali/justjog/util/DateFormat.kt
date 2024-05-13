package ramzi.eljabali.justjog.util

enum class DateFormat(val format: String) {
    YYYY_MM_DD("yyyy-MM-dd"),
    YYYY_MM_DD_T_TIME("yyyy-MM-dd'T'HH:mm:ss"),
    EEE_MMM_D_YYYY("EEE, MMM d yyyy"),
    HH_MM_SS("HH:mm:ss")
}
