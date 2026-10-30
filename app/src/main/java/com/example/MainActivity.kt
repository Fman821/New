package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.FontSizeDialog
import com.example.ui.components.ModernTasbihView
import com.example.ui.components.ThemeSelectorDialog
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.AzkarCategoriesScreen
import com.example.ui.screens.AzkarListScreen
import com.example.ui.screens.AzkarReaderScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NamesOfAllahScreen
import com.example.ui.screens.PrayerQiblaScreen
import com.example.ui.screens.QuranAndDuaScreen
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
    var quranInitialTab by remember { mutableIntStateOf(0) }
    // Intercept back button when inside category or secondary screen
    if (uiState.selectedCategory != null) {
        BackHandler { viewModel.closeCategory() }
    } else if (uiState.currentTab != AppTab.HOME) {
        BackHandler { viewModel.setTab(AppTab.HOME) }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (uiState.selectedCategory == null) {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "Amharic Azkar",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "አማርኛ አዝካር • ${uiState.currentTheme.titleAm}",
                                style = MaterialTheme.typography.labelSmall,
                                color = islamicColors.goldAccent,
                                fontSize = 11.sp
                            )
                        }
                    },
                    navigationIcon = {
                        if (uiState.currentTab == AppTab.NAMES || uiState.currentTab == AppTab.FAVORITES || uiState.currentTab == AppTab.ABOUT) {
                            IconButton(onClick = { viewModel.setTab(AppTab.HOME) }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    },
                    actions = {
                        // 99 Names shortcut
                        IconButton(
                            onClick = { viewModel.setTab(AppTab.NAMES) },
                            modifier = Modifier.testTag("names_shortcut_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "99 Names of Allah",
                                tint = if (uiState.currentTab == AppTab.NAMES) islamicColors.goldAccent else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Favorites shortcut
                        IconButton(
                            onClick = { viewModel.setTab(AppTab.FAVORITES) },
                            modifier = Modifier.testTag("favorites_shortcut_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Favorites",
                                tint = if (uiState.currentTab == AppTab.FAVORITES) islamicColors.goldAccent else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // About screen button
                        IconButton(
                            onClick = { viewModel.setTab(AppTab.ABOUT) },
                            modifier = Modifier.testTag("about_app_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "About Amharic Azkar",
                                tint = if (uiState.currentTab == AppTab.ABOUT) islamicColors.goldAccent else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

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
                        AppTab.AZKAR to Icons.Default.WbSunny,
                        AppTab.QURAN_DUA to Icons.Default.MenuBook,
                        AppTab.PRAYER_QIBLA to Icons.Default.Mosque,
                        AppTab.TASBIH to Icons.Default.TouchApp
                    )

                    tabs.forEach { (tab, icon) ->
                        val isSelected = uiState.currentTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (tab == AppTab.QURAN_DUA) {
                                    quranInitialTab = 0
                                }
                                viewModel.setTab(tab)
                            },
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
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp
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
                AzkarReaderScreen(
                    category = uiState.selectedCategory!!,
                    readerMode = uiState.readerMode,
                    guidedIndex = uiState.guidedItemIndex,
                    itemCompletedCounts = uiState.itemCompletedCounts,
                    favorites = uiState.favorites,
                    arabicFontScale = uiState.arabicFontScale,
                    audioState = uiState.audioState,
                    onBack = { viewModel.closeCategory() },
                    onSetReaderMode = { viewModel.setReaderMode(it) },
                    onSetGuidedIndex = { viewModel.setGuidedItemIndex(it) },
                    onIncrementCount = { catId, itemId, max -> viewModel.incrementItemCount(catId, itemId, max) },
                    onResetCount = { catId, itemId -> viewModel.resetItemCount(catId, itemId) },
                    onResetCategorySession = { catId -> viewModel.resetCategorySession(catId) },
                    onToggleFavorite = { id -> viewModel.toggleFavorite(id) },
                    onToggleAudio = { url -> viewModel.toggleAudio(url) },
                    onUpdateFontScale = { viewModel.setArabicFontScale(it) }
                )
            } else {
                Crossfade(targetState = uiState.currentTab, label = "tabCrossfade") { tab ->
                    when (tab) {
                        AppTab.HOME -> {
                            val isVerseBookmarked = uiState.favorites.contains("daily_verse_albaqarah_152")
                            HomeScreen(
                                categories = uiState.categories,
                                morningProgress = uiState.morningProgress,
                                eveningProgress = uiState.eveningProgress,
                                recommendedPeriod = uiState.recommendedPeriod,
                                tasbihCount = uiState.tasbihCount,
                                tasbihTarget = uiState.tasbihTarget,
                                isVerseBookmarked = isVerseBookmarked,
                                onToggleBookmarkVerse = { viewModel.toggleBookmarkDailyVerse() },
                                onStartMorningAzkar = { viewModel.openMorningAzkar(guided = true) },
                                onStartEveningAzkar = { viewModel.openEveningAzkar(guided = true) },
                                onCategoryClick = { viewModel.openCategory(it, guided = true) },
                                onNavigateTab = { targetTab ->
                                    if (targetTab == AppTab.DUAS) {
                                        quranInitialTab = 1
                                        viewModel.setTab(AppTab.QURAN_DUA)
                                    } else {
                                        viewModel.setTab(targetTab)
                                    }
                                },
                                onOpenPrayerTab = {
                                    viewModel.setTab(AppTab.PRAYER_QIBLA)
                                },
                                onOpenQiblaTab = {
                                    viewModel.setTab(AppTab.PRAYER_QIBLA)
                                },
                                onOpenThemeDialog = { showThemeDialog = true },
                                onTasbihQuickTap = { viewModel.incrementTasbih() },
                                onNavigateTasbihTab = { viewModel.setTab(AppTab.TASBIH) }
                            )
                        }
                        AppTab.AZKAR -> {
                            AzkarCategoriesScreen(
                                categories = uiState.categories,
                                morningProgress = uiState.morningProgress,
                                eveningProgress = uiState.eveningProgress,
                                onStartMorning = { viewModel.openMorningAzkar(guided = true) },
                                onStartEvening = { viewModel.openEveningAzkar(guided = true) },
                                onCategoryClick = { viewModel.openCategory(it, guided = true) }
                            )
                        }
                        AppTab.QURAN_DUA, AppTab.DUAS -> {
                            QuranAndDuaScreen(
                                duas = uiState.duas,
                                favorites = uiState.favorites,
                                arabicFontScale = uiState.arabicFontScale,
                                onToggleFavorite = { id -> viewModel.toggleFavorite(id) },
                                initialTab = quranInitialTab
                            )
                        }
                        AppTab.PRAYER_QIBLA -> {
                            PrayerQiblaScreen(
                                currentCity = uiState.currentCity,
                                prayerSchedule = uiState.prayerSchedule,
                                onSelectCity = { viewModel.selectCity(it) }
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
                        AppTab.ABOUT -> {
                            AboutScreen(
                                onBack = { viewModel.setTab(AppTab.HOME) }
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
