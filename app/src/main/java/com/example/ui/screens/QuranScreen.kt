package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuranSurah
import com.example.data.repository.QuranRepository
import com.example.ui.components.IslamicGeometricDivider
import com.example.ui.components.IslamicNumberBadge
import com.example.ui.theme.LocalIslamicColors

/**
 * Screen for reading prominent noble Surahs and browsing the full Quran index.
 */
@Composable
fun QuranScreen(
    arabicFontScale: Float,
    modifier: Modifier = Modifier
) {
    val islamicColors = LocalIslamicColors.current
    var selectedSurah by remember { mutableStateOf<QuranSurah?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    if (selectedSurah != null) {
        BackHandler { selectedSurah = null }
        SurahReaderView(
            surah = selectedSurah!!,
            arabicFontScale = arabicFontScale,
            onBack = { selectedSurah = null }
        )
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Search field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                placeholder = {
                    Text(
                        text = "ሱራዎችን በስም ወይም በቁጥር ይፈልጉ...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "ፈልግ",
                        tint = islamicColors.goldAccent
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "ፍለጋውን አጽዳ")
                        }
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = islamicColors.goldAccent,
                    unfocusedBorderColor = islamicColors.cardBorder,
                    focusedContainerColor = islamicColors.cardBg,
                    unfocusedContainerColor = islamicColors.cardBg
                ),
                singleLine = true
            )

            val filteredSurahs = remember(searchQuery) {
                if (searchQuery.isBlank()) {
                    QuranRepository.ALL_SURAHS
                } else {
                    val q = searchQuery.trim().lowercase()
                    QuranRepository.ALL_SURAHS.filter {
                        it.nameAmharic.lowercase().contains(q) ||
                        it.nameTranslit.lowercase().contains(q) ||
                        it.nameArabic.contains(q) ||
                        it.number.toString() == q
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Prominent Surahs horizontal carousel if no search active
                if (searchQuery.isBlank()) {
                    item {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "ተመራጭ ሱራዎች",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = "ሙሉ ንባብ",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = islamicColors.goldAccent
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(QuranRepository.PROMINENT_SURAHS) { surah ->
                                    ProminentSurahCard(
                                        surah = surah,
                                        onClick = { selectedSurah = surah }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    item {
                        Text(
                            text = "ሁሉም ሱራዎች (114)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }
                }

                // Surah list
                items(filteredSurahs) { surah ->
                    SurahRowItem(
                        surah = surah,
                        onClick = {
                            // Check if prominent reader is available, else fallback
                            val prominent = QuranRepository.PROMINENT_SURAHS.firstOrNull { it.number == surah.number }
                            selectedSurah = prominent ?: surah
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProminentSurahCard(
    surah: QuranSurah,
    onClick: () -> Unit
) {
    val islamicColors = LocalIslamicColors.current

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = islamicColors.cardBg,
        border = BorderStroke(1.dp, islamicColors.goldAccent.copy(alpha = 0.6f)),
        modifier = Modifier
            .width(200.dp)
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                IslamicNumberBadge(
                    number = surah.number,
                    badgeColor = islamicColors.goldAccent,
                    textColor = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = surah.revelationType,
                    style = MaterialTheme.typography.labelSmall,
                    color = islamicColors.goldAccent
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = surah.nameArabic,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = islamicColors.arabicCalligraphyColor,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = surah.nameAmharic,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${surah.versesCount} አንቀጾች • ${surah.englishMeaning}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SurahRowItem(
    surah: QuranSurah,
    onClick: () -> Unit
) {
    val islamicColors = LocalIslamicColors.current

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = islamicColors.cardBg,
        border = BorderStroke(1.dp, islamicColors.cardBorder.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IslamicNumberBadge(
                    number = surah.number,
                    badgeColor = islamicColors.goldAccent,
                    textColor = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = surah.nameAmharic,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${surah.nameTranslit} • ${surah.versesCount} አንቀጾች (${surah.revelationType})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = surah.nameArabic,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = islamicColors.arabicCalligraphyColor
            )
        }
    }
}

@Composable
private fun SurahReaderView(
    surah: QuranSurah,
    arabicFontScale: Float,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val islamicColors = LocalIslamicColors.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Reader Header bar
        Surface(
            color = islamicColors.cardBg,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ተመለስ",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Column {
                        Text(
                            text = surah.nameAmharic,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${surah.versesCount} አንቀጾች • ${surah.revelationType}",
                            style = MaterialTheme.typography.labelSmall,
                            color = islamicColors.goldAccent
                        )
                    }
                }

                // Share surah
                IconButton(
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            val content = buildString {
                                append(surah.nameArabic)
                                append(" - ")
                                append(surah.nameAmharic)
                                append("\n\n")
                                surah.prominentVerses.forEach { v ->
                                    append("${v.numberInSurah}. ${v.arabic}\n")
                                    append("${v.amharicTranslation}\n\n")
                                }
                                append("[Amharic Azkar - አማርኛ አዝካር]")
                            }
                            putExtra(Intent.EXTRA_TEXT, content)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "ሱራውን ያጋሩ"))
                    }
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "አጋራ", tint = islamicColors.goldAccent)
                }
            }
        }

        // Surah content list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Surah Title banner
            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = islamicColors.cardBg,
                    border = BorderStroke(1.dp, islamicColors.goldAccent.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = surah.nameArabic,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = islamicColors.arabicCalligraphyColor
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = surah.nameAmharic,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${surah.versesCount} አንቀጾች • ${surah.englishMeaning}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        IslamicGeometricDivider(
                            color = islamicColors.goldAccent.copy(alpha = 0.4f),
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                        Text(
                            text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                            fontSize = (20 * arabicFontScale).sp,
                            fontWeight = FontWeight.Bold,
                            color = islamicColors.arabicCalligraphyColor
                        )
                    }
                }
            }

            // Verses
            items(surah.prominentVerses) { verse ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
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
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IslamicNumberBadge(
                                number = verse.numberInSurah,
                                badgeColor = islamicColors.goldAccent,
                                textColor = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Arabic verse
                        Text(
                            text = verse.arabic,
                            fontSize = (22 * arabicFontScale).sp,
                            lineHeight = (36 * arabicFontScale).sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Right,
                            color = islamicColors.arabicCalligraphyColor,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Amharic translation only (Quran transliteration is intentionally hidden).

                        // Amharic Translation
                        Text(
                            text = verse.amharicTranslation,
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = 24.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
