import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../models/azkar_models.dart';
import '../providers/azkar_provider.dart';
import '../theme/app_theme.dart';
import '../widgets/theme_dialog.dart';
import 'azkar_list_screen.dart';
import 'duas_screen.dart';
import 'favorites_screen.dart';
import 'names_screen.dart';

class HomeScreen extends StatefulWidget {
  final VoidCallback? onOpenTasbih;

  const HomeScreen({super.key, this.onOpenTasbih});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  int _navIndex = 0;

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<AzkarProvider>();
    final colors = IslamicThemeColors.fromMode(provider.currentTheme);

    return Scaffold(
      backgroundColor: const Color(0xFF040E14),
      bottomNavigationBar: _buildBottomNav(context, colors),
      body: Stack(
        children: [
          // 1. FULL-SCREEN NIGHT SKY & MOSQUE BACKGROUND
          Positioned.fill(
            child: CustomPaint(
              painter: _IslamicNightSkyPainter(
                skyColorTop: const Color(0xFF02070D),
                skyColorBottom: const Color(0xFF09241B),
                mosqueSilhouetteColor: const Color(0xFF03130E).withValues(alpha: 0.95),
                moonGold: const Color(0xFFFFD700),
              ),
            ),
          ),

          // Optional subtle night sky backdrop image overlay with soft blending
          Positioned.fill(
            child: Image.network(
              'https://images.unsplash.com/photo-1519817650390-64a93db51149?auto=format&fit=crop&w=1080&q=80',
              fit: BoxFit.cover,
              errorBuilder: (_, __, ___) => const SizedBox.shrink(),
              color: const Color(0xFF041410).withValues(alpha: 0.75),
              colorBlendMode: BlendMode.darken,
            ),
          ),

          // Top dark gradient protection for status bar readability
          Positioned(
            top: 0,
            left: 0,
            right: 0,
            height: 180,
            child: DecoratedBox(
              decoration: BoxDecoration(
                gradient: LinearGradient(
                  begin: Alignment.topCenter,
                  end: Alignment.bottomCenter,
                  colors: [
                    const Color(0xFF02070D).withValues(alpha: 0.9),
                    Colors.transparent,
                  ],
                ),
              ),
            ),
          ),

          // 2. MAIN SCROLLABLE CONTENT
          SafeArea(
            child: SingleChildScrollView(
              physics: const BouncingScrollPhysics(),
              padding: const EdgeInsets.symmetric(horizontal: 18, vertical: 12),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  // --- HEADER WITH SALAM & SETTINGS ---
                  _buildHeader(context, colors),
                  const SizedBox(height: 18),

                  // --- AYAH CARD (فَاذْكُرُونِي أَذْكُرْكُمْ) ---
                  _buildAyahCard(context, colors),
                  const SizedBox(height: 24),

                  // Section Title
                  Row(
                    children: [
                      Container(
                        width: 4,
                        height: 20,
                        decoration: BoxDecoration(
                          color: const Color(0xFFFFD700),
                          borderRadius: BorderRadius.circular(2),
                        ),
                      ),
                      const SizedBox(width: 8),
                      const Text(
                        'ዋና ዋና የአዝካር ክፍሎች',
                        style: TextStyle(
                          color: Colors.white,
                          fontSize: 17,
                          fontWeight: FontWeight.bold,
                          letterSpacing: 0.3,
                        ),
                      ),
                      const Spacer(),
                      Text(
                        'ዕለታዊ ዚክሮች',
                        style: TextStyle(
                          color: const Color(0xFFD4AF37).withValues(alpha: 0.85),
                          fontSize: 12,
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 14),

                  // --- 4 MAIN CARDS (2x2 GRID) ---
                  _build2x2CardsGrid(context, provider),
                  const SizedBox(height: 20),

                  // Quick Tasbih shortcut banner if available
                  if (widget.onOpenTasbih != null) ...[
                    _buildTasbihBanner(context, provider),
                    const SizedBox(height: 16),
                  ],
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }

  // --- HEADER WIDGET ---
  Widget _buildHeader(BuildContext context, IslamicThemeColors colors) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        // Salam & Title Column
        Expanded(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: [
                  Container(
                    padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
                    decoration: BoxDecoration(
                      color: const Color(0xFF00E676).withValues(alpha: 0.15),
                      borderRadius: BorderRadius.circular(12),
                      border: Border.all(
                        color: const Color(0xFF00E676).withValues(alpha: 0.3),
                        width: 0.8,
                      ),
                    ),
                    child: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        const Icon(
                          Icons.brightness_2_rounded,
                          size: 11,
                          color: Color(0xFFFFD700),
                        ),
                        const SizedBox(width: 5),
                        Text(
                          'ኑሩል አዝካር • Nur Azkar',
                          style: TextStyle(
                            color: const Color(0xFFFFD700).withValues(alpha: 0.95),
                            fontSize: 11,
                            fontWeight: FontWeight.w600,
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 6),
              const Text(
                'አሰላሙ ዐለይኩም',
                style: TextStyle(
                  color: Colors.white,
                  fontSize: 22,
                  fontWeight: FontWeight.bold,
                  letterSpacing: 0.4,
                ),
              ),
              Text(
                'ወረሕመቱላሂ ወበረካቱህ',
                style: TextStyle(
                  color: Colors.white.withValues(alpha: 0.75),
                  fontSize: 13,
                  fontWeight: FontWeight.w400,
                ),
              ),
            ],
          ),
        ),

        // Settings / Theme Button
        Material(
          color: Colors.transparent,
          child: InkWell(
            onTap: () => showThemeDialog(context),
            borderRadius: BorderRadius.circular(16),
            child: Container(
              padding: const EdgeInsets.all(10),
              decoration: BoxDecoration(
                color: const Color(0xFF0E3025).withValues(alpha: 0.85),
                borderRadius: BorderRadius.circular(16),
                border: Border.all(
                  color: const Color(0xFFD4AF37).withValues(alpha: 0.4),
                  width: 1.2,
                ),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withValues(alpha: 0.3),
                    blurRadius: 8,
                    offset: const Offset(0, 3),
                  ),
                ],
              ),
              child: const Icon(
                Icons.settings_rounded,
                color: Color(0xFFFFD700),
                size: 22,
              ),
            ),
          ),
        ),
      ],
    );
  }

  // --- AYAH CARD (فَاذْكُرُونِي أَذْكُرْكُمْ) ---
  Widget _buildAyahCard(BuildContext context, IslamicThemeColors colors) {
    return Container(
      decoration: BoxDecoration(
        color: const Color(0xFF08241C).withValues(alpha: 0.88),
        borderRadius: BorderRadius.circular(20),
        border: Border.all(
          color: const Color(0xFFD4AF37).withValues(alpha: 0.65),
          width: 1.5,
        ),
        boxShadow: [
          BoxShadow(
            color: const Color(0xFFD4AF37).withValues(alpha: 0.14),
            blurRadius: 18,
            spreadRadius: 1,
            offset: const Offset(0, 4),
          ),
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.5),
            blurRadius: 12,
            offset: const Offset(0, 6),
          ),
        ],
      ),
      child: ClipRRect(
        borderRadius: BorderRadius.circular(19),
        child: Stack(
          children: [
            // Decorative subtle Islamic arch background pattern
            Positioned(
              right: -20,
              bottom: -20,
              child: Icon(
                Icons.mosque_rounded,
                size: 130,
                color: const Color(0xFFD4AF37).withValues(alpha: 0.05),
              ),
            ),

            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 18, vertical: 16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  // Top Row: Bismillah & Tag
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                        decoration: BoxDecoration(
                          color: const Color(0xFFD4AF37).withValues(alpha: 0.15),
                          borderRadius: BorderRadius.circular(8),
                          border: Border.all(
                            color: const Color(0xFFD4AF37).withValues(alpha: 0.35),
                            width: 0.8,
                          ),
                        ),
                        child: const Text(
                          '📖 የዕለቱ የቁርኣን አንቀጽ',
                          style: TextStyle(
                            color: Color(0xFFFFD700),
                            fontSize: 11,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                      Text(
                        'ሱረቱል በቀራህ ፡ 152',
                        style: TextStyle(
                          color: Colors.white.withValues(alpha: 0.6),
                          fontSize: 11,
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 14),

                  // Arabic Ayah Text
                  const Text(
                    '« فَاذْكُرُونِي أَذْكُرْكُمْ وَاشْكُرُوا لِي وَلَا تَكْفُرُونِ »',
                    textAlign: TextAlign.center,
                    textDirection: TextDirection.rtl,
                    style: TextStyle(
                      fontFamily: 'Amiri',
                      fontSize: 22,
                      fontWeight: FontWeight.bold,
                      color: Color(0xFFFFDE59),
                      height: 1.7,
                      letterSpacing: 0.5,
                      shadows: [
                        Shadow(
                          color: Color(0x66FFDE59),
                          blurRadius: 10,
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(height: 12),

                  // Divider line with small golden center star
                  Row(
                    children: [
                      Expanded(
                        child: Divider(
                          color: const Color(0xFFD4AF37).withValues(alpha: 0.3),
                          thickness: 1,
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 8),
                        child: Icon(
                          Icons.star_rounded,
                          size: 14,
                          color: const Color(0xFFFFD700).withValues(alpha: 0.75),
                        ),
                      ),
                      Expanded(
                        child: Divider(
                          color: const Color(0xFFD4AF37).withValues(alpha: 0.3),
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
                      color: Color(0xFFE4F4EC),
                      fontSize: 13.5,
                      fontWeight: FontWeight.w500,
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

  // --- 4 MAIN CARDS (2x2 GRID) ---
  Widget _build2x2CardsGrid(BuildContext context, AzkarProvider provider) {
    // Look up morning and evening categories dynamically
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
          imageUrl: 'https://images.unsplash.com/photo-1542816417-0983c9c9ad53?auto=format&fit=crop&w=600&q=80',
          icon: Icons.wb_sunny_rounded,
          iconColor: const Color(0xFFFFD54F),
          glowColor: const Color(0xFFFF9800),
          gradientColors: [
            const Color(0xFF1B3B2B).withValues(alpha: 0.3),
            const Color(0xFF041A13).withValues(alpha: 0.95),
          ],
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
          imageUrl: 'https://images.unsplash.com/photo-1564769625905-50e93615e769?auto=format&fit=crop&w=600&q=80',
          icon: Icons.nightlight_round,
          iconColor: const Color(0xFF80D8FF),
          glowColor: const Color(0xFF00B0FF),
          gradientColors: [
            const Color(0xFF0C243B).withValues(alpha: 0.3),
            const Color(0xFF03141F).withValues(alpha: 0.95),
          ],
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
          imageUrl: 'https://images.unsplash.com/photo-1591604129939-f1efa4d9f7fa?auto=format&fit=crop&w=600&q=80',
          icon: Icons.auto_awesome_rounded,
          iconColor: const Color(0xFFFFD700),
          glowColor: const Color(0xFFFFD700),
          gradientColors: [
            const Color(0xFF282006).withValues(alpha: 0.3),
            const Color(0xFF151004).withValues(alpha: 0.95),
          ],
          onTap: () {
            Navigator.push(
              context,
              MaterialPageRoute(
                builder: (_) => const NamesOfAllahScreen(),
              ),
            );
          },
        ),

        // CARD 4: "ይህ ተይዟል / Coming Soon" (Locked Card)
        _buildLockedCard(context),
      ],
    );
  }

  // --- REUSABLE CARD WITH BACKGROUND IMAGE & GRADIENT ---
  Widget _buildImageCard({
    required BuildContext context,
    required String title,
    required String subtitle,
    required String badgeText,
    required String imageUrl,
    required IconData icon,
    required Color iconColor,
    required Color glowColor,
    required List<Color> gradientColors,
    required VoidCallback onTap,
  }) {
    return Container(
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(18),
        border: Border.all(
          color: const Color(0xFFD4AF37).withValues(alpha: 0.45),
          width: 1.2,
        ),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.4),
            blurRadius: 10,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: ClipRRect(
        borderRadius: BorderRadius.circular(17),
        child: Material(
          color: const Color(0xFF081C15),
          child: InkWell(
            onTap: onTap,
            splashColor: const Color(0xFFFFD700).withValues(alpha: 0.2),
            child: Stack(
              children: [
                // Background Photo
                Positioned.fill(
                  child: Image.network(
                    imageUrl,
                    fit: BoxFit.cover,
                    errorBuilder: (_, __, ___) => Container(
                      decoration: BoxDecoration(
                        gradient: LinearGradient(
                          begin: Alignment.topLeft,
                          end: Alignment.bottomRight,
                          colors: gradientColors,
                        ),
                      ),
                    ),
                  ),
                ),

                // Dark & Colored Gradient Overlay
                Positioned.fill(
                  child: DecoratedBox(
                    decoration: BoxDecoration(
                      gradient: LinearGradient(
                        begin: Alignment.topCenter,
                        end: Alignment.bottomCenter,
                        stops: const [0.0, 0.45, 1.0],
                        colors: [
                          Colors.transparent,
                          gradientColors[0],
                          gradientColors[1],
                        ],
                      ),
                    ),
                  ),
                ),

                // Card Content (Badge, Icon, Title, Subtitle)
                Padding(
                  padding: const EdgeInsets.all(12),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      // Top Row: Icon with glowing circle + count badge
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Container(
                            padding: const EdgeInsets.all(8),
                            decoration: BoxDecoration(
                              shape: BoxShape.circle,
                              color: const Color(0xFF03100B).withValues(alpha: 0.85),
                              border: Border.all(
                                color: iconColor.withValues(alpha: 0.6),
                                width: 1.2,
                              ),
                              boxShadow: [
                                BoxShadow(
                                  color: glowColor.withValues(alpha: 0.35),
                                  blurRadius: 8,
                                ),
                              ],
                            ),
                            child: Icon(icon, size: 20, color: iconColor),
                          ),
                          Container(
                            padding: const EdgeInsets.symmetric(horizontal: 7, vertical: 3),
                            decoration: BoxDecoration(
                              color: Colors.black.withValues(alpha: 0.7),
                              borderRadius: BorderRadius.circular(8),
                              border: Border.all(
                                color: Colors.white.withValues(alpha: 0.2),
                                width: 0.6,
                              ),
                            ),
                            child: Text(
                              badgeText,
                              style: const TextStyle(
                                color: Color(0xFFFFD700),
                                fontSize: 10,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                          ),
                        ],
                      ),
                      const Spacer(),

                      // Title & Subtitle
                      Text(
                        title,
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                        style: const TextStyle(
                          color: Colors.white,
                          fontSize: 15,
                          fontWeight: FontWeight.bold,
                          letterSpacing: 0.2,
                          shadows: [
                            Shadow(
                              color: Colors.black,
                              blurRadius: 4,
                            ),
                          ],
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
                              style: TextStyle(
                                color: Colors.white.withValues(alpha: 0.75),
                                fontSize: 11,
                              ),
                            ),
                          ),
                          Icon(
                            Icons.arrow_forward_ios_rounded,
                            size: 11,
                            color: const Color(0xFFFFD700).withValues(alpha: 0.8),
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

  // --- CARD 4: "ይህ ተይዟል / Coming Soon" (LOCKED CARD) ---
  Widget _buildLockedCard(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(18),
        border: Border.all(
          color: const Color(0xFFD4AF37).withValues(alpha: 0.25),
          width: 1.2,
        ),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.5),
            blurRadius: 10,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: ClipRRect(
        borderRadius: BorderRadius.circular(17),
        child: Material(
          color: const Color(0xFF071510),
          child: InkWell(
            onTap: () => _showComingSoonBottomSheet(context),
            splashColor: const Color(0xFFFFD700).withValues(alpha: 0.1),
            child: Stack(
              children: [
                // Frosted mesh background
                Positioned.fill(
                  child: DecoratedBox(
                    decoration: BoxDecoration(
                      gradient: LinearGradient(
                        begin: Alignment.topLeft,
                        end: Alignment.bottomRight,
                        colors: [
                          const Color(0xFF0F261E).withValues(alpha: 0.8),
                          const Color(0xFF06140F).withValues(alpha: 0.95),
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
                    color: Colors.white.withValues(alpha: 0.04),
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
                            padding: const EdgeInsets.all(8),
                            decoration: BoxDecoration(
                              shape: BoxShape.circle,
                              color: const Color(0xFF1E2823),
                              border: Border.all(
                                color: const Color(0xFFD4AF37).withValues(alpha: 0.5),
                                width: 1.2,
                              ),
                            ),
                            child: const Icon(
                              Icons.lock_rounded,
                              size: 20,
                              color: Color(0xFFFFD700),
                            ),
                          ),
                          Container(
                            padding: const EdgeInsets.symmetric(horizontal: 7, vertical: 3),
                            decoration: BoxDecoration(
                              color: const Color(0xFFB71C1C).withValues(alpha: 0.25),
                              borderRadius: BorderRadius.circular(8),
                              border: Border.all(
                                color: const Color(0xFFE57373).withValues(alpha: 0.4),
                                width: 0.6,
                              ),
                            ),
                            child: const Row(
                              mainAxisSize: MainAxisSize.min,
                              children: [
                                Text(
                                  'የተቆለፈ',
                                  style: TextStyle(
                                    color: Color(0xFFFF8A80),
                                    fontSize: 10,
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ],
                      ),
                      const Spacer(),

                      // Title
                      const Text(
                        'ይህ ተይዟል',
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                        style: TextStyle(
                          color: Color(0xFFE0E0E0),
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
                              'Coming Soon • በቅርቡ',
                              maxLines: 1,
                              overflow: TextOverflow.ellipsis,
                              style: TextStyle(
                                color: Colors.white.withValues(alpha: 0.55),
                                fontSize: 11,
                              ),
                            ),
                          ),
                          Icon(
                            Icons.info_outline_rounded,
                            size: 13,
                            color: Colors.white.withValues(alpha: 0.5),
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

  // --- FAST TASBIH SHORTCUT BANNER ---
  Widget _buildTasbihBanner(BuildContext context, AzkarProvider provider) {
    return Container(
      decoration: BoxDecoration(
        color: const Color(0xFF0A2B20).withValues(alpha: 0.75),
        borderRadius: BorderRadius.circular(16),
        border: Border.all(
          color: const Color(0xFF00E676).withValues(alpha: 0.25),
          width: 1,
        ),
      ),
      child: ListTile(
        contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 4),
        leading: Container(
          padding: const EdgeInsets.all(8),
          decoration: BoxDecoration(
            color: const Color(0xFFFFD700).withValues(alpha: 0.15),
            shape: BoxShape.circle,
            border: Border.all(
              color: const Color(0xFFFFD700).withValues(alpha: 0.4),
              width: 1,
            ),
          ),
          child: const Icon(Icons.touch_app_rounded, color: Color(0xFFFFD700), size: 22),
        ),
        title: const Text(
          'ፈጣን ተስቢሕ ቆጣሪ',
          style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 14),
        ),
        subtitle: Text(
          'ሱብሓነላህ • ዒላማ: ${provider.tasbihTarget} | የተቆጠረ: ${provider.tasbihTotal}',
          style: TextStyle(color: Colors.white.withValues(alpha: 0.7), fontSize: 11),
        ),
        trailing: ElevatedButton(
          style: ElevatedButton.styleFrom(
            backgroundColor: const Color(0xFFFFD700),
            foregroundColor: Colors.black,
            padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 8),
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
            elevation: 0,
          ),
          onPressed: widget.onOpenTasbih,
          child: const Text('ክፈት', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 12)),
        ),
      ),
    );
  }

  // --- BOTTOM SHEET FOR "COMING SOON" ---
  void _showComingSoonBottomSheet(BuildContext context) {
    showModalBottomSheet(
      context: context,
      backgroundColor: const Color(0xFF071F17),
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
                    color: Colors.white.withValues(alpha: 0.3),
                    borderRadius: BorderRadius.circular(2),
                  ),
                ),
                const SizedBox(height: 18),
                Container(
                  padding: const EdgeInsets.all(16),
                  decoration: BoxDecoration(
                    color: const Color(0xFF0D3528),
                    shape: BoxShape.circle,
                    border: Border.all(color: const Color(0xFFFFD700), width: 1.5),
                  ),
                  child: const Icon(
                    Icons.lock_clock_rounded,
                    color: Color(0xFFFFD700),
                    size: 36,
                  ),
                ),
                const SizedBox(height: 14),
                const Text(
                  'ይህ ክፍል በቅርቡ ይከፈታል!',
                  style: TextStyle(
                    color: Colors.white,
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                const SizedBox(height: 8),
                Text(
                  'በቀጣዩ የአፕሊኬሽኑ ዝመና (Update) ላይ ሩቅያህ ሸሪዓ (Ruqyah Shari\'ah)፣ የረመዳን ልዩ አዝካሮች እና የተመረጡ የቁርኣን ድምፅ ንባቦች ይካተታሉ።',
                  textAlign: TextAlign.center,
                  style: TextStyle(
                    color: Colors.white.withValues(alpha: 0.75),
                    fontSize: 13,
                    height: 1.5,
                  ),
                ),
                const SizedBox(height: 20),
                SizedBox(
                  width: double.infinity,
                  child: ElevatedButton(
                    style: ElevatedButton.styleFrom(
                      backgroundColor: const Color(0xFFFFD700),
                      foregroundColor: Colors.black,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(12),
                      ),
                      padding: const EdgeInsets.symmetric(vertical: 12),
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

  // --- 5-ITEM BOTTOM NAVIGATION BAR ---
  Widget _buildBottomNav(BuildContext context, IslamicThemeColors colors) {
    return Container(
      decoration: BoxDecoration(
        color: const Color(0xFF03100B),
        border: Border(
          top: BorderSide(
            color: const Color(0xFFD4AF37).withValues(alpha: 0.25),
            width: 1,
          ),
        ),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.7),
            blurRadius: 12,
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
              _buildNavItem(
                index: 0,
                icon: Icons.home_rounded,
                label: 'መነሻ',
                isSelected: _navIndex == 0,
                onTap: () => setState(() => _navIndex = 0),
              ),
              _buildNavItem(
                index: 1,
                icon: Icons.menu_book_rounded,
                label: 'አዝካር',
                isSelected: _navIndex == 1,
                onTap: () {
                  setState(() => _navIndex = 1);
                  final provider = context.read<AzkarProvider>();
                  if (provider.categories.isNotEmpty) {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => AzkarListScreen(category: provider.categories.first),
                      ),
                    ).then((_) => setState(() => _navIndex = 0));
                  }
                },
              ),
              _buildNavItem(
                index: 2,
                icon: Icons.favorite_rounded,
                label: 'ጸሎት',
                isSelected: _navIndex == 2,
                onTap: () {
                  setState(() => _navIndex = 2);
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (_) => const DuasScreen()),
                  ).then((_) => setState(() => _navIndex = 0));
                },
              ),
              _buildNavItem(
                index: 3,
                icon: Icons.star_rounded,
                label: 'ተወዳጆች',
                isSelected: _navIndex == 3,
                onTap: () {
                  setState(() => _navIndex = 3);
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (_) => const FavoritesScreen()),
                  ).then((_) => setState(() => _navIndex = 0));
                },
              ),
              _buildNavItem(
                index: 4,
                icon: Icons.settings_rounded,
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
    final activeColor = const Color(0xFFFFD700);
    final inactiveColor = Colors.white.withValues(alpha: 0.5);

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

  // Fallbacks if categories are empty during initial load
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
// CUSTOM PAINTER: FULL-SCREEN NIGHT SKY, STARS, CRESCENT MOON & MOSQUE DOMES
// ============================================================================
class _IslamicNightSkyPainter extends CustomPainter {
  final Color skyColorTop;
  final Color skyColorBottom;
  final Color mosqueSilhouetteColor;
  final Color moonGold;

  _IslamicNightSkyPainter({
    required this.skyColorTop,
    required this.skyColorBottom,
    required this.mosqueSilhouetteColor,
    required this.moonGold,
  });

  @override
  void paint(Canvas canvas, Size size) {
    // 1. Sky Gradient
    final Rect rect = Offset.zero & size;
    final Paint skyPaint = Paint()
      ..shader = LinearGradient(
        begin: Alignment.topCenter,
        end: Alignment.bottomCenter,
        colors: [skyColorTop, skyColorBottom],
      ).createShader(rect);
    canvas.drawRect(rect, skyPaint);

    // 2. Stars
    final Paint starPaint = Paint()..color = Colors.white.withValues(alpha: 0.75);
    final List<Offset> stars = [
      Offset(size.width * 0.10, size.height * 0.07),
      Offset(size.width * 0.22, size.height * 0.12),
      Offset(size.width * 0.35, size.height * 0.05),
      Offset(size.width * 0.52, size.height * 0.09),
      Offset(size.width * 0.68, size.height * 0.04),
      Offset(size.width * 0.88, size.height * 0.14),
      Offset(size.width * 0.16, size.height * 0.20),
      Offset(size.width * 0.42, size.height * 0.18),
      Offset(size.width * 0.60, size.height * 0.23),
      Offset(size.width * 0.82, size.height * 0.22),
      Offset(size.width * 0.94, size.height * 0.28),
      Offset(size.width * 0.06, size.height * 0.32),
      Offset(size.width * 0.74, size.height * 0.35),
    ];
    for (int i = 0; i < stars.length; i++) {
      final double radius = (i % 3 == 0) ? 1.6 : 1.1;
      canvas.drawCircle(stars[i], radius, starPaint);
    }

    // 3. Glowing Crescent Moon in the upper sky
    final double moonX = size.width * 0.84;
    final double moonY = size.height * 0.09;
    final double moonRadius = 22.0;

    // Glowing aura
    final Paint glowPaint = Paint()
      ..color = moonGold.withValues(alpha: 0.2)
      ..maskFilter = const MaskFilter.blur(BlurStyle.normal, 14);
    canvas.drawCircle(Offset(moonX, moonY), moonRadius + 6, glowPaint);

    // Crescent Path
    final Path moonPath = Path()
      ..addOval(Rect.fromCircle(center: Offset(moonX, moonY), radius: moonRadius));
    final Path cutout = Path()
      ..addOval(Rect.fromCircle(center: Offset(moonX - 8, moonY - 4), radius: moonRadius - 2));
    final Path crescent = Path.combine(PathOperation.difference, moonPath, cutout);

    final Paint crescentPaint = Paint()
      ..color = moonGold
      ..style = PaintingStyle.fill;
    canvas.drawPath(crescent, crescentPaint);

    // Golden star near crescent moon
    final Paint moonStarPaint = Paint()..color = moonGold.withValues(alpha: 0.95);
    canvas.drawCircle(Offset(moonX - 15, moonY + 3), 2.0, moonStarPaint);

    // 4. Mosque silhouette along bottom
    final Path mosquePath = Path();
    final double baseY = size.height;
    final double roofY = size.height * 0.84;

    mosquePath.moveTo(0, baseY);
    mosquePath.lineTo(0, roofY + 25);

    // Left Minaret
    mosquePath.lineTo(size.width * 0.07, roofY + 25);
    mosquePath.lineTo(size.width * 0.07, roofY - 45);
    mosquePath.lineTo(size.width * 0.09, roofY - 65); // spire
    mosquePath.lineTo(size.width * 0.11, roofY - 45);
    mosquePath.lineTo(size.width * 0.11, roofY + 25);

    // Small Dome Left
    mosquePath.lineTo(size.width * 0.20, roofY + 25);
    mosquePath.quadraticBezierTo(size.width * 0.30, roofY - 15, size.width * 0.40, roofY + 25);

    // Grand Central Dome
    mosquePath.lineTo(size.width * 0.43, roofY + 20);
    mosquePath.quadraticBezierTo(size.width * 0.58, roofY - 40, size.width * 0.73, roofY + 20);

    // Right Minaret
    mosquePath.lineTo(size.width * 0.85, roofY + 25);
    mosquePath.lineTo(size.width * 0.85, roofY - 55);
    mosquePath.lineTo(size.width * 0.87, roofY - 75); // spire
    mosquePath.lineTo(size.width * 0.89, roofY - 55);
    mosquePath.lineTo(size.width * 0.89, roofY + 25);

    mosquePath.lineTo(size.width, roofY + 25);
    mosquePath.lineTo(size.width, baseY);
    mosquePath.close();

    final Paint mosquePaint = Paint()
      ..color = mosqueSilhouetteColor
      ..style = PaintingStyle.fill;
    canvas.drawPath(mosquePath, mosquePaint);
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) => false;
}
