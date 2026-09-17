package com.bagas.pinjam100.core.extension

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun SetStatusBarColor(
    color: Color,
    darkIcons: Boolean = true
) {
    val view = LocalView.current

    SideEffect {
        val window = (view.context as Activity).window

        @Suppress("DEPRECATION")
        window.statusBarColor = color.toArgb()

        WindowCompat.getInsetsController(
            window,
            view
        ).isAppearanceLightStatusBars = darkIcons
    }
}