package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.model.AzkarCategory
import com.example.data.model.AzkarItem
import com.example.data.model.DuaItem
import com.example.data.repository.AudioPlaybackState
import com.example.ui.components.AzkarCard
import com.example.ui.theme.LocalIslamicColors

/**
 * Screen showing all favorited / bookmarked Azkar and Duas for quick daily access.
 */
@Composable
fun FavoritesScreen(
    categories: List<AzkarCategory>,
    duas: List<DuaItem>,
    favorites: Set<String>,
    itemCompletedCounts: Map<String, Int>,
    arabicFontScale: Float,
    audioState: AudioPlaybackState,
    onIncrementCount: (String, Int) -> Unit,
    onResetCount: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onToggleAudio: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val islamicColors = LocalIslamicColors.current

    // Gather all favorited Azkar items
    val allAzkarItems = categories.flatMap { it.items }
    val favoritedAzkar = allAzkarItems.filter { favorites.contains(it.id) }

    // Gather all favorited Dua items
    val favoritedDuas = duas.filter { favorites.contains("dua_${it.id}") }.map { dua ->
        AzkarItem(
            id = "dua_${dua.id}",
            title = dua.title,
            ar = dua.ar,
            am = dua.am,
            repeat = dua.repeat,
            source = dua.source,
            audio = dua.audio
        )
    }

    val combinedFavorites = favoritedAzkar + favoritedDuas

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("favorites_screen")
    ) {
        // Header
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
                    text = "ተወዳጅ አዝካሮችና ዱዓዎች",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "በቀላሉ ለማግኘት የመረጧቸው ${combinedFavorites.size} ማስታወሻዎች",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (combinedFavorites.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.StarBorder,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = islamicColors.goldAccent.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "ምንም የተመረጠ አዝካር የለም",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "በማንኛውም አዝካር ወይም ዱዓ ላይ ያለውን የኮከብ ምልክት (★) በመጫን እዚህ ማስቀመጥ ይችላሉ።",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(combinedFavorites, key = { it.id }) { item ->
                    val curCount = itemCompletedCounts[item.id] ?: 0
                    AzkarCard(
                        item = item,
                        completedCount = curCount,
                        isFavorite = true,
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
