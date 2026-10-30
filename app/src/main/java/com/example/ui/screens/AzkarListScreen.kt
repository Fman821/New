package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.AzkarCategory
import com.example.data.model.AzkarItem
import com.example.data.repository.AudioPlaybackState
import com.example.ui.components.AzkarCard
import com.example.ui.theme.LocalIslamicColors

/**
 * Screen displaying the full Azkar list of a selected category
 * with completion progress, search filter, and individual counter controls.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AzkarListScreen(
    category: AzkarCategory,
    itemCompletedCounts: Map<String, Int>,
    favorites: Set<String>,
    arabicFontScale: Float,
    audioState: AudioPlaybackState,
    onBack: () -> Unit,
    onIncrementCount: (String, Int) -> Unit,
    onResetCount: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onToggleAudio: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val islamicColors = LocalIslamicColors.current
    var searchQuery by remember { mutableStateOf("") }

    val filteredItems = remember(searchQuery, category.items) {
        if (searchQuery.isBlank()) {
            category.items
        } else {
            val q = searchQuery.trim().lowercase()
            category.items.filter {
                it.title.lowercase().contains(q) ||
                    it.am.lowercase().contains(q) ||
                    it.ar.contains(q)
            }
        }
    }

    // Progress calculation
    val completedCount = category.items.count { item ->
        val count = itemCompletedCounts[item.id] ?: 0
        count >= item.repeat
    }
    val totalCount = category.items.size
    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount.toFloat() else 0f

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("azkar_list_screen")
    ) {
        // Top Header
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ተመለስ",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = category.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = category.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Progress Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "የዛሬ እድገት: $completedCount ከ $totalCount ተጠናቋል",
                        style = MaterialTheme.typography.labelSmall,
                        color = islamicColors.goldAccent,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = islamicColors.goldAccent,
                    trackColor = islamicColors.cardBorder.copy(alpha = 0.2f),
                    strokeCap = StrokeCap.Round
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Search field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            "በአዝካሩ ስም ወይም ትርጉም ይፈልጉ...",
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
                                    contentDescription = "ፍለጋውን አጽዳ",
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

        // List of Azkar
        if (filteredItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ምንም የተገኘ አዝካር የለም",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredItems, key = { it.id }) { item ->
                    val curCount = itemCompletedCounts[item.id] ?: 0
                    val isFav = favorites.contains(item.id)

                    AzkarCard(
                        item = item,
                        completedCount = curCount,
                        isFavorite = isFav,
                        arabicFontScale = arabicFontScale,
                        audioState = audioState,
                        onIncrementCount = { onIncrementCount(item.id, item.repeat) },
                        onResetCount = { onResetCount(item.id) },
                        onToggleFavorite = { onToggleFavorite(item.id) },
                        onToggleAudio = onToggleAudio
                    )
                }
            }
        }
    }
}
