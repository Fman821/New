package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.example.data.model.AppThemeMode

@Immutable
data class ExtendedIslamicColors(
    val themeMode: AppThemeMode,
    val cardBorder: Color,
    val goldAccent: Color,
    val arabicCalligraphyColor: Color,
    val subtextColor: Color,
    val cardBg: Color,
    val isGlassmorphism: Boolean = false,
    val isMasjidArch: Boolean = false
)

val LocalIslamicColors = staticCompositionLocalOf {
    ExtendedIslamicColors(
        themeMode = AppThemeMode.SACRED_LIGHT,
        cardBorder = SacredLightBorder,
        goldAccent = SacredLightSecondary,
        arabicCalligraphyColor = SacredLightPrimary,
        subtextColor = SacredLightSubtext,
        cardBg = SacredLightSurface
    )
}

// 1. Sacred Light M3 Scheme
private val SacredLightColorScheme = lightColorScheme(
    primary = SacredLightPrimary,
    onPrimary = Color.White,
    secondary = SacredLightSecondary,
    onSecondary = Color.White,
    background = SacredLightBg,
    onBackground = SacredLightText,
    surface = SacredLightSurface,
    onSurface = SacredLightText,
    surfaceVariant = Color(0xFFF3ECE0),
    onSurfaceVariant = SacredLightSubtext,
    outline = SacredLightBorder
)

// 2. Midnight Ottoman M3 Scheme
private val OttomanColorScheme = darkColorScheme(
    primary = OttomanPrimary,
    onPrimary = OttomanBg,
    secondary = OttomanSecondary,
    onSecondary = OttomanBg,
    background = OttomanBg,
    onBackground = OttomanText,
    surface = OttomanSurface,
    onSurface = OttomanText,
    surfaceVariant = Color(0xFF16254F),
    onSurfaceVariant = OttomanSubtext,
    outline = OttomanBorder
)

// 3. Glassmorphism & 3D M3 Scheme
private val GlassColorScheme = darkColorScheme(
    primary = GlassPrimary,
    onPrimary = GlassBg,
    secondary = GlassSecondary,
    onSecondary = GlassBg,
    background = GlassBg,
    onBackground = GlassText,
    surface = GlassSurface,
    onSurface = GlassText,
    surfaceVariant = Color(0xFF1A463B),
    onSurfaceVariant = GlassSubtext,
    outline = GlassBorder
)

// 4. Masjid Architecture M3 Scheme
private val MasjidColorScheme = lightColorScheme(
    primary = MasjidPrimary,
    onPrimary = Color.White,
    secondary = MasjidSecondary,
    onSecondary = Color.White,
    background = MasjidBg,
    onBackground = MasjidText,
    surface = MasjidSurface,
    onSurface = MasjidText,
    surfaceVariant = Color(0xFFE9EFEB),
    onSurfaceVariant = MasjidSubtext,
    outline = MasjidBorder
)

// 5. Dark Emerald & Neon M3 Scheme
private val EmeraldNeonColorScheme = darkColorScheme(
    primary = EmeraldNeonPrimary,
    onPrimary = EmeraldNeonBg,
    secondary = EmeraldNeonSecondary,
    onSecondary = EmeraldNeonBg,
    background = EmeraldNeonBg,
    onBackground = EmeraldNeonText,
    surface = EmeraldNeonSurface,
    onSurface = EmeraldNeonText,
    surfaceVariant = Color(0xFF12372A),
    onSurfaceVariant = EmeraldNeonSubtext,
    outline = EmeraldNeonBorder
)

@Composable
fun AzkarAppTheme(
    themeMode: AppThemeMode = AppThemeMode.SACRED_LIGHT,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeMode) {
        AppThemeMode.SACRED_LIGHT -> SacredLightColorScheme
        AppThemeMode.MIDNIGHT_OTTOMAN -> OttomanColorScheme
        AppThemeMode.GLASSMORPHISM_3D -> GlassColorScheme
        AppThemeMode.MASJID_ARCHITECTURE -> MasjidColorScheme
        AppThemeMode.DARK_EMERALD_NEON -> EmeraldNeonColorScheme
    }

    val islamicColors = when (themeMode) {
        AppThemeMode.SACRED_LIGHT -> ExtendedIslamicColors(
            themeMode = themeMode,
            cardBorder = SacredLightBorder,
            goldAccent = SacredLightSecondary,
            arabicCalligraphyColor = SacredLightPrimary,
            subtextColor = SacredLightSubtext,
            cardBg = SacredLightSurface
        )
        AppThemeMode.MIDNIGHT_OTTOMAN -> ExtendedIslamicColors(
            themeMode = themeMode,
            cardBorder = OttomanBorder,
            goldAccent = OttomanSecondary,
            arabicCalligraphyColor = OttomanSecondary,
            subtextColor = OttomanSubtext,
            cardBg = OttomanSurface
        )
        AppThemeMode.GLASSMORPHISM_3D -> ExtendedIslamicColors(
            themeMode = themeMode,
            cardBorder = GlassBorder,
            goldAccent = GlassPrimary,
            arabicCalligraphyColor = GlassPrimary,
            subtextColor = GlassSubtext,
            cardBg = GlassSurface,
            isGlassmorphism = true
        )
        AppThemeMode.MASJID_ARCHITECTURE -> ExtendedIslamicColors(
            themeMode = themeMode,
            cardBorder = MasjidBorder,
            goldAccent = MasjidSecondary,
            arabicCalligraphyColor = MasjidPrimary,
            subtextColor = MasjidSubtext,
            cardBg = MasjidSurface,
            isMasjidArch = true
        )
        AppThemeMode.DARK_EMERALD_NEON -> ExtendedIslamicColors(
            themeMode = themeMode,
            cardBorder = EmeraldNeonBorder,
            goldAccent = EmeraldNeonPrimary,
            arabicCalligraphyColor = EmeraldNeonPrimary,
            subtextColor = EmeraldNeonSubtext,
            cardBg = EmeraldNeonSurface
        )
    }

    CompositionLocalProvider(LocalIslamicColors provides islamicColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
