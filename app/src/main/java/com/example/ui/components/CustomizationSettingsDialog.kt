package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.AppThemeMode
import com.example.ui.theme.LocalIslamicColors

@Composable
fun CustomizationSettingsDialog(
    currentTheme: AppThemeMode,
    fontScale: Float,
    vibrationEnabled: Boolean,
    onOpenTheme: () -> Unit,
    onOpenFontSize: () -> Unit,
    onOpenReminders: () -> Unit,
    onToggleVibration: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = LocalIslamicColors.current
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.Settings, contentDescription = null, tint = colors.goldAccent) },
        title = {
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text("የአፕ ማበጀት", fontWeight = FontWeight.Bold)
                Text(
                    "አፑን እንደሚመችዎት ያስተካክሉ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(11.dp)) {
                SectionLabel("መልክ", "የሚታየውን አቀራረብ ይቀይሩ")
                SettingRow(
                    icon = { Icon(Icons.Default.Palette, contentDescription = null, tint = colors.goldAccent) },
                    title = "የገጽታ ቅጥ",
                    detail = currentTheme.titleAm,
                    onClick = onOpenTheme
                )
                SettingRow(
                    icon = { Icon(Icons.Default.FormatSize, contentDescription = null, tint = colors.goldAccent) },
                    title = "የፊደል መጠን",
                    detail = when {
                        fontScale < 0.9f -> "ትንሽ"
                        fontScale < 1.1f -> "መደበኛ"
                        fontScale < 1.3f -> "ትልቅ"
                        else -> "በጣም ትልቅ"
                    },
                    onClick = onOpenFontSize
                )
                SectionLabel("አጠቃቀም", "የአፑን ባህሪያት ያስተካክሉ")
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = colors.cardBg,
                    border = BorderStroke(1.dp, colors.cardBorder.copy(alpha = 0.65f))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Vibration, contentDescription = null, tint = colors.goldAccent)
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("የታስቢህ ንዝረት", fontWeight = FontWeight.SemiBold)
                            Text(
                                if (vibrationEnabled) "በርቷል" else "ጠፍቷል",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = vibrationEnabled,
                            onCheckedChange = { onToggleVibration() },
                            modifier = Modifier.semantics {
                                contentDescription = "የታስቢህ ንዝረት ${if (vibrationEnabled) "አጥፋ" else "አብራ"}"
                            }
                        )
                    }
                }
                SettingRow(
                    icon = { Icon(Icons.Default.Alarm, contentDescription = null, tint = colors.goldAccent) },
                    title = "የአዝካር ማስታወሻ",
                    detail = "የጠዋትና የማታ ሰዓት ያስተካክሉ",
                    onClick = onOpenReminders
                )
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = colors.cardBg.copy(alpha = 0.72f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(11.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = colors.goldAccent)
                        Spacer(Modifier.width(9.dp))
                        Text(
                            "ለውጦችዎ በራስ-ሰር ይቀመጣሉ።",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("ጨርስ", color = colors.goldAccent, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
private fun SectionLabel(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            title,
            style = MaterialTheme.typography.labelLarge,
            color = LocalIslamicColors.current.goldAccent,
            fontWeight = FontWeight.Bold
        )
        Text(
            subtitle,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SettingRow(
    icon: @Composable () -> Unit,
    title: String,
    detail: String,
    onClick: () -> Unit
) {
    val colors = LocalIslamicColors.current
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = colors.cardBg,
        border = BorderStroke(1.dp, colors.cardBorder.copy(alpha = 0.65f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
