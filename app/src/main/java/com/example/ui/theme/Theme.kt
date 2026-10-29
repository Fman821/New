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
    val secondaryGold: Color,
    val arabicCalligraphyColor: Color,
    val primaryText: Color,
    val subtextColor: Color,
    val cardBg: Color,
    val surfaceHighlight: Color,
    val isGlassmorphism: Boolean = false,
    val isMasjidArch: Boolean = false,
    val isDark: Boolean = false
)

val LocalIslamicColors = staticCompositionLocalOf {
    ExtendedIslamicColors(
        themeMode = AppThemeMode.SACRED_LIGHT,
        cardBorder = SacredLightBorder,
        goldAccent = ChampagneGold,
        secondaryGold = SoftGold,
        arabicCalligraphyColor = ArabicEmerald,
        primaryText = PrimaryTextDark,
        subtextColor = SecondaryTextDark,
        cardBg = SacredLightSurface,
        surfaceHighlight = LightSurfaceVariant,
        isDark = false
    )
}

// 1. Sacred Light M3 Scheme (Warm Cream & White)
private val SacredLightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = Color.White,
    secondary = ChampagneGold,
    onSecondary = PrimaryTextDark,
    background = WarmCream,
    onBackground = PrimaryTextDark,
    surface = PureWhite,
    onSurface = PrimaryTextDark,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = SecondaryTextDark,
    outline = SubtleBorder,
    outlineVariant = Color(0xFFF0EBE1)
)

// 2. Sacred Night M3 Scheme (Deep Emerald & Dark Background)
private val SacredNightColorScheme = darkColorScheme(
    primary = ChampagneGold,
    onPrimary = DarkBackground,
    secondary = SoftGold,
    onSecondary = DarkBackground,
    background = DarkBackground,
    onBackground = WarmCream,
    surface = DeepEmerald,
    onSurface = WarmCream,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFA6BAAF),
    outline = Color(0x33D9B66F),
    outlineVariant = Color(0x22FFFFFF)
)

// 3. Midnight Ottoman M3 Scheme
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

// 4. Glassmorphism M3 Scheme
private val GlassColorScheme = darkColorScheme(
    primary = ChampagneGold,
    onPrimary = GlassBg,
    secondary = GlassSecondary,
    onSecondary = GlassBg,
    background = GlassBg,
    onBackground = GlassText,
    surface = GlassSurface,
    onSurface = GlassText,
    surfaceVariant = Color(0xFF133B31),
    onSurfaceVariant = GlassSubtext,
    outline = GlassBorder
)

// 5. Masjid Architecture M3 Scheme
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

@Composable
fun AzkarAppTheme(
    themeMode: AppThemeMode = AppThemeMode.SACRED_LIGHT,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeMode) {
        AppThemeMode.SACRED_LIGHT -> SacredLightColorScheme
        AppThemeMode.DARK_EMERALD_NEON -> SacredNightColorScheme
        AppThemeMode.MIDNIGHT_OTTOMAN -> OttomanColorScheme
        AppThemeMode.GLASSMORPHISM_3D -> GlassColorScheme
        AppThemeMode.MASJID_ARCHITECTURE -> MasjidColorScheme
    }

    val isDark = when (themeMode) {
        AppThemeMode.SACRED_LIGHT, AppThemeMode.MASJID_ARCHITECTURE -> false
        else -> true
    }

    val islamicColors = when (themeMode) {
        AppThemeMode.SACRED_LIGHT -> ExtendedIslamicColors(
            themeMode = themeMode,
            cardBorder = SubtleBorder,
            goldAccent = ChampagneGold,
            secondaryGold = SoftGold,
            arabicCalligraphyColor = ArabicEmerald,
            primaryText = PrimaryTextDark,
            subtextColor = SecondaryTextDark,
            cardBg = PureWhite,
            surfaceHighlight = LightSurfaceVariant,
            isDark = false
        )
        AppThemeMode.DARK_EMERALD_NEON -> ExtendedIslamicColors(
            themeMode = themeMode,
            cardBorder = Color(0x33F2D99A),
            goldAccent = SoftGold,
            secondaryGold = ChampagneGold,
            arabicCalligraphyColor = SoftGold,
            primaryText = Color(0xFFFAF7F0),
            subtextColor = Color(0xFFA6BAAF),
            cardBg = DarkForestGreen,
            surfaceHighlight = DarkSurfaceVariant,
            isDark = true
        )
        AppThemeMode.MIDNIGHT_OTTOMAN -> ExtendedIslamicColors(
            themeMode = themeMode,
            cardBorder = OttomanBorder,
            goldAccent = OttomanSecondary,
            secondaryGold = OttomanPrimary,
            arabicCalligraphyColor = OttomanSecondary,
            primaryText = OttomanText,
            subtextColor = OttomanSubtext,
            cardBg = OttomanSurface,
            surfaceHighlight = Color(0xFF16254F),
            isDark = true
        )
        AppThemeMode.GLASSMORPHISM_3D -> ExtendedIslamicColors(
            themeMode = themeMode,
            cardBorder = GlassBorder,
            goldAccent = ChampagneGold,
            secondaryGold = GlassSecondary,
            arabicCalligraphyColor = ChampagneGold,
            primaryText = GlassText,
            subtextColor = GlassSubtext,
            cardBg = GlassSurface,
            surfaceHighlight = Color(0xFF133B31),
            isGlassmorphism = true,
            isDark = true
        )
        AppThemeMode.MASJID_ARCHITECTURE -> ExtendedIslamicColors(
            themeMode = themeMode,
            cardBorder = MasjidBorder,
            goldAccent = ArabicGold,
            secondaryGold = ChampagneGold,
            arabicCalligraphyColor = PrimaryGreen,
            primaryText = MasjidText,
            subtextColor = MasjidSubtext,
            cardBg = MasjidSurface,
            surfaceHighlight = Color(0xFFE9EFEB),
            isMasjidArch = true,
            isDark = false
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
