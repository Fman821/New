package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.IslamicGeometricDivider
import com.example.ui.theme.DeepEmerald
import com.example.ui.theme.LocalIslamicColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBack: () -> Unit
) {
    val islamicColors = LocalIslamicColors.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ስለ መተግበሪያው (About)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("about_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Logo & Brand Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("about_brand_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = DeepEmerald
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo Image with Circular Frame & Gold Accent
                    Box(
                        modifier = Modifier
                            .size(92.dp)
                            .clip(CircleShape)
                            .border(2.dp, islamicColors.goldAccent, CircleShape)
                            .background(Color(0xFF063B2D)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_app_logo),
                            contentDescription = "Amharic Azkar Logo",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Amharic Azkar",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFFAF7F0),
                        letterSpacing = 0.5.sp
                    )

                    Text(
                        text = "አማርኛ አዝካር",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = islamicColors.goldAccent,
                        fontSize = 17.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Daily Remembrance, Peaceful Life.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFF2D99A),
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.3.sp
                    )
                    Text(
                        text = "«ዕለታዊ ዝክር፣ ሰላማዊ ሕይወት»",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFFAF7F0).copy(alpha = 0.85f),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            IslamicGeometricDivider(color = islamicColors.goldAccent)
            Spacer(modifier = Modifier.height(16.dp))

            // Respectful Description Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = islamicColors.cardBg
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    islamicColors.cardBorder.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = "ስለ አማርኛ አዝካር (About the App)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = islamicColors.goldAccent
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Amharic Azkar is an Islamic companion designed to help users read and follow their daily Morning and Evening Azkar through Arabic Dhikr, Amharic translations, repetition counters, and daily progress tracking.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "አማርኛ አዝካር ሙስሊሙ ማህበረሰብ ዕለታዊ የጥዋትና የማታ አዝካሮችን በቀላሉ በአማርኛ ትርጉምና ትክክለኛ የቁጥር ቆጣሪ በመታገዝ በየቀኑ እንዲያነብ የተዘጋጀ መንፈሳዊ ረዳት ነው።",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Features Overview
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = islamicColors.cardBg
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    islamicColors.cardBorder.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "ዋና ዋና አገልግሎቶች (Key Features)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = islamicColors.goldAccent
                    )

                    FeatureItemRow(
                        icon = Icons.Default.WbSunny,
                        iconColor = Color(0xFFD9B66F),
                        title = "የጥዋት አዝካር (Morning Azkar)",
                        desc = "ከፈጅር በኋላ የሚባሉ ትክክለኛ አዝካሮች ከአማርኛ ትርጉም ጋር"
                    )

                    FeatureItemRow(
                        icon = Icons.Default.NightsStay,
                        iconColor = Color(0xFF64B5F6),
                        title = "የማታ አዝካር (Evening Azkar)",
                        desc = "ከዐስር በኋላ እስከ ፀሐይ መጥለቂያ የሚደረጉ ዚክሮች"
                    )

                    FeatureItemRow(
                        icon = Icons.Default.TouchApp,
                        iconColor = islamicColors.goldAccent,
                        title = "ስማርት ተስቢሕ (Interactive Tasbih)",
                        desc = "የንዝረት ድጋፍ፣ የዙር ቆጣሪና ብጁ ዚክሮች ያለው ተስቢሕ"
                    )

                    FeatureItemRow(
                        icon = Icons.Default.MenuBook,
                        iconColor = Color(0xFF4CAF50),
                        title = "የተመረጡ ሱራዎችና ዱዓዎች (Quran & Duas)",
                        desc = "ሱረቱል ያሲን፣ ዋቂዓህ፣ ሙልክና በርካታ ዕለታዊ ዱዓዎች"
                    )

                    FeatureItemRow(
                        icon = Icons.Default.AutoAwesome,
                        iconColor = islamicColors.goldAccent,
                        title = "99ኙ የአላህ ስሞች (99 Names of Allah)",
                        desc = "አስማኡል ሁስና በአረብኛ፣ በአማርኛ ትርጉምና ማብራሪያ"
                    )

                    FeatureItemRow(
                        icon = Icons.Default.Palette,
                        iconColor = Color(0xFFAB47BC),
                        title = "5 ልዩ እስላማዊ ቴሞች (5 Islamic Themes)",
                        desc = "የቀን ብርሃን፣ የሌሊት ጽሞና፣ የኦቶማን ሰማያዊ፣ ግላስና መስጊድ"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // App Version & Technical Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = islamicColors.cardBg
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    islamicColors.cardBorder.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ስሪት (Version)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "1.0 (Build 1)",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = islamicColors.goldAccent
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ቋንቋዎች (Languages)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "አማርኛ • عربي • English",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ኦፍላይን አገልግሎት",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Offline ready",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "ሙሉ በሙሉ ኦፍላይን ይሰራል",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "«አላህን በብዙ የሚያወሱ ወንዶችና የሚያወሱ ሴቶችም አላህ ለእነርሱ ምህረትንና ታላቅ ምንዳን አዘጋጅቷል»",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FeatureItemRow(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(iconColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
    }
}
