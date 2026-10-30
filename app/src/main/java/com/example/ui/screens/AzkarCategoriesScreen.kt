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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.Flight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AzkarCategory
import com.example.data.model.AzkarSessionProgress
import com.example.ui.theme.LocalIslamicColors

/**
 * Dedicated Azkar Tab Screen:
 * Highlights Morning & Evening Azkar with dedicated status sections at top,
 * followed by other daily categories (After Prayer, House, Travel, etc.).
 */
@Composable
fun AzkarCategoriesScreen(
    categories: List<AzkarCategory>,
    morningProgress: AzkarSessionProgress?,
    eveningProgress: AzkarSessionProgress?,
    onStartMorning: () -> Unit,
    onStartEvening: () -> Unit,
    onCategoryClick: (AzkarCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val islamicColors = LocalIslamicColors.current

    val morningCat = categories.firstOrNull { it.id == "morning" }
    val eveningCat = categories.firstOrNull { it.id == "evening" }
    val otherCategories = categories.filter { it.id != "morning" && it.id != "evening" }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("azkar_categories_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Banner
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = islamicColors.cardBg,
                border = BorderStroke(1.dp, islamicColors.goldAccent.copy(alpha = 0.5f)),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Text(
                        text = "የአዝካር ማዕከል",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "የጥዋትና የማታ አዝካሮች የቀንዎ መሰረት ናቸው። በቋሚነት ያንብቧቸው።",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Dedicated Section Header: Morning & Evening
        item {
            Text(
                text = "የቀኑ ሁለት ዋና ዋና አዝካሮች",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Morning Azkar Prominent Card
        item {
            val total = morningProgress?.totalItems ?: (morningCat?.items?.size ?: 24)
            val done = morningProgress?.completedItems ?: 0
            val fraction = morningProgress?.progressFraction ?: 0f
            val isComplete = morningProgress?.isComplete == true

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = islamicColors.cardBg,
                border = BorderStroke(1.5.dp, islamicColors.goldAccent.copy(alpha = 0.6f)),
                shadowElevation = 3.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onStartMorning() }
                    .testTag("azkar_tab_morning_card")
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
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(islamicColors.goldAccent.copy(alpha = 0.16f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WbSunny,
                                    contentDescription = "የጥዋት አዝካር",
                                    tint = islamicColors.goldAccent,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "የጥዋት አዝካር",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "أذكار الصباح",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = islamicColors.goldAccent
                                )
                            }
                        }

                        if (isComplete) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "ተጠናቋል",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(26.dp)
                            )
                        } else {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = "$done / $total",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { fraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = islamicColors.goldAccent,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        strokeCap = StrokeCap.Round
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onStartMorning,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = islamicColors.goldAccent,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isComplete) "አዝካሩን እንደገና ያንብቡ" else if (done > 0) "ከቆሙበት ይቀጥሉ" else "አዝካር ይጀምሩ",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Evening Azkar Prominent Card
        item {
            val total = eveningProgress?.totalItems ?: (eveningCat?.items?.size ?: 22)
            val done = eveningProgress?.completedItems ?: 0
            val fraction = eveningProgress?.progressFraction ?: 0f
            val isComplete = eveningProgress?.isComplete == true

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = islamicColors.cardBg,
                border = BorderStroke(1.5.dp, islamicColors.goldAccent.copy(alpha = 0.6f)),
                shadowElevation = 3.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onStartEvening() }
                    .testTag("azkar_tab_evening_card")
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
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(islamicColors.goldAccent.copy(alpha = 0.16f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NightsStay,
                                    contentDescription = "የማታ አዝካር",
                                    tint = islamicColors.goldAccent,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "የማታ አዝካር",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "أذكار المساء",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = islamicColors.goldAccent
                                )
                            }
                        }

                        if (isComplete) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "ተጠናቋል",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(26.dp)
                            )
                        } else {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = "$done / $total",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { fraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = islamicColors.goldAccent,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        strokeCap = StrokeCap.Round
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onStartEvening,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = islamicColors.goldAccent,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isComplete) "አዝካሩን እንደገና ያንብቡ" else if (done > 0) "ከቆሙበት ይቀጥሉ" else "አዝካር ይጀምሩ",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Section: Other Azkar Categories
        if (otherCategories.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "ተጨማሪ የአዝካር ክፍሎች",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            items(otherCategories) { category ->
                val iconVector: ImageVector = when (category.icon) {
                    "sun" -> Icons.Default.WbSunny
                    "clock" -> Icons.Default.Schedule
                    "moon" -> Icons.Default.NightsStay
                    "home" -> Icons.Default.Home
                    "plane" -> Icons.Outlined.Flight
                    else -> Icons.Default.FormatListBulleted
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = islamicColors.cardBg,
                    border = BorderStroke(1.dp, islamicColors.cardBorder.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCategoryClick(category) }
                        .testTag("azkar_category_${category.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(islamicColors.goldAccent.copy(alpha = 0.16f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = iconVector,
                                contentDescription = category.title,
                                tint = islamicColors.goldAccent,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = category.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = category.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = "${category.items.size} አዝካር",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Open",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
