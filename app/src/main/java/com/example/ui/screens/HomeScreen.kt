package com.example.ui.screens

import android.content.Intent
import android.icu.util.IslamicCalendar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AzkarCategory
import com.example.data.model.AzkarSessionProgress
import com.example.data.model.RecommendedPeriod
import com.example.ui.components.IslamicEmblemMotif
import com.example.ui.components.IslamicGeometricDivider
import com.example.ui.theme.LocalIslamicColors
import com.example.ui.viewmodel.AppTab
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Redesigned Home Screen elevating Morning Azkar & Evening Azkar
 * into the TWO MOST IMPORTANT FEATURES of the entire app.
 *
 * Structure:
 * 1. App Header with Emblem, Greeting, Date, and Theme Selector.
 * 2. HERO SECTION: Two large, visually dominant Morning & Evening Azkar Cards (time-aware).
 * 3. DAILY AZKAR PROGRESS COMPONENT: Real calculated progress bar and completion states.
 * 4. SECONDARY FEATURES: Daily Verse, lifestyle tools (Quran, Duas, 99 Names, Prayer Times, Tasbih).
 */
@Composable
fun HomeScreen(
    categories: List<AzkarCategory>,
    morningProgress: AzkarSessionProgress?,
    eveningProgress: AzkarSessionProgress?,
    recommendedPeriod: RecommendedPeriod,
    tasbihCount: Int,
    tasbihTarget: Int,
    isVerseBookmarked: Boolean,
    onToggleBookmarkVerse: () -> Unit,
    onStartMorningAzkar: () -> Unit,
    onStartEveningAzkar: () -> Unit,
    onCategoryClick: (AzkarCategory) -> Unit,
    onNavigateTab: (AppTab) -> Unit,
    onOpenPrayerTab: () -> Unit,
    onOpenQiblaTab: () -> Unit,
    onOpenThemeDialog: () -> Unit,
    onTasbihQuickTap: () -> Unit,
    onNavigateTasbihTab: () -> Unit,
    modifier: Modifier = Modifier
) {
    val islamicColors = LocalIslamicColors.current
    val context = LocalContext.current

    val currentDateStr = remember {
        val sdf = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("am", "ET"))
        sdf.format(Date())
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // =====================================================================
        // 1. ELEGANT APP HEADER
        // =====================================================================
        item {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = islamicColors.cardBg,
                border = BorderStroke(1.dp, islamicColors.cardBorder.copy(alpha = 0.5f)),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        IslamicEmblemMotif(
                            primaryColor = islamicColors.arabicCalligraphyColor,
                            accentColor = islamicColors.goldAccent,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "አማርኛ አዝካር",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                letterSpacing = 0.3.sp
                            )
                            Text(
                                text = "አማርኛ አዝካር • ዕለታዊ ዝክር፣ ሰላማዊ ሕይወት",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = islamicColors.arabicCalligraphyColor
                            )
                            Text(
                                text = currentDateStr,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onOpenThemeDialog,
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("home_theme_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "የገጽታ ምርጫ",
                            tint = islamicColors.goldAccent
                        )
                    }
                }
            }
        }

        // =====================================================================
        // HIJRI DATE CARD
        // =====================================================================
        item {
            HijriDateCard()
        }

        // =====================================================================
        // 2. HERO SECTION: MORNING & EVENING AZKAR (PRIMARY VISUAL FOCUS)
        // =====================================================================
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = islamicColors.goldAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ዕለታዊ አዝካር",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Text(
                        text = "የቀኑ ዋና ተግባር",
                        style = MaterialTheme.typography.labelSmall,
                        color = islamicColors.goldAccent,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Adaptive Hero layout: Side-by-side on wide screens, stacked on narrow mobile
                BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                    val isWide = maxWidth >= 600.dp
                    if (isWide) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            MorningAzkarHeroCard(
                                progress = morningProgress,
                                isRecommended = recommendedPeriod == RecommendedPeriod.MORNING,
                                onStart = onStartMorningAzkar,
                                modifier = Modifier.weight(1f)
                            )
                            EveningAzkarHeroCard(
                                progress = eveningProgress,
                                isRecommended = recommendedPeriod == RecommendedPeriod.EVENING,
                                onStart = onStartEveningAzkar,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            MorningAzkarHeroCard(
                                progress = morningProgress,
                                isRecommended = recommendedPeriod == RecommendedPeriod.MORNING,
                                onStart = onStartMorningAzkar,
                                modifier = Modifier.fillMaxWidth()
                            )
                            EveningAzkarHeroCard(
                                progress = eveningProgress,
                                isRecommended = recommendedPeriod == RecommendedPeriod.EVENING,
                                onStart = onStartEveningAzkar,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }

        // =====================================================================
        // 3. DAILY AZKAR PROGRESS COMPONENT
        // =====================================================================
        item {
            DailyAzkarProgressComponent(
                morningProgress = morningProgress,
                eveningProgress = eveningProgress
            )
        }

        // =====================================================================
        // 4. SECONDARY SECTION: FEATURED DAILY VERSE (Al-Baqarah 152)
        // =====================================================================
        item {
            FeaturedVerseCard(
                isBookmarked = isVerseBookmarked,
                onToggleBookmark = onToggleBookmarkVerse,
                onShare = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        val text = """
                            «فَاذْكُرُونِي أَذْكُرْكُمْ وَاشْكُرُوا لِي وَلَا تَكْفُرُونِ»
                            
                            «አስታውሱኝ አስታውሳችኋለሁና፤ ለእኔም አመስግኑ፤ አትካዱኝም፡፡»
                            [ሱረቱል አል-በቀራህ • 152]
                            
                            (Amharic Azkar - አማርኛ አዝካር)
                        """.trimIndent()
                        putExtra(Intent.EXTRA_TEXT, text)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "የዕለቱ አንቀጽን ያጋሩ"))
                }
            )
        }

        // =====================================================================
        // 5. SECONDARY SECTION: LIFESTYLE & KNOWLEDGE TOOLS
        // =====================================================================
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "ተጨማሪ አገልግሎቶች",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "ቁርኣንና ዱዓ",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 2-Column Grid of Secondary Features
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SecondaryActionCard(
                            title = "ቅዱስ ቁርኣን",
                            subtitle = "የቁርኣን ንባብ",
                            badgeText = "114 ሱራ",
                            icon = Icons.Default.MenuBook,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateTab(AppTab.QURAN_DUA) }
                        )

                        SecondaryActionCard(
                            title = "ዕለታዊ ዱዓዎች",
                            subtitle = "የተለያዩ ዱዓዎች",
                            badgeText = "ተመርጠው",
                            icon = Icons.Outlined.VolunteerActivism,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateTab(AppTab.DUAS) }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SecondaryActionCard(
                            title = "99 የአላህ ስሞች",
                            subtitle = "አል-አስማኡል ሁስና",
                            badgeText = "99 ስሞች",
                            icon = Icons.Default.AutoAwesome,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateTab(AppTab.NAMES) }
                        )

                        SecondaryActionCard(
                            title = "ተስቢሕ ቆጣሪ",
                            subtitle = "ዲጂታል ቆጣሪ",
                            badgeText = "$tasbihCount / $tasbihTarget",
                            icon = Icons.Default.TouchApp,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateTab(AppTab.TASBIH) }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SecondaryActionCard(
                            title = "የሰላት ወቅት",
                            subtitle = "የዕለት ሰላት ሰዓቶች",
                            badgeText = "ትክክለኛ",
                            icon = Icons.Default.Mosque,
                            modifier = Modifier.weight(1f),
                            onClick = onOpenPrayerTab
                        )

                        SecondaryActionCard(
                            title = "የቂብላ አቅጣጫ",
                            subtitle = "የቂብላ አቅጣጫ",
                            badgeText = "ካዕባ",
                            icon = Icons.Default.Navigation,
                            modifier = Modifier.weight(1f),
                            onClick = onOpenQiblaTab
                        )
                    }
                }
            }
        }

        // =====================================================================
        // 6. TASBIH QUICK WIDGET
        // =====================================================================
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = islamicColors.cardBg,
                border = BorderStroke(1.dp, islamicColors.cardBorder.copy(alpha = 0.5f)),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "ፈጣን ተስቢሕ (Quick Tasbih)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "ሱብሓነላህ • $tasbihCount ከ $tasbihTarget ተቆጥሯል",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onTasbihQuickTap,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(islamicColors.goldAccent)
                                .testTag("home_quick_tasbih_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.TouchApp,
                                contentDescription = "Count Tasbih",
                                tint = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(onClick = onNavigateTasbihTab) {
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Open Tasbih Tab",
                                tint = islamicColors.goldAccent
                            )
                        }
                    }
                }
            }
        }

        // Bottom breathing space
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


