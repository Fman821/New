# Changelog

## 2026-10-29 — Visual Polish & Light Theme Architecture
- Reworked the Flutter Home Screen with a clean, vibrant Light Theme (crisp off-white background with soft emerald `#0C3B2E` and gold `#D4AF37` accents).
- Strictly single navigation bar architecture: nested navigation bar disabled inside child views to completely prevent duplicated or overlapping bottom navigation bars.
- 2x2 Grid View for the 4 primary categories (Morning Azkar, Evening Azkar, 99 Names of Allah, and Locked Coming Soon card) with graceful asset and motif rendering.
- Added offline-ready fallbacks and Islamic geometric art styling to eliminate loading flashes.
- GitHub Actions CI workflow updated with `android.newDsl=false` and build config compatibility to guarantee clean automated APK builds.
- Preserved all Azkar, Dua, 99 Names, Tasbih, and Favorites features.
