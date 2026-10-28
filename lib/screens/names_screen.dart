import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/azkar_provider.dart';
import '../theme/app_theme.dart';

class NamesOfAllahScreen extends StatefulWidget {
  const NamesOfAllahScreen({super.key});

  @override
  State<NamesOfAllahScreen> createState() => _NamesOfAllahScreenState();
}

class _NamesOfAllahScreenState extends State<NamesOfAllahScreen> {
  String query = '';

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<AzkarProvider>();
    final colors = IslamicThemeColors.fromMode(provider.currentTheme);

    final filtered = provider.names.where((n) {
      if (query.isEmpty) return true;
      final q = query.toLowerCase();
      return n.ar.contains(q) || n.tr.toLowerCase().contains(q) || n.am.toLowerCase().contains(q);
    }).toList();

    return Column(
      children: [
        Padding(
          padding: const EdgeInsets.all(12),
          child: TextField(
            decoration: InputDecoration(
              hintText: 'በስሙ ወይም በትርጉሙ ይፈልጉ...',
              prefixIcon: Icon(Icons.search, color: colors.goldAccent),
              border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
            ),
            onChanged: (val) => setState(() => query = val),
          ),
        ),
        Expanded(
          child: GridView.builder(
            padding: const EdgeInsets.all(12),
            gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
              crossAxisCount: 2,
              crossAxisSpacing: 10,
              mainAxisSpacing: 10,
              childAspectRatio: 1.1,
            ),
            itemCount: filtered.length,
            itemBuilder: (context, i) {
              final item = filtered[i];
              return Card(
                color: colors.surface,
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
                child: Padding(
                  padding: const EdgeInsets.all(10),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      CircleAvatar(
                        radius: 12,
                        backgroundColor: colors.goldAccent.withValues(alpha: 0.2),
                        child: Text('${item.n}', style: TextStyle(fontSize: 10, color: colors.goldAccent, fontWeight: FontWeight.bold)),
                      ),
                      const SizedBox(height: 4),
                      Text(item.ar, style: TextStyle(color: colors.arabicText, fontSize: 20, fontWeight: FontWeight.bold)),
                      Text(item.tr, style: TextStyle(color: colors.goldAccent, fontSize: 12, fontWeight: FontWeight.w600)),
                      const SizedBox(height: 4),
                      Text(item.am, textAlign: TextAlign.center, maxLines: 2, overflow: TextOverflow.ellipsis, style: TextStyle(color: colors.text, fontSize: 11)),
                    ],
                  ),
                ),
              );
            },
          ),
        ),
      ],
    );
  }
}
