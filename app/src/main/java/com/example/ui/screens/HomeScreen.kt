package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AzkarCategory
import com.example.ui.components.IslamicEmblemMotif
import com.example.ui.components.IslamicGeometricDivider
import com.example.ui.theme.LocalIslamicColors
import com.example.ui.viewmodel.AppTab
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Premium Redesigned Home Screen for Nur Islamic Lifestyle App.
 * Includes:
 * A. Elegant Header with Emblem, Greeting, and Hijri/Gregorian date.
 * B. Featured Daily Verse Card (Al-Baqarah 152) with verified Arabic text, translation, bookmark, and share.
 * C. 8 Quick Access Category Cards (Morning Azkar, Evening Azkar, Quran, Duas, 99 Names, Tasbih, Prayer Times, Qibla).
 * D. Daily Inspiration Card (Sahih al-Bukhari 6407).
 * E. Tasbih Preview Banner with quick count increment.
 */
@Composable
fun HomeScreen(
    categories: List<AzkarCategory>,
    tasbihCount: Int,
    tasbihTarget: Int,
    isVerseBookmarked: Boolean,
    onToggleBookmarkVerse: () -> Unit,
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
        // A. ELEGANT HEADER
        // =====================================================================
        item {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = islamicColors.cardBg,
                border = BorderStroke(1.dp, islamicColors.cardBorder.copy(alpha = 0.6f)),
                shadowElevation = if (islamicColors.isGlassmorphism) 0.dp else 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Islamic Emblem Motif
                        IslamicEmblemMotif(
                            primaryColor = islamicColors.arabicCalligraphyColor,
                            accentColor = islamicColors.goldAccent,
                            modifier = Modifier.size(46.dp)
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Nur Islamic Lifestyle",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                letterSpacing = 0.3.sp
                            )
                            Text(
                                text = "አሰላሙ ዐለይኩም ወረሕመቱላህ",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = islamicColors.arabicCalligraphyColor
                            )
                            Text(
                                text = currentDateStr,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Theme selector button
                    IconButton(
                        onClick = onOpenThemeDialog,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "Theme",
                            tint = islamicColors.goldAccent
                        )
                    }
                }
            }
        }

        // =====================================================================
        // B. FEATURED DAILY VERSE CARD (Al-Baqarah 152)
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
                            
                            (Nur Islamic Lifestyle - ኑሩል እስላም)
                        """.trimIndent()
                        putExtra(Intent.EXTRA_TEXT, text)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "የዕለቱ አንቀጽን ያጋሩ"))
                }
            )
        }

        // =====================================================================
        // C. QUICK ACCESS CATEGORIES (8 Premium Cards)
        // =====================================================================
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "ዋና ዋና ክፍሎች (Quick Access)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "8 አገልግሎቶች",
                        style = MaterialTheme.typography.labelSmall,
                        color = islamicColors.goldAccent,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 2-Column Responsive Grid of the 8 Main Cards
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Row 1: Morning Azkar & Evening Azkar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val morningCat = categories.firstOrNull { it.id == "morning" } ?: categories.firstOrNull()
                        val eveningCat = categories.firstOrNull { it.id == "evening" } ?: categories.getOrNull(1)

                        QuickActionCard(
                            title = "የጥዋት አዝካር",
                            subtitle = "Morning Azkar",
                            badgeText = "${morningCat?.items?.size ?: 30}+",
                            icon = Icons.Default.WbSunny,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                if (morningCat != null) onCategoryClick(morningCat) else onNavigateTab(AppTab.HOME)
                            }
                        )

                        QuickActionCard(
                            title = "የማታ አዝካር",
                            subtitle = "Evening Azkar",
                            badgeText = "${eveningCat?.items?.size ?: 30}+",
                            icon = Icons.Default.NightsStay,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                if (eveningCat != null) onCategoryClick(eveningCat) else onNavigateTab(AppTab.HOME)
                            }
                        )
                    }

                    // Row 2: Holy Quran & Duas
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickActionCard(
                            title = "ቅዱስ ቁርኣን",
                            subtitle = "Quran Surahs",
                            badgeText = "114 ሱራ",
                            icon = Icons.Default.MenuBook,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateTab(AppTab.DUAS) }
                        )

                        QuickActionCard(
                            title = "ዕለታዊ ዱዓዎች",
                            subtitle = "Essential Duas",
                            badgeText = "ተመርጠው",
                            icon = Icons.Outlined.VolunteerActivism,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateTab(AppTab.DUAS) }
                        )
                    }

                    // Row 3: 99 Names of Allah & Tasbih Counter
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickActionCard(
                            title = "99 የአላህ ስሞች",
                            subtitle = "Asmaul Husna",
                            badgeText = "99 ስሞች",
                            icon = Icons.Default.AutoAwesome,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateTab(AppTab.NAMES) }
                        )

                        QuickActionCard(
                            title = "ተስቢሕ ቆጣሪ",
                            subtitle = "Tasbih Counter",
                            badgeText = "$tasbihCount / $tasbihTarget",
                            icon = Icons.Default.TouchApp,
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateTasbihTab
                        )
                    }

                    // Row 4: Prayer Times & Qibla Direction
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickActionCard(
                            title = "የሰላት ወቅቶች",
                            subtitle = "Prayer Times",
                            badgeText = "ትክክለኛ",
                            icon = Icons.Default.Mosque,
                            modifier = Modifier.weight(1f),
                            onClick = onOpenPrayerTab
                        )

                        QuickActionCard(
                            title = "ቂብላ አቅጣጫ",
                            subtitle = "Qibla Compass",
                            badgeText = "ካዕባ 🕋",
                            icon = Icons.Default.Navigation,
                            modifier = Modifier.weight(1f),
                            onClick = onOpenQiblaTab
                        )
                    }
                }
            }
        }

        // =====================================================================
        // D. DAILY INSPIRATION (Verified Hadith - Sahih al-Bukhari 6407)
        // =====================================================================
        item {
            DailyInspirationCard()
        }

        // =====================================================================
        // E. TASBIH PREVIEW BANNER
        // =====================================================================
        item {
            TasbihPreviewBanner(
                count = tasbihCount,
                target = tasbihTarget,
                onQuickTap = onTasbihQuickTap,
                onOpenFullTasbih = onNavigateTasbihTab
            )
        }
    }
}

@Composable
private fun FeaturedVerseCard(
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onShare: () -> Unit
) {
    val islamicColors = LocalIslamicColors.current

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = islamicColors.cardBg,
        border = BorderStroke(1.2.dp, islamicColors.goldAccent.copy(alpha = 0.65f)),
        shadowElevation = if (islamicColors.isGlassmorphism) 0.dp else 3.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header Row: Tag & Action icons (Bookmark & Share)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = islamicColors.goldAccent.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, islamicColors.goldAccent.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = "የዕለቱ የተመረጠ አንቀጽ (Daily Verse)",
                        style = MaterialTheme.typography.labelSmall,
                        color = islamicColors.goldAccent,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Row {
                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Bookmark Verse",
                            tint = if (isBookmarked) islamicColors.goldAccent else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = onShare,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Verse",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Verified Arabic Quranic Verse
            Text(
                text = "«فَاذْكُرُونِي أَذْكُرْكُمْ وَاشْكُرُوا لِي وَلَا تَكْفُرُونِ»",
                fontSize = 24.sp,
                lineHeight = 38.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Right,
                color = islamicColors.arabicCalligraphyColor,
                modifier = Modifier.fillMaxWidth()
            )

            IslamicGeometricDivider(
                color = islamicColors.goldAccent.copy(alpha = 0.4f),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Verified Amharic Translation
            Text(
                text = "«አስታውሱኝ አስታውሳችኋለሁና፤ ለእኔም አመስግኑ፤ አትካዱኝም፡፡»",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Verified English Translation & Reference
            Text(
                text = "“So remember Me; I will remember you. And be grateful to Me and do not deny Me.”",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "ሱረቱል አል-በቀራህ (Surah Al-Baqarah) • 152",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = islamicColors.goldAccent
            )
        }
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    subtitle: String,
    badgeText: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val islamicColors = LocalIslamicColors.current

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = islamicColors.cardBg,
        border = BorderStroke(1.dp, islamicColors.cardBorder.copy(alpha = 0.55f)),
        modifier = modifier
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(islamicColors.goldAccent.copy(alpha = 0.16f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = islamicColors.goldAccent,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = islamicColors.goldAccent.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelSmall,
                        color = islamicColors.goldAccent,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DailyInspirationCard() {
    val islamicColors = LocalIslamicColors.current
    val context = LocalContext.current

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = islamicColors.cardBg,
        border = BorderStroke(1.dp, islamicColors.cardBorder.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
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
                Text(
                    text = "ዕለታዊ ምክር / ሐዲስ (Daily Reflection)",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = islamicColors.goldAccent
                )
                IconButton(
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            val body = """
                                የአላህ መልእክተኛ (ﷺ) እንዲህ ብለዋል፡-
                                «مَثَلُ الَّذِي يَذْكُرُ رَبَّهُ وَالَّذِي لَا يَذْكُرُ رَبَّهُ مَثَلُ الْحَيِّ وَالْمَيِّتِ»
                                «ጌታውን የሚያወሳና የማያወሳ ሰው ምሳሌ፤ ልክ የሕያውና የሙት ምሳሌ ነው፡፡»
                                [ሶሒሕ አል-ቡኻሪ 6407]
                                
                                (Nur Islamic Lifestyle)
                            """.trimIndent()
                            putExtra(Intent.EXTRA_TEXT, body)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "ሐዲሱን ያጋሩ"))
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "«مَثَلُ الَّذِي يَذْكُرُ رَبَّهُ وَالَّذِي لَا يَذْكُرُ رَبَّهُ مَثَلُ الْحَيِّ وَالْمَيِّتِ»",
                fontSize = 18.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Right,
                color = islamicColors.arabicCalligraphyColor,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "የአላህ መልእክተኛ (ﷺ) እንዲህ ብለዋል፡- «ጌታውን የሚያወሳና የማያወሳ ሰው ምሳሌ፤ ልክ የሕያውና የሙት ምሳሌ ነው፡፡»",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "ምንጭ: ሶሒሕ አል-ቡኻሪ 6407 (Sahih al-Bukhari)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TasbihPreviewBanner(
    count: Int,
    target: Int,
    onQuickTap: () -> Unit,
    onOpenFullTasbih: () -> Unit
) {
    val islamicColors = LocalIslamicColors.current
    val progress = (count.toFloat() / target.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(),
        label = "previewProgress"
    )

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = islamicColors.cardBg,
        border = BorderStroke(1.dp, islamicColors.goldAccent.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
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
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOpenFullTasbih() }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "ፈጣን ተስቢሕ ቆጣሪ",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Open Tasbih",
                            tint = islamicColors.goldAccent,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = "ሱብሓነላህ • ዒላማ: $target ዚክሮች",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Interactive increment button with big number
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(islamicColors.goldAccent.copy(alpha = 0.2f))
                        .clickable { onQuickTap() }
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        shape = CircleShape,
                        color = islamicColors.goldAccent,
                        modifier = Modifier.size(46.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "$count",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 17.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(islamicColors.cardBorder.copy(alpha = 0.4f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(islamicColors.goldAccent, islamicColors.arabicCalligraphyColor)
                            )
                        )
                )
            }
        }
    }
}
