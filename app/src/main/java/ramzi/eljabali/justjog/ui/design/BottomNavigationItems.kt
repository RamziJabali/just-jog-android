package ramzi.eljabali.justjog.ui.design

import androidx.annotation.DrawableRes
import ramzi.eljabali.justjog.R

enum class BottomNavigationItems(title: String, @DrawableRes val icon: Int) {
    HOME("home", R.drawable.home),
    CALENDAR("calendar", R.drawable.calendar),
}