/**
 * Displays the current Hijri date using Android's built-in ICU calendar.
 * Available from Android 7.0 (API 24), matching the app's minimum SDK.
 */
@Composable
private fun HijriDateCard() {
    val islamicColors = LocalIslamicColors.current
    val hijri = remember {
        IslamicCalendar().apply { timeInMillis = System.currentTimeMillis() }
    }
    val day = hijri.get(IslamicCalendar.DAY_OF_MONTH)
    val month = hijri.get(IslamicCalendar.MONTH)
    val year = hijri.get(IslamicCalendar.YEAR)
    val monthsAm = listOf(
        "مُحَرَّم", "صَفَر", "ربيع الأول", "ربيع الثاني",
        "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
        "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
    )
    val monthName = monthsAm.getOrElse(month) { "" }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = islamicColors.cardBg,
        border = BorderStroke(1.dp, islamicColors.goldAccent.copy(alpha = 0.55f)),
        modifier = Modifier.fillMaxWidth().testTag("hijri_date_card")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "የሂጅራ ቀን (Hijri Date)",
                    style = MaterialTheme.typography.labelMedium,
                    color = islamicColors.goldAccent,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$day $monthName $year ሂጅራ",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "የእስልምና ቀን መቁጠሪያ",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IslamicEmblemMotif(
                primaryColor = islamicColors.arabicCalligraphyColor,
                accentColor = islamicColors.goldAccent,
                modifier = Modifier.size(42.dp)
            )
        }
    }
}

