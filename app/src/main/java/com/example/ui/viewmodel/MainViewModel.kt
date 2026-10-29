package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AllahName
import com.example.data.model.AppThemeMode
import com.example.data.model.AzkarCategory
import com.example.data.model.AzkarReaderMode
import com.example.data.model.AzkarSessionProgress
import com.example.data.model.DuaItem
import com.example.data.model.IslamicCity
import com.example.data.model.PrayerSchedule
import com.example.data.model.RecommendedPeriod
import com.example.data.prayer.PrayerTimesCalculator
import com.example.data.repository.AudioPlaybackState
import com.example.data.repository.AudioPlayerManager
import com.example.data.repository.AzkarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

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
    val readerMode: AzkarReaderMode = AzkarReaderMode.GUIDED,
    val guidedItemIndex: Int = 0,
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
    val morningProgress: AzkarSessionProgress? = null,
    val eveningProgress: AzkarSessionProgress? = null,
    val recommendedPeriod: RecommendedPeriod = RecommendedPeriod.BALANCED,
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
            // Apply daily date reset check (only resets daily counters when local date changes)
            repository.checkAndApplyDailyReset()

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

            val allCounts = repository.getAllItemCompletedCounts()
            val morningCat = categories.firstOrNull { it.id == "morning" }
            val eveningCat = categories.firstOrNull { it.id == "evening" }

            val morningProg = computeSessionProgress(morningCat, allCounts)
            val eveningProg = computeSessionProgress(eveningCat, allCounts)
            val recommended = calculateRecommendedPeriod()

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
                    itemCompletedCounts = allCounts,
                    morningProgress = morningProg,
                    eveningProgress = eveningProg,
                    recommendedPeriod = recommended,
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

    private fun calculateRecommendedPeriod(): RecommendedPeriod {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 4..11 -> RecommendedPeriod.MORNING
            in 15..22 -> RecommendedPeriod.EVENING
            else -> RecommendedPeriod.BALANCED
        }
    }

    private fun computeSessionProgress(category: AzkarCategory?, counts: Map<String, Int>): AzkarSessionProgress? {
        if (category == null) return null
        val total = category.items.size
        val completed = category.items.count { item ->
            val count = counts["${category.id}_${item.id}"] ?: counts[item.id] ?: 0
            count >= item.repeat
        }
        val remaining = (total - completed).coerceAtLeast(0)
        val fraction = if (total > 0) completed.toFloat() / total.toFloat() else 0f
        val isComplete = total > 0 && completed == total
        val isStarted = completed > 0 || category.items.any { item ->
            val count = counts["${category.id}_${item.id}"] ?: counts[item.id] ?: 0
            count > 0
        }
        val resumeIdx = repository.getResumeIndex(category.id).coerceIn(0, (total - 1).coerceAtLeast(0))
        return AzkarSessionProgress(
            categoryId = category.id,
            completedItems = completed,
            totalItems = total,
            remainingItems = remaining,
            progressFraction = fraction,
            isComplete = isComplete,
            isStarted = isStarted,
            resumeIndex = resumeIdx
        )
    }

    // ================= Navigation ================= //

    fun setTab(tab: AppTab) {
        _uiState.update { it.copy(currentTab = tab, selectedCategory = null, searchQuery = "") }
    }

    fun openMorningAzkar(guided: Boolean = true) {
        val morning = _uiState.value.categories.firstOrNull { it.id == "morning" } ?: return
        val resumeIdx = repository.getResumeIndex("morning").coerceIn(0, (morning.items.size - 1).coerceAtLeast(0))
        _uiState.update {
            it.copy(
                selectedCategory = morning,
                readerMode = if (guided) AzkarReaderMode.GUIDED else AzkarReaderMode.LIST,
                guidedItemIndex = resumeIdx,
                searchQuery = ""
            )
        }
    }

    fun openEveningAzkar(guided: Boolean = true) {
        val evening = _uiState.value.categories.firstOrNull { it.id == "evening" } ?: return
        val resumeIdx = repository.getResumeIndex("evening").coerceIn(0, (evening.items.size - 1).coerceAtLeast(0))
        _uiState.update {
            it.copy(
                selectedCategory = evening,
                readerMode = if (guided) AzkarReaderMode.GUIDED else AzkarReaderMode.LIST,
                guidedItemIndex = resumeIdx,
                searchQuery = ""
            )
        }
    }

    fun openCategory(category: AzkarCategory, guided: Boolean = false) {
        val resumeIdx = repository.getResumeIndex(category.id).coerceIn(0, (category.items.size - 1).coerceAtLeast(0))
        _uiState.update {
            it.copy(
                selectedCategory = category,
                readerMode = if (guided) AzkarReaderMode.GUIDED else AzkarReaderMode.LIST,
                guidedItemIndex = resumeIdx,
                searchQuery = ""
            )
        }
    }

    fun openCategoryById(categoryId: String, guided: Boolean = false) {
        val cat = _uiState.value.categories.firstOrNull { it.id == categoryId }
        if (cat != null) {
            openCategory(cat, guided)
        }
    }

    fun closeCategory() {
        stopAudio()
        _uiState.update { it.copy(selectedCategory = null, searchQuery = "") }
    }

    fun setReaderMode(mode: AzkarReaderMode) {
        _uiState.update { it.copy(readerMode = mode) }
    }

    fun setGuidedItemIndex(index: Int) {
        val category = _uiState.value.selectedCategory ?: return
        val safeIndex = index.coerceIn(0, (category.items.size - 1).coerceAtLeast(0))
        repository.saveResumeIndex(category.id, safeIndex)
        _uiState.update { it.copy(guidedItemIndex = safeIndex) }
    }

    fun nextGuidedItem() {
        val category = _uiState.value.selectedCategory ?: return
        val nextIdx = (_uiState.value.guidedItemIndex + 1).coerceAtMost(category.items.size - 1)
        setGuidedItemIndex(nextIdx)
    }

    fun prevGuidedItem() {
        val prevIdx = (_uiState.value.guidedItemIndex - 1).coerceAtLeast(0)
        setGuidedItemIndex(prevIdx)
    }

    fun resetCategorySession(categoryId: String) {
        val category = _uiState.value.categories.firstOrNull { it.id == categoryId } ?: return
        repository.resetCategoryProgress(categoryId, category.items.map { it.id })
        val allCounts = repository.getAllItemCompletedCounts()
        val morningCat = _uiState.value.categories.firstOrNull { it.id == "morning" }
        val eveningCat = _uiState.value.categories.firstOrNull { it.id == "evening" }
        _uiState.update {
            it.copy(
                itemCompletedCounts = allCounts,
                guidedItemIndex = 0,
                morningProgress = computeSessionProgress(morningCat, allCounts),
                eveningProgress = computeSessionProgress(eveningCat, allCounts)
            )
        }
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

    fun incrementItemCount(categoryId: String, itemId: String, maxRepeat: Int) {
        repository.incrementItemCount(categoryId, itemId, maxRepeat)
        val updatedCounts = repository.getAllItemCompletedCounts()
        val morningCat = _uiState.value.categories.firstOrNull { it.id == "morning" }
        val eveningCat = _uiState.value.categories.firstOrNull { it.id == "evening" }
        _uiState.update {
            it.copy(
                itemCompletedCounts = updatedCounts,
                morningProgress = computeSessionProgress(morningCat, updatedCounts),
                eveningProgress = computeSessionProgress(eveningCat, updatedCounts)
            )
        }
    }

    fun incrementItemCount(itemId: String, maxRepeat: Int) {
        val catId = _uiState.value.selectedCategory?.id ?: ""
        incrementItemCount(catId, itemId, maxRepeat)
    }

    fun resetItemCount(categoryId: String, itemId: String) {
        repository.resetItemCount(categoryId, itemId)
        val updatedCounts = repository.getAllItemCompletedCounts()
        val morningCat = _uiState.value.categories.firstOrNull { it.id == "morning" }
        val eveningCat = _uiState.value.categories.firstOrNull { it.id == "evening" }
        _uiState.update {
            it.copy(
                itemCompletedCounts = updatedCounts,
                morningProgress = computeSessionProgress(morningCat, updatedCounts),
                eveningProgress = computeSessionProgress(eveningCat, updatedCounts)
            )
        }
    }

    fun resetItemCount(itemId: String) {
        val catId = _uiState.value.selectedCategory?.id ?: ""
        resetItemCount(catId, itemId)
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
