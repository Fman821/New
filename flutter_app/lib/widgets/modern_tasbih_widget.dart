import 'dart:math';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/azkar_provider.dart';
import '../theme/app_theme.dart';

class ModernTasbihWidget extends StatelessWidget {
  const ModernTasbihWidget({super.key});

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<AzkarProvider>();
    final colors = IslamicThemeColors.fromMode(provider.currentTheme);

    final presets = [
      "ሱብሓነላህ",
      "አልሐምዱሊላህ",
      "አላሁ አክበር",
      "ላ ኢላሃ ኢለላህ",
      "አስተግፊሩላህ",
      "ሶለዋት ዐላ ነቢይ",
    ];

    final targets = [33, 99, 100, 1000];

    return SingleChildScrollView(
      padding: const EdgeInsets.all(16),
      child: Column(
        children: [
          SizedBox(
            height: 38,
            child: ListView.separated(
              scrollDirection: Axis.horizontal,
              itemCount: presets.length,
              separatorBuilder: (_, __) => const SizedBox(width: 8),
              itemBuilder: (context, i) {
                final isSelected = presets[i] == provider.tasbihPreset;
                return ChoiceChip(
                  label: Text(presets[i], style: const TextStyle(fontSize: 12)),
                  selected: isSelected,
                  selectedColor: colors.goldAccent,
                  labelStyle: TextStyle(
                    color: isSelected ? Colors.black : colors.text,
                    fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
                  ),
                  onSelected: (_) => provider.setTasbihPreset(presets[i]),
                );
              },
            ),
          ),
          const SizedBox(height: 16),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Row(
                children: targets.map((t) {
                  final isSelected = t == provider.tasbihTarget;
                  return Padding(
                    padding: const EdgeInsets.only(right: 6),
                    child: OutlinedButton(
                      style: OutlinedButton.styleFrom(
                        backgroundColor: isSelected ? colors.goldAccent : Colors.transparent,
                        foregroundColor: isSelected ? Colors.black : colors.text,
                        side: BorderSide(color: colors.border),
                        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 0),
                        minimumSize: const Size(40, 32),
                      ),
                      onPressed: () => provider.setTasbihTarget(t),
                      child: Text('$t'),
                    ),
                  );
                }).toList(),
              ),
              Row(
                children: [
                  IconButton(
                    icon: Icon(
                      provider.vibrationEnabled ? Icons.vibration : Icons.phone_android,
                      color: provider.vibrationEnabled ? colors.goldAccent : colors.subtext,
                    ),
                    onPressed: provider.toggleVibration,
                  ),
                  IconButton(
                    icon: Icon(Icons.refresh, color: colors.subtext),
                    onPressed: provider.resetTasbih,
                  ),
                ],
              ),
            ],
          ),
          const SizedBox(height: 30),
          GestureDetector(
            onTap: provider.incrementTasbih,
            child: SizedBox(
              width: 250,
              height: 250,
              child: CustomPaint(
                painter: _TasbihPainter(
                  progress: provider.tasbihCount / provider.tasbihTarget,
                  activeColor: colors.goldAccent,
                  trackColor: colors.border.withOpacity(0.3),
                ),
                child: Center(
                  child: Container(
                    width: 190,
                    height: 190,
                    decoration: BoxDecoration(
                      color: colors.surface,
                      shape: BoxShape.circle,
                      boxShadow: [
                        BoxShadow(
                          color: Colors.black.withOpacity(0.1),
                          blurRadius: 10,
                          offset: const Offset(0, 4),
                        ),
                      ],
                    ),
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Text(
                          provider.tasbihPreset,
                          style: TextStyle(
                            color: colors.arabicText,
                            fontSize: 16,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        const SizedBox(height: 6),
                        Text(
                          '${provider.tasbihCount}',
                          style: TextStyle(
                            color: colors.text,
                            fontSize: 46,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        Text(
                          'ዒላማ: ${provider.tasbihTarget}',
                          style: TextStyle(
                            color: colors.goldAccent,
                            fontSize: 12,
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              ),
            ),
          ),
          const SizedBox(height: 30),
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 14),
            decoration: BoxDecoration(
              color: colors.surface,
              borderRadius: BorderRadius.circular(16),
              border: Border.all(color: colors.border.withOpacity(0.4)),
            ),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                Column(
                  children: [
                    Text('ዙር (Laps)', style: TextStyle(color: colors.subtext, fontSize: 12)),
                    Text(
                      '${provider.tasbihLaps}',
                      style: TextStyle(color: colors.goldAccent, fontSize: 20, fontWeight: FontWeight.bold),
                    ),
                  ],
                ),
                Container(height: 30, width: 1, color: colors.border.withOpacity(0.5)),
                Column(
                  children: [
                    Text('አጠቃላይ ድምር', style: TextStyle(color: colors.subtext, fontSize: 12)),
                    Text(
                      '${provider.tasbihTotal}',
                      style: TextStyle(color: colors.text, fontSize: 20, fontWeight: FontWeight.bold),
                    ),
                  ],
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

class _TasbihPainter extends CustomPainter {
  final double progress;
  final Color activeColor;
  final Color trackColor;

  _TasbihPainter({
    required this.progress,
    required this.activeColor,
    required this.trackColor,
  });

  @override
  void paint(Canvas canvas, Size size) {
    final center = Offset(size.width / 2, size.height / 2);
    final radius = (size.width - 20) / 2;

    final trackPaint = Paint()
      ..color = trackColor
      ..style = PaintingStyle.stroke
      ..strokeWidth = 12;

    final activePaint = Paint()
      ..color = activeColor
      ..style = PaintingStyle.stroke
      ..strokeCap = StrokeCap.round
      ..strokeWidth = 12;

    canvas.drawCircle(center, radius, trackPaint);
    canvas.drawArc(
      Rect.fromCircle(center: center, radius: radius),
      -pi / 2,
      2 * pi * progress.clamp(0.0, 1.0),
      false,
      activePaint,
    );
  }

  @override
  bool shouldRepaint(covariant _TasbihPainter oldDelegate) {
    return oldDelegate.progress != progress;
  }
}
