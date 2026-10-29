# Amharic Azkar (አማርኛ አዝካር)
> **Daily Remembrance, Peaceful Life. • «ዕለታዊ ዝክር፣ ሰላማዊ ሕይወት»**

A modern, beautifully crafted Android application built with **Kotlin** and **Jetpack Compose**, focusing primarily on authentic **Morning Azkar (የጥዋት አዝካር)** and **Evening Azkar (የማታ አዝካር)** with clear Amharic translations, repetition counters, daily progress tracking, Quranic & Prophetic Duas, the 99 Names of Allah, and an interactive Tasbih counter.

---

## ✨ Features

- **5 Distinct Visual Themes**:
  1. **Sacred Light (የቀን ብርሃን)**: Soft cream background (`#F9F6F0`), crisp white cards with delicate gold borders, and deep green Arabic calligraphy.
  2. **Midnight Ottoman (የኦቶማን ሰማያዊ)**: Royal midnight navy (`#0A1128`) with luxury copper and rose gold accents.
  3. **Glassmorphism & 3D (ዘመናዊ ግላስ)**: Translucent frosted glass cards with radiant gold outlines and emerald glow.
  4. **Masjid Architecture (የመስጊድ ቅስት)**: Traditional mosque architecture aesthetics featuring *mihrab* arch motifs and serene forest green.
  5. **Dark Emerald & Neon (ኤመራልድ ኒዮን)**: Rich dark emerald with glowing neon gold badges.
- **Categorized Azkar**:
  - Morning Adhkar (የጠዋት አዝካር - 24 items)
  - Noon / Post-Prayer Adhkar (የምሳ አዝካር - 6 items)
  - Evening Adhkar (የማታ አዝካር - 22 items)
  - Home Adhkar (የቤት አዝካር - 13 items)
  - Travel Adhkar (የጉዞ አዝካር - 6 items)
  - Other Daily Adhkar & Reminders (ሌሎች አዝካሮች - 12 items)
- **Interactive Azkar Reader**:
  - Complete Arabic text with full vowel marks (tashkeel).
  - Accurate Amharic translation and Ge'ez phonetic transliteration.
  - Per-item repetition tracking with audio recitation playback support.
  - One-tap text sharing and bookmarking (Favorites).
- **Modern Digital Tasbih**:
  - Animated circular progress ring with custom targets (33, 99, 100, 1000).
  - Tactile haptic feedback and tap animation.
  - Quick Dhikr presets (*Subhanallah*, *Alhamdulillah*, *Allahu Akbar*, etc.).
  - Lap and total count persistence.
- **99 Names of Allah (Asmaul Husna)**:
  - Complete 99 Names with search by number, Arabic, transliteration, and Amharic meaning.
  - Detailed modal view for focused recitation.
- **Adjustable Arabic Typography**:
  - Dynamic font scale adjustment (Normal, Medium, Large, Extra Large).

---

## 🏗️ Architecture & Tech Stack

The project strictly follows the **MVVM (Model-View-ViewModel)** architectural pattern:

```
app/src/main/
├── AndroidManifest.xml
├── assets/
│   ├── azkar.json               # Structured Azkar categories & items
│   ├── duas.json                # Essential Quranic & Prophetic Duas
│   └── names.json               # 99 Names of Allah & translations
├── java/com/example/
│   ├── MainActivity.kt          # Main Activity & Root Navigation Scaffold
│   ├── data/
│   │   ├── model/
│   │   │   └── Models.kt        # Data classes (AzkarItem, Category, Dua, Theme)
│   │   └── repository/
│   │       ├── AzkarRepository.kt   # Asset loading & SharedPreferences persistence
│   │       └── AudioPlayerManager.kt# MediaPlayer wrapper for audio streaming
│   ├── ui/
│   │   ├── components/
│   │   │   ├── AzkarCard.kt         # Card with repeats, audio, share & star
│   │   │   ├── ModernTasbihView.kt  # Interactive circular Tasbih counter
│   │   │   ├── IslamicDecorative.kt # Islamic arch shapes & geometric dividers
│   │   │   └── ThemeSelectorDialog.kt # 5-Theme switcher & Font size dialogs
│   │   ├── screens/
│   │   │   ├── HomeScreen.kt        # Greeting, Quick Tasbih & Category grid
│   │   │   ├── AzkarListScreen.kt   # Category list with search & progress bar
│   │   │   ├── DuasScreen.kt        # Duas browser with search
│   │   │   ├── NamesOfAllahScreen.kt# 99 Names grid & detail dialog
│   │   │   └── FavoritesScreen.kt   # Starred Azkar & Duas
│   │   ├── theme/
│   │   │   ├── Color.kt             # Color palettes for all 5 themes
│   │   │   ├── Theme.kt             # CompositionLocal & M3 ColorSchemes
│   │   │   └── Type.kt              # Typography tuned for Arabic & Ge'ez
│   │   └── viewmodel/
│   │       └── MainViewModel.kt     # StateFlow & Coroutines business logic
└── res/
    ├── drawable/                    # Custom adaptive vector icon layers
    ├── mipmap-*/                    # App launcher icons across densities
    └── values/                      # strings.xml, colors.xml, themes.xml
```

- **UI Framework**: Jetpack Compose (Material 3)
- **Language**: Kotlin 2.0+
- **Concurrency**: Kotlin Coroutines & `StateFlow`
- **Audio Playback**: Android `MediaPlayer` streaming via HTTPS
- **Target SDK**: Android 16 / Android 14+ (minSdk 24)

---

## 🚀 How to Export and Push to GitHub

### Option A: Direct Push from AI Studio
1. Open the project in Google AI Studio.
2. In the top-right settings/export menu, click **Push to GitHub** or **Export as ZIP**.
3. Authenticate with your GitHub account and select your destination repository.

### Option B: Manual Git Push (Using Downloaded ZIP)
1. Download the ZIP file from the AI Studio editor.
2. Unpack the ZIP to your desired workspace directory.
3. Open a terminal in the project root:
   ```bash
   cd path/to/unzipped-folder

   # Initialize git repository
   git init

   # Set default branch to main
   git branch -M main

   # Stage all files (.gitignore will exclude build caches and keystores)
   git add .

   # Create initial commit
   git commit -m "Initial commit: Nur Islamic Azkar, Dua & Tasbih Android App"

   # Link to your GitHub repository
   git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPOSITORY_NAME.git

   # Push to GitHub
   git push -u origin main
   ```

---

## 🛠️ Building & Running in Android Studio

1. **System Requirements**:
   - **Android Studio**: Android Studio Jellyfish (2023.3.1), Koala (2024.1.1), or Ladybug (2024.2.1)+
   - **JDK**: Java 17 or Java 21 (configured in Android Studio under *Settings > Build, Execution, Deployment > Build Tools > Gradle > Gradle JDK*)
2. **Open the Project**:
   - In Android Studio, select **File > Open...** and choose the repository folder.
   - Allow Gradle to sync dependencies automatically.
3. **Run the App**:
   - Select a physical Android device or an Android Virtual Device (AVD running Android 7.0 / API 24 or newer).
   - Click the green **Run (▶)** button or press `Shift + F10`.
4. **Run Unit Tests**:
   - Run the local JVM test suite from the terminal:
     ```bash
     ./gradlew testDebugUnitTest
     ```

---

## 📜 Permissions

Declared in `AndroidManifest.xml`:
- `android.permission.INTERNET`: Required to stream audio recitations of Surahs & Azkar.
- `android.permission.ACCESS_NETWORK_STATE`: Checks connectivity prior to streaming.
- `android.permission.VIBRATE`: Provides tactile haptic feedback for the Tasbih counter.
