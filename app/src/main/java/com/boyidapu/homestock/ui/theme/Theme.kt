package com.boyidapu.homestock.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = DeepTeal,
    onPrimary = CleanWhite,
    primaryContainer = SoftSage,
    onPrimaryContainer = ForestCharcoal,
    secondary = SoftSage,
    onSecondary = ForestCharcoal,
    background = WarmMist,
    onBackground = ForestCharcoal,
    surface = CleanWhite,
    onSurface = ForestCharcoal,
    surfaceVariant = WarmMist,
    onSurfaceVariant = MutedSageGray
)

@Composable
fun IntiVentoryTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