/**
 * CARD 1 — MORNING AZKAR HERO CARD
 * Deep emerald-green background with champagne-gold accents, sunrise-inspired visual touches,
 * subtle geometric watermark, and live progress tracking.
 */
@Composable
private fun MorningAzkarHeroCard(
    progress: AzkarSessionProgress?,
    isRecommended: Boolean,
    onStart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val islamicColors = LocalIslamicColors.current
    val total = progress?.totalItems ?: 24
    val completed = progress?.completedItems ?: 0
    val remaining = progress?.remainingItems ?: total
    val fraction = progress?.progressFraction ?: 0f
    val isComplete = progress?.isComplete == true
    val isStarted = progress?.isStarted == true

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF072E23),
            Color(0xFF0A3C2E),
            Color(0xFF06251C)
        ),
        start = Offset(0f, 0f),
        end = Offset(800f, 800f)
    )

    Surface(
        shape = RoundedCornerShape(26.dp),
        color = Color(0xFF072E23),
        border = BorderStroke(
            width = if (isRecommended) 2.dp else 1.5.dp,
            color = if (isRecommended) islamicColors.goldAccent else islamicColors.goldAccent.copy(alpha = 0.5f)
        ),
        shadowElevation = if (isRecommended) 8.dp else 4.dp,
        modifier = modifier
            .clip(RoundedCornerShape(26.dp))
            .clickable { onStart() }
            .testTag("morning_azkar_hero_card")
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .fillMaxWidth()
        ) {
            // Sunrise Artwork Canvas (Gentle golden dawn rays & star)
            Canvas(modifier = Modifier.matchParentSize()) {
                val w = size.width
                val h = size.height
                // Dawn glow circle at top-right
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x33F2D99A), Color(0x11D9B66F), Color.Transparent),
                        center = Offset(w * 0.85f, h * 0.25f),
                        radius = w * 0.45f
                    ),
                    center = Offset(w * 0.85f, h * 0.25f),
                    radius = w * 0.45f
                )
                // Geometric subtle rays
                for (i in 0..7) {
                    val angle = (i * 22.5f) * (Math.PI / 180f)
                    val r1 = 30f
                    val r2 = 90f
                    val cx = w * 0.85f
                    val cy = h * 0.25f
                    drawLine(
                        color = Color(0x22F2D99A),
                        start = Offset(cx + (r1 * kotlin.math.cos(angle)).toFloat(), cy + (r1 * kotlin.math.sin(angle)).toFloat()),
                        end = Offset(cx + (r2 * kotlin.math.cos(angle)).toFloat(), cy + (r2 * kotlin.math.sin(angle)).toFloat()),
                        strokeWidth = 1.5f
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Top Tag Row: Sunrise Icon + Recommended Badge / Status Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0x33F2D99A)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WbSunny,
                                contentDescription = "Sunrise",
                                tint = islamicColors.goldAccent,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        if (isRecommended) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = islamicColors.goldAccent,
                                contentColor = Color.Black
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.WbSunny,
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp),
                                        tint = Color.Black
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "አሁን የሚመከር",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }

                    // Completion or count chip
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isComplete) Color(0x334CAF50) else Color(0x22FFFFFF),
                        border = BorderStroke(1.dp, if (isComplete) Color(0xFF4CAF50) else Color(0x33FFFFFF))
                    ) {
                        Text(
                            text = if (isComplete) "ተጠናቋል ✓" else "$total አዝካሮች",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isComplete) Color(0xFF81C784) else Color(0xFFFAF7F0),
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Titles
                Text(
                    text = "የጥዋት አዝካር",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFFAF7F0)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Morning Azkar",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFC5BAA5)
                    )
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.bodyMedium,
                        color = islamicColors.goldAccent
                    )
                    Text(
                        text = "أذكار الصباح",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = islamicColors.goldAccent
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Progress Bar and Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isComplete) "የዛሬው የጥዋት አዝካር ተጠናቋል! ($completed/$total)"
                        else if (isStarted) "$completed ተጠናቋል • $remaining ይቀራሉ"
                        else "አልተጀመረም • $total አዝካሮች ተዘጋጅተዋል",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isComplete) Color(0xFF81C784) else Color(0xFFFAF7F0).copy(alpha = 0.9f),
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${(fraction * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = islamicColors.goldAccent,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { fraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = islamicColors.goldAccent,
                    trackColor = Color(0x33FFFFFF),
                    strokeCap = StrokeCap.Round
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action Button
                Button(
                    onClick = onStart,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isComplete) Color(0x33F2D99A) else islamicColors.goldAccent,
                        contentColor = if (isComplete) islamicColors.goldAccent else Color.Black
                    ),
                    border = if (isComplete) BorderStroke(1.dp, islamicColors.goldAccent) else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("start_morning_azkar_button")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isComplete) Icons.Default.CheckCircle
                            else if (isStarted) Icons.Default.PlayArrow
                            else Icons.Default.WbSunny,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isComplete) "አዝካሩን ከልስ (Review Azkar)"
                            else if (isStarted) "ክፍለ-ጊዜውን ቀጥል (Resume Session)"
                            else "የጥዋት አዝካር ጀምር (Start Morning Azkar)",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

