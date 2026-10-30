package com.example.ui.components

import android.content.Intent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AzkarItem
import com.example.data.repository.AudioPlaybackState
import com.example.ui.theme.LocalIslamicColors

/**
 * An elegant card representing an Azkar item with Arabic text,
 * Amharic translation, repeat progress counter, audio player, and sharing.
 */
@Composable
fun AzkarCard(
    item: AzkarItem,
    completedCount: Int,
    isFavorite: Boolean,
    arabicFontScale: Float,
    audioState: AudioPlaybackState,
    onIncrementCount: () -> Unit,
    onResetCount: () -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleAudio: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val islamicColors = LocalIslamicColors.current
    val isDone = completedCount >= item.repeat

    val cardShape = if (islamicColors.isMasjidArch) {
        RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    } else {
        RoundedCornerShape(18.dp)
    }

    val isAudioForThis = when (audioState) {
        is AudioPlaybackState.Playing -> audioState.audioUrl == item.audio
        is AudioPlaybackState.Buffering -> audioState.audioUrl == item.audio
        is AudioPlaybackState.Paused -> audioState.audioUrl == item.audio
        else -> false
    }

    val isPlaying = isAudioForThis && audioState is AudioPlaybackState.Playing
    val isBuffering = isAudioForThis && audioState is AudioPlaybackState.Buffering
    val isPaused = isAudioForThis && audioState is AudioPlaybackState.Paused

    val borderStroke = BorderStroke(
        width = if (islamicColors.isGlassmorphism) 1.5.dp else 1.dp,
        color = if (isDone) islamicColors.goldAccent else islamicColors.cardBorder.copy(alpha = 0.6f)
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("azkar_card_${item.id}"),
        shape = cardShape,
        color = islamicColors.cardBg,
        tonalElevation = 2.dp,
        shadowElevation = if (islamicColors.isGlassmorphism) 0.dp else 2.dp,
        border = borderStroke
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Title, Repeat Badge, Audio button, Favorite & Share
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Title and repeat requirement
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = islamicColors.goldAccent.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${item.repeat}x",
                            style = MaterialTheme.typography.labelMedium,
                            color = islamicColors.goldAccent,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Action icons: Audio, Favorite, Share
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Audio playback button if URL exists
                    if (item.audio.isNotBlank()) {
                        IconButton(
                            onClick = { onToggleAudio(item.audio) },
                            modifier = Modifier.size(44.dp)
                        ) {
                            if (isBuffering) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = islamicColors.goldAccent
                                )
                            } else if (isPlaying) {
                                Icon(
                                    imageVector = Icons.Outlined.Pause,
                                    contentDescription = "ድምፁን አቁም",
                                    tint = islamicColors.goldAccent
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = if (isPaused) "ድምፁን ቀጥል" else "ድምፅ አጫውት",
                                    tint = islamicColors.goldAccent
                                )
                            }
                        }
                    }

                    // Favorite Button
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                            contentDescription = if (isFavorite) "ከተወዳጆች አስወግድ" else "ወደ ተወዳጆች ጨምር",
                            tint = if (isFavorite) islamicColors.goldAccent else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Share button
                    IconButton(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                val shareBody = buildString {
                                    append(item.title)
                                    append("\n\n")
                                    append(item.ar)
                                    append("\n\n")
                                    if (item.translit.isNotBlank()) {
                                        append("ድምጸ ንባብ:\n${item.translit}\n\n")
                                    }
                                    append("ትርጉም:\n${item.am}")
                                    if (item.source.isNotBlank()) {
                                        append("\n\nምንጭ: ${item.source}")
                                    }
                                    append("\n\n[Azkar & Dua - ኑሩል አዝካር]")
                                }
                                putExtra(Intent.EXTRA_TEXT, shareBody)
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "አዝካሩን ያጋሩ"))
                        },
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "አዝካሩን አጋራ",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Arabic text with custom scaling and lush typography
            val arabicFontSize = (22 * arabicFontScale).sp
            val arabicLineHeight = (36 * arabicFontScale).sp

            Text(
                text = item.ar,
                fontSize = arabicFontSize,
                lineHeight = arabicLineHeight,
                textAlign = TextAlign.Right,
                fontWeight = FontWeight.SemiBold,
                color = islamicColors.arabicCalligraphyColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )

            // Geometric divider
            IslamicGeometricDivider(
                color = islamicColors.goldAccent.copy(alpha = 0.4f),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Transliteration if available
            if (item.translit.isNotBlank()) {
                Text(
                    text = item.translit,
                    style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                    color = islamicColors.goldAccent,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            // Amharic Translation
            Text(
                text = "ትርጉም",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = islamicColors.goldAccent,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = item.am,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 28.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // Footer: Source and Repeat Counter Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (item.source.isNotBlank()) {
                    Text(
                        text = "ምንጭ: ${item.source}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                // Interactive Repeat Counter Pill
                Row(verticalAlignment = Alignment.CenterVertically) {
                    var showResetConfirm by remember { mutableStateOf(false) }

                    if (completedCount > 0) {
                        IconButton(
                            onClick = { showResetConfirm = true },
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "ቆጣሪውን ዳግም አስጀምር",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (showResetConfirm) {
                        androidx.compose.material3.AlertDialog(
                            onDismissRequest = { showResetConfirm = false },
                            title = {
                                Text(
                                    text = "ቆጣሪውን ማጽዳት ይፈልጋሉ?",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            text = {
                                Text(
                                    text = "የዚህ አዝካር የተከናወነ ድግግሞሽ ቆጣሪ ወደ 0 ይመለሳል።",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            confirmButton = {
                                androidx.compose.material3.TextButton(
                                    onClick = {
                                        onResetCount()
                                        showResetConfirm = false
                                    }
                                ) {
                                    Text("አጽዳ", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                                }
                            },
                            dismissButton = {
                                androidx.compose.material3.TextButton(
                                    onClick = { showResetConfirm = false }
                                ) {
                                    Text("ተመለስ")
                                }
                            }
                        )
                    }

                    val buttonBg by animateColorAsState(
                        if (isDone) islamicColors.goldAccent else islamicColors.goldAccent.copy(alpha = 0.15f),
                        label = "pillBg"
                    )
                    val textColor by animateColorAsState(
                        if (isDone) Color.Black else islamicColors.goldAccent,
                        label = "pillText"
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(buttonBg)
                            .clickable { onIncrementCount() }
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isDone) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "አዝካሩ ተጠናቋል",
                                    tint = textColor,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "ተጠናቋል ($completedCount/${item.repeat})",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = textColor,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Text(
                                    text = "$completedCount / ${item.repeat} ደግሙ",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = textColor,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
