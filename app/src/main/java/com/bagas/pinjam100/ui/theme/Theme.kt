package com.bagas.pinjam100.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = White,
    primaryContainer = DashboardLightBlue,
    onPrimaryContainer = PrimaryDarkBlue,
    secondary = SecondaryYellow,
    onSecondary = Black,
    secondaryContainer = SecondaryYellow,
    onSecondaryContainer = Black,
    tertiary = PrimaryDarkBlue,
    onTertiary = White,
    tertiaryContainer = DashboardLightBlue,
    onTertiaryContainer = PrimaryDarkBlue,
    background = BackgroundLight,
    onBackground = TextDark,
    surface = SurfaceLight,
    onSurface = TextDark,
    surfaceVariant = DashboardLightBlue,
    onSurfaceVariant = TextDark,
    error = Error,
    onError = White
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    onPrimary = White,
    primaryContainer = DashboardLightBlue,
    onPrimaryContainer = PrimaryDarkBlue,
    secondary = SecondaryYellow,
    onSecondary = Black,
    secondaryContainer = SecondaryYellow,
    onSecondaryContainer = Black,
    tertiary = PrimaryBlue,
    onTertiary = White,
    tertiaryContainer = PrimaryDarkBlue,
    onTertiaryContainer = White,
    background = BackgroundDark,
    onBackground = TextLight,
    surface = SurfaceDark,
    onSurface = TextLight,
    surfaceVariant = PrimaryDarkBlue,
    onSurfaceVariant = TextLight,
    error = Error,
    onError = White
)

@Composable
fun Pinjam100Theme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}