/**
 * CARD 2 — EVENING AZKAR HERO CARD
 * Rich forest-green background with soft gold highlights, sunset & crescent moon visual touches,
 * subtle geometric watermark, and live progress tracking.
 */
@Composable
private fun EveningAzkarHeroCard(
    progress: AzkarSessionProgress?,
    isRecommended: Boolean,
    onStart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val islamicColors = LocalIslamicColors.current
    val total = progress?.totalItems ?: 22
    val completed = progress?.completedItems ?: 0
    val remaining = progress?.remainingItems ?: total
    val fraction = progress?.progressFraction ?: 0f
    val isComplete = progress?.isComplete == true
    val isStarted = progress?.isStarted == true

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF0A3326),
            Color(0xFF0F4433),
            Color(0xFF07241A)
        ),
        start = Offset(0f, 0f),
        end = Offset(800f, 800f)
    )

    Surface(
        shape = RoundedCornerShape(26.dp),
        color = Color(0xFF0A3326),
        border = BorderStroke(
            width = if (isRecommended) 2.dp else 1.5.dp,
            color = if (isRecommended) islamicColors.goldAccent else islamicColors.goldAccent.copy(alpha = 0.5f)
        ),
        shadowElevation = if (isRecommended) 8.dp else 4.dp,
        modifier = modifier
            .clip(RoundedCornerShape(26.dp))
            .clickable { onStart() }
            .testTag("evening_azkar_hero_card")
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .fillMaxWidth()
        ) {
            // Sunset / Dusk Twilight Artwork Canvas (Crescent moon glow & stars)
            Canvas(modifier = Modifier.matchParentSize()) {
                val w = size.width
                val h = size.height

                // Dusk crescent glow at top-right
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x33D9B66F), Color(0x110A3326), Color.Transparent),
                        center = Offset(w * 0.85f, h * 0.25f),
                        radius = w * 0.42f
                    ),
                    center = Offset(w * 0.85f, h * 0.25f),
                    radius = w * 0.42f
                )

                // Crescent moon shape
                val moonPath = Path().apply {
                    val cx = w * 0.84f
                    val cy = h * 0.24f
                    val r = 24f
                    moveTo(cx, cy - r)
                    cubicTo(cx - r * 0.9f, cy - r * 0.5f, cx - r * 0.9f, cy + r * 0.5f, cx, cy + r)
                    cubicTo(cx - r * 0.4f, cy + r * 0.4f, cx - r * 0.4f, cy - r * 0.4f, cx, cy - r)
                    close()
                }
                drawPath(path = moonPath, color = Color(0x33F2D99A))

                // Delicate stars
                drawCircle(color = Color(0x44F2D99A), radius = 2.5f, center = Offset(w * 0.72f, h * 0.16f))
                drawCircle(color = Color(0x33F2D99A), radius = 2f, center = Offset(w * 0.92f, h * 0.42f))
                drawCircle(color = Color(0x44F2D99A), radius = 1.5f, center = Offset(w * 0.76f, h * 0.48f))
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Top Tag Row: Moon Icon + Recommended Badge / Status Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0x33F2D99A)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.NightsStay,
                                contentDescription = "Sunset & Evening",
                                tint = islamicColors.goldAccent,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        if (isRecommended) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = islamicColors.goldAccent,
                                contentColor = Color.Black
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.NightsStay,
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp),
                                        tint = Color.Black
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "አሁን የሚመከር",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isComplete) Color(0x334CAF50) else Color(0x22FFFFFF),
                        border = BorderStroke(1.dp, if (isComplete) Color(0xFF4CAF50) else Color(0x33FFFFFF))
                    ) {
                        Text(
                            text = if (isComplete) "ተጠናቋል ✓" else "$total አዝካሮች",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isComplete) Color(0xFF81C784) else Color(0xFFFAF7F0),
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Titles
                Text(
                    text = "የማታ አዝካር",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFFAF7F0)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Evening Azkar",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFC5BAA5)
                    )
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.bodyMedium,
                        color = islamicColors.goldAccent
                    )
                    Text(
                        text = "أذكار المساء",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = islamicColors.goldAccent
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Progress Bar and Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isComplete) "የዛሬው የማታ አዝካር ተጠናቋል! ($completed/$total)"
                        else if (isStarted) "$completed ተጠናቋል • $remaining ይቀራሉ"
                        else "አልተጀመረም • $total አዝካሮች ተዘጋጅተዋል",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isComplete) Color(0xFF81C784) else Color(0xFFFAF7F0).copy(alpha = 0.9f),
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${(fraction * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = islamicColors.goldAccent,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { fraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = islamicColors.goldAccent,
                    trackColor = Color(0x33FFFFFF),
                    strokeCap = StrokeCap.Round
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action Button
                Button(
                    onClick = onStart,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isComplete) Color(0x33F2D99A) else islamicColors.goldAccent,
                        contentColor = if (isComplete) islamicColors.goldAccent else Color.Black
                    ),
                    border = if (isComplete) BorderStroke(1.dp, islamicColors.goldAccent) else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("start_evening_azkar_button")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isComplete) Icons.Default.CheckCircle
                            else if (isStarted) Icons.Default.PlayArrow
                            else Icons.Default.NightsStay,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isComplete) "አዝካሩን ከልስ (Review Azkar)"
                            else if (isStarted) "ክፍለ-ጊዜውን ቀጥል (Resume Session)"
                            else "የማታ አዝካር ጀምር (Start Evening Azkar)",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

/**
 * Compact but beautiful Daily Azkar Progress Component.
 * Displays Morning Azkar progress, Evening Azkar progress, total daily progress,
 * and clear status badges.
 */
@Composable
private fun DailyAzkarProgressComponent(
    morningProgress: AzkarSessionProgress?,
    eveningProgress: AzkarSessionProgress?,
    modifier: Modifier = Modifier
) {
    val islamicColors = LocalIslamicColors.current

    val mTotal = morningProgress?.totalItems ?: 24
    val mDone = morningProgress?.completedItems ?: 0
    val eTotal = eveningProgress?.totalItems ?: 22
    val eDone = eveningProgress?.completedItems ?: 0

    val combinedTotal = mTotal + eTotal
    val combinedDone = mDone + eDone
    val combinedFraction = if (combinedTotal > 0) combinedDone.toFloat() / combinedTotal.toFloat() else 0f

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = islamicColors.cardBg,
        border = BorderStroke(1.dp, islamicColors.cardBorder.copy(alpha = 0.5f)),
        shadowElevation = 2.dp,
        modifier = modifier
            .fillMaxWidth()
            .testTag("daily_azkar_progress_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = islamicColors.goldAccent,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "የዕለቱ አጠቃላይ የአዝካር እድገት",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = "$combinedDone / $combinedTotal (${(combinedFraction * 100).toInt()}%)",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = islamicColors.goldAccent
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Dual / Combined Progress Bar
            LinearProgressIndicator(
                progress = { combinedFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .testTag("daily_total_progress_bar"),
                color = islamicColors.goldAccent,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Two Session Status Chips Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Morning Chip
                val isMDone = morningProgress?.isComplete == true
                val isMStarted = morningProgress?.isStarted == true
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    border = BorderStroke(
                        1.dp,
                        if (isMDone) Color(0xFF4CAF50) else islamicColors.cardBorder.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isMDone) Icons.Default.CheckCircle else Icons.Default.WbSunny,
                            contentDescription = null,
                            tint = if (isMDone) Color(0xFF4CAF50) else islamicColors.goldAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "የጥዋት አዝካር",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isMDone) "ተጠናቋል ($mDone/$mTotal)"
                                else if (isMStarted) "$mDone/$mTotal በሂደት"
                                else "አልተጀመረም (0/$mTotal)",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.sp,
                                color = if (isMDone) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Evening Chip
                val isEDone = eveningProgress?.isComplete == true
                val isEStarted = eveningProgress?.isStarted == true
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    border = BorderStroke(
                        1.dp,
                        if (isEDone) Color(0xFF4CAF50) else islamicColors.cardBorder.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isEDone) Icons.Default.CheckCircle else Icons.Default.NightsStay,
                            contentDescription = null,
                            tint = if (isEDone) Color(0xFF4CAF50) else islamicColors.goldAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "የማታ አዝካር",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isEDone) "ተጠናቋል ($eDone/$eTotal)"
                                else if (isEStarted) "$eDone/$eTotal በሂደት"
                                else "አልተጀመረም (0/$eTotal)",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.sp,
                                color = if (isEDone) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Secondary lifestyle action card (distinctly smaller and subordinate to the Hero Cards).
 */
@Composable
private fun SecondaryActionCard(
    title: String,
    subtitle: String,
    badgeText: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val islamicColors = LocalIslamicColors.current

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = islamicColors.cardBg,
        border = BorderStroke(1.dp, islamicColors.cardBorder.copy(alpha = 0.4f)),
        shadowElevation = 1.dp,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("secondary_card_${title}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(islamicColors.goldAccent.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = islamicColors.goldAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
            )
        }
    }
}

