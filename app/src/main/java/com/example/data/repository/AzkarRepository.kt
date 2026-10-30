package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.AllahName
import com.example.data.model.AppThemeMode
import com.example.data.model.AzkarCategory
import com.example.data.model.AzkarItem
import com.example.data.model.DuaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Repository responsible for loading Islamic Azkar, Duas, and Asmaul Husna from assets,
 * as well as persisting user preferences (Theme, Favorites, Tasbih counters, Font sizes).
 */
class AzkarRepository(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("nurul_azkar_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_THEME = "pref_theme"
        private const val KEY_ARABIC_FONT_SCALE = "pref_arabic_font_scale"
        private const val KEY_FAVORITES = "pref_favorites_set"
        private const val KEY_TASBIH_COUNT = "pref_tasbih_count"
        private const val KEY_TASBIH_TARGET = "pref_tasbih_target"
        private const val KEY_TASBIH_TOTAL = "pref_tasbih_total"
        private const val KEY_TASBIH_LAPS = "pref_tasbih_laps"
        private const val KEY_TASBIH_PRESET = "pref_tasbih_preset"
        private const val KEY_TASBIH_VIBRATION = "pref_tasbih_vibration"
        private const val KEY_COMPLETED_AZKAR_PREFIX = "pref_done_azkar_"
        private const val KEY_DAILY_DATE = "pref_daily_date"
        private const val KEY_RESUME_INDEX_PREFIX = "pref_resume_idx_"
    }

    /**
     * Reads all Azkar categories and their items from assets/azkar.json
     */
    suspend fun loadAzkarCategories(): List<AzkarCategory> = withContext(Dispatchers.IO) {
        val categories = mutableListOf<AzkarCategory>()
        try {
            val jsonString = readAssetFile("azkar.json")
            val root = JSONObject(jsonString)
            val jsonCats = root.getJSONArray("categories")

            for (i in 0 until jsonCats.length()) {
                val catObj = jsonCats.getJSONObject(i)
                val id = catObj.optString("id", "")
                val title = catObj.optString("title", "")
                val subtitle = catObj.optString("subtitle", "")
                val icon = catObj.optString("icon", "sun")
                val image = catObj.optString("image", "")

                val itemsList = mutableListOf<AzkarItem>()
                val itemsArr = catObj.optJSONArray("items")
                if (itemsArr != null) {
                    for (j in 0 until itemsArr.length()) {
                        val itemObj = itemsArr.getJSONObject(j)
                        itemsList.add(
                            AzkarItem(
                                id = itemObj.optString("id", "$j"),
                                title = itemObj.optString("title", ""),
                                ar = itemObj.optString("ar", "").trim(),
                                am = itemObj.optString("am", "").trim(),
                                repeat = itemObj.optInt("repeat", 1),
                                source = itemObj.optString("source", ""),
                                audio = itemObj.optString("audio", ""),
                                translit = itemObj.optString("translit", "").trim()
                            )
                        )
                    }
                }

                categories.add(
                    AzkarCategory(
                        id = id,
                        title = title,
                        subtitle = subtitle,
                        icon = icon,
                        image = image,
                        items = itemsList
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        categories
    }

    /**
     * Reads essential Duas from assets/duas.json
     */
    suspend fun loadDuas(): List<DuaItem> = withContext(Dispatchers.IO) {
        val duas = mutableListOf<DuaItem>()
        try {
            val jsonString = readAssetFile("duas.json")
            val jsonArr = JSONArray(jsonString)
            for (i in 0 until jsonArr.length()) {
                val obj = jsonArr.getJSONObject(i)
                duas.add(
                    DuaItem(
                        id = obj.optString("id", "$i"),
                        title = obj.optString("title", ""),
                        ar = obj.optString("ar", "").trim(),
                        am = obj.optString("am", "").trim(),
                        repeat = obj.optInt("repeat", 1),
                        source = obj.optString("source", ""),
                        audio = obj.optString("audio", "")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        duas
    }

    /**
     * Reads 99 Names of Allah from assets/names.json
     */
    suspend fun loadNamesOfAllah(): List<AllahName> = withContext(Dispatchers.IO) {
        val names = mutableListOf<AllahName>()
        try {
            val jsonString = readAssetFile("names.json")
            val jsonArr = JSONArray(jsonString)
            for (i in 0 until jsonArr.length()) {
                val obj = jsonArr.getJSONObject(i)
                names.add(
                    AllahName(
                        n = obj.optInt("n", i + 1),
                        ar = obj.optString("ar", "").trim(),
                        tr = obj.optString("tr", "").trim(),
                        am = obj.optString("am", "").trim()
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        names
    }

    private fun readAssetFile(fileName: String): String {
        val inputStream = context.assets.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.use { it.readText() }
    }

    // ================= Theme & Preferences ================= //

    fun getSavedTheme(): AppThemeMode {
        val name = prefs.getString(KEY_THEME, AppThemeMode.DARK_EMERALD_NEON.name)
        return try {
            AppThemeMode.valueOf(name ?: AppThemeMode.DARK_EMERALD_NEON.name)
        } catch (_: Exception) {
            AppThemeMode.DARK_EMERALD_NEON
        }
    }

    fun saveTheme(theme: AppThemeMode) {
        prefs.edit().putString(KEY_THEME, theme.name).apply()
    }

    fun getArabicFontScale(): Float {
        return prefs.getFloat(KEY_ARABIC_FONT_SCALE, 1.0f)
    }

    fun saveArabicFontScale(scale: Float) {
        prefs.edit().putFloat(KEY_ARABIC_FONT_SCALE, scale).apply()
    }

    // ================= Favorites ================= //

    fun getFavorites(): Set<String> {
        return prefs.getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet()
    }

    fun toggleFavorite(itemId: String): Boolean {
        val current = getFavorites().toMutableSet()
        val isFav = if (current.contains(itemId)) {
            current.remove(itemId)
            false
        } else {
            current.add(itemId)
            true
        }
        prefs.edit().putStringSet(KEY_FAVORITES, current).apply()
        return isFav
    }

    // ================= Tasbih State ================= //

    fun getTasbihCount(): Int = prefs.getInt(KEY_TASBIH_COUNT, 0)
    fun saveTasbihCount(count: Int) = prefs.edit().putInt(KEY_TASBIH_COUNT, count).apply()

    fun getTasbihTarget(): Int = prefs.getInt(KEY_TASBIH_TARGET, 33)
    fun saveTasbihTarget(target: Int) = prefs.edit().putInt(KEY_TASBIH_TARGET, target).apply()

    fun getTasbihTotal(): Int = prefs.getInt(KEY_TASBIH_TOTAL, 0)
    fun incrementTasbihTotal() {
        val current = getTasbihTotal()
        prefs.edit().putInt(KEY_TASBIH_TOTAL, current + 1).apply()
    }

    fun getTasbihLaps(): Int = prefs.getInt(KEY_TASBIH_LAPS, 0)
    fun saveTasbihLaps(laps: Int) = prefs.edit().putInt(KEY_TASBIH_LAPS, laps).apply()

    fun getTasbihPreset(): String = prefs.getString(KEY_TASBIH_PRESET, "ሱብሓነላህ") ?: "ሱብሓነላህ"
    fun saveTasbihPreset(text: String) = prefs.edit().putString(KEY_TASBIH_PRESET, text).apply()

    fun getTasbihVibrationEnabled(): Boolean = prefs.getBoolean(KEY_TASBIH_VIBRATION, true)
    fun saveTasbihVibrationEnabled(enabled: Boolean) = prefs.edit().putBoolean(KEY_TASBIH_VIBRATION, enabled).apply()

    // ================= Per-Item Repeat Counts & Daily Session Policy ================= //

    fun getTodayDate(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
        return sdf.format(java.util.Date())
    }

    /**
     * Clear daily counts only when the local calendar date changes.
     * Preserves favorites, settings, theme, Tasbih total, and cumulative stats.
     */
    fun checkAndApplyDailyReset() {
        val today = getTodayDate()
        val lastDate = prefs.getString(KEY_DAILY_DATE, null)
        if (lastDate == null) {
            prefs.edit().putString(KEY_DAILY_DATE, today).apply()
        } else if (lastDate != today) {
            // New day! Reset today's active progress
            val editor = prefs.edit()
            val allKeys = prefs.all.keys
            for (key in allKeys) {
                if (key.startsWith(KEY_COMPLETED_AZKAR_PREFIX)) {
                    editor.remove(key)
                }
            }
            editor.putString(KEY_DAILY_DATE, today)
            editor.apply()
        }
    }

    fun getItemCompletedCount(uniqueKey: String): Int {
        return prefs.getInt(KEY_COMPLETED_AZKAR_PREFIX + uniqueKey, 0)
    }

    fun getItemCompletedCount(categoryId: String, itemId: String): Int {
        val compositeKey = "${categoryId}_$itemId"
        if (prefs.contains(KEY_COMPLETED_AZKAR_PREFIX + compositeKey)) {
            return prefs.getInt(KEY_COMPLETED_AZKAR_PREFIX + compositeKey, 0)
        }
        return prefs.getInt(KEY_COMPLETED_AZKAR_PREFIX + itemId, 0)
    }

    fun incrementItemCount(uniqueKey: String, maxRepeat: Int): Int {
        val current = getItemCompletedCount(uniqueKey)
        val next = if (current < maxRepeat) current + 1 else maxRepeat
        prefs.edit().putInt(KEY_COMPLETED_AZKAR_PREFIX + uniqueKey, next).apply()
        return next
    }

    fun incrementItemCount(categoryId: String, itemId: String, maxRepeat: Int): Int {
        val current = getItemCompletedCount(categoryId, itemId)
        val next = if (current < maxRepeat) current + 1 else maxRepeat
        val compositeKey = "${categoryId}_$itemId"
        prefs.edit()
            .putInt(KEY_COMPLETED_AZKAR_PREFIX + compositeKey, next)
            .putInt(KEY_COMPLETED_AZKAR_PREFIX + itemId, next)
            .apply()
        return next
    }

    fun resetItemCount(uniqueKey: String) {
        prefs.edit().putInt(KEY_COMPLETED_AZKAR_PREFIX + uniqueKey, 0).apply()
    }

    fun resetItemCount(categoryId: String, itemId: String) {
        val compositeKey = "${categoryId}_$itemId"
        prefs.edit()
            .putInt(KEY_COMPLETED_AZKAR_PREFIX + compositeKey, 0)
            .putInt(KEY_COMPLETED_AZKAR_PREFIX + itemId, 0)
            .apply()
    }

    fun resetCategoryProgress(categoryId: String, itemIds: List<String>) {
        val editor = prefs.edit()
        for (id in itemIds) {
            editor.remove(KEY_COMPLETED_AZKAR_PREFIX + "${categoryId}_$id")
            editor.remove(KEY_COMPLETED_AZKAR_PREFIX + id)
        }
        editor.putInt(KEY_RESUME_INDEX_PREFIX + categoryId, 0)
        editor.apply()
    }

    fun getResumeIndex(categoryId: String): Int {
        return prefs.getInt(KEY_RESUME_INDEX_PREFIX + categoryId, 0)
    }

    fun saveResumeIndex(categoryId: String, index: Int) {
        prefs.edit().putInt(KEY_RESUME_INDEX_PREFIX + categoryId, index).apply()
    }

    fun getAllItemCompletedCounts(): Map<String, Int> {
        val result = mutableMapOf<String, Int>()
        val all = prefs.all
        for ((key, value) in all) {
            if (key.startsWith(KEY_COMPLETED_AZKAR_PREFIX) && value is Int) {
                val itemKey = key.removePrefix(KEY_COMPLETED_AZKAR_PREFIX)
                result[itemKey] = value
            }
        }
        return result
    }

    // ================= City Selection ================= //

    fun getSavedCityId(): String {
        return prefs.getString("pref_selected_city_id", "addis_ababa") ?: "addis_ababa"
    }

    fun saveCityId(cityId: String) {
        prefs.edit().putString("pref_selected_city_id", cityId).apply()
    }
}
