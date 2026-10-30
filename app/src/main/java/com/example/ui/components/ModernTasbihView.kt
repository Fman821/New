package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalIslamicColors

val TASBIH_PRESETS = listOf(
    "ሱብሓነላህ" to "سُبْحَانَ اللَّهِ",
    "አልሐምዱሊላህ" to "الْحَمْدُ لِلَّهِ",
    "አላሁ አክበር" to "اللَّهُ أَكْبَرُ",
    "ላ ኢላሃ ኢለላህ" to "لَا إِلَٰهَ إِلَّا اللَّهُ",
    "አስተግፊሩላህ" to "أَسْتَغْفِرُ اللَّهَ",
    "ሶለዋት ዐላ ነቢይ" to "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ",
    "ሱብሓነላሂ ወቢሐምዲሂ" to "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ",
    "ላ ሐውለ ወላ ቁወተ" to "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ"
)

val TARGET_OPTIONS = listOf(33, 99, 100, 1000)

/**
 * Modern digital Tasbih counter with animated ring, tactile feedback,
 * target cycle tracking, and pre-selected Azkar phrases.
 */
@Composable
fun ModernTasbihView(
    count: Int,
    target: Int,
    total: Int,
    laps: Int,
    presetText: String,
    vibrationEnabled: Boolean,
    onIncrement: () -> Unit,
    onReset: () -> Unit,
    onSetTarget: (Int) -> Unit,
    onSetPreset: (String) -> Unit,
    onToggleVibration: () -> Unit,
    modifier: Modifier = Modifier
) {
    val islamicColors = LocalIslamicColors.current
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val buttonScale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1.0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label = "tasbihScale"
    )

    val progress = (count.toFloat() / target.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(),
        label = "progressAnim"
    )

    // Current Arabic text for the selected preset
    val currentPresetArabic = TASBIH_PRESETS.firstOrNull { it.first == presetText }?.second
        ?: "سُبْحَانَ اللَّهِ"

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Preset selector chips
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "የዚክር ቃል ይምረጡ",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(TASBIH_PRESETS) { (am, _) ->
                    val isSelected = am == presetText
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSetPreset(am) },
                        label = { Text(text = am, style = MaterialTheme.typography.labelSmall) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = islamicColors.goldAccent,
                            selectedLabelColor = Color.Black
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = islamicColors.cardBorder
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Target Selector & Controls (33, 99, 100, 1000 + Reset + Vibration)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                TARGET_OPTIONS.forEach { opt ->
                    val isSelected = opt == target
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) islamicColors.goldAccent else islamicColors.cardBg,
                        border = BorderStroke(1.dp, islamicColors.cardBorder),
                        modifier = Modifier
                            .clickable { onSetTarget(opt) }
                            .padding(2.dp)
                    ) {
                        Text(
                            text = "$opt",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Row {
                var showResetDialog by remember { mutableStateOf(false) }

                IconButton(onClick = onToggleVibration) {
                    Icon(
                        imageVector = Icons.Default.Vibration,
                        contentDescription = "ንዝረት አብራ ወይም አጥፋ",
                        tint = if (vibrationEnabled) islamicColors.goldAccent else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
                IconButton(onClick = { showResetDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "ቆጣሪውን ዜሮ አድርግ",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (showResetDialog) {
                    androidx.compose.material3.AlertDialog(
                        onDismissRequest = { showResetDialog = false },
                        title = {
                            Text(
                                text = "ተስቢሕ ቆጣሪውን ማጽዳት ይፈልጋሉ?",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        text = {
                            Text(
                                text = "የአሁኑ የቆጣሪ ቁጥር ($count) ወደ ዜሮ ይመለሳል።",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        confirmButton = {
                            androidx.compose.material3.TextButton(
                                onClick = {
                                    onReset()
                                    showResetDialog = false
                                }
                            ) {
                                Text("አጽዳ", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                            }
                        },
                        dismissButton = {
                            androidx.compose.material3.TextButton(
                                onClick = { showResetDialog = false }
                            ) {
                                Text("ተመለስ")
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Center: Circular Animated Tasbih Ring & Big Button
        Box(
            modifier = Modifier
                .size(260.dp)
                .scale(buttonScale)
                .testTag("tasbih_counter_button"),
            contentAlignment = Alignment.Center
        ) {
            // Background Canvas for Circular Ring
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 14.dp.toPx()
                val radius = (size.minDimension - strokeWidth) / 2
                val center = this.center

                // Background track ring
                drawCircle(
                    color = islamicColors.cardBorder.copy(alpha = 0.25f),
                    radius = radius,
                    style = Stroke(width = strokeWidth)
                )

                // Foreground active ring
                drawArc(
                    brush = Brush.sweepGradient(
                        listOf(
                            islamicColors.goldAccent,
                            islamicColors.arabicCalligraphyColor,
                            islamicColors.goldAccent
                        )
                    ),
                    startAngle = -90f,
                    sweepAngle = animatedProgress * 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            // Big Inner Tap Button
            Surface(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        if (vibrationEnabled) {
                            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                            if (vibrator?.hasVibrator() == true) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    vibrator.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE))
                                } else {
                                    @Suppress("DEPRECATION")
                                    vibrator.vibrate(40)
                                }
                            }
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                        onIncrement()
                    },
                shape = CircleShape,
                color = islamicColors.cardBg,
                border = BorderStroke(2.dp, islamicColors.goldAccent.copy(alpha = 0.6f)),
                shadowElevation = if (islamicColors.isGlassmorphism) 0.dp else 8.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Arabic Zikr
                    Text(
                        text = currentPresetArabic,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = islamicColors.arabicCalligraphyColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Big Number Count
                    Text(
                        text = "$count",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Target pill
                    Text(
                        text = "ዒላማ: $target",
                        style = MaterialTheme.typography.labelSmall,
                        color = islamicColors.goldAccent,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bottom Stats: Total and Laps
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = islamicColors.cardBg,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "ዙር",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "$laps",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = islamicColors.goldAccent
                )
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(30.dp)
                    .background(islamicColors.cardBorder.copy(alpha = 0.5f))
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "አጠቃላይ ድምር",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "$total",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
