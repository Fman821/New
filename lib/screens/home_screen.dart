import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../models/azkar_models.dart';
import '../providers/azkar_provider.dart';
import '../theme/app_theme.dart';
import '../widgets/theme_dialog.dart';
import 'azkar_list_screen.dart';
import 'duas_screen.dart';
import 'names_screen.dart';

/// HomePage: Modern, vibrant, and clean Light Theme Islamic Azkar & Dua home screen
/// Features:
/// 1. Light & Bright Islamic Theme: Crisp off-white background with elegant soft emerald and gold accents.
/// 2. Single Navigation Bar: Nested bottom navigation is disabled by default when hosted in MainNavigationScreen.
/// 3. Header: Islamic greeting in rich dark emerald green with a gold badge and settings button.
/// 4. Ayah Card: Pristine white card with gold border and deep emerald Arabic calligraphy («فَاذْكُرُونِي أَذْكُرْكُمْ»).
/// 5. Strict 2x2 Grid: ONLY 4 main cards (Morning Azkar, Evening Azkar, 99 Names, Locked Coming Soon).
class HomePage extends StatefulWidget {
  final VoidCallback? onOpenTasbih;
  final bool showBottomNav;

  const HomePage({
    super.key,
    this.onOpenTasbih,
    this.showBottomNav = false,
  });

  @override
  State<HomePage> createState() => _HomePageState();
}

/// Backwards compatibility alias for existing project navigators
class HomeScreen extends HomePage {
  const HomeScreen({
    super.key,
    super.onOpenTasbih,
    super.showBottomNav = false,
  });
}

class _HomePageState extends State<HomePage> {
  int _navIndex = 0;

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<AzkarProvider>();
    final colors = IslamicThemeColors.fromMode(provider.currentTheme);

