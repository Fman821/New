import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../models/azkar_models.dart';
import '../providers/azkar_provider.dart';
import '../theme/app_theme.dart';
import '../widgets/theme_dialog.dart';
import '../widgets/islamic_divider.dart';
import 'azkar_list_screen.dart';
import 'duas_screen.dart';
import 'names_screen.dart';

/// Canonical, production-quality Home Page for Nur Islamic Lifestyle.
/// Source of Truth: Morning Azkar and Evening Azkar are the TWO MOST IMPORTANT
/// FEATURES, followed by the Daily Progress component, with the Daily Verse and
/// lifestyle tools placed in a subordinate secondary position.
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

/// Backwards compatibility alias for existing project imports
class HomeScreen extends HomePage {
  const HomeScreen({
    super.key,
    super.onOpenTasbih,
    super.showBottomNav = false,
  });
}

class _HomePageState extends State<HomePage> {
  @override
  Widget build(BuildContext context) {
    final provider = context.watch<AzkarProvider>();
    final colors = IslamicThemeColors.fromMode(provider.currentTheme);

    // Dynamic matching of morning and evening categories
    final morningCat = provider.categories.firstWhere(
      (c) => c.id.toLowerCase().contains('morning') || c.title.contains('ጠዋት') || c.title.contains('ጥዋት'),
      orElse: () => provider.categories.isNotEmpty ? provider.categories.first : _fallbackMorningCategory(),
    );

    final eveningCat = provider.categories.firstWhere(
      (c) => c.id.toLowerCase().contains('evening') || c.title.contains('ማታ') || c.title.contains('ምሽት'),
      orElse: () => provider.categories.length > 1 ? provider.categories[1] : _fallbackEveningCategory(),
    );

    // Calculate real completed progress
    final mTotal = morningCat.items.isNotEmpty ? morningCat.items.length : 24;
    final mDone = morningCat.items.where((i) => (provider.itemCompletedCounts[i.id] ?? 0) >= i.repeat).length;
    final mProgress = mTotal > 0 ? (mDone / mTotal).clamp(0.0, 1.0) : 0.0;

    final eTotal = eveningCat.items.isNotEmpty ? eveningCat.items.length : 22;
    final eDone = eveningCat.items.where((i) => (provider.itemCompletedCounts[i.id] ?? 0) >= i.repeat).length;
    final eProgress = eTotal > 0 ? (eDone / eTotal).clamp(0.0, 1.0) : 0.0;

    final totalAzkar = mTotal + eTotal;
    final totalDone = mDone + eDone;
    final totalProgress = totalAzkar > 0 ? (totalDone / totalAzkar).clamp(0.0, 1.0) : 0.0;

    // Time-aware recommendation
    final hour = DateTime.now().hour;
    final isMorningTime = hour >= 4 && hour < 12;
    final isEveningTime = hour >= 15 && hour < 23;

    return Scaffold(
      backgroundColor: colors.bg,
      body: SafeArea(
        child: SingleChildScrollView(
          physics: const BouncingScrollPhysics(),
          padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 12.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              // ===============================================================
              // 1. APP HEADER
              // ===============================================================
              _buildHeader(context, colors),
              const SizedBox(height: 14),

              // ===============================================================
              // 2. HERO SECTION: MORNING & EVENING AZKAR (PRIMARY FOCUS)
              // ===============================================================
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Row(
                    children: [
                      Icon(Icons.auto_awesome, size: 16, color: colors.goldAccent),
                      const SizedBox(width: 6),
                      Text(
                        'ዕለታዊ አዝካር (Daily Azkar)',
                        style: TextStyle(
                          color: colors.text,
                          fontSize: 16,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ],
                  ),
                  Text(
                    'የቀኑ ዋና ተግባር',
                    style: TextStyle(
                      color: colors.goldAccent,
                      fontSize: 12,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 10),

              // CARD 1: MORNING AZKAR HERO CARD
              _buildHeroAzkarCard(
                context: context,
                colors: colors,
                titleAm: 'የጥዋት አዝካር',
                titleEn: 'Morning Azkar',
                titleAr: 'أذكار الصباح',
                totalItems: mTotal,
                completedItems: mDone,
                progress: mProgress,
                isRecommended: isMorningTime,
                isCompleted: mTotal > 0 && mDone == mTotal,
                isMorning: true,
                onTap: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (_) => AzkarListScreen(category: morningCat),
                    ),
                  );
                },
              ),
              const SizedBox(height: 12),

              // CARD 2: EVENING AZKAR HERO CARD
              _buildHeroAzkarCard(
                context: context,
                colors: colors,
                titleAm: 'የማታ አዝካር',
                titleEn: 'Evening Azkar',
                titleAr: 'أذكار المساء',
                totalItems: eTotal,
                completedItems: eDone,
                progress: eProgress,
                isRecommended: isEveningTime,
                isCompleted: eTotal > 0 && eDone == eTotal,
                isMorning: false,
                onTap: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (_) => AzkarListScreen(category: eveningCat),
                    ),
                  );
                },
              ),
              const SizedBox(height: 14),

              // ===============================================================
              // 3. DAILY AZKAR PROGRESS COMPONENT
              // ===============================================================
              _buildDailyProgressComponent(
                colors: colors,
                totalDone: totalDone,
                totalAzkar: totalAzkar,
                totalProgress: totalProgress,
                mDone: mDone,
                mTotal: mTotal,
                eDone: eDone,
                eTotal: eTotal,
              ),
              const SizedBox(height: 16),

              // ===============================================================
              // 4. SECONDARY: DAILY AYAH CARD (VISUALLY SECONDARY)
              // ===============================================================
              _buildSecondaryAyahCard(context, colors),
              const SizedBox(height: 16),

              // ===============================================================
              // 5. SECONDARY: LIFESTYLE & TOOLS GRID
              // ===============================================================
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    'ተጨማሪ አገልግሎቶች (Tools & Lifestyle)',
                    style: TextStyle(
                      color: colors.text,
                      fontSize: 14,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  Text(
                    'ቁርኣንና ዱዓ',
                    style: TextStyle(
                      color: colors.subtext,
                      fontSize: 11,
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 10),

              _buildSecondaryToolsGrid(context, colors, provider),
              const SizedBox(height: 14),

              // ===============================================================
              // 6. QUICK TASBIH WIDGET
              // ===============================================================
              _buildQuickTasbihWidget(context, colors, provider),
              const SizedBox(height: 16),
            ],
          ),
        ),
      ),
    );
  }

  // ===========================================================================
  // 1. HEADER
  // ===========================================================================
  Widget _buildHeader(BuildContext context, IslamicThemeColors colors) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
      decoration: BoxDecoration(
        color: colors.surface,
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: colors.border.withValues(alpha: 0.5)),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.04),
            blurRadius: 8,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Row(
            children: [
              Container(
                width: 42,
                height: 42,
                decoration: BoxDecoration(
                  shape: BoxShape.circle,
                  color: colors.goldAccent.withValues(alpha: 0.15),
                  border: Border.all(color: colors.goldAccent.withValues(alpha: 0.5)),
                ),
                child: Center(
                  child: Icon(Icons.mosque, size: 22, color: colors.goldAccent),
                ),
              ),
              const SizedBox(width: 12),
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'Nur Islamic Lifestyle',
                    style: TextStyle(
                      color: colors.text,
                      fontSize: 15,
                      fontWeight: FontWeight.w800,
                      letterSpacing: 0.2,
                    ),
                  ),
                  Text(
                    'አሰላሙ ዐለይኩም ወረሕመቱላህ',
                    style: TextStyle(
                      color: const Color(0xFF0E5A3E),
                      fontSize: 12,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                ],
              ),
            ],
          ),
          IconButton(
            icon: Icon(Icons.palette_outlined, color: colors.goldAccent),
            onPressed: () => showThemeDialog(context),
            tooltip: 'Theme',
          ),
        ],
      ),
    );
  }

  // ===========================================================================
  // 2. HERO AZKAR CARDS (DEEP EMERALD / FOREST GREEN WITH SUNRISE & DUSK GLOW)
  // ===========================================================================
  Widget _buildHeroAzkarCard({
    required BuildContext context,
    required IslamicThemeColors colors,
    required String titleAm,
    required String titleEn,
    required String titleAr,
    required int totalItems,
    required int completedItems,
    required double progress,
    required bool isRecommended,
    required bool isCompleted,
    required bool isMorning,
    required VoidCallback onTap,
  }) {
    final bgGradient = isMorning
        ? const LinearGradient(
            begin: Alignment.topLeft,
            end: Alignment.bottomRight,
            colors: [Color(0xFF072E23), Color(0xFF0A3C2E), Color(0xFF06251C)],
          )
        : const LinearGradient(
            begin: Alignment.topLeft,
            end: Alignment.bottomRight,
            colors: [Color(0xFF0A3326), Color(0xFF0F4433), Color(0xFF07241A)],
          );

    return InkWell(
      onTap: onTap,
      borderRadius: BorderRadius.circular(24),
      child: Container(
        decoration: BoxDecoration(
          gradient: bgGradient,
          borderRadius: BorderRadius.circular(24),
          border: Border.all(
            color: isRecommended ? colors.goldAccent : colors.goldAccent.withValues(alpha: 0.5),
            width: isRecommended ? 2.0 : 1.4,
          ),
          boxShadow: [
            BoxShadow(
              color: isRecommended
                  ? colors.goldAccent.withValues(alpha: 0.2)
                  : Colors.black.withValues(alpha: 0.15),
              blurRadius: 12,
              offset: const Offset(0, 4),
            ),
          ],
        ),
        padding: const EdgeInsets.all(18),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            // Top Badge Row: Icon + Recommendation + Item Count
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Row(
                  children: [
                    Container(
                      width: 36,
                      height: 36,
                      decoration: BoxDecoration(
                        shape: BoxShape.circle,
                        color: colors.goldAccent.withValues(alpha: 0.18),
                      ),
                      child: Icon(
                        isMorning ? Icons.wb_sunny_rounded : Icons.nightlight_round,
                        size: 20,
                        color: colors.goldAccent,
                      ),
                    ),
                    const SizedBox(width: 8),
                    if (isRecommended)
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
                        decoration: BoxDecoration(
                          color: colors.goldAccent,
                          borderRadius: BorderRadius.circular(10),
                        ),
                        child: Row(
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            Icon(
                              isMorning ? Icons.wb_sunny : Icons.nightlight_round,
                              size: 11,
                              color: Colors.black,
                            ),
                            const SizedBox(width: 4),
                            const Text(
                              'አሁን የሚመከር',
                              style: TextStyle(
                                color: Colors.black,
                                fontSize: 11,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                          ],
                        ),
                      ),
                  ],
                ),
                Container(
                  padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                  decoration: BoxDecoration(
                    color: isCompleted
                        ? const Color(0xFF4CAF50).withValues(alpha: 0.2)
                        : Colors.white.withValues(alpha: 0.12),
                    borderRadius: BorderRadius.circular(10),
                    border: Border.all(
                      color: isCompleted ? const Color(0xFF4CAF50) : Colors.white24,
                      width: 0.8,
                    ),
                  ),
                  child: Text(
                    isCompleted ? 'ተጠናቋል ✓' : '$totalItems አዝካሮች',
                    style: TextStyle(
                      color: isCompleted ? const Color(0xFF81C784) : const Color(0xFFFAF7F0),
                      fontSize: 11,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
              ],
            ),
            const SizedBox(height: 12),

            // Titles: Amharic + Arabic
            Text(
              titleAm,
              style: const TextStyle(
                color: Color(0xFFFAF7F0),
                fontSize: 20,
                fontWeight: FontWeight.w800,
              ),
            ),
            const SizedBox(height: 2),
            Row(
              children: [
                Text(
                  titleEn,
                  style: const TextStyle(
                    color: Color(0xFFC5BAA5),
                    fontSize: 12.5,
                    fontWeight: FontWeight.w500,
                  ),
                ),
                const SizedBox(width: 6),
                Text('•', style: TextStyle(color: colors.goldAccent)),
                const SizedBox(width: 6),
                Text(
                  titleAr,
                  style: TextStyle(
                    color: colors.goldAccent,
                    fontSize: 14,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 12),

            // Progress Summary & Bar
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  isCompleted
                      ? 'የዛሬው ክፍለ-ጊዜ ተጠናቋል! ($completedItems/$totalItems)'
                      : completedItems > 0
                          ? '$completedItems ተጠናቋል • ${totalItems - completedItems} ይቀራሉ'
                          : 'አልተጀመረም • $totalItems አዝካሮች ተዘጋጅተዋል',
                  style: TextStyle(
                    color: isCompleted ? const Color(0xFF81C784) : Colors.white70,
                    fontSize: 11.5,
                    fontWeight: FontWeight.w500,
                  ),
                ),
                Text(
                  '${(progress * 100).toInt()}%',
                  style: TextStyle(
                    color: colors.goldAccent,
                    fontSize: 12,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 6),
            ClipRRect(
              borderRadius: BorderRadius.circular(4),
              child: LinearProgressIndicator(
                value: progress,
                backgroundColor: Colors.white12,
                valueColor: AlwaysStoppedAnimation<Color>(colors.goldAccent),
                minHeight: 6,
              ),
            ),
            const SizedBox(height: 14),

            // Action Button
            ElevatedButton(
              onPressed: onTap,
              style: ElevatedButton.styleFrom(
                backgroundColor: isCompleted ? Colors.white12 : colors.goldAccent,
                foregroundColor: isCompleted ? colors.goldAccent : Colors.black,
                elevation: 0,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(14),
                  side: isCompleted ? BorderSide(color: colors.goldAccent) : BorderSide.none,
                ),
                padding: const EdgeInsets.symmetric(vertical: 11),
              ),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(
                    isCompleted
                        ? Icons.check_circle_outline
                        : (completedItems > 0 ? Icons.play_arrow_rounded : Icons.star_border_rounded),
                    size: 18,
                  ),
                  const SizedBox(width: 6),
                  Text(
                    isCompleted
                        ? 'አዝካሩን ከልስ (Review)'
                        : (completedItems > 0 ? 'ቀጥል (Resume • ንጥል ${completedItems + 1})' : '$titleAm ጀምር (Start)'),
                    style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 13),
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
  // 3. DAILY AZKAR PROGRESS COMPONENT
  // ===========================================================================
  Widget _buildDailyProgressComponent({
    required IslamicThemeColors colors,
    required int totalDone,
    required int totalAzkar,
    required double totalProgress,
    required int mDone,
    required int mTotal,
    required int eDone,
    required int eTotal,
  }) {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: colors.surface,
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: colors.border.withValues(alpha: 0.6)),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.03),
            blurRadius: 6,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Row(
                children: [
                  Icon(Icons.insights_rounded, size: 16, color: colors.goldAccent),
                  const SizedBox(width: 6),
                  Text(
                    'የዕለቱ አጠቃላይ የአዝካር እድገት',
                    style: TextStyle(
                      color: colors.text,
                      fontSize: 13,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ],
              ),
              Text(
                '$totalDone / $totalAzkar (${(totalProgress * 100).toInt()}%)',
                style: TextStyle(
                  color: colors.goldAccent,
                  fontSize: 13,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ],
          ),
          const SizedBox(height: 8),

          ClipRRect(
            borderRadius: BorderRadius.circular(4),
            child: LinearProgressIndicator(
              value: totalProgress,
              backgroundColor: colors.border.withValues(alpha: 0.3),
              valueColor: AlwaysStoppedAnimation<Color>(colors.goldAccent),
              minHeight: 8,
            ),
          ),
          const SizedBox(height: 12),

          Row(
            children: [
              // Morning Chip
              Expanded(
                child: Container(
                  padding: const EdgeInsets.all(10),
                  decoration: BoxDecoration(
                    color: colors.bg,
                    borderRadius: BorderRadius.circular(12),
                    border: Border.all(
                      color: mDone == mTotal && mTotal > 0
                          ? const Color(0xFF4CAF50)
                          : colors.border.withValues(alpha: 0.5),
                    ),
                  ),
                  child: Row(
                    children: [
                      Icon(
                        mDone == mTotal && mTotal > 0 ? Icons.check_circle : Icons.wb_sunny_rounded,
                        size: 16,
                        color: mDone == mTotal && mTotal > 0 ? const Color(0xFF4CAF50) : colors.goldAccent,
                      ),
                      const SizedBox(width: 6),
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              'የጥዋት አዝካር',
                              style: TextStyle(
                                color: colors.text,
                                fontSize: 11,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                            Text(
                              mDone == mTotal && mTotal > 0
                                  ? 'ተጠናቋል ($mDone/$mTotal)'
                                  : '$mDone/$mTotal በሂደት',
                              style: TextStyle(
                                color: colors.subtext,
                                fontSize: 10,
                              ),
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
              ),
              const SizedBox(width: 8),

              // Evening Chip
              Expanded(
                child: Container(
                  padding: const EdgeInsets.all(10),
                  decoration: BoxDecoration(
                    color: colors.bg,
                    borderRadius: BorderRadius.circular(12),
                    border: Border.all(
                      color: eDone == eTotal && eTotal > 0
                          ? const Color(0xFF4CAF50)
                          : colors.border.withValues(alpha: 0.5),
                    ),
                  ),
                  child: Row(
                    children: [
                      Icon(
                        eDone == eTotal && eTotal > 0 ? Icons.check_circle : Icons.nightlight_round,
                        size: 16,
                        color: eDone == eTotal && eTotal > 0 ? const Color(0xFF4CAF50) : colors.goldAccent,
                      ),
                      const SizedBox(width: 6),
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              'የማታ አዝካር',
                              style: TextStyle(
                                color: colors.text,
                                fontSize: 11,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                            Text(
                              eDone == eTotal && eTotal > 0
                                  ? 'ተጠናቋል ($eDone/$eTotal)'
                                  : '$eDone/$eTotal በሂደት',
                              style: TextStyle(
                                color: colors.subtext,
                                fontSize: 10,
                              ),
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }

  // ===========================================================================
  // 4. SECONDARY AYAH CARD (SMALLER & COMPACT)
  // ===========================================================================
  Widget _buildSecondaryAyahCard(BuildContext context, IslamicThemeColors colors) {
    return Container(
      decoration: BoxDecoration(
        color: colors.surface,
        borderRadius: BorderRadius.circular(18),
        border: Border.all(color: colors.goldAccent.withValues(alpha: 0.4)),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.02),
            blurRadius: 6,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      padding: const EdgeInsets.all(14),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Row(
                children: [
                  Icon(Icons.menu_book_rounded, size: 14, color: colors.goldAccent),
                  const SizedBox(width: 6),
                  Text(
                    'የዕለቱ የቁርኣን አንቀጽ (Daily Verse)',
                    style: TextStyle(
                      color: colors.goldAccent,
                      fontSize: 11.5,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ],
              ),
              Text(
                'ሱረቱል አል-በቀራህ • 152',
                style: TextStyle(color: colors.subtext, fontSize: 10),
              ),
            ],
          ),
          const SizedBox(height: 8),
          const Text(
            '«فَاذْكُرُونِي أَذْكُرْكُمْ وَاشْكُرُوا لِي وَلَا تَكْفُرُونِ»',
            textAlign: TextAlign.right,
            textDirection: TextDirection.rtl,
            style: TextStyle(
              fontSize: 18,
              fontWeight: FontWeight.bold,
              color: Color(0xFF0E5A3E),
              height: 1.6,
            ),
          ),
          const SizedBox(height: 6),
          const Text(
            '«አስታውሱኝ፤ አስታውሳችኋለሁና። አመስግኑኝም፤ አትካዱኝም።»',
            style: TextStyle(
              fontSize: 12.5,
              fontWeight: FontWeight.w500,
              color: Color(0xFF1E3A2F),
            ),
          ),
        ],
      ),
    );
  }

  // ===========================================================================
  // 5. SECONDARY TOOLS GRID (QURAN, DUAS, 99 NAMES, PRAYER TIMES, QIBLA)
  // ===========================================================================
  Widget _buildSecondaryToolsGrid(
    BuildContext context,
    IslamicThemeColors colors,
    AzkarProvider provider,
  ) {
    return Column(
      children: [
        Row(
          children: [
            Expanded(
              child: _buildSecondaryToolItem(
                colors: colors,
                title: 'ቅዱስ ቁርኣን',
                subtitle: '114 ሱራዎች',
                icon: Icons.menu_book,
                onTap: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (_) => const DuasScreen()),
                  );
                },
              ),
            ),
            const SizedBox(width: 10),
            Expanded(
              child: _buildSecondaryToolItem(
                colors: colors,
                title: 'ዕለታዊ ዱዓዎች',
                subtitle: 'የተመረጡ ዱዓዎች',
                icon: Icons.volunteer_activism_outlined,
                onTap: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (_) => const DuasScreen()),
                  );
                },
              ),
            ),
          ],
        ),
        const SizedBox(height: 10),
        Row(
          children: [
            Expanded(
              child: _buildSecondaryToolItem(
                colors: colors,
                title: '99 የአላህ ስሞች',
                subtitle: 'አስማኡል ሁስና',
                icon: Icons.auto_awesome,
                onTap: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (_) => const NamesOfAllahScreen()),
                  );
                },
              ),
            ),
            const SizedBox(width: 10),
            Expanded(
              child: _buildSecondaryToolItem(
                colors: colors,
                title: 'ዲጂታል ተስቢሕ',
                subtitle: '${provider.tasbihCount} / ${provider.tasbihTarget}',
                icon: Icons.touch_app,
                onTap: () => widget.onOpenTasbih?.call(),
              ),
            ),
          ],
        ),
      ],
    );
  }

  Widget _buildSecondaryToolItem({
    required IslamicThemeColors colors,
    required String title,
    required String subtitle,
    required IconData icon,
    required VoidCallback onTap,
  }) {
    return InkWell(
      onTap: onTap,
      borderRadius: BorderRadius.circular(16),
      child: Container(
        padding: const EdgeInsets.all(12),
        decoration: BoxDecoration(
          color: colors.surface,
          borderRadius: BorderRadius.circular(16),
          border: Border.all(color: colors.border.withValues(alpha: 0.5)),
        ),
        child: Row(
          children: [
            Container(
              width: 34,
              height: 34,
              decoration: BoxDecoration(
                shape: BoxShape.circle,
                color: colors.goldAccent.withValues(alpha: 0.15),
              ),
              child: Icon(icon, size: 18, color: colors.goldAccent),
            ),
            const SizedBox(width: 8),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    title,
                    style: TextStyle(
                      color: colors.text,
                      fontSize: 12,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  Text(
                    subtitle,
                    style: TextStyle(
                      color: colors.subtext,
                      fontSize: 10,
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
  // 6. QUICK TASBIH WIDGET
  // ===========================================================================
  Widget _buildQuickTasbihWidget(
    BuildContext context,
    IslamicThemeColors colors,
    AzkarProvider provider,
  ) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
      decoration: BoxDecoration(
        color: colors.surface,
        borderRadius: BorderRadius.circular(18),
        border: Border.all(color: colors.border.withValues(alpha: 0.5)),
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'ፈጣን ተስቢሕ (Quick Tasbih)',
                style: TextStyle(
                  color: colors.text,
                  fontSize: 13,
                  fontWeight: FontWeight.bold,
                ),
              ),
              Text(
                '${provider.tasbihPreset} • ${provider.tasbihCount} ከ ${provider.tasbihTarget}',
                style: TextStyle(
                  color: colors.subtext,
                  fontSize: 11,
                ),
              ),
            ],
          ),
          InkWell(
            onTap: () => provider.incrementTasbih(),
            borderRadius: BorderRadius.circular(22),
            child: Container(
              width: 44,
              height: 44,
              decoration: BoxDecoration(
                shape: BoxShape.circle,
                color: colors.goldAccent,
              ),
              child: const Icon(Icons.touch_app, color: Colors.black, size: 22),
            ),
          ),
        ],
      ),
    );
  }

  static AzkarCategory _fallbackMorningCategory() {
    return AzkarCategory(
      id: 'morning',
      title: 'የጥዋት አዝካር',
      subtitle: 'በጠዋት ለማንበብ (ከፈጅር በኋላ)',
      icon: 'sun',
      items: [],
    );
  }

  static AzkarCategory _fallbackEveningCategory() {
    return AzkarCategory(
      id: 'evening',
      title: 'የማታ አዝካር',
      subtitle: 'ከአስር በኋላ እስከ ፀሐይ መጥለቅ',
      icon: 'moon',
      items: [],
    );
  }
}
