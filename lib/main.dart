import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'providers/azkar_provider.dart';
import 'theme/app_theme.dart';
import 'widgets/theme_dialog.dart';
import 'widgets/modern_tasbih_widget.dart';
import 'screens/home_screen.dart';
import 'screens/duas_screen.dart';
import 'screens/names_screen.dart';
import 'screens/favorites_screen.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(
    ChangeNotifierProvider(
      create: (_) => AzkarProvider(),
      child: const NurAzkarApp(),
    ),
  );
}

class NurAzkarApp extends StatelessWidget {
  const NurAzkarApp({super.key});

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<AzkarProvider>();
    final themeColors = IslamicThemeColors.fromMode(provider.currentTheme);

    return MaterialApp(
      title: 'Nur Azkar',
      debugShowCheckedModeBanner: false,
      theme: themeColors.toThemeData(),
      home: const MainNavigationScreen(),
    );
  }
}

class MainNavigationScreen extends StatefulWidget {
  const MainNavigationScreen({super.key});

  @override
  State<MainNavigationScreen> createState() => _MainNavigationScreenState();
}

class _MainNavigationScreenState extends State<MainNavigationScreen> {
  int _currentIndex = 0;

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<AzkarProvider>();
    final colors = IslamicThemeColors.fromMode(provider.currentTheme);

    final screens = [
      HomeScreen(onOpenTasbih: () => setState(() => _currentIndex = 2)),
      const DuasScreen(),
      const ModernTasbihWidget(),
      const NamesOfAllahScreen(),
      const FavoritesScreen(),
    ];

    if (provider.isLoading) {
      return Scaffold(
        body: Center(
          child: CircularProgressIndicator(color: colors.goldAccent),
        ),
      );
    }

    return Scaffold(
      appBar: AppBar(
        title: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text('Azkar & Dua • ኑሩል አዝካር', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
            Text(provider.currentTheme.titleAm, style: TextStyle(fontSize: 11, color: colors.goldAccent)),
          ],
        ),
        actions: [
          IconButton(
            icon: Icon(Icons.palette, color: colors.goldAccent),
            onPressed: () => showThemeDialog(context),
          ),
        ],
      ),
      body: screens[_currentIndex],
      bottomNavigationBar: NavigationBar(
        selectedIndex: _currentIndex,
        onDestinationSelected: (idx) => setState(() => _currentIndex = idx),
        destinations: const [
          NavigationDestination(icon: Icon(Icons.home), label: 'ዋና ገጽ'),
          NavigationDestination(icon: Icon(Icons.menu_book), label: 'ዱዓዎች'),
          NavigationDestination(icon: Icon(Icons.touch_app), label: 'ተስቢሕ'),
          NavigationDestination(icon: Icon(Icons.auto_awesome), label: '99 ስሞች'),
          NavigationDestination(icon: Icon(Icons.star), label: 'ተወዳጆች'),
        ],
      ),
    );
  }
}
