package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DuaItem
import com.example.ui.theme.LocalIslamicColors

/**
 * Screen providing combined Quran Surahs reading and Essential Duas browsing.
 */
@Composable
fun QuranAndDuaScreen(
    duas: List<DuaItem>,
    favorites: Set<String>,
    arabicFontScale: Float,
    onToggleFavorite: (String) -> Unit,
    initialTab: Int = 0,
    modifier: Modifier = Modifier
) {
    val islamicColors = LocalIslamicColors.current
    var selectedTab by remember(initialTab) { mutableIntStateOf(initialTab) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = islamicColors.cardBg,
            contentColor = islamicColors.goldAccent,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = islamicColors.goldAccent,
                    height = 3.dp
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ቅዱስ ቁርኣን",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    }
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Outlined.VolunteerActivism, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ዕለታዊ ዱዓዎች",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    }
                }
            )
        }

        if (selectedTab == 0) {
            QuranScreen(arabicFontScale = arabicFontScale)
        } else {
            DuasScreen(
                duas = duas,
                favorites = favorites,
                arabicFontScale = arabicFontScale,
                onToggleFavorite = onToggleFavorite
            )
        }
    }
}
