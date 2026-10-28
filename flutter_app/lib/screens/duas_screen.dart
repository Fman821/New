import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../models/azkar_models.dart';
import '../providers/azkar_provider.dart';
import '../widgets/azkar_card.dart';

class DuasScreen extends StatefulWidget {
  const DuasScreen({super.key});

  @override
  State<DuasScreen> createState() => _DuasScreenState();
}

class _DuasScreenState extends State<DuasScreen> {
  String query = '';

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<AzkarProvider>();

    final filtered = provider.duas.where((d) {
      if (query.isEmpty) return true;
      final q = query.toLowerCase();
      return d.title.toLowerCase().contains(q) || d.am.toLowerCase().contains(q);
    }).map((d) => AzkarItem(
      id: 'dua_${d.id}',
      title: d.title,
      ar: d.ar,
      am: d.am,
      repeat: d.repeat,
      source: d.source,
    )).toList();

    return Column(
      children: [
        Padding(
          padding: const EdgeInsets.all(12),
          child: TextField(
            decoration: InputDecoration(
              hintText: 'በዱዓው ርዕስ ወይም ትርጉም ይፈልጉ...',
              prefixIcon: const Icon(Icons.search),
              border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
            ),
            onChanged: (val) => setState(() => query = val),
          ),
        ),
        Expanded(
          child: ListView.separated(
            padding: const EdgeInsets.symmetric(horizontal: 12),
            itemCount: filtered.length,
            separatorBuilder: (_, __) => const SizedBox(height: 10),
            itemBuilder: (context, i) => AzkarCard(item: filtered[i]),
          ),
        ),
      ],
    );
  }
}
