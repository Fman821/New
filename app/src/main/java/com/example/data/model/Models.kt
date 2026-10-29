package com.example.data.model

/**
 * Represents an individual Zikr / Azkar entry.
 */
data class AzkarItem(
    val id: String,
    val title: String,
    val ar: String,
    val am: String,
    val repeat: Int = 1,
    val source: String = "",
    val audio: String = "",
    val translit: String = ""
)

/**
 * Represents an Azkar Category (e.g. Morning, Evening, House, Travel).
 */
data class AzkarCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: String,
    val image: String = "",
    val items: List<AzkarItem> = emptyList()
)

/**
 * Represents an Islamic Dua entry.
 */
data class DuaItem(
    val id: String,
    val title: String,
    val ar: String,
    val am: String,
    val category: String = "general",
    val repeat: Int = 1,
    val source: String = "",
    val audio: String = "",
    val translit: String = ""
)

/**
 * Represents one of the 99 Beautiful Names of Allah (Asmaul Husna).
 */
data class AllahName(
    val n: Int,
    val ar: String,
    val tr: String,
    val am: String,
    val explanation: String = ""
)

/**
 * Represents a Surah in the Holy Quran.
 */
data class QuranSurah(
    val number: Int,
    val nameArabic: String,
    val nameTranslit: String,
    val nameAmharic: String,
    val englishMeaning: String,
    val versesCount: Int,
    val revelationType: String, // "Mecca" or "Medina"
    val prominentVerses: List<QuranVerse> = emptyList()
)

/**
 * Individual Quran verse with verified Arabic, transliteration, and Amharic translation.
 */
data class QuranVerse(
    val numberInSurah: Int,
    val arabic: String,
    val transliteration: String,
    val amharicTranslation: String
)

/**
 * Supported prayer times calculation result.
 */
data class PrayerSchedule(
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String,
    val nextPrayerName: String,
    val nextPrayerTime: String,
    val minutesUntilNext: Long,
    val cityName: String,
    val calculationMethod: String
)

/**
 * City selection for prayer times and Qibla direction.
 */
data class IslamicCity(
    val id: String,
    val nameEn: String,
    val nameAm: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val timezoneHours: Double
)

/**
 * Qibla calculation information.
 */
data class QiblaInfo(
    val bearingDegrees: Float,
    val distanceKm: Double,
    val cityName: String
)

/**
 * 5 Visual Theme Concepts:
 * 1. SACRED_LIGHT: Warm cream & pure white (#FAF7F0, #FFFFFF) with champagne gold and emerald text
 * 2. DARK_EMERALD_NEON: Deep emerald & forest green (#06251D, #0B382B) with soft gold highlights (Sacred Night)
 * 3. MIDNIGHT_OTTOMAN: Deep midnight navy (#0A1128) with copper and rose gold accents
 * 4. GLASSMORPHISM_3D: Translucent frosted glass cards with emerald & gold glow
 * 5. MASJID_ARCHITECTURE: Arched mihrab shapes, serene green and white with silver accents
 */
enum class AppThemeMode(val titleAm: String, val descAm: String) {
    SACRED_LIGHT("የቀን ብርሃን (Sacred Light)", "ነጭና ወርቃማ ለስላሳ የቀን እይታ"),
    DARK_EMERALD_NEON("የሌሊት ጽሞና (Sacred Night)", "ጥልቅ ኤመራልድ ከሚያበራ ወርቅ ጋር"),
    MIDNIGHT_OTTOMAN("የኦቶማን ሰማያዊ (Ottoman)", "ጥልቅ ሰማያዊ ከመዳብና ሮዝ ወርቅ ጋር"),
    GLASSMORPHISM_3D("ዘመናዊ ግላስ (Glass)", "የብርጭቆ ዲዛይን በኤመራልድና ወርቅ ፍንጣቂ"),
    MASJID_ARCHITECTURE("የመስጊድ ቅስት (Masjid)", "የመስጊዶች ቅስት አርክቴክቸርና የተረጋጋ አረንጓዴ")
}
