package ramzi.eljabali.justjog.ui.design

import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ramzi.eljabali.justjog.ui.design.Spacings.cubic_spacing
import ramzi.eljabali.justjog.ui.design.Spacings.linear_spacing
import ramzi.eljabali.justjog.ui.design.Spacings.nonic_spacing
import ramzi.eljabali.justjog.ui.design.Spacings.octic_spacing
import ramzi.eljabali.justjog.ui.design.Spacings.quadratic_spacing
import ramzi.eljabali.justjog.ui.design.Spacings.quartic_spacing
import ramzi.eljabali.justjog.ui.design.Spacings.quintic_spacing
import ramzi.eljabali.justjog.ui.design.Spacings.septic_spacing
import ramzi.eljabali.justjog.ui.design.Spacings.sextic_spacing

//https://en.m.wikipedia.org/wiki/Degree_of_a_polynomial


private object Spacings {
    val linear_spacing = 2.dp
    val quadratic_spacing = 4.dp
    val cubic_spacing = 8.dp
    val quartic_spacing = 16.dp
    val quintic_spacing = 32.dp
    val sextic_spacing = 64.dp
    val septic_spacing = 128.dp
    val octic_spacing = 256.dp
    val nonic_spacing = 512.dp
}

object Spacing {
    object Horizontal {
        val xxs = linear_spacing
        val xs = quadratic_spacing
        val s = cubic_spacing
        val m = quartic_spacing
        val l = quintic_spacing
        val xl = sextic_spacing
        val xxl = septic_spacing
        val xxxl = octic_spacing
    }

    object Vertical {
        val xxs = linear_spacing
        val xs = quadratic_spacing
        val s = cubic_spacing
        val m = quartic_spacing
        val l = quintic_spacing
        val xl = sextic_spacing
        val xxl = septic_spacing
        val xxxl = octic_spacing
        val xl4 = nonic_spacing
    }

    object Surrounding {
        val xxs = linear_spacing
        val xs = quadratic_spacing
        val s = cubic_spacing
        val m = quartic_spacing
        val l = quintic_spacing
        val xl = sextic_spacing
        val xxl = septic_spacing
        val xxxl = octic_spacing
        val xl4 = nonic_spacing
    }
}

object CardSize {
    val xs = 25.dp
    val s = 35.dp
    val m = 50.dp
    val l = 100.dp
    val xl = 150.dp
    val xxl = 200.dp
    val xxxl = 250.dp
    val xl4 = 300.dp
    val xl5 = 350.dp
}

object TextSize {
    val xxs = 12.sp
    val xs = 14.sp
    val s = 16.sp
    val m = 18.sp
    val l = 22.sp
    val xl = 26.sp
    val xxl = 32.sp
    val xxxl = 42.sp
    val xl4 = 64.sp
}

object ButtonSize {
    val xs = 20.dp
    val s = 25.dp
    val m = 50.dp
    val l = 100.dp
    val xl = 150.dp
    val xxl = 200.dp
    val xxxl = 250.dp
    val xl4 = 300.dp
    val xl5 = 350.dp
}

object TextFieldSize {
    val s = 25.dp
    val m = 50.dp
    val xm = 55.dp
    val l = 100.dp
    val xl = 150.dp
    val xxl = 200.dp
    val xxxl = 250.dp
    val xl4 = 300.dp
    val xl5 = 350.dp
}

object ImageSize {
    val xs = 25.dp
    val s = 35.dp
    val m = 50.dp
    val l = 100.dp
    val xl = 150.dp
    val xxl = 200.dp
    val xxxl = 250.dp
    val xl4 = 300.dp
    val xl5 = 350.dp
}

object IconSize {
    val xs = 15.dp
    val s = 20.dp
    val m = 25.dp
    val l = 30.dp
    val xl = 35.dp
    val xxl = 40.dp
    val xxxl = 50.dp
    val xl4 = 60.dp
    val xl5 = 70.dp
    val xl6 = 80.dp
}

object SpinnerSize {
    val xs = 15.dp
    val s = 20.dp
    val m = 25.dp
    val l = 30.dp
    val xl = 35.dp
    val xxl = 40.dp
    val xxxl = 50.dp
}

object LetterSpacing {
    const val xxs = -0.12
    const val xs = -0.09
    const val s = -0.05
    const val m = 0
    const val l = 0.15
    const val xl = 0.2
    const val xxl = 0.3
}

object CornerRadius {
    val xxs = 1.dp
    val xs = 2.dp
    val s = 4.dp
    val m = 6.dp
    val l = 12.dp
    val xl = 18.dp
    val xxl = 24.dp
    val xxxl = 30.dp
}

object Elevation {
    val none = 0.dp
    val xs = 2.dp
    val s = 4.dp
    val m = 6.dp
    val l = 12.dp
}

object Transparency {
    const val xxs = 0.90
    const val xs = 0.75
    const val s = 0.50
    const val m = 0.25
    const val l = 0.10
    const val xl = 0.05
    const val xxl = 0.03
}

object FloatingActionButton {
    val default = 56.dp
}

object CardElevation {
    val default = 6.dp
}
object FabElevation {
    val default = 6.dp
}
