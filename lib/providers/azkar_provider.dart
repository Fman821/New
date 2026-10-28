import 'dart:convert';
import 'package:flutter/services.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:audioplayers/audioplayers.dart';
import '../models/azkar_models.dart';

class AzkarProvider extends ChangeNotifier {
  List<AzkarCategory> _categories = [];
  List<DuaItem> _duas = [];
  List<AllahName> _names = [];

  AppThemeMode _currentTheme = AppThemeMode.sacredLight;
  double _arabicFontScale = 1.0;
  Set<String> _favorites = {};

  int _tasbihCount = 0;
  int _tasbihTarget = 33;
  int _tasbihTotal = 0;
  int _tasbihLaps = 0;
  String _tasbihPreset = "ሱብሓነላህ";
  bool _vibrationEnabled = true;

  final Map<String, int> _itemCompletedCounts = {};
  final AudioPlayer _audioPlayer = AudioPlayer();
  String? _currentlyPlayingUrl;
  PlayerState _playerState = PlayerState.stopped;
  bool _isLoading = true;

  List<AzkarCategory> get categories => _categories;
  List<DuaItem> get duas => _duas;
  List<AllahName> get names => _names;
  AppThemeMode get currentTheme => _currentTheme;
  double get arabicFontScale => _arabicFontScale;
  Set<String> get favorites => _favorites;
  int get tasbihCount => _tasbihCount;
  int get tasbihTarget => _tasbihTarget;
  int get tasbihTotal => _tasbihTotal;
  int get tasbihLaps => _tasbihLaps;
  String get tasbihPreset => _tasbihPreset;
  bool get vibrationEnabled => _vibrationEnabled;
  Map<String, int> get itemCompletedCounts => _itemCompletedCounts;
  String? get currentlyPlayingUrl => _currentlyPlayingUrl;
  PlayerState get playerState => _playerState;
  bool get isLoading => _isLoading;

  AzkarProvider() {
    _initData();
    _initAudioListener();
  }

  void _initAudioListener() {
    _audioPlayer.onPlayerStateChanged.listen((state) {
      _playerState = state;
      notifyListeners();
    });
  }

  Future<void> _initData() async {
    final prefs = await SharedPreferences.getInstance();

    final savedThemeIndex = prefs.getInt('pref_theme_mode') ?? 0;
    _currentTheme = AppThemeMode.values[savedThemeIndex.clamp(0, AppThemeMode.values.length - 1)];
    _arabicFontScale = prefs.getDouble('pref_arabic_font_scale') ?? 1.0;
    _favorites = (prefs.getStringList('pref_favorites') ?? []).toSet();
    _tasbihCount = prefs.getInt('pref_tasbih_count') ?? 0;
    _tasbihTarget = prefs.getInt('pref_tasbih_target') ?? 33;
    _tasbihTotal = prefs.getInt('pref_tasbih_total') ?? 0;

    try {
      final azkarStr = await rootBundle.loadString('assets/azkar.json');
      final azkarJson = json.decode(azkarStr);
      final rawCats = azkarJson['categories'] as List<dynamic>? ?? [];
      _categories = rawCats.map((e) => AzkarCategory.fromJson(e)).toList();

      final duasStr = await rootBundle.loadString('assets/duas.json');
      final List<dynamic> duasJson = json.decode(duasStr);
      _duas = duasJson.map((e) => DuaItem.fromJson(e)).toList();

      final namesStr = await rootBundle.loadString('assets/names.json');
      final List<dynamic> namesJson = json.decode(namesStr);
      _names = namesJson.map((e) => AllahName.fromJson(e)).toList();
    } catch (e) {
      debugPrint('Error loading JSON assets: $e');
    }

    _isLoading = false;
    notifyListeners();
  }

  Future<void> setTheme(AppThemeMode mode) async {
    _currentTheme = mode;
    notifyListeners();
    final prefs = await SharedPreferences.getInstance();
    await prefs.setInt('pref_theme_mode', mode.index);
  }

  Future<void> setArabicFontScale(double scale) async {
    _arabicFontScale = scale;
    notifyListeners();
    final prefs = await SharedPreferences.getInstance();
    await prefs.setDouble('pref_arabic_font_scale', scale);
  }

  Future<void> toggleFavorite(String id) async {
    if (_favorites.contains(id)) {
      _favorites.remove(id);
    } else {
      _favorites.add(id);
    }
    notifyListeners();
    final prefs = await SharedPreferences.getInstance();
    await prefs.setStringList('pref_favorites', _favorites.toList());
  }

  void incrementItemCount(String id, int maxRepeat) {
    int current = _itemCompletedCounts[id] ?? 0;
    _itemCompletedCounts[id] = current < maxRepeat ? current + 1 : 0;
    notifyListeners();
  }

  void resetItemCount(String id) {
    _itemCompletedCounts[id] = 0;
    notifyListeners();
  }

  void incrementTasbih() {
    if (_vibrationEnabled) {
      HapticFeedback.lightImpact();
    }
    _tasbihTotal++;
    int next = _tasbihCount + 1;
    if (next >= _tasbihTarget) {
      _tasbihCount = 0;
      _tasbihLaps++;
      if (_vibrationEnabled) {
        HapticFeedback.mediumImpact();
      }
    } else {
      _tasbihCount = next;
    }
    notifyListeners();
    _saveTasbih();
  }

  void resetTasbih() {
    _tasbihCount = 0;
    notifyListeners();
    _saveTasbih();
  }

  void setTasbihTarget(int target) {
    _tasbihTarget = target;
    _tasbihCount = 0;
    notifyListeners();
    _saveTasbih();
  }

  void setTasbihPreset(String preset) {
    _tasbihPreset = preset;
    notifyListeners();
  }

  void toggleVibration() {
    _vibrationEnabled = !_vibrationEnabled;
    notifyListeners();
  }

  Future<void> _saveTasbih() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setInt('pref_tasbih_count', _tasbihCount);
    await prefs.setInt('pref_tasbih_target', _tasbihTarget);
    await prefs.setInt('pref_tasbih_total', _tasbihTotal);
  }

  Future<void> toggleAudio(String url) async {
    if (url.isEmpty) return;
    if (_currentlyPlayingUrl == url && _playerState == PlayerState.playing) {
      await _audioPlayer.pause();
    } else if (_currentlyPlayingUrl == url && _playerState == PlayerState.paused) {
      await _audioPlayer.resume();
    } else {
      _currentlyPlayingUrl = url;
      await _audioPlayer.stop();
      await _audioPlayer.play(UrlSource(url));
    }
    notifyListeners();
  }

  @override
  void dispose() {
    _audioPlayer.dispose();
    super.dispose();
  }
}
