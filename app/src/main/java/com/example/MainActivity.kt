package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.FontSizeDialog
import com.example.ui.components.ModernTasbihView
import com.example.ui.components.ThemeSelectorDialog
import com.example.ui.screens.AzkarListScreen
import com.example.ui.screens.DuasScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NamesOfAllahScreen
import com.example.ui.theme.AzkarAppTheme
import com.example.ui.theme.LocalIslamicColors
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            AzkarAppTheme(themeMode = uiState.currentTheme) {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val islamicColors = LocalIslamicColors.current

    var showThemeDialog by remember { mutableStateOf(false) }
    var showFontDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (uiState.selectedCategory == null) {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "Azkar & Dua • ኑሩል አዝካር",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = uiState.currentTheme.titleAm,
                                style = MaterialTheme.typography.labelSmall,
                                color = islamicColors.goldAccent
                            )
                        }
                    },
                    actions = {
                        // Font size dialog button
                        IconButton(
                            onClick = { showFontDialog = true },
                            modifier = Modifier.testTag("font_size_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.FormatSize,
                                contentDescription = "Font Size",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Theme switch button
                        IconButton(
                            onClick = { showThemeDialog = true },
                            modifier = Modifier.testTag("theme_selector_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = "Theme Switcher",
                                tint = islamicColors.goldAccent
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        scrolledContainerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        },
        bottomBar = {
            if (uiState.selectedCategory == null) {
                NavigationBar(
                    containerColor = islamicColors.cardBg,
                    tonalElevation = 6.dp,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    val tabs = listOf(
                        AppTab.HOME to Icons.Default.Home,
                        AppTab.DUAS to Icons.Default.MenuBook,
                        AppTab.TASBIH to Icons.Default.TouchApp,
                        AppTab.NAMES to Icons.Default.AutoAwesome,
                        AppTab.FAVORITES to Icons.Default.Star
                    )

                    tabs.forEach { (tab, icon) ->
                        val isSelected = uiState.currentTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.setTab(tab) },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = tab.titleAm
                                )
                            },
                            label = {
                                Text(
                                    text = tab.titleAm,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.Black,
                                selectedTextColor = islamicColors.goldAccent,
                                indicatorColor = islamicColors.goldAccent,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = islamicColors.goldAccent)
                }
            } else if (uiState.selectedCategory != null) {
                AzkarListScreen(
                    category = uiState.selectedCategory!!,
                    itemCompletedCounts = uiState.itemCompletedCounts,
                    favorites = uiState.favorites,
                    arabicFontScale = uiState.arabicFontScale,
                    audioState = uiState.audioState,
                    onBack = { viewModel.closeCategory() },
                    onIncrementCount = { id, max -> viewModel.incrementItemCount(id, max) },
                    onResetCount = { id -> viewModel.resetItemCount(id) },
                    onToggleFavorite = { id -> viewModel.toggleFavorite(id) },
                    onToggleAudio = { url -> viewModel.toggleAudio(url) }
                )
            } else {
                Crossfade(targetState = uiState.currentTab, label = "tabCrossfade") { tab ->
                    when (tab) {
                        AppTab.HOME -> {
                            HomeScreen(
                                categories = uiState.categories,
                                tasbihCount = uiState.tasbihCount,
                                tasbihTarget = uiState.tasbihTarget,
                                onCategoryClick = { viewModel.openCategory(it) },
                                onTasbihQuickTap = { viewModel.incrementTasbih() },
                                onNavigateTasbihTab = { viewModel.setTab(AppTab.TASBIH) }
                            )
                        }
                        AppTab.DUAS -> {
                            DuasScreen(
                                duas = uiState.duas,
                                favorites = uiState.favorites,
                                arabicFontScale = uiState.arabicFontScale,
                                onToggleFavorite = { id -> viewModel.toggleFavorite(id) }
                            )
                        }
                        AppTab.TASBIH -> {
                            ModernTasbihView(
                                count = uiState.tasbihCount,
                                target = uiState.tasbihTarget,
                                total = uiState.tasbihTotal,
                                laps = uiState.tasbihLaps,
                                presetText = uiState.tasbihPresetText,
                                vibrationEnabled = uiState.tasbihVibrationEnabled,
                                onIncrement = { viewModel.incrementTasbih() },
                                onReset = { viewModel.resetTasbih() },
                                onSetTarget = { viewModel.setTasbihTarget(it) },
                                onSetPreset = { viewModel.setTasbihPreset(it) },
                                onToggleVibration = { viewModel.toggleVibration() }
                            )
                        }
                        AppTab.NAMES -> {
                            NamesOfAllahScreen(names = uiState.namesOfAllah)
                        }
                        AppTab.FAVORITES -> {
                            FavoritesScreen(
                                categories = uiState.categories,
                                duas = uiState.duas,
                                favorites = uiState.favorites,
                                itemCompletedCounts = uiState.itemCompletedCounts,
                                arabicFontScale = uiState.arabicFontScale,
                                audioState = uiState.audioState,
                                onIncrementCount = { id, max -> viewModel.incrementItemCount(id, max) },
                                onResetCount = { id -> viewModel.resetItemCount(id) },
                                onToggleFavorite = { id -> viewModel.toggleFavorite(id) },
                                onToggleAudio = { url -> viewModel.toggleAudio(url) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Theme Selector Dialog
    if (showThemeDialog) {
        ThemeSelectorDialog(
            currentTheme = uiState.currentTheme,
            onSelectTheme = { theme ->
                viewModel.setTheme(theme)
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )
    }

    // Font Size Dialog
    if (showFontDialog) {
        FontSizeDialog(
            currentScale = uiState.arabicFontScale,
            onScaleSelected = { scale ->
                viewModel.setArabicFontScale(scale)
                showFontDialog = false
            },
            onDismiss = { showFontDialog = false }
        )
    }
}
