import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../models/azkar_models.dart';
import '../providers/azkar_provider.dart';
import '../theme/app_theme.dart';
import '../widgets/azkar_card.dart';

class AzkarListScreen extends StatefulWidget {
  final AzkarCategory category;

  const AzkarListScreen({super.key, required this.category});

  @override
  State<AzkarListScreen> createState() => _AzkarListScreenState();
}

class _AzkarListScreenState extends State<AzkarListScreen> {
  String query = '';

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<AzkarProvider>();
    final colors = IslamicThemeColors.fromMode(provider.currentTheme);

    final filtered = widget.category.items.where((i) {
      if (query.isEmpty) return true;
      final q = query.toLowerCase();
      return i.title.toLowerCase().contains(q) || i.am.toLowerCase().contains(q) || i.ar.contains(q);
    }).toList();

    return Scaffold(
      appBar: AppBar(
        title: Text(widget.category.title),
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(12),
            child: TextField(
              decoration: InputDecoration(
                hintText: 'በአዝካሩ ስም ወይም ትርጉም ይፈልጉ...',
                prefixIcon: Icon(Icons.search, color: colors.goldAccent),
                filled: true,
                fillColor: colors.surface,
                border: OutlineInputBorder(borderRadius: BorderRadius.circular(12), borderSide: BorderSide.none),
              ),
              onChanged: (val) => setState(() => query = val),
            ),
          ),
          Expanded(
            child: ListView.separated(
              padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
              itemCount: filtered.length,
              separatorBuilder: (_, __) => const SizedBox(height: 10),
              itemBuilder: (context, i) => AzkarCard(item: filtered[i]),
            ),
          ),
        ],
      ),
    );
  }
}
