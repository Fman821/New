import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/azkar_provider.dart';
import '../theme/app_theme.dart';
import 'azkar_list_screen.dart';

class HomeScreen extends StatelessWidget {
  final VoidCallback onOpenTasbih;

  const HomeScreen({super.key, required this.onOpenTasbih});

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<AzkarProvider>();
    final colors = IslamicThemeColors.fromMode(provider.currentTheme);

    return ListView(
      padding: const EdgeInsets.all(16),
      children: [
        Card(
          color: colors.surface,
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
          child: Padding(
            padding: const EdgeInsets.all(16),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text('بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ',
                        style: TextStyle(color: colors.arabicText, fontWeight: FontWeight.bold, fontSize: 16)),
                    Container(
                      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
                      decoration: BoxDecoration(
                        color: colors.goldAccent.withOpacity(0.15),
                        borderRadius: BorderRadius.circular(8),
                      ),
                      child: Text('የእለቱ ማስታወሻ', style: TextStyle(color: colors.goldAccent, fontSize: 11)),
                    ),
                  ],
                ),
                const SizedBox(height: 8),
                Text(
                  '«وَالذَّاكِرِينَ اللَّهَ كَثِيرًا وَالذَّاكِرَاتِ أَعَدَّ اللَّهُ لَهُم مَّغْفِرَةً وَأَجْرًا عَظِيمًا»',
                  textAlign: TextAlign.right,
                  textDirection: TextDirection.rtl,
                  style: TextStyle(color: colors.arabicText, fontSize: 16, height: 1.6),
                ),
                const SizedBox(height: 6),
                Text(
                  '«አላህን በብዙ የሚያወሱ ወንዶችና ሴቶች፤ አላህ ለእነርሱ ምሕረትንና ታላቅ ምንዳን አዘጋጅቶላቸዋል፡፡»',
                  style: TextStyle(color: colors.text, fontSize: 13),
                ),
              ],
            ),
          ),
        ),
        const SizedBox(height: 14),
        Card(
          color: colors.surface,
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
          child: ListTile(
            title: Text('ፈጣን ተስቢሕ ቆጣሪ', style: TextStyle(color: colors.text, fontWeight: FontWeight.bold)),
            subtitle: Text('ሱብሓነላህ • ዒላማ: ${provider.tasbihTarget}', style: TextStyle(color: colors.subtext, fontSize: 12)),
            trailing: GestureDetector(
              onTap: provider.incrementTasbih,
              child: CircleAvatar(
                radius: 24,
                backgroundColor: colors.goldAccent,
                child: Text('${provider.tasbihCount}', style: const TextStyle(color: Colors.black, fontWeight: FontWeight.bold)),
              ),
            ),
            onTap: onOpenTasbih,
          ),
        ),
        const SizedBox(height: 16),
        Text('የአዝካር ክፍሎች', style: TextStyle(color: colors.text, fontWeight: FontWeight.bold, fontSize: 16)),
        const SizedBox(height: 8),
        ...provider.categories.map((cat) {
          return Card(
            color: colors.surface,
            margin: const EdgeInsets.only(bottom: 10),
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
            child: ListTile(
              leading: CircleAvatar(
                backgroundColor: colors.goldAccent.withOpacity(0.15),
                child: Icon(Icons.wb_sunny, color: colors.goldAccent),
              ),
              title: Text(cat.title, style: TextStyle(color: colors.text, fontWeight: FontWeight.bold)),
              subtitle: Text('${cat.subtitle}\n${cat.items.length} አዝካሮች', style: TextStyle(color: colors.subtext, fontSize: 12)),
              trailing: Icon(Icons.chevron_right, color: colors.subtext),
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (_) => AzkarListScreen(category: cat)),
                );
              },
            ),
          );
        }),
      ],
    );
  }
}
