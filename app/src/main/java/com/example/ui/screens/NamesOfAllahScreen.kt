package com.example.ui.screens

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AllahName
import com.example.ui.components.IslamicGeometricDivider
import com.example.ui.theme.LocalIslamicColors

/**
 * Screen displaying the 99 Beautiful Names of Allah (Asmaul Husna)
 * with Amharic meanings, transliteration, and search.
 */
@Composable
fun NamesOfAllahScreen(
    names: List<AllahName>,
    modifier: Modifier = Modifier
) {
    val islamicColors = LocalIslamicColors.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedName by remember { mutableStateOf<AllahName?>(null) }

    val filteredNames = remember(searchQuery, names) {
        if (searchQuery.isBlank()) {
            names
        } else {
            val q = searchQuery.trim().lowercase()
            names.filter {
                it.ar.contains(q) ||
                    it.tr.lowercase().contains(q) ||
                    it.am.lowercase().contains(q) ||
                    it.n.toString() == q
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("names_of_allah_screen")
    ) {
        // Header & Search
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
                    text = "የአላህ 99 ውብ ስሞች (Asmaul Husna)",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "«አላህ መልካም ስሞች አሉት በእነርሱም ለምኑት» (አል-አዕራፍ 180)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            "በስሙ ወይም በትርጉሙ ይፈልጉ...",
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

        // 2-column Grid of Names
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredNames, key = { it.n }) { name ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = islamicColors.cardBg,
                    border = BorderStroke(1.dp, islamicColors.cardBorder.copy(alpha = 0.5f)),
                    shadowElevation = if (islamicColors.isGlassmorphism) 0.dp else 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedName = name }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Top row with number badge
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(islamicColors.goldAccent.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${name.n}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = islamicColors.goldAccent
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Arabic Name
                        Text(
                            text = name.ar,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = islamicColors.arabicCalligraphyColor,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        // Transliteration
                        Text(
                            text = name.tr,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = islamicColors.goldAccent,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Amharic Meaning
                        Text(
                            text = name.am,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }

    // Detail Modal Dialog when tapping any Name
    selectedName?.let { item ->
        AlertDialog(
            onDismissRequest = { selectedName = null },
            confirmButton = {
                TextButton(onClick = { selectedName = null }) {
                    Text("እሺ", color = islamicColors.goldAccent, fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "ስም ቁጥር ${item.n}",
                        style = MaterialTheme.typography.labelMedium,
                        color = islamicColors.goldAccent,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = item.ar,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = islamicColors.arabicCalligraphyColor,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = item.tr,
                        style = MaterialTheme.typography.titleMedium,
                        color = islamicColors.goldAccent,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IslamicGeometricDivider(
                        color = islamicColors.goldAccent.copy(alpha = 0.4f),
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                    Text(
                        text = "የስሙ ትርጉም:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.am,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )
                }
            }
        )
    }
}
