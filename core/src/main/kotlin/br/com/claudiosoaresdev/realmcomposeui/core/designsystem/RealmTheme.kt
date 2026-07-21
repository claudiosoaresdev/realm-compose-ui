package br.com.claudiosoaresdev.realmcomposeui.core.designsystem

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiTextStyle
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiTheme

/**
 * O tema é dado do servidor. Este arquivo define só o *formato* dos tokens e o fallback —
 * nunca a paleta de uma casa.
 */

val LocalSduiTheme = staticCompositionLocalOf { SduiTheme() }

object RealmTheme {
    val sdui: SduiTheme
        @Composable @ReadOnlyComposable get() = LocalSduiTheme.current
}

@Composable
fun RealmSduiTheme(
    theme: SduiTheme,
    content: @Composable () -> Unit,
) {
    val colors = theme.colors
    val colorScheme = remember(theme) {
        darkColorScheme(
            primary = colors.primary.toColor(),
            onPrimary = colors.textPrimary.toColor(),
            primaryContainer = colors.primaryDark.toColor(),
            secondary = colors.secondary.toColor(),
            background = theme.background.color.toColor(),
            onBackground = colors.textPrimary.toColor(),
            surface = colors.surface.toColor(),
            onSurface = colors.textPrimary.toColor(),
            surfaceVariant = colors.surfaceVariant.toColor(),
            onSurfaceVariant = colors.textSecondary.toColor(),
            outline = colors.outline.toColor(),
        )
    }
    val typography = remember(theme) {
        with(theme.typography) {
            Typography(
                displayLarge = display.toTextStyle(),
                headlineMedium = headline.toTextStyle(),
                titleMedium = title.toTextStyle(),
                bodyMedium = body.toTextStyle(),
                labelLarge = label.toTextStyle(),
            )
        }
    }
    val shapes = remember(theme) {
        with(theme.shapes) {
            Shapes(
                small = RoundedCornerShape(smallRadiusDp.dp),
                medium = RoundedCornerShape(cardRadiusDp.dp),
                large = RoundedCornerShape(largeRadiusDp.dp),
            )
        }
    }

    CompositionLocalProvider(LocalSduiTheme provides theme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            shapes = shapes,
            content = content,
        )
    }
}

/** `#RRGGBB` ou `#AARRGGBB`; valor inválido cai no fallback em vez de estourar. */
fun String?.toColor(fallback: Color = Color.Transparent): Color {
    val hex = this?.removePrefix("#") ?: return fallback
    val value = hex.toULongOrNull(radix = 16) ?: return fallback
    return when (hex.length) {
        6 -> Color(value.toLong() or 0xFF000000L)
        8 -> Color(value.toLong())
        else -> fallback
    }
}

fun SduiTextStyle.toTextStyle(): TextStyle = TextStyle(
    fontFamily = when (fontFamily) {
        "serif" -> FontFamily.Serif
        "monospace" -> FontFamily.Monospace
        else -> FontFamily.SansSerif
    },
    fontSize = fontSizeSp.sp,
    fontWeight = FontWeight(fontWeight.coerceIn(100, 900)),
    lineHeight = lineHeightSp.sp,
    letterSpacing = letterSpacingSp.sp,
)
