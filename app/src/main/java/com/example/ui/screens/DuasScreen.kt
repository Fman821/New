package com.example.ui.screens

import android.content.Intent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DuaItem
import com.example.ui.components.IslamicGeometricDivider
import com.example.ui.theme.LocalIslamicColors

/**
 * Screen displaying essential Quranic and Prophetic Duas.
 */
@Composable
fun DuasScreen(
    duas: List<DuaItem>,
    favorites: Set<String>,
    arabicFontScale: Float,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val islamicColors = LocalIslamicColors.current
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    val filteredDuas = remember(searchQuery, duas) {
        if (searchQuery.isBlank()) {
            duas
        } else {
            val q = searchQuery.trim().lowercase()
            duas.filter {
                it.title.lowercase().contains(q) ||
                    it.am.lowercase().contains(q) ||
                    it.ar.contains(q)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("duas_screen")
    ) {
        // Search & Header
        Surface(
            color = islamicColors.cardBg,
            border = BorderStroke(1.dp, islamicColors.cardBorder.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "ጠቃሚ ዱዓዎች (Essential Duas)",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "ከቁርኣንና ከሐዲሥ የተመረጡ ዱዓዎች",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            "በዱዓው ርዕስ ወይም ትርጉም ይፈልጉ...",
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = islamicColors.goldAccent
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = islamicColors.goldAccent,
                        unfocusedBorderColor = islamicColors.cardBorder.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                )
            }
        }

        // Dua Cards
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredDuas, key = { it.id }) { dua ->
                val isFav = favorites.contains("dua_${dua.id}")

                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = islamicColors.cardBg,
                    border = BorderStroke(1.dp, islamicColors.cardBorder.copy(alpha = 0.5f)),
                    shadowElevation = if (islamicColors.isGlassmorphism) 0.dp else 2.dp,
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
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(islamicColors.goldAccent.copy(alpha = 0.15f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${dua.repeat}x",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = islamicColors.goldAccent
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = dua.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Row {
                                IconButton(
                                    onClick = { onToggleFavorite("dua_${dua.id}") },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isFav) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                        contentDescription = "Favorite",
                                        tint = if (isFav) islamicColors.goldAccent else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                            type = "text/plain"
                                            val text = "${dua.title}\n\n${dua.ar}\n\nትርጉም:\n${dua.am}\n\nምንጭ: ${dua.source}\n\n[Azkar & Dua - ኑሩል አዝካር]"
                                            putExtra(Intent.EXTRA_TEXT, text)
                                        }
                                        context.startActivity(Intent.createChooser(shareIntent, "ዱዓውን ያጋሩ"))
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Share",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Arabic text
                        Text(
                            text = dua.ar,
                            fontSize = (22 * arabicFontScale).sp,
                            lineHeight = (36 * arabicFontScale).sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Right,
                            color = islamicColors.arabicCalligraphyColor,
                            modifier = Modifier.fillMaxWidth()
                        )

                        IslamicGeometricDivider(
                            color = islamicColors.goldAccent.copy(alpha = 0.35f),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        // Amharic translation
                        Text(
                            text = dua.am,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 24.sp
                        )

                        if (dua.source.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "ምንጭ: ${dua.source}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
