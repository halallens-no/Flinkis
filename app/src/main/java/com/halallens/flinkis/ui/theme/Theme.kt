package com.halallens.flinkis.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.halallens.flinkis.domain.model.ThemeType

// === BOY COLOR SCHEMES ===
private val BoyLightColorScheme = lightColorScheme(
    primary = BoyPrimary,
    onPrimary = Color.White,
    primaryContainer = BoyPrimaryLight,
    onPrimaryContainer = BoyPrimaryDark,
    secondary = BoySecondary,
    onSecondary = Color.White,
    secondaryContainer = BoySecondaryLight,
    onSecondaryContainer = BoySecondaryDark,
    tertiary = BoyAccent,
    onTertiary = Color.White,
    background = BoyBackground,
    onBackground = Gray900,
    surface = BoySurface,
    onSurface = Gray900,
    surfaceVariant = Gray100,
    onSurfaceVariant = Gray700,
    error = Error,
    onError = Color.White,
    outline = Gray400,
    outlineVariant = Gray300
)

private val BoyDarkColorScheme = darkColorScheme(
    primary = BoyPrimary,
    onPrimary = Color.White,
    primaryContainer = BoyPrimaryDark,
    onPrimaryContainer = BoyPrimaryLight,
    secondary = BoySecondary,
    onSecondary = Color.White,
    secondaryContainer = BoySecondaryDark,
    onSecondaryContainer = BoySecondaryLight,
    tertiary = BoyAccent,
    onTertiary = Color.White,
    background = BoyBackgroundDark,
    onBackground = Color.White,
    surface = BoySurfaceDark,
    onSurface = Color.White,
    surfaceVariant = Gray800,
    onSurfaceVariant = Gray300,
    error = Error,
    onError = Color.White,
    outline = Gray600,
    outlineVariant = Gray700
)

// === GIRL COLOR SCHEMES ===
private val GirlLightColorScheme = lightColorScheme(
    primary = GirlPrimary,
    onPrimary = Color.White,
    primaryContainer = GirlPrimaryLight,
    onPrimaryContainer = GirlPrimaryDark,
    secondary = GirlSecondary,
    onSecondary = Color.White,
    secondaryContainer = GirlSecondaryLight,
    onSecondaryContainer = GirlSecondaryDark,
    tertiary = GirlAccent,
    onTertiary = Color.White,
    background = GirlBackground,
    onBackground = Gray900,
    surface = GirlSurface,
    onSurface = Gray900,
    surfaceVariant = Gray100,
    onSurfaceVariant = Gray700,
    error = Error,
    onError = Color.White,
    outline = Gray400,
    outlineVariant = Gray300
)

private val GirlDarkColorScheme = darkColorScheme(
    primary = GirlPrimary,
    onPrimary = Color.White,
    primaryContainer = GirlPrimaryDark,
    onPrimaryContainer = GirlPrimaryLight,
    secondary = GirlSecondary,
    onSecondary = Color.White,
    secondaryContainer = GirlSecondaryDark,
    onSecondaryContainer = GirlSecondaryLight,
    tertiary = GirlAccent,
    onTertiary = Color.White,
    background = GirlBackgroundDark,
    onBackground = Color.White,
    surface = GirlSurfaceDark,
    onSurface = Color.White,
    surfaceVariant = Gray800,
    onSurfaceVariant = Gray300,
    error = Error,
    onError = Color.White,
    outline = Gray600,
    outlineVariant = Gray700
)

// === NEUTRAL COLOR SCHEMES ===
private val NeutralLightColorScheme = lightColorScheme(
    primary = NeutralPrimary,
    onPrimary = Color.White,
    primaryContainer = NeutralPrimaryLight,
    onPrimaryContainer = NeutralPrimaryDark,
    secondary = NeutralSecondary,
    onSecondary = Color.White,
    secondaryContainer = NeutralSecondaryLight,
    onSecondaryContainer = NeutralSecondaryDark,
    tertiary = NeutralAccent,
    onTertiary = Color.White,
    background = NeutralBackground,
    onBackground = Gray900,
    surface = NeutralSurface,
    onSurface = Gray900,
    surfaceVariant = Gray100,
    onSurfaceVariant = Gray700,
    error = Error,
    onError = Color.White,
    outline = Gray400,
    outlineVariant = Gray300
)

private val NeutralDarkColorScheme = darkColorScheme(
    primary = NeutralPrimary,
    onPrimary = Color.White,
    primaryContainer = NeutralPrimaryDark,
    onPrimaryContainer = NeutralPrimaryLight,
    secondary = NeutralSecondary,
    onSecondary = Color.White,
    secondaryContainer = NeutralSecondaryDark,
    onSecondaryContainer = NeutralSecondaryLight,
    tertiary = NeutralAccent,
    onTertiary = Color.White,
    background = NeutralBackgroundDark,
    onBackground = Color.White,
    surface = NeutralSurfaceDark,
    onSurface = Color.White,
    surfaceVariant = Gray800,
    onSurfaceVariant = Gray300,
    error = Error,
    onError = Color.White,
    outline = Gray600,
    outlineVariant = Gray700
)

/**
 * MyRoutine Theme - Supports Boy, Girl, and Neutral color schemes
 */
@Composable
fun MyRoutineTheme(
    themeType: ThemeType = ThemeType.NEUTRAL,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeType) {
        ThemeType.BOY -> if (darkTheme) BoyDarkColorScheme else BoyLightColorScheme
        ThemeType.GIRL -> if (darkTheme) GirlDarkColorScheme else GirlLightColorScheme
        ThemeType.NEUTRAL -> if (darkTheme) NeutralDarkColorScheme else NeutralLightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = KidTypography,
        shapes = KidShapes,
        content = content
    )
}
