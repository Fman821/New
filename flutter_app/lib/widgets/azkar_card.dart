import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:share_plus/share_plus.dart';
import 'package:audioplayers/audioplayers.dart';
import '../models/azkar_models.dart';
import '../providers/azkar_provider.dart';
import '../theme/app_theme.dart';
import 'islamic_divider.dart';

class AzkarCard extends StatelessWidget {
  final AzkarItem item;

  const AzkarCard({super.key, required this.item});

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<AzkarProvider>();
    final colors = IslamicThemeColors.fromMode(provider.currentTheme);
    final count = provider.itemCompletedCounts[item.id] ?? 0;
    final isDone = count >= item.repeat;
    final isFav = provider.favorites.contains(item.id);
    final isPlaying = provider.currentlyPlayingUrl == item.audio &&
        provider.playerState == PlayerState.playing;

    return Card(
      elevation: colors.isGlass ? 0 : 2,
      color: colors.surface,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
      child: Container(
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(16),
          border: Border.all(
            color: isDone ? colors.goldAccent : colors.border.withOpacity(0.5),
            width: isDone ? 1.5 : 1,
          ),
        ),
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Row(
              children: [
                Container(
                  padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                  decoration: BoxDecoration(
                    color: colors.goldAccent.withOpacity(0.15),
                    borderRadius: BorderRadius.circular(8),
                  ),
                  child: Text(
                    '${item.repeat}x',
                    style: TextStyle(
                      color: colors.goldAccent,
                      fontWeight: FontWeight.bold,
                      fontSize: 12,
                    ),
                  ),
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: Text(
                    item.title,
                    style: TextStyle(
                      color: colors.text,
                      fontWeight: FontWeight.bold,
                      fontSize: 16,
                    ),
                  ),
                ),
                if (item.audio.isNotEmpty)
                  IconButton(
                    icon: Icon(
                      isPlaying ? Icons.pause_circle_filled : Icons.play_circle_fill,
                      color: colors.goldAccent,
                      size: 26,
                    ),
                    onPressed: () => provider.toggleAudio(item.audio),
                  ),
                IconButton(
                  icon: Icon(
                    isFav ? Icons.star : Icons.star_border,
                    color: isFav ? colors.goldAccent : colors.subtext,
                  ),
                  onPressed: () => provider.toggleFavorite(item.id),
                ),
                IconButton(
                  icon: Icon(Icons.share, color: colors.subtext, size: 20),
                  onPressed: () {
                    Share.share(
                      '${item.title}\n\n${item.ar}\n\nትርጉም:\n${item.am}\n\n[Nur Azkar - ኑሩል አዝካር]',
                    );
                  },
                ),
              ],
            ),
            const SizedBox(height: 12),
            Text(
              item.ar,
              textAlign: TextAlign.right,
              textDirection: TextDirection.rtl,
              style: TextStyle(
                color: colors.arabicText,
                fontSize: 22 * provider.arabicFontScale,
                height: 1.8,
                fontWeight: FontWeight.w600,
              ),
            ),
            IslamicGeometricDivider(color: colors.goldAccent),
            if (item.translit.isNotEmpty) ...[
              Text(
                item.translit,
                style: TextStyle(
                  color: colors.goldAccent,
                  fontStyle: FontStyle.italic,
                  fontSize: 13,
                ),
              ),
              const SizedBox(height: 6),
            ],
            Text(
              item.am,
              style: TextStyle(color: colors.text, fontSize: 14, height: 1.5),
            ),
            const SizedBox(height: 12),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  item.source.isNotEmpty ? 'ምንጭ: ${item.source}' : '',
                  style: TextStyle(color: colors.subtext, fontSize: 11),
                ),
                InkWell(
                  onTap: () => provider.incrementItemCount(item.id, item.repeat),
                  borderRadius: BorderRadius.circular(20),
                  child: Container(
                    padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 6),
                    decoration: BoxDecoration(
                      color: isDone ? colors.goldAccent : colors.goldAccent.withOpacity(0.15),
                      borderRadius: BorderRadius.circular(20),
                    ),
                    child: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        if (isDone) ...[
                          const Icon(Icons.check, size: 16, color: Colors.black),
                          const SizedBox(width: 4),
                        ],
                        Text(
                          isDone ? 'ተጠናቋል ($count/${item.repeat})' : '$count / ${item.repeat} ደግሙ',
                          style: TextStyle(
                            color: isDone ? Colors.black : colors.goldAccent,
                            fontWeight: FontWeight.bold,
                            fontSize: 12,
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
      ),
    );
  }
}
