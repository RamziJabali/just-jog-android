package ramzi.eljabali.justjog.ui.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ramzi.eljabali.justjog.ui.design.BottomNavigationItems
import ramzi.eljabali.justjog.ui.design.BottomNavigationItems.STATISTICS
import ramzi.eljabali.justjog.ui.design.BottomNavigationItems.CALENDAR


@Composable
fun BottomNavigationView() {
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    NavigationBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        BottomNavigationItems.entries.toList().forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index
//                    navController.navigate(item.title)
                },
                alwaysShowLabel = true,
                label = { Text(text = item.title)},
                icon = {
                    Icon(
                        imageVector = if (selectedItemIndex == index) {
                            item.selectedIcon
                        } else {
                            item.unSelectedIcon
                        },
                        contentDescription = item.title
                    )
                }
            )
        }
    }
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun BottomNavigationViewPreview() {
    BottomNavigationView()
}