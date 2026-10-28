import 'package:flutter/material.dart';
import '../models/azkar_models.dart';

class IslamicThemeColors {
  final Color bg;
  final Color surface;
  final Color goldAccent;
  final Color arabicText;
  final Color border;
  final Color text;
  final Color subtext;
  final bool isGlass;

  const IslamicThemeColors({
    required this.bg,
    required this.surface,
    required this.goldAccent,
    required this.arabicText,
    required this.border,
    required this.text,
    required this.subtext,
    this.isGlass = false,
  });

  static const sacredLight = IslamicThemeColors(
    bg: Color(0xFFF9F6F0),
    surface: Color(0xFFFFFFFF),
    goldAccent: Color(0xFFC59B27),
    arabicText: Color(0xFF0E5A3E),
    border: Color(0xFFE8D399),
    text: Color(0xFF1E2823),
    subtext: Color(0xFF5A6961),
  );

  static const midnightOttoman = IslamicThemeColors(
    bg: Color(0xFF0A1128),
    surface: Color(0xFF111D3D),
    goldAccent: Color(0xFFF1D595),
    arabicText: Color(0xFFE0A98B),
    border: Color(0xFF8B5A3E),
    text: Color(0xFFE9EEF7),
    subtext: Color(0xFF98A6C2),
  );

  static const glassmorphism = IslamicThemeColors(
    bg: Color(0xFF06231C),
    surface: Color(0xCC133B31),
    goldAccent: Color(0xFFFFD700),
    arabicText: Color(0xFFFFD700),
    border: Color(0x66FFD700),
    text: Color(0xFFF3FFF9),
    subtext: Color(0xFFA5CFC3),
    isGlass: true,
  );

  static const masjidArch = IslamicThemeColors(
    bg: Color(0xFFF5F7F6),
    surface: Color(0xFFFFFFFF),
    goldAccent: Color(0xFFC19E53),
    arabicText: Color(0xFF0E4F3B),
    border: Color(0xFFD4DDD8),
    text: Color(0xFF1B2B24),
    subtext: Color(0xFF566960),
  );

  static const darkEmerald = IslamicThemeColors(
    bg: Color(0xFF051B13),
    surface: Color(0xFF0A2B20),
    goldAccent: Color(0xFFFFDE59),
    arabicText: Color(0xFFFFDE59),
    border: Color(0x6600E676),
    text: Color(0xFFEBFFF6),
    subtext: Color(0xFF8FD8B8),
  );

  static IslamicThemeColors fromMode(AppThemeMode mode) {
    switch (mode) {
      case AppThemeMode.sacredLight:
        return sacredLight;
      case AppThemeMode.midnightOttoman:
        return midnightOttoman;
      case AppThemeMode.glassmorphism3D:
        return glassmorphism;
      case AppThemeMode.masjidArchitecture:
        return masjidArch;
      case AppThemeMode.darkEmeraldNeon:
        return darkEmerald;
    }
  }

  ThemeData toThemeData() {
    final isDark = bg.computeLuminance() < 0.5;
    return ThemeData(
      useMaterial3: true,
      brightness: isDark ? Brightness.dark : Brightness.light,
      scaffoldBackgroundColor: bg,
      cardColor: surface,
      colorScheme: ColorScheme(
        brightness: isDark ? Brightness.dark : Brightness.light,
        primary: goldAccent,
        onPrimary: Colors.black,
        secondary: arabicText,
        onSecondary: Colors.white,
        error: Colors.redAccent,
        onError: Colors.white,
        surface: surface,
        onSurface: text,
      ),
      appBarTheme: AppBarTheme(
        backgroundColor: bg,
        foregroundColor: text,
        elevation: 0,
      ),
    );
  }
}