    return Scaffold(
      backgroundColor: const Color(0xFFF7F9F7),
      // Only show inner bottom nav if explicitly requested (prevents duplicate navigation bar)
      bottomNavigationBar: widget.showBottomNav ? _buildBottomNav(context, colors) : null,
      body: Stack(
        children: [
          // -------------------------------------------------------------
          // 1. LIGHT & BRIGHT BACKGROUND WITH SUBTLE ISLAMIC ART MOTIFS
          // -------------------------------------------------------------
          Positioned.fill(
            child: CustomPaint(
              painter: _IslamicLightAtmospherePainter(
                bgColorTop: const Color(0xFFFCFDFC),
                bgColorBottom: const Color(0xFFEEF5F0),
                accentGold: const Color(0xFFD4AF37).withValues(alpha: 0.12),
                accentGreen: const Color(0xFF0F5132).withValues(alpha: 0.06),
              ),
            ),
          ),

          // Safe asset loader with errorBuilder for optional custom background
          Positioned.fill(
            child: Image.asset(
              'assets/images/background_light.png',
              fit: BoxFit.cover,
              errorBuilder: (_, __, ___) => Image.network(
                'https://images.unsplash.com/photo-1542816417-0983c9c9ad53?auto=format&fit=crop&w=1080&q=80',
                fit: BoxFit.cover,
                color: Colors.white.withValues(alpha: 0.92),
                colorBlendMode: BlendMode.screen,
                errorBuilder: (_, __, ___) => const SizedBox.shrink(),
              ),
            ),
          ),

          // Top subtle ambient glow to ensure perfect readability
          Positioned(
            top: 0,
            left: 0,
            right: 0,
            height: 120,
            child: DecoratedBox(
              decoration: BoxDecoration(
                gradient: LinearGradient(
                  begin: Alignment.topCenter,
                  end: Alignment.bottomCenter,
                  colors: [
                    Colors.white.withValues(alpha: 0.8),
                    Colors.transparent,
                  ],
                ),
              ),
            ),
          ),

          // -------------------------------------------------------------
          // 2. MAIN SCROLLABLE CONTENT (LIGHT THEME)
          // -------------------------------------------------------------
          SafeArea(
            child: SingleChildScrollView(
              physics: const BouncingScrollPhysics(),
              padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 12.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  // --- TOP HEADER (GREETING & SETTINGS BUTTON) ---
                  _buildHeader(context, colors),
                  const SizedBox(height: 16),

                  // --- AYAH CARD («فَاذْكُرُونِي أَذْكُرْكُمْ») ---
                  _buildAyahCard(context, colors),
                  const SizedBox(height: 20),

                  // --- SECTION HEADER ---
                  Row(
                    children: [
                      Container(
                        width: 4,
                        height: 18,
                        decoration: BoxDecoration(
                          color: const Color(0xFFC59B27),
                          borderRadius: BorderRadius.circular(2),
                        ),
                      ),
                      const SizedBox(width: 8),
                      const Text(
                        'ዋና ዋና ክፍሎች',
                        style: TextStyle(
                          color: Color(0xFF0C3B2E),
                          fontSize: 16,
                          fontWeight: FontWeight.bold,
                          letterSpacing: 0.2,
                        ),
                      ),
                      const Spacer(),
                      Text(
                        'ዕለታዊ ዚክሮች',
                        style: TextStyle(
                          color: const Color(0xFF8C6D1F),
                          fontSize: 12,
                          fontWeight: FontWeight.w600,
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 12),

                  // --- STRICTLY 4 CARDS (2x2 GRID IN LIGHT THEME) ---
                  _build2x2CardsGrid(context, provider),
                  const SizedBox(height: 16),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }

  // ===========================================================================
  // TOP HEADER: GREETING & SETTINGS (LIGHT THEME)
  // ===========================================================================
  Widget _buildHeader(BuildContext context, IslamicThemeColors colors) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        // Islamic Greeting Column
        Expanded(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // Badge
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 9, vertical: 4),
                decoration: BoxDecoration(
                  color: const Color(0xFFE8F5E9),
                  borderRadius: BorderRadius.circular(12),
                  border: Border.all(
                    color: const Color(0xFFA5D6A7),
                    width: 0.9,
                  ),
                ),
                child: Row(
                  mainAxisSize: MainAxisSize.min,
                  children: const [
                    Icon(
                      Icons.brightness_5_rounded,
                      size: 12,
                      color: Color(0xFFC59B27),
                    ),
                    SizedBox(width: 5),
                    Text(
                      'ኑሩል አዝካር • Nur Azkar',
                      style: TextStyle(
                        color: Color(0xFF0F5132),
                        fontSize: 11,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 6),
              const Text(
                'አሰላሙ ዐለይኩም',
                style: TextStyle(
                  color: Color(0xFF0C3B2E),
                  fontSize: 22,
                  fontWeight: FontWeight.bold,
                  letterSpacing: 0.2,
                ),
              ),
              const Text(
                'ወረሕመቱላሂ ወበረካቱህ',
                style: TextStyle(
                  color: Color(0xFF386650),
                  fontSize: 13,
                  fontWeight: FontWeight.w500,
                ),
              ),
            ],
          ),
        ),

        // Settings / Palette Action Button
        Material(
          color: Colors.transparent,
          child: InkWell(
            onTap: () => showThemeDialog(context),
            borderRadius: BorderRadius.circular(16),
            child: Container(
              padding: const EdgeInsets.all(10),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(16),
                border: Border.all(
                  color: const Color(0xFFD4AF37).withValues(alpha: 0.5),
                  width: 1.2,
                ),
                boxShadow: [
                  BoxShadow(
                    color: const Color(0xFF0C3B2E).withValues(alpha: 0.08),
                    blurRadius: 10,
                    offset: const Offset(0, 3),
                  ),
                ],
              ),
              child: const Icon(
                Icons.settings_suggest_rounded,
                color: Color(0xFFC59B27),
                size: 22,
              ),
            ),
          ),
        ),
      ],
    );
  }

  // ===========================================================================
  // AYAH CARD: «فَاذْكُرُونِي أَذْكُرْكُمْ» (LIGHT & BRIGHT)
  // ===========================================================================
  Widget _buildAyahCard(BuildContext context, IslamicThemeColors colors) {
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(20),
        border: Border.all(
          color: const Color(0xFFD4AF37),
          width: 1.4,
        ),
        boxShadow: [
          BoxShadow(
            color: const Color(0xFFD4AF37).withValues(alpha: 0.16),
            blurRadius: 14,
            spreadRadius: 1,
            offset: const Offset(0, 4),
          ),
          BoxShadow(
            color: const Color(0xFF0C3B2E).withValues(alpha: 0.06),
            blurRadius: 10,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: ClipRRect(
        borderRadius: BorderRadius.circular(19),
        child: Stack(
          children: [
            // Decorative background mosque watermark in soft gold
            Positioned(
              right: -10,
              bottom: -15,
              child: Icon(
                Icons.mosque_rounded,
                size: 110,
                color: const Color(0xFFD4AF37).withValues(alpha: 0.08),
              ),
            ),

            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 18, vertical: 16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  // Top Row: Tag & Reference
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                        decoration: BoxDecoration(
                          color: const Color(0xFFFFF9E6),
                          borderRadius: BorderRadius.circular(8),
                          border: Border.all(
                            color: const Color(0xFFD4AF37).withValues(alpha: 0.5),
                            width: 0.9,
                          ),
                        ),
                        child: const Text(
                          '📖 የዕለቱ የቁርኣን አንቀጽ',
                          style: TextStyle(
                            color: Color(0xFF8C6D1F),
                            fontSize: 11,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                      const Text(
                        'ሱረቱል በቀራህ ፡ 152',
                        style: TextStyle(
                          color: Color(0xFF4A6B5D),
                          fontSize: 11,
                          fontWeight: FontWeight.w600,
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 14),

                  // Arabic Ayah
                  const Text(
                    '« فَاذْكُرُونِي أَذْكُرْكُمْ وَاشْكُرُوا لِي وَلَا تَكْفُرُونِ »',
                    textAlign: TextAlign.center,
                    textDirection: TextDirection.rtl,
                    style: TextStyle(
                      fontFamily: 'Amiri',
                      fontSize: 21,
                      fontWeight: FontWeight.bold,
                      color: Color(0xFF0A3E2F),
                      height: 1.7,
                      letterSpacing: 0.5,
                    ),
                  ),
                  const SizedBox(height: 12),

                  // Star Divider
                  Row(
                    children: [
                      Expanded(
                        child: Divider(
                          color: const Color(0xFFD4AF37).withValues(alpha: 0.45),
                          thickness: 1,
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 8),
                        child: Icon(
                          Icons.star_rounded,
                          size: 15,
                          color: const Color(0xFFC59B27),
                        ),
                      ),
                      Expanded(
                        child: Divider(
                          color: const Color(0xFFD4AF37).withValues(alpha: 0.45),
                          thickness: 1,
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 10),

                  // Amharic Translation
                  const Text(
                    '«አስታውሱኝ፤ አስታውሳችኋለሁና። አመስግኑኝም፤ አትካዱኝም።»',
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      color: Color(0xFF1E3A2F),
                      fontSize: 13.5,
                      fontWeight: FontWeight.w600,
                      height: 1.5,
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  // ===========================================================================
  // 4 MAIN CARDS (STRICTLY 2x2 GRID - LIGHT THEME)
  // ===========================================================================
  Widget _build2x2CardsGrid(BuildContext context, AzkarProvider provider) {
    // Dynamic matching of morning and evening categories with robust fallbacks
    final morningCat = provider.categories.firstWhere(
      (c) => c.id.toLowerCase().contains('morning') || c.title.contains('ጠዋት') || c.title.contains('ጥዋት'),
      orElse: () => provider.categories.isNotEmpty ? provider.categories.first : _fallbackMorningCategory(),
    );

    final eveningCat = provider.categories.firstWhere(
      (c) => c.id.toLowerCase().contains('evening') || c.title.contains('ማታ') || c.title.contains('ምሽት'),
      orElse: () => provider.categories.length > 1 ? provider.categories[1] : _fallbackEveningCategory(),
    );

    final morningCount = morningCat.items.isNotEmpty ? morningCat.items.length : 25;
    final eveningCount = eveningCat.items.isNotEmpty ? eveningCat.items.length : 27;

    return GridView.count(
      crossAxisCount: 2,
      crossAxisSpacing: 14,
      mainAxisSpacing: 14,
      childAspectRatio: 0.88,
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      children: [
        // CARD 1: የጥዋት አዝካር (Morning Azkar)
        _buildImageCard(
          context: context,
          title: 'የጥዋት አዝካር',
          subtitle: 'የማለዳ ዚክሮች',
          badgeText: '$morningCount ዚክሮች',
          assetPath: 'assets/images/morning_bg.jpg',
          networkFallbackUrl: 'https://images.unsplash.com/photo-1542816417-0983c9c9ad53?auto=format&fit=crop&w=600&q=80',
          icon: Icons.wb_sunny_rounded,
          iconColor: const Color(0xFFE68A00),
          iconBgColor: const Color(0xFFFFF8E1),
          onTap: () {
            Navigator.push(
              context,
              MaterialPageRoute(
                builder: (_) => AzkarListScreen(category: morningCat),
              ),
            );
          },
        ),

        // CARD 2: የማታ አዝካር (Evening Azkar)
        _buildImageCard(
          context: context,
          title: 'የማታ አዝካር',
          subtitle: 'የምሽት ዚክሮች',
          badgeText: '$eveningCount ዚክሮች',
          assetPath: 'assets/images/evening_bg.jpg',
          networkFallbackUrl: 'https://images.unsplash.com/photo-1564769625905-50e93615e769?auto=format&fit=crop&w=600&q=80',
          icon: Icons.nightlight_round,
          iconColor: const Color(0xFF0277BD),
          iconBgColor: const Color(0xFFE1F5FE),
          onTap: () {
            Navigator.push(
              context,
              MaterialPageRoute(
                builder: (_) => AzkarListScreen(category: eveningCat),
              ),
            );
          },
        ),

        // CARD 3: 99 የአላህ ስሞች (99 Names of Allah with Kaaba photo)
        _buildImageCard(
          context: context,
          title: '99 የአላህ ስሞች',
          subtitle: 'አስማኡል ሁስና',
          badgeText: '99 ስሞች',
          assetPath: 'assets/images/kaaba_bg.jpg',
          networkFallbackUrl: 'https://images.unsplash.com/photo-1591604129939-f1efa4d9f7fa?auto=format&fit=crop&w=600&q=80',
          icon: Icons.auto_awesome_rounded,
          iconColor: const Color(0xFFC59B27),
          iconBgColor: const Color(0xFFFFF9C4),
          onTap: () {
            Navigator.push(
              context,
              MaterialPageRoute(
                builder: (_) => const NamesOfAllahScreen(),
              ),
            );
          },
        ),

        // CARD 4: "ይህ ተይዟል" / Coming Soon (Locked Card)
        _buildLockedCard(context),
      ],
    );
  }

  // ===========================================================================
  // REUSABLE CARD WITH ASSET/PHOTO & LIGHT-THEME GLASS OVERLAY
  // ===========================================================================
  Widget _buildImageCard({
    required BuildContext context,
    required String title,
    required String subtitle,
    required String badgeText,
    required String assetPath,
    required String networkFallbackUrl,
    required IconData icon,
    required Color iconColor,
    required Color iconBgColor,
    required VoidCallback onTap,
  }) {
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(18),
        border: Border.all(
          color: const Color(0xFFD4AF37).withValues(alpha: 0.4),
          width: 1.2,
        ),
        boxShadow: [
          BoxShadow(
            color: const Color(0xFF0C3B2E).withValues(alpha: 0.08),
            blurRadius: 10,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: ClipRRect(
        borderRadius: BorderRadius.circular(17),
        child: Material(
          color: Colors.white,
          child: InkWell(
            onTap: onTap,
            splashColor: const Color(0xFFD4AF37).withValues(alpha: 0.15),
            child: Stack(
              children: [
                // 1. Background Photo: Attempts local asset first, then network fallback
                Positioned.fill(
                  child: Image.asset(
                    assetPath,
                    fit: BoxFit.cover,
                    errorBuilder: (_, __, ___) => Image.network(
                      networkFallbackUrl,
                      fit: BoxFit.cover,
                      errorBuilder: (_, __, ___) => Container(
                        decoration: const BoxDecoration(
                          gradient: LinearGradient(
                            begin: Alignment.topLeft,
                            end: Alignment.bottomRight,
                            colors: [Color(0xFFE8F5E9), Color(0xFFFFF8E1)],
                          ),
                        ),
                      ),
                    ),
                  ),
                ),

                // 2. Light Tint & Contrast Gradient (Bright, crisp frosted bottom for high text legibility)
                Positioned.fill(
                  child: DecoratedBox(
                    decoration: BoxDecoration(
                      gradient: LinearGradient(
                        begin: Alignment.topCenter,
                        end: Alignment.bottomCenter,
                        colors: [
                          Colors.white.withValues(alpha: 0.20),
                          Colors.white.withValues(alpha: 0.92),
                          Colors.white,
                        ],
                        stops: const [0.0, 0.65, 1.0],
                      ),
                    ),
                  ),
                ),

                // 3. Card Typography & Badges
                Padding(
                  padding: const EdgeInsets.all(12),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      // Top Row: Icon + Badge
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Container(
                            padding: const EdgeInsets.all(7),
                            decoration: BoxDecoration(
                              shape: BoxShape.circle,
                              color: iconBgColor,
                              border: Border.all(
                                color: iconColor.withValues(alpha: 0.5),
                                width: 1.2,
                              ),
                              boxShadow: [
                                BoxShadow(
                                  color: iconColor.withValues(alpha: 0.2),
                                  blurRadius: 6,
                                ),
                              ],
                            ),
                            child: Icon(icon, size: 18, color: iconColor),
                          ),
                          Container(
                            padding: const EdgeInsets.symmetric(horizontal: 7, vertical: 3),
                            decoration: BoxDecoration(
                              color: Colors.white,
                              borderRadius: BorderRadius.circular(8),
                              border: Border.all(
                                color: const Color(0xFFD4AF37).withValues(alpha: 0.6),
                                width: 0.8,
                              ),
                              boxShadow: [
                                BoxShadow(
                                  color: Colors.black.withValues(alpha: 0.05),
                                  blurRadius: 4,
                                ),
                              ],
                            ),
                            child: Text(
                              badgeText,
                              style: const TextStyle(
                                color: Color(0xFF8C6D1F),
                                fontSize: 10,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                          ),
                        ],
                      ),
                      const Spacer(),

                      // Title & Subtitle in crisp dark emerald
                      Text(
                        title,
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                        style: const TextStyle(
                          color: Color(0xFF0C3B2E),
                          fontSize: 15,
                          fontWeight: FontWeight.bold,
                          letterSpacing: 0.2,
                        ),
                      ),
                      const SizedBox(height: 2),
                      Row(
                        children: [
                          Expanded(
                            child: Text(
                              subtitle,
                              maxLines: 1,
                              overflow: TextOverflow.ellipsis,
                              style: const TextStyle(
                                color: Color(0xFF476859),
                                fontSize: 11,
                                fontWeight: FontWeight.w500,
                              ),
                            ),
                          ),
                          const Icon(
                            Icons.arrow_forward_ios_rounded,
                            size: 11,
                            color: Color(0xFFC59B27),
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  // ===========================================================================
  // CARD 4: "ይህ ተይዟል" / Coming Soon (LOCKED CARD - LIGHT THEME)
  // ===========================================================================
  Widget _buildLockedCard(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(18),
        border: Border.all(
          color: const Color(0xFFD4AF37).withValues(alpha: 0.3),
          width: 1.2,
        ),
        boxShadow: [
          BoxShadow(
            color: const Color(0xFF0C3B2E).withValues(alpha: 0.06),
            blurRadius: 8,
            offset: const Offset(0, 3),
          ),
        ],
      ),
      child: ClipRRect(
        borderRadius: BorderRadius.circular(17),
        child: Material(
          color: const Color(0xFFFAFBFA),
          child: InkWell(
            onTap: () => _showComingSoonBottomSheet(context),
            splashColor: const Color(0xFFD4AF37).withValues(alpha: 0.1),
            child: Stack(
              children: [
                // Soft background gradient
                Positioned.fill(
                  child: DecoratedBox(
                    decoration: BoxDecoration(
                      gradient: LinearGradient(
                        begin: Alignment.topLeft,
                        end: Alignment.bottomRight,
                        colors: [
                          Colors.white,
                          const Color(0xFFF3F7F4),
                        ],
                      ),
                    ),
                  ),
                ),

                // Large faint lock watermark in background
                Positioned(
                  right: -10,
                  bottom: -10,
                  child: Icon(
                    Icons.lock_outline_rounded,
                    size: 90,
                    color: const Color(0xFF0C3B2E).withValues(alpha: 0.04),
                  ),
                ),

                // Card details
                Padding(
                  padding: const EdgeInsets.all(12),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      // Top Row: Lock icon + "Locked" badge
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Container(
                            padding: const EdgeInsets.all(7),
                            decoration: BoxDecoration(
                              shape: BoxShape.circle,
                              color: const Color(0xFFFFF9E6),
                              border: Border.all(
                                color: const Color(0xFFD4AF37).withValues(alpha: 0.6),
                                width: 1.2,
                              ),
                            ),
                            child: const Icon(
                              Icons.lock_rounded,
                              size: 18,
                              color: Color(0xFFC59B27),
                            ),
                          ),
                          Container(
                            padding: const EdgeInsets.symmetric(horizontal: 7, vertical: 3),
                            decoration: BoxDecoration(
                              color: const Color(0xFFFFEBEE),
                              borderRadius: BorderRadius.circular(8),
                              border: Border.all(
                                color: const Color(0xFFEF9A9A),
                                width: 0.8,
                              ),
                            ),
                            child: const Text(
                              'ይህ ተይዟል',
                              style: TextStyle(
                                color: Color(0xFFC62828),
                                fontSize: 9.5,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                          ),
                        ],
                      ),
                      const Spacer(),

                      // Title & Subtitle in dark slate
                      const Text(
                        'በቅርቡ...',
                        style: TextStyle(
                          color: Color(0xFF263238),
                          fontSize: 15,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      const SizedBox(height: 2),
                      Row(
                        children: const [
                          Expanded(
                            child: Text(
                              'ሩቅያህና ተጨማሪ',
                              style: TextStyle(
                                color: Color(0xFF546E7A),
                                fontSize: 11,
                                fontWeight: FontWeight.w500,
                              ),
                            ),
                          ),
                          Icon(
                            Icons.info_outline_rounded,
                            size: 13,
                            color: Color(0xFF78909C),
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  // ===========================================================================
  // BOTTOM SHEET MODAL: EXPLANATION FOR "ይህ ተይዟል / COMING SOON" (LIGHT THEME)
  // ===========================================================================
  void _showComingSoonBottomSheet(BuildContext context) {
    showModalBottomSheet(
      context: context,
      backgroundColor: Colors.white,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(24)),
      ),
      builder: (ctx) {
        return SafeArea(
          child: Padding(
            padding: const EdgeInsets.all(22),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Container(
                  width: 42,
                  height: 4,
                  decoration: BoxDecoration(
                    color: Colors.grey.withValues(alpha: 0.3),
                    borderRadius: BorderRadius.circular(2),
                  ),
                ),
                const SizedBox(height: 18),
                Container(
                  padding: const EdgeInsets.all(16),
                  decoration: BoxDecoration(
                    color: const Color(0xFFFFF9E6),
                    shape: BoxShape.circle,
                    border: Border.all(color: const Color(0xFFD4AF37), width: 1.5),
                  ),
                  child: const Icon(
                    Icons.lock_clock_rounded,
                    color: Color(0xFFC59B27),
                    size: 36,
                  ),
                ),
                const SizedBox(height: 14),
                const Text(
                  'ይህ ክፍል በቅርቡ ይከፈታል!',
                  style: TextStyle(
                    color: Color(0xFF0C3B2E),
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                const SizedBox(height: 8),
                const Text(
                  'በቀጣዩ የአፕሊኬሽኑ ዝመና (Update) ላይ ሩቅያህ ሸሪዓ (Ruqyah Shari\'ah)፣ የረመዳን ልዩ አዝካሮች እና የተመረጡ የቁርኣን ድምፅ ንባቦች ይካተታሉ።',
                  textAlign: TextAlign.center,
                  style: TextStyle(
                    color: Color(0xFF37474F),
                    fontSize: 13,
                    height: 1.5,
                  ),
                ),
                const SizedBox(height: 20),
                SizedBox(
                  width: double.infinity,
                  child: ElevatedButton(
                    style: ElevatedButton.styleFrom(
                      backgroundColor: const Color(0xFF0F5132),
                      foregroundColor: Colors.white,
                      elevation: 0,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(12),
                      ),
                      padding: const EdgeInsets.symmetric(vertical: 13),
                    ),
                    onPressed: () => Navigator.pop(ctx),
                    child: const Text(
                      'ተረድቻለሁ',
                      style: TextStyle(fontWeight: FontWeight.bold, fontSize: 14),
                    ),
                  ),
                ),
              ],
            ),
          ),
        );
      },
    );
  }

  // ===========================================================================
  // 5-ITEM BOTTOM NAVIGATION BAR (LIGHT THEME - OPTIONAL STANDALONE)
  // ===========================================================================
  Widget _buildBottomNav(BuildContext context, IslamicThemeColors colors) {
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        border: Border(
          top: BorderSide(
            color: const Color(0xFFD4AF37).withValues(alpha: 0.25),
            width: 1,
          ),
        ),
        boxShadow: [
          BoxShadow(
            color: const Color(0xFF0C3B2E).withValues(alpha: 0.08),
            blurRadius: 10,
            offset: const Offset(0, -2),
          ),
        ],
      ),
      child: SafeArea(
        child: Padding(
          padding: const EdgeInsets.symmetric(vertical: 6),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: [
              // 1. መነሻ (Home)
              _buildNavItem(
                index: 0,
                icon: Icons.home_rounded,
                label: 'መነሻ',
                isSelected: _navIndex == 0,
                onTap: () => setState(() => _navIndex = 0),
              ),
              // 2. ዱዓዎች (Duas)
              _buildNavItem(
                index: 1,
                icon: Icons.menu_book_rounded,
                label: 'ዱዓዎች',
                isSelected: _navIndex == 1,
                onTap: () {
                  setState(() => _navIndex = 1);
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (_) => const DuasScreen()),
                  ).then((_) => setState(() => _navIndex = 0));
                },
              ),
              // 3. ተስቢሕ (Tasbih)
              _buildNavItem(
                index: 2,
                icon: Icons.touch_app_rounded,
                label: 'ተስቢሕ',
                isSelected: _navIndex == 2,
                onTap: () {
                  if (widget.onOpenTasbih != null) {
                    widget.onOpenTasbih!();
                  } else {
                    setState(() => _navIndex = 2);
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => Scaffold(
                          appBar: AppBar(title: const Text('ተስቢሕ (ዲጂታል ቆጣሪ)')),
                          body: const DuasScreen(),
                        ),
                      ),
                    ).then((_) => setState(() => _navIndex = 0));
                  }
                },
              ),
              // 4. 99 ስሞች (99 Names)
              _buildNavItem(
                index: 3,
                icon: Icons.auto_awesome_rounded,
                label: '99 ስሞች',
                isSelected: _navIndex == 3,
                onTap: () {
                  setState(() => _navIndex = 3);
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (_) => const NamesOfAllahScreen()),
                  ).then((_) => setState(() => _navIndex = 0));
                },
              ),
              // 5. ቅንብር (Settings)
              _buildNavItem(
                index: 4,
                icon: Icons.palette_outlined,
                label: 'ቅንብር',
                isSelected: _navIndex == 4,
                onTap: () {
                  showThemeDialog(context);
                },
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildNavItem({
    required int index,
    required IconData icon,
    required String label,
    required bool isSelected,
    required VoidCallback onTap,
  }) {
    const activeColor = Color(0xFF0F5132);
    final inactiveColor = const Color(0xFF607D8B).withValues(alpha: 0.8);

    return InkWell(
      onTap: onTap,
      borderRadius: BorderRadius.circular(12),
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(
              icon,
              color: isSelected ? activeColor : inactiveColor,
              size: 22,
            ),
            const SizedBox(height: 3),
            Text(
              label,
              style: TextStyle(
                color: isSelected ? activeColor : inactiveColor,
                fontSize: 11,
                fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
              ),
            ),
          ],
        ),
      ),
    );
  }

  // Safe Fallback Categories
  static AzkarCategory _fallbackMorningCategory() {
    return AzkarCategory(
      id: 'morning',
      title: 'የጥዋት አዝካር',
      subtitle: 'በጥዋት የሚባሉ አዝካሮች',
      icon: 'sun',
      items: [],
    );
  }

  static AzkarCategory _fallbackEveningCategory() {
    return AzkarCategory(
      id: 'evening',
      title: 'የማታ አዝካር',
      subtitle: 'በማታ የሚባሉ አዝካሮች',
      icon: 'moon',
      items: [],
    );
  }
}

// ============================================================================
// CUSTOM PAINTER: LIGHT ISLAMIC ATMOSPHERE & GEOMETRIC ARCH EMBELLISHMENT
// ============================================================================
class _IslamicLightAtmospherePainter extends CustomPainter {
  final Color bgColorTop;
  final Color bgColorBottom;
  final Color accentGold;
  final Color accentGreen;

  _IslamicLightAtmospherePainter({
    required this.bgColorTop,
    required this.bgColorBottom,
    required this.accentGold,
    required this.accentGreen,
  });

  @override
  void paint(Canvas canvas, Size size) {
    // 1. Soft Warm Gradient Background
    final Rect rect = Offset.zero & size;
    final Paint bgPaint = Paint()
      ..shader = LinearGradient(
        begin: Alignment.topCenter,
        end: Alignment.bottomCenter,
        colors: [bgColorTop, bgColorBottom],
      ).createShader(rect);
    canvas.drawRect(rect, bgPaint);

    // 2. Subtle Decorative Mosque Silhouette at bottom with soft opacity
    final Path mosquePath = Path();
    final double baseY = size.height;
    final double roofY = size.height * 0.88;

    mosquePath.moveTo(0, baseY);
    mosquePath.lineTo(0, roofY + 20);

    // Left Minaret
    mosquePath.lineTo(size.width * 0.08, roofY + 20);
    mosquePath.lineTo(size.width * 0.08, roofY - 35);
    mosquePath.lineTo(size.width * 0.10, roofY - 50);
    mosquePath.lineTo(size.width * 0.12, roofY - 35);
    mosquePath.lineTo(size.width * 0.12, roofY + 20);

    // Small Dome Left
    mosquePath.lineTo(size.width * 0.22, roofY + 20);
    mosquePath.quadraticBezierTo(size.width * 0.32, roofY - 12, size.width * 0.42, roofY + 20);

    // Central Grand Dome
    mosquePath.lineTo(size.width * 0.45, roofY + 15);
    mosquePath.quadraticBezierTo(size.width * 0.60, roofY - 32, size.width * 0.75, roofY + 15);

    // Right Minaret
    mosquePath.lineTo(size.width * 0.86, roofY + 20);
    mosquePath.lineTo(size.width * 0.86, roofY - 45);
    mosquePath.lineTo(size.width * 0.88, roofY - 60);
    mosquePath.lineTo(size.width * 0.90, roofY - 45);
    mosquePath.lineTo(size.width * 0.90, roofY + 20);

    mosquePath.lineTo(size.width, roofY + 20);
    mosquePath.lineTo(size.width, baseY);
    mosquePath.close();

    final Paint mosquePaint = Paint()
      ..color = accentGreen
      ..style = PaintingStyle.fill;
    canvas.drawPath(mosquePath, mosquePaint);

    // 3. Subtle gold arch aura in upper right
    final Paint auraPaint = Paint()
      ..color = accentGold
      ..maskFilter = const MaskFilter.blur(BlurStyle.normal, 28);
    canvas.drawCircle(Offset(size.width * 0.85, 70), 55, auraPaint);
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) => false;
}
