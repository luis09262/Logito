package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DivineDarkColorScheme = darkColorScheme(
    primary = EnergyGold,
    onPrimary = TitaniumDeep,
    primaryContainer = MagmaDark,
    onPrimaryContainer = EnergyGoldLight,
    secondary = SolarPlasma,
    onSecondary = ObsidianBase,
    secondaryContainer = TitaniumSurface,
    onSecondaryContainer = PlatinumLight,
    tertiary = OverchargeCyan,
    onTertiary = ObsidianBase,
    background = ObsidianBase,
    onBackground = TextPrimary,
    surface = ObsidianElevated,
    onSurface = TextPrimary,
    surfaceVariant = TitaniumSurface,
    onSurfaceVariant = TextSecondary,
    outline = TitaniumBorder,
    outlineVariant = GlassWhite10
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false, // Forced stylized aesthetic for world-class divine VFX
    content: @Composable () -> Unit
) {
    val colorScheme = DivineDarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = ObsidianBase.toArgb()
            window.navigationBarColor = ObsidianBase.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
