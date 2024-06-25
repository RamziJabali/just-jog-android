package ramzi.eljabali.justjog.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import ramzi.eljabali.justjog.model.permissioninformation.LocationPermissionTextProvider
import ramzi.eljabali.justjog.model.permissioninformation.PermissionTextProvider
import ramzi.eljabali.justjog.ui.design.CardElevation
import ramzi.eljabali.justjog.ui.design.CornerRadius
import ramzi.eljabali.justjog.ui.design.Spacing


@Composable
fun PermissionDialogBox(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
) {
    Popup(
        popupPositionProvider = WindowCenterOffsetPositionProvider(),
        properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = false,
            dismissOnClickOutside = true,
            clippingEnabled = false
        ),
        onDismissRequest = onDismiss,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.Surrounding.s),
            shape = RoundedCornerShape(CornerRadius.s),
            elevation = CardDefaults.cardElevation(defaultElevation = CardElevation.default)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Permission Required",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                )
                Text(
                    text = permissionTextProvider.getDescription(isPermanentlyDeclined),
                    fontWeight = FontWeight.W300,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(Spacing.Surrounding.s)
                )
                HorizontalDivider()
                Text(
                    text = if (isPermanentlyDeclined) {
                        "Grant permission"
                    } else {
                        "OK"
                    },
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clickable {
                            if (isPermanentlyDeclined) {
                                onGoToAppSettingsClick()
                            } else {
                                onOkClick()
                            }
                        }
                        .padding(Spacing.Surrounding.m)
                )
            }
        }
    }
}

class WindowCenterOffsetPositionProvider(
    private val x: Int = 0,
    private val y: Int = 0
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        return IntOffset(
            (windowSize.width - popupContentSize.width) / 2 + x,
            (windowSize.height - popupContentSize.height) / 2 + y
        )
    }
}

@Preview(backgroundColor = 0xFFFFFFFF)
@Composable()
fun PreviewPermissionDialogBoxNotPermanentlyDeclined() {
    PermissionDialogBox(
        permissionTextProvider = LocationPermissionTextProvider(),
        isPermanentlyDeclined = false,
        onDismiss = {},
        onOkClick = {},
        onGoToAppSettingsClick = {}
    )
}

@Preview(backgroundColor = 0xFFFFFFFF)
@Composable()
fun PreviewPermissionDialogBoxPermanentlyDeclined() {
    PermissionDialogBox(
        permissionTextProvider = LocationPermissionTextProvider(),
        isPermanentlyDeclined = true,
        onDismiss = {},
        onOkClick = {},
        onGoToAppSettingsClick = {}
    )
}