package com.example.ui.screens

import android.content.Intent
import android.os.SystemClock
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AzkarCategory
import com.example.data.model.AzkarItem
import com.example.data.model.AzkarReaderMode
import com.example.data.repository.AudioPlaybackState
import com.example.ui.components.AzkarCard
import com.example.ui.components.FontSizeDialog
import com.example.ui.components.IslamicEmblemMotif
import com.example.ui.components.IslamicGeometricDivider
import com.example.ui.theme.LocalIslamicColors

/**
 * Premium, distraction-free Azkar Reading Screen.
 * Provides both Guided Mode (one-by-one with prominent interactive counter and progress)
 * and List Mode (scrollable overview), with session resumption, accurate verified counts,
 * haptic feedback, and a graceful completion experience.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AzkarReaderScreen(
    category: AzkarCategory,
    readerMode: AzkarReaderMode,
    guidedIndex: Int,
    itemCompletedCounts: Map<String, Int>,
    favorites: Set<String>,
    arabicFontScale: Float,
    audioState: AudioPlaybackState,
    onBack: () -> Unit,
    onSetReaderMode: (AzkarReaderMode) -> Unit,
    onSetGuidedIndex: (Int) -> Unit,
    onIncrementCount: (String, String, Int) -> Unit,
    onResetCount: (String, String) -> Unit,
    onResetCategorySession: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onToggleAudio: (String) -> Unit,
    onUpdateFontScale: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val islamicColors = LocalIslamicColors.current
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    var showFontDialog by remember { mutableStateOf(false) }
    var showResetSessionDialog by remember { mutableStateOf(false) }
    var showCompletionModal by remember { mutableStateOf(false) }
    var listSearchQuery by remember { mutableStateOf("") }

    val items = category.items
    val totalCount = items.size
    val safeIndex = guidedIndex.coerceIn(0, (totalCount - 1).coerceAtLeast(0))

    // Real progress calculation
    val completedCount = items.count { item ->
        val count = itemCompletedCounts["${category.id}_${item.id}"]
            ?: itemCompletedCounts[item.id]
            ?: 0
        count >= item.repeat
    }
    val progressFraction = if (totalCount > 0) completedCount.toFloat() / totalCount.toFloat() else 0f
    val isAllCompleted = totalCount > 0 && completedCount == totalCount

    // Automatically suggest completion modal when user just completed all items
    LaunchedEffect(isAllCompleted) {
        if (isAllCompleted && readerMode == AzkarReaderMode.GUIDED) {
            showCompletionModal = true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("azkar_reader_screen")
    ) {
        // =====================================================================
        // TOP APP BAR / HEADER
        // =====================================================================
        Surface(
            color = islamicColors.cardBg,
            border = BorderStroke(1.dp, islamicColors.cardBorder.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp)
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
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("reader_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Column {
                            Text(
                                text = category.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (category.id == "morning") "የጥዋት አዝካር • أذكار الصباح"
                                else if (category.id == "evening") "የማታ አዝካር • أذكار المساء"
                                else category.subtitle,
                                style = MaterialTheme.typography.labelSmall,
                                color = islamicColors.goldAccent,
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Mode switch (Guided vs List)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                val nextMode = if (readerMode == AzkarReaderMode.GUIDED) {
                                    AzkarReaderMode.LIST
                                } else {
                                    AzkarReaderMode.GUIDED
                                }
                                onSetReaderMode(nextMode)
                            },
                            modifier = Modifier
                                .size(38.dp)
                                .testTag("toggle_reader_mode_button")
                        ) {
                            Icon(
                                imageVector = if (readerMode == AzkarReaderMode.GUIDED) Icons.Default.FormatListBulleted else Icons.Default.ViewCarousel,
                                contentDescription = if (readerMode == AzkarReaderMode.GUIDED) "Switch to List Mode" else "Switch to Guided Mode",
                                tint = islamicColors.goldAccent
                            )
                        }

                        IconButton(
                            onClick = { showFontDialog = true },
                            modifier = Modifier
                                .size(38.dp)
                                .testTag("reader_font_size_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.FormatSize,
                                contentDescription = "Adjust Arabic Font Size",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        IconButton(
                            onClick = { showResetSessionDialog = true },
                            modifier = Modifier
                                .size(38.dp)
                                .testTag("reset_session_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.RestartAlt,
                                contentDescription = "Reset Session",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Progress Bar & Stats
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
                        text = "${(progressFraction * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = islamicColors.goldAccent,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                LinearProgressIndicator(
                    progress = { progressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .testTag("session_progress_bar"),
                    color = islamicColors.goldAccent,
                    trackColor = islamicColors.goldAccent.copy(alpha = 0.2f),
                    strokeCap = StrokeCap.Round
                )
            }
        }

        // =====================================================================
        // CONTENT: GUIDED MODE VS LIST MODE
        // =====================================================================
        if (readerMode == AzkarReaderMode.GUIDED && items.isNotEmpty()) {
            GuidedAzkarView(
                category = category,
                items = items,
                currentIndex = safeIndex,
                itemCompletedCounts = itemCompletedCounts,
                favorites = favorites,
                arabicFontScale = arabicFontScale,
                audioState = audioState,
                isAllCompleted = isAllCompleted,
                onSelectIndex = { onSetGuidedIndex(it) },
                onIncrement = { item ->
                    onIncrementCount(category.id, item.id, item.repeat)
                },
                onResetItem = { item ->
                    onResetCount(category.id, item.id)
                },
                onToggleFavorite = { item ->
                    onToggleFavorite(item.id)
                },
                onToggleAudio = { url ->
                    onToggleAudio(url)
                },
                onOpenCompletion = { showCompletionModal = true },
                onPrev = {
                    if (safeIndex > 0) onSetGuidedIndex(safeIndex - 1)
                },
                onNext = {
                    if (safeIndex < items.size - 1) onSetGuidedIndex(safeIndex + 1)
                }
            )
        } else {
            // Full List Mode
            ListAzkarView(
                category = category,
                items = items,
                searchQuery = listSearchQuery,
                onSearchQueryChange = { listSearchQuery = it },
                itemCompletedCounts = itemCompletedCounts,
                favorites = favorites,
                arabicFontScale = arabicFontScale,
                audioState = audioState,
                onIncrement = { item ->
                    onIncrementCount(category.id, item.id, item.repeat)
                },
                onReset = { item ->
                    onResetCount(category.id, item.id)
                },
                onToggleFavorite = { item ->
                    onToggleFavorite(item.id)
                },
                onToggleAudio = { url ->
                    onToggleAudio(url)
                }
            )
        }
    }

    // =====================================================================
    // DIALOGS & MODALS
    // =====================================================================

    // Font Size Dialog
    if (showFontDialog) {
        FontSizeDialog(
            currentScale = arabicFontScale,
            onScaleSelected = { scale ->
                onUpdateFontScale(scale)
                showFontDialog = false
            },
            onDismiss = { showFontDialog = false }
        )
    }

    // Reset Session Confirmation Dialog
    if (showResetSessionDialog) {
        AlertDialog(
            onDismissRequest = { showResetSessionDialog = false },
            title = {
                Text(
                    text = "ክፍለ-ጊዜውን እንደገና መጀመር?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "የዚህ ${category.title} የዛሬ የቆጣሪ ድግግሞሾች በሙሉ ወደ 0 ይመለሳሉ። ታሪክና ተወዳጅ ምልክቶች አይጠፉም።",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onResetCategorySession(category.id)
                        showResetSessionDialog = false
                    }
                ) {
                    Text("እንደገና ጀምር (Reset)", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetSessionDialog = false }) {
                    Text("ተመለስ (Cancel)")
                }
            }
        )
    }

    // Completion Screen Modal
    if (showCompletionModal) {
        AzkarCompletionDialog(
            categoryTitle = category.title,
            completedCount = completedCount,
            totalCount = totalCount,
            onReview = {
                showCompletionModal = false
                onSetGuidedIndex(0)
            },
            onReturnHome = {
                showCompletionModal = false
                onBack()
            },
            onDismiss = { showCompletionModal = false }
        )
    }
}

/**
 * Guided view displaying one Azkar at a time with prominent count interactions.
 */
