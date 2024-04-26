package ramzi.eljabali.justjog.ui.design

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


//private val DarkColorScheme = darkColorScheme(
//    primary = primaryDarkHighContrast,
//    onPrimary = onPrimaryDarkHighContrast,
//    primaryContainer = primaryContainerDarkHighContrast,
//    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
//    inversePrimary = inversePrimaryDarkHighContrast,
//    secondary = secondaryDarkHighContrast,
//    onSecondary = onSecondaryDarkHighContrast,
//    secondaryContainer = secondaryContainerDarkHighContrast,
//    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
//    tertiary = tertiaryDarkHighContrast,
//    onTertiary = onTertiaryDarkHighContrast,
//    tertiaryContainer = tertiaryContainerDarkHighContrast,
//    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
//    background = backgroundDarkHighContrast,
//    onBackground = onBackgroundDarkHighContrast,
//    surface = surfaceDarkHighContrast,
//    onSurface = onSurfaceDarkHighContrast,
//    surfaceVariant = surfaceVariantDarkHighContrast,
//    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
//    inverseSurface = inverseSurfaceDarkHighContrast,
//    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
//    error = errorDarkHighContrast,
//    onError = onErrorDarkHighContrast,
//    errorContainer = errorContainerDarkHighContrast,
//    onErrorContainer = onErrorContainerDarkHighContrast,
//    outline = outlineDarkHighContrast,
//    outlineVariant = outlineVariantDarkHighContrast,
//    scrim = scrimDarkHighContrast,
//    surfaceBright = surfaceBrightDarkHighContrast,
//    surfaceContainer = surfaceContainerDarkHighContrast,
//    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
//    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
//    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
//    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
//    surfaceDim = surfaceDimDarkHighContrast
//
//)
private val DarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = Color.White,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast
)


private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override

    onTertiary = Color.White,
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun JustJogTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}