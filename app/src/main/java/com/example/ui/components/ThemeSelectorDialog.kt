package com.example.ui.components

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppThemeMode
import com.example.ui.theme.GlassBg
import com.example.ui.theme.GlassPrimary
import com.example.ui.theme.LocalIslamicColors
import com.example.ui.theme.MasjidBg
import com.example.ui.theme.MasjidPrimary
import com.example.ui.theme.OttomanBg
import com.example.ui.theme.OttomanPrimary
import com.example.ui.theme.SacredLightBg
import com.example.ui.theme.SacredLightPrimary

/**
 * Beautiful modal dialog to select between the 5 Islamic design concepts.
 */
@Composable
fun ThemeSelectorDialog(
    currentTheme: AppThemeMode,
    onSelectTheme: (AppThemeMode) -> Unit,
    onDismiss: () -> Unit
) {
    val islamicColors = LocalIslamicColors.current

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("ዝጋ", color = islamicColors.goldAccent, fontWeight = FontWeight.Bold)
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = null,
                    tint = islamicColors.goldAccent
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "የመተግበሪያውን ገጽታ ይምረጡ",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(AppThemeMode.values()) { mode ->
                    val isSelected = mode == currentTheme

                    val dotColors = when (mode) {
                        AppThemeMode.SACRED_LIGHT -> Pair(SacredLightBg, SacredLightPrimary)
                        AppThemeMode.DARK_EMERALD_NEON -> Pair(com.example.ui.theme.SacredNightBg, com.example.ui.theme.SacredNightPrimary)
                        AppThemeMode.MIDNIGHT_OTTOMAN -> Pair(OttomanBg, OttomanPrimary)
                        AppThemeMode.GLASSMORPHISM_3D -> Pair(GlassBg, GlassPrimary)
                        AppThemeMode.MASJID_ARCHITECTURE -> Pair(MasjidBg, MasjidPrimary)
                    }
                    val dotBg = dotColors.first
                    val dotAccent = dotColors.second

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) islamicColors.goldAccent.copy(alpha = 0.15f) else islamicColors.cardBg,
                        border = BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) islamicColors.goldAccent else islamicColors.cardBorder.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectTheme(mode) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Two-tone palette badge preview
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(dotBg)
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(dotAccent)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = mode.titleAm,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = mode.descAm,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "የተመረጠ ገጽታ",
                                    tint = islamicColors.goldAccent
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

/**
 * Dialog to adjust Arabic and Amharic font size for optimal reading comfort.
 */
@Composable
fun FontSizeDialog(
    currentScale: Float,
    onScaleSelected: (Float) -> Unit,
    onDismiss: () -> Unit
) {
    val islamicColors = LocalIslamicColors.current
    val fontOptions = listOf(
        0.85f to "ትንሽ",
        1.0f to "መደበኛ",
        1.2f to "ትልቅ",
        1.4f to "በጣም ትልቅ"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("አረጋግጥ", color = islamicColors.goldAccent, fontWeight = FontWeight.Bold)
            }
        },
        title = {
            Text(
                text = "የንባብ ፊደል መጠን",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "ለዓይንዎ የሚመችዎትን መጠን ይምረጡ። ለውጡ በአዝካር ንባብ ላይ ይታያል።",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = islamicColors.surfaceHighlight,
                    border = BorderStroke(1.dp, islamicColors.cardBorder.copy(alpha = 0.6f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "ቅድመ እይታ",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                            fontSize = (24 * currentScale).sp,
                            lineHeight = (40 * currentScale).sp,
                            fontWeight = FontWeight.Medium,
                            color = islamicColors.arabicCalligraphyColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                fontOptions.forEach { (scale, label) ->
                    val isSelected = kotlin.math.abs(currentScale - scale) < 0.05f

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) islamicColors.goldAccent.copy(alpha = 0.15f) else islamicColors.cardBg,
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) islamicColors.goldAccent else islamicColors.cardBorder.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onScaleSelected(scale) }
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = islamicColors.goldAccent,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}