@Composable
private fun GuidedAzkarView(
    category: AzkarCategory,
    items: List<AzkarItem>,
    currentIndex: Int,
    itemCompletedCounts: Map<String, Int>,
    favorites: Set<String>,
    arabicFontScale: Float,
    audioState: AudioPlaybackState,
    isAllCompleted: Boolean,
    onSelectIndex: (Int) -> Unit,
    onIncrement: (AzkarItem) -> Unit,
    onResetItem: (AzkarItem) -> Unit,
    onToggleFavorite: (AzkarItem) -> Unit,
    onToggleAudio: (String) -> Unit,
    onOpenCompletion: () -> Unit,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {
    val context = LocalContext.current
    val islamicColors = LocalIslamicColors.current
    val haptic = LocalHapticFeedback.current

    val item = items[currentIndex]
    val completedCount = itemCompletedCounts["${category.id}_${item.id}"]
        ?: itemCompletedCounts[item.id]
        ?: 0
    val isItemDone = completedCount >= item.repeat
    val isFavorite = favorites.contains(item.id)

    // Debounced counting to prevent accidental double counting
    var lastTapTime by remember { mutableLongStateOf(0L) }
    var showResetConfirm by remember { mutableStateOf(false) }

    val isAudioForThis = when (audioState) {
        is AudioPlaybackState.Playing -> audioState.audioUrl == item.audio
        is AudioPlaybackState.Buffering -> audioState.audioUrl == item.audio
        is AudioPlaybackState.Paused -> audioState.audioUrl == item.audio
        else -> false
    }
    val isPlaying = isAudioForThis && audioState is AudioPlaybackState.Playing
    val isBuffering = isAudioForThis && audioState is AudioPlaybackState.Buffering

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("guided_azkar_view")
    ) {
        // Step Indicator Row (Horizontally scrollable pills)
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(islamicColors.cardBg.copy(alpha = 0.5f))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            itemsIndexed(items) { idx, stepItem ->
                val isStepDone = (itemCompletedCounts["${category.id}_${stepItem.id}"]
                    ?: itemCompletedCounts[stepItem.id] ?: 0) >= stepItem.repeat
                val isSelected = idx == currentIndex

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) islamicColors.goldAccent
                    else if (isStepDone) islamicColors.goldAccent.copy(alpha = 0.25f)
                    else islamicColors.cardBg,
                    border = BorderStroke(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) islamicColors.goldAccent
                        else if (isStepDone) islamicColors.goldAccent.copy(alpha = 0.6f)
                        else islamicColors.cardBorder.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier
                        .clickable { onSelectIndex(idx) }
                        .testTag("step_pill_$idx")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isStepDone) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Done",
                                modifier = Modifier.size(12.dp),
                                tint = if (isSelected) Color.Black else islamicColors.goldAccent
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                        }
                        Text(
                            text = "${idx + 1}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.Black
                            else if (isStepDone) islamicColors.goldAccent
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Scrollable Card area
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Main Azkar Card
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = islamicColors.cardBg,
                border = BorderStroke(
                    width = if (isItemDone) 1.5.dp else 1.dp,
                    color = if (isItemDone) islamicColors.goldAccent else islamicColors.cardBorder.copy(alpha = 0.5f)
                ),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    // Header: Position, Target, Audio, Favorite, Share
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = islamicColors.goldAccent.copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, islamicColors.goldAccent.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = "ንጥል ${currentIndex + 1} ከ ${items.size}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = islamicColors.goldAccent,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = "${item.repeat}x ድገሙ",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Audio button if audio available
                            if (item.audio.isNotBlank()) {
                                IconButton(
                                    onClick = { onToggleAudio(item.audio) },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .testTag("guided_audio_button")
                                ) {
                                    if (isBuffering) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(18.dp),
                                            strokeWidth = 2.dp,
                                            color = islamicColors.goldAccent
                                        )
                                    } else if (isPlaying) {
                                        Icon(
                                            imageVector = Icons.Outlined.Pause,
                                            contentDescription = "Pause",
                                            tint = islamicColors.goldAccent
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = "Play Audio",
                                            tint = islamicColors.goldAccent
                                        )
                                    }
                                }
                            }

                            // Favorite
                            IconButton(
                                onClick = { onToggleFavorite(item) },
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("guided_favorite_button")
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                    contentDescription = "Favorite",
                                    tint = if (isFavorite) islamicColors.goldAccent else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            // Share
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
                                            append("\n\n[Nur Islamic Lifestyle - ኑሩል እስላም]")
                                        }
                                        putExtra(Intent.EXTRA_TEXT, shareBody)
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "አዝካሩን ያጋሩ"))
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("guided_share_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Arabic Text
                    val arabicFontSize = (24 * arabicFontScale).sp
                    val arabicLineHeight = (40 * arabicFontScale).sp

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

                    IslamicGeometricDivider(
                        color = islamicColors.goldAccent.copy(alpha = 0.4f),
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    // Transliteration
                    if (item.translit.isNotBlank()) {
                        Text(
                            text = item.translit,
                            style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                            color = islamicColors.goldAccent,
                            lineHeight = 22.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    // Amharic Translation
                    Text(
                        text = item.am,
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 25.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (item.source.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text(
                                text = "ምንጭ: ${item.source}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // =================================================================
            // LARGE PROMINENT INTERACTIVE COUNTER BUTTON
            // =================================================================
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                val counterFraction = (completedCount.toFloat() / item.repeat.toFloat()).coerceIn(0f, 1f)
                val animatedProgress by animateFloatAsState(targetValue = counterFraction, label = "counterProgress")
                val buttonBg by animateColorAsState(
                    if (isItemDone) islamicColors.goldAccent else islamicColors.cardBg,
                    label = "btnBg"
                )
                val textColor by animateColorAsState(
                    if (isItemDone) Color.Black else islamicColors.goldAccent,
                    label = "btnTextColor"
                )

                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = buttonBg,
                    border = BorderStroke(
                        width = 2.dp,
                        color = if (isItemDone) islamicColors.goldAccent else islamicColors.goldAccent.copy(alpha = 0.5f)
                    ),
                    shadowElevation = if (isItemDone) 6.dp else 3.dp,
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .clip(RoundedCornerShape(28.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            val now = SystemClock.uptimeMillis()
                            if (now - lastTapTime > 150) {
                                lastTapTime = now
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onIncrement(item)
                            }
                        }
                        .testTag("guided_counter_button")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp, horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Circular progress ring
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(54.dp)
                        ) {
                            Canvas(modifier = Modifier.size(54.dp)) {
                                drawCircle(
                                    color = if (isItemDone) Color.Black.copy(alpha = 0.15f) else islamicColors.goldAccent.copy(alpha = 0.2f),
                                    style = Stroke(width = 4.dp.toPx())
                                )
                                drawArc(
                                    color = if (isItemDone) Color.Black else islamicColors.goldAccent,
                                    startAngle = -90f,
                                    sweepAngle = animatedProgress * 360f,
                                    useCenter = false,
                                    style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                                )
                            }
                            if (isItemDone) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Complete",
                                    tint = Color.Black,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Text(
                                    text = "$completedCount",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor
                                )
                            }
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = if (isItemDone) "ተጠናቋል! (Completed)" else "ቆጣሪውን ይንኩ (Tap to Count)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                            Text(
                                text = "$completedCount ከ ${item.repeat} ድግግሞሽ",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isItemDone) Color.Black.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Reset item button
                        if (completedCount > 0) {
                            IconButton(
                                onClick = { showResetConfirm = true },
                                modifier = Modifier
                                    .size(40.dp)
                                    .testTag("guided_reset_item_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Reset",
                                    tint = if (isItemDone) Color.Black.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.size(40.dp))
                        }
                    }
                }
            }

            if (showResetConfirm) {
                AlertDialog(
                    onDismissRequest = { showResetConfirm = false },
                    title = {
                        Text("ይህን አዝካር ማጽዳት?", fontWeight = FontWeight.Bold)
                    },
                    text = {
                        Text("የዚህ አዝካር የተከናወነ ድግግሞሽ ቆጣሪ ወደ 0 ይመለሳል።")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                onResetItem(item)
                                showResetConfirm = false
                            }
                        ) {
                            Text("አጽዳ (Reset)", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showResetConfirm = false }) {
                            Text("ተመለስ (Cancel)")
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Completion Banner if all items completed
            if (isAllCompleted) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = islamicColors.goldAccent.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, islamicColors.goldAccent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenCompletion() }
                        .testTag("session_complete_banner")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Completed",
                                tint = islamicColors.goldAccent,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "ማሻአላህ! ክፍለ-ጊዜው ተጠናቋል",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${items.size}/${items.size} አዝካሮች ተከናውነዋል • ማጠቃለያውን ይመልከቱ",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "View Summary",
                            tint = islamicColors.goldAccent
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // =====================================================================
        // BOTTOM NAVIGATION CONTROLS (Prev / Next)
        // =====================================================================
        Surface(
            color = islamicColors.cardBg,
            border = BorderStroke(1.dp, islamicColors.cardBorder.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = onPrev,
                    enabled = currentIndex > 0,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(
                        1.dp,
                        if (currentIndex > 0) islamicColors.goldAccent else Color.Transparent
                    ),
                    modifier = Modifier.testTag("guided_prev_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Previous",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("ወደ ኋላ (Prev)")
                }

                Text(
                    text = "${currentIndex + 1} / ${items.size}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Button(
                    onClick = {
                        if (currentIndex < items.size - 1) {
                            onNext()
                        } else if (isAllCompleted) {
                            onOpenCompletion()
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isItemDone) islamicColors.goldAccent else MaterialTheme.colorScheme.primary,
                        contentColor = if (isItemDone) Color.Black else Color.White
                    ),
                    modifier = Modifier.testTag("guided_next_button")
                ) {
                    Text(
                        text = if (currentIndex < items.size - 1) "ቀጣይ (Next)" else "አጠናቅ (Finish)",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

/**
 * Full List View showing all cards in a vertical scroll with search filter.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListAzkarView(
    category: AzkarCategory,
    items: List<AzkarItem>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    itemCompletedCounts: Map<String, Int>,
    favorites: Set<String>,
    arabicFontScale: Float,
    audioState: AudioPlaybackState,
    onIncrement: (AzkarItem) -> Unit,
    onReset: (AzkarItem) -> Unit,
    onToggleFavorite: (AzkarItem) -> Unit,
    onToggleAudio: (String) -> Unit
) {
    val islamicColors = LocalIslamicColors.current

    val filteredItems = remember(searchQuery, items) {
        if (searchQuery.isBlank()) {
            items
        } else {
            val q = searchQuery.trim().lowercase()
            items.filter {
                it.title.lowercase().contains(q) ||
                    it.am.lowercase().contains(q) ||
                    it.ar.contains(q)
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("list_azkar_view"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Search Field
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("azkar_search_input"),
                placeholder = { Text("አዝካር ይፈልጉ (Search Azkar)...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = islamicColors.goldAccent
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = islamicColors.goldAccent,
                    unfocusedBorderColor = islamicColors.cardBorder.copy(alpha = 0.5f),
                    focusedContainerColor = islamicColors.cardBg,
                    unfocusedContainerColor = islamicColors.cardBg
                )
            )
        }

        items(filteredItems, key = { it.id }) { item ->
            val count = itemCompletedCounts["${category.id}_${item.id}"]
                ?: itemCompletedCounts[item.id]
                ?: 0
            val isFavorite = favorites.contains(item.id)

            AzkarCard(
                item = item,
                completedCount = count,
                isFavorite = isFavorite,
                arabicFontScale = arabicFontScale,
                audioState = audioState,
                onIncrementCount = { onIncrement(item) },
                onResetCount = { onReset(item) },
                onToggleFavorite = { onToggleFavorite(item) },
                onToggleAudio = onToggleAudio
            )
        }
    }
}

/**
 * Elegant Completion Dialog when user completes the entire session.
 */
@Composable
fun AzkarCompletionDialog(
    categoryTitle: String,
    completedCount: Int,
    totalCount: Int,
    onReview: () -> Unit,
    onReturnHome: () -> Unit,
    onDismiss: () -> Unit
) {
    val islamicColors = LocalIslamicColors.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = null,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Peaceful Islamic Emblem
                IslamicEmblemMotif(
                    primaryColor = islamicColors.arabicCalligraphyColor,
                    accentColor = islamicColors.goldAccent,
                    modifier = Modifier.size(76.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "ማሻአላህ!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = islamicColors.goldAccent
                )

                Text(
                    text = "የ$categoryTitle ክፍለ-ጊዜ ተጠናቋል",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = islamicColors.goldAccent.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, islamicColors.goldAccent.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$completedCount ከ $totalCount አዝካሮች",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = islamicColors.goldAccent
                        )
                        Text(
                            text = "የዛሬውን የአዝካር ድግግሞሽ በተሳካ ሁኔታ አጠናቀዋል",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "«أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ»\n«አዋጅ! አላህን በማውሳት ልቦች ይረካሉ»",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onReturnHome,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = islamicColors.goldAccent,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("completion_return_home_button")
            ) {
                Icon(imageVector = Icons.Default.Home, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("ወደ ዋና ገጽ ተመለስ (Home)", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onReview,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("completion_review_button")
            ) {
                Text("አዝካሩን ከልስ (Review Azkar)")
            }
        }
    )
}