/**
 * Featured Daily Verse Card (Al-Baqarah 152).
 */
@Composable
private fun FeaturedVerseCard(
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    val islamicColors = LocalIslamicColors.current

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = islamicColors.cardBg,
        border = BorderStroke(1.dp, islamicColors.goldAccent.copy(alpha = 0.4f)),
        shadowElevation = 2.dp,
        modifier = modifier
            .fillMaxWidth()
            .testTag("daily_verse_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = islamicColors.goldAccent,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "የዕለቱ የቁርኣን አንቀጽ",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = islamicColors.goldAccent
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = if (isBookmarked) "ከተወዳጆች አስወግድ" else "ወደ ተወዳጆች ጨምር",
                            tint = if (isBookmarked) islamicColors.goldAccent else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onShare,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "የዕለቱን አንቀጽ አጋራ",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "«فَاذْكُرُونِي أَذْكُرْكُمْ وَاشْكُرُوا لِي وَلَا تَكْفُرُونِ»",
                fontSize = 20.sp,
                lineHeight = 34.sp,
                textAlign = TextAlign.Right,
                fontWeight = FontWeight.Bold,
                color = islamicColors.arabicCalligraphyColor,
                modifier = Modifier.fillMaxWidth()
            )

            IslamicGeometricDivider(
                color = islamicColors.goldAccent.copy(alpha = 0.4f),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "«አስታውሱኝ አስታውሳችኋለሁና፤ ለእኔም አመስግኑ፤ አትካዱኝም፡፡»",
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "ሱረቱል አል-በቀራህ • 152",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
