package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AllahName
import com.example.data.model.AppThemeMode
import com.example.data.model.AzkarCategory
import com.example.data.model.AzkarItem
import com.example.data.model.DuaItem
import com.example.data.model.IslamicCity
import com.example.data.model.PrayerSchedule
import com.example.data.prayer.PrayerTimesCalculator
import com.example.data.repository.AudioPlaybackState
import com.example.data.repository.AudioPlayerManager
import com.example.data.repository.AzkarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Main navigation destinations in the application.
 */
enum class AppTab(val titleAm: String) {
    HOME("ዋና ገጽ"),
    AZKAR("አዝካር"),
    QURAN_DUA("ቁርኣንና ዱዓ"),
    DUAS("ዱዓዎች"),
    PRAYER_QIBLA("ሰላትና ቂብላ"),
    TASBIH("ተስቢሕ"),
    NAMES("99 ስሞች"),
    FAVORITES("ተወዳጆች")
}

/**
 * UI State holding comprehensive application state.
 */
data class MainUiState(
    val currentTab: AppTab = AppTab.HOME,
    val selectedCategory: AzkarCategory? = null,
    val categories: List<AzkarCategory> = emptyList(),
    val duas: List<DuaItem> = emptyList(),
    val namesOfAllah: List<AllahName> = emptyList(),
    val favorites: Set<String> = emptySet(),
    val currentTheme: AppThemeMode = AppThemeMode.SACRED_LIGHT,
    val arabicFontScale: Float = 1.0f,
    val searchQuery: String = "",
    // Prayer & Qibla state
    val currentCity: IslamicCity = PrayerTimesCalculator.DEFAULT_CITIES.first(),
    val prayerSchedule: PrayerSchedule = PrayerTimesCalculator.calculatePrayerTimes(PrayerTimesCalculator.DEFAULT_CITIES.first()),
    // Tasbih state
    val tasbihCount: Int = 0,
    val tasbihTarget: Int = 33,
    val tasbihTotal: Int = 0,
    val tasbihLaps: Int = 0,
    val tasbihPresetText: String = "ሱብሓነላህ",
    val tasbihVibrationEnabled: Boolean = true,
    // Per-Azkar item completed count in current session
    val itemCompletedCounts: Map<String, Int> = emptyMap(),
    val audioState: AudioPlaybackState = AudioPlaybackState.Idle,
    val isLoading: Boolean = true
)

