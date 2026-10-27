package com.example.ui.screens

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.Flight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AzkarCategory
import com.example.ui.components.IslamicGeometricDivider
import com.example.ui.theme.LocalIslamicColors

/**
 * Main Home screen featuring:
 * - Serene Islamic Greeting Banner & Daily Verse
 * - Quick Tasbih Widget
 * - Grid of 6 Azkar categories with rich styling and counts
 */
@Composable
fun HomeScreen(
    categories: List<AzkarCategory>,
    tasbihCount: Int,
    tasbihTarget: Int,
    onCategoryClick: (AzkarCategory) -> Unit,
    onTasbihQuickTap: () -> Unit,
    onNavigateTasbihTab: () -> Unit,
    modifier: Modifier = Modifier
) {
    val islamicColors = LocalIslamicColors.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Banner: Islamic greeting & Daily Verse
        item {
            HeroGreetingCard()
        }

        // Quick Tasbih Widget
        item {
            QuickTasbihBanner(
                count = tasbihCount,
                target = tasbihTarget,
                onQuickTap = onTasbihQuickTap,
                onOpenFullTasbih = onNavigateTasbihTab
            )
        }

        // Section Title: Azkar Categories
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "የአዝካር ክፍሎች (Azkar Categories)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "${categories.size} ክፍሎች",
                    style = MaterialTheme.typography.labelSmall,
                    color = islamicColors.goldAccent
                )
            }
        }

        // Category Cards
        items(categories.size) { index ->
            val cat = categories[index]
            CategoryCard(
                category = cat,
                onClick = { onCategoryClick(cat) }
            )
        }

        // Bottom peaceful quote
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "«أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ»\n«ንቁ! አላህን በማውሳት ልቦች ይረካሉ»",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    color = islamicColors.arabicCalligraphyColor,
                    lineHeight = 24.sp
                )
            }
        }
    }
}

@Composable
private fun HeroGreetingCard() {
    val islamicColors = LocalIslamicColors.current

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = islamicColors.cardBg,
        border = BorderStroke(1.dp, islamicColors.goldAccent.copy(alpha = 0.5f)),
        shadowElevation = if (islamicColors.isGlassmorphism) 0.dp else 3.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = islamicColors.arabicCalligraphyColor
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(islamicColors.goldAccent.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "የእለቱ ማስታወሻ",
                        style = MaterialTheme.typography.labelSmall,
                        color = islamicColors.goldAccent,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "«وَالذَّاكِرِينَ اللَّهَ كَثِيرًا وَالذَّاكِرَاتِ أَعَدَّ اللَّهُ لَهُم مَّغْفِرَةً وَأَجْرًا عَظِيمًا»",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Right,
                color = islamicColors.arabicCalligraphyColor,
                lineHeight = 28.sp,
                modifier = Modifier.fillMaxWidth()
            )

            IslamicGeometricDivider(
                color = islamicColors.goldAccent.copy(alpha = 0.35f),
                modifier = Modifier.padding(vertical = 6.dp)
            )

            Text(
                text = "«አላህን በብዙ የሚያወሱ ወንዶችና ሴቶች፤ አላህ ለእነርሱ ምሕረትንና ታላቅ ምንዳን አዘጋጅቶላቸዋል፡፡»",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "ሱረቱል አል-አሕዛብ 35",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun QuickTasbihBanner(
    count: Int,
    target: Int,
    onQuickTap: () -> Unit,
    onOpenFullTasbih: () -> Unit
) {
    val islamicColors = LocalIslamicColors.current

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = islamicColors.cardBg,
        border = BorderStroke(1.dp, islamicColors.cardBorder.copy(alpha = 0.6f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Open Tasbih",
                        tint = islamicColors.goldAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "ሱብሓነላህ • ዒላማ: $target",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Quick Tap Circle Button
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(islamicColors.goldAccent.copy(alpha = 0.2f))
                    .clickable { onQuickTap() }
                    .padding(2.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = islamicColors.goldAccent,
                    modifier = Modifier.size(48.dp)
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
    }
}

@Composable
private fun CategoryCard(
    category: AzkarCategory,
    onClick: () -> Unit
) {
    val islamicColors = LocalIslamicColors.current

    val iconVector: ImageVector = when (category.icon) {
        "sun" -> Icons.Default.WbSunny
        "clock" -> Icons.Default.Schedule
        "moon" -> Icons.Default.NightsStay
        "home" -> Icons.Default.Home
        "plane" -> Icons.Outlined.Flight
        else -> Icons.Default.FormatListBulleted
    }

    Surface(
        shape = if (islamicColors.isMasjidArch) {
            RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 14.dp, bottomEnd = 14.dp)
        } else {
            RoundedCornerShape(16.dp)
        },
        color = islamicColors.cardBg,
        border = BorderStroke(1.dp, islamicColors.cardBorder.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("category_card_${category.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Icon Badge
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(islamicColors.goldAccent.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    tint = islamicColors.goldAccent,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = category.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${category.items.size} አዝካሮች",
                    style = MaterialTheme.typography.labelSmall,
                    color = islamicColors.goldAccent,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "View Category",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
