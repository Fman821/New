package com.example.data.model

/**
 * Represents an individual Zikr / Azkar entry.
 *
 * @param id Unique identifier
 * @param title Short title in Amharic (e.g. "አያተል ኩርሲ")
 * @param ar Arabic text with full tashkeel
 * @param am Amharic translation
 * @param repeat Recommended repetition count (1, 3, 33, 100...)
 * @param source Reference source (e.g. "ቁርኣን", "ቡኻሪ", "ሙስሊም")
 * @param audio URL of audio recitation if available
 * @param translit Amharic transliteration (ድምጸ ንባብ)
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
    val repeat: Int = 1,
    val source: String = "",
    val audio: String = ""
)

/**
 * Represents one of the 99 Beautiful Names of Allah (Asmaul Husna).
 *
 * @param n Number (1 to 99)
 * @param ar Arabic name
 * @param tr Transliteration
 * @param am Amharic meaning
 */
data class AllahName(
    val n: Int,
    val ar: String,
    val tr: String,
    val am: String
)

/**
 * 5 Visual Theme Concepts requested by user:
 * 1. SACRED_LIGHT: Soft cream & white background (#F9F6F0) with delicate gold borders & dark green Arabic
 * 2. MIDNIGHT_OTTOMAN: Deep midnight navy (#0A1128) with copper and rose gold accents
 * 3. GLASSMORPHISM_3D: Translucent frosted glass cards, emerald & gold glow
 * 4. MASJID_ARCHITECTURE: Arched mihrab shapes, serene green and white with silver accents
 * 5. DARK_EMERALD_NEON: Deep emerald (#061D15) with glowing neon gold & green accents
 */
enum class AppThemeMode(val titleAm: String, val descAm: String) {
    SACRED_LIGHT("የቀን ብርሃን", "ነጭና ወርቃማ ለስላሳ የቀን እይታ"),
    MIDNIGHT_OTTOMAN("የኦቶማን ሰማያዊ", "ጥልቅ ሰማያዊ ከመዳብና ሮዝ ወርቅ ጋር"),
    GLASSMORPHISM_3D("ዘመናዊ ግላስ", "የብርጭቆ ዲዛይን በኤመራልድና ወርቅ ፍንጣቂ"),
    MASJID_ARCHITECTURE("የመስጊድ ቅስት", "የመስጊዶች ቅስት አርክቴክቸርና የተረጋጋ አረንጓዴ"),
    DARK_EMERALD_NEON("ኤመራልድ ኒዮን", "ጥቁር ኤመራልድ ከሚያበራ ኒዮን ወርቅ ጋር")
}