/**
 * ViewModel orchestrating business logic and state following MVVM pattern.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AzkarRepository(application.applicationContext)
    val audioManager = AudioPlayerManager(application.applicationContext)

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
        observeAudio()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val savedTheme = repository.getSavedTheme()
            val fontScale = repository.getArabicFontScale()
            val favorites = repository.getFavorites()
            val tasbihCount = repository.getTasbihCount()
            val tasbihTarget = repository.getTasbihTarget()
            val tasbihTotal = repository.getTasbihTotal()
            val savedCityId = repository.getSavedCityId()

            val city = PrayerTimesCalculator.DEFAULT_CITIES.firstOrNull { it.id == savedCityId }
                ?: PrayerTimesCalculator.DEFAULT_CITIES.first()
            val schedule = PrayerTimesCalculator.calculatePrayerTimes(city)

            val categories = repository.loadAzkarCategories()
            val duas = repository.loadDuas()
            val names = repository.loadNamesOfAllah()

            _uiState.update {
                it.copy(
                    currentTheme = savedTheme,
                    arabicFontScale = fontScale,
                    favorites = favorites,
                    tasbihCount = tasbihCount,
                    tasbihTarget = tasbihTarget,
                    tasbihTotal = tasbihTotal,
                    currentCity = city,
                    prayerSchedule = schedule,
                    categories = categories,
                    duas = duas,
                    namesOfAllah = names,
                    isLoading = false
                )
            }
        }
    }

    private fun observeAudio() {
        viewModelScope.launch {
            audioManager.playbackState.collect { audioState ->
                _uiState.update { it.copy(audioState = audioState) }
            }
        }
    }

    // ================= Navigation ================= //

    fun setTab(tab: AppTab) {
        _uiState.update { it.copy(currentTab = tab, selectedCategory = null, searchQuery = "") }
    }

    fun openCategory(category: AzkarCategory) {
        _uiState.update { it.copy(selectedCategory = category, searchQuery = "") }
    }

    fun openCategoryById(categoryId: String) {
        val cat = _uiState.value.categories.firstOrNull { it.id == categoryId }
        if (cat != null) {
            openCategory(cat)
        }
    }

    fun closeCategory() {
        _uiState.update { it.copy(selectedCategory = null, searchQuery = "") }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    // ================= Prayer & Qibla ================= //

    fun selectCity(city: IslamicCity) {
        repository.saveCityId(city.id)
        val schedule = PrayerTimesCalculator.calculatePrayerTimes(city)
        _uiState.update {
            it.copy(
                currentCity = city,
                prayerSchedule = schedule
            )
        }
    }

    // ================= Theme & Settings ================= //

    fun setTheme(theme: AppThemeMode) {
        repository.saveTheme(theme)
        _uiState.update { it.copy(currentTheme = theme) }
    }

    fun setArabicFontScale(scale: Float) {
        repository.saveArabicFontScale(scale)
        _uiState.update { it.copy(arabicFontScale = scale) }
    }

    // ================= Favorites ================= //

    fun toggleFavorite(itemId: String) {
        repository.toggleFavorite(itemId)
        val updatedFavs = repository.getFavorites()
        _uiState.update { it.copy(favorites = updatedFavs) }
    }

    fun toggleBookmarkDailyVerse() {
        toggleFavorite("daily_verse_albaqarah_152")
    }

    // ================= Per-Azkar item count tracking ================= //

    fun incrementItemCount(itemId: String, maxRepeat: Int) {
        val current = _uiState.value.itemCompletedCounts[itemId] ?: repository.getItemCompletedCount(itemId)
        val next = if (current < maxRepeat) current + 1 else 0
        repository.incrementItemCount(itemId, maxRepeat)
        _uiState.update {
            it.copy(itemCompletedCounts = it.itemCompletedCounts + (itemId to next))
        }
    }

    fun resetItemCount(itemId: String) {
        repository.resetItemCount(itemId)
        _uiState.update {
            it.copy(itemCompletedCounts = it.itemCompletedCounts + (itemId to 0))
        }
    }

    // ================= Tasbih ================= //

    fun incrementTasbih() {
        val currentCount = _uiState.value.tasbihCount
        val target = _uiState.value.tasbihTarget
        val total = _uiState.value.tasbihTotal + 1
        repository.incrementTasbihTotal()

        val nextCount = currentCount + 1
        val laps = if (nextCount >= target) _uiState.value.tasbihLaps + 1 else _uiState.value.tasbihLaps
        val finalCount = if (nextCount >= target) 0 else nextCount

        repository.saveTasbihCount(finalCount)

        _uiState.update {
            it.copy(
                tasbihCount = finalCount,
                tasbihTotal = total,
                tasbihLaps = laps
            )
        }
    }

    fun resetTasbih() {
        repository.saveTasbihCount(0)
        _uiState.update { it.copy(tasbihCount = 0) }
    }

    fun setTasbihTarget(target: Int) {
        repository.saveTasbihTarget(target)
        _uiState.update { it.copy(tasbihTarget = target, tasbihCount = 0) }
    }

    fun setTasbihPreset(text: String) {
        _uiState.update { it.copy(tasbihPresetText = text) }
    }

    fun toggleVibration() {
        _uiState.update { it.copy(tasbihVibrationEnabled = !it.tasbihVibrationEnabled) }
    }

    // ================= Audio ================= //

    fun toggleAudio(url: String) {
        audioManager.play(url)
    }

    fun stopAudio() {
        audioManager.stop()
    }

    override fun onCleared() {
        super.onCleared()
        audioManager.release()
    }
}
