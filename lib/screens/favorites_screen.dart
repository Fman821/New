import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../models/azkar_models.dart';
import '../providers/azkar_provider.dart';
import '../widgets/azkar_card.dart';

class FavoritesScreen extends StatelessWidget {
  const FavoritesScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<AzkarProvider>();

    final allItems = [
      ...provider.categories.expand((c) => c.items),
      ...provider.duas.map((d) => AzkarItem(
        id: 'dua_${d.id}',
        title: d.title,
        ar: d.ar,
        am: d.am,
        repeat: d.repeat,
        source: d.source,
      )),
    ];

    final favs = allItems.where((i) => provider.favorites.contains(i.id)).toList();

    if (favs.isEmpty) {
      return const Center(
        child: Text('ምንም የተመረጠ አዝካር የለም\n(ኮከብ ምልክቱን በመጫን እዚህ ማስቀመጥ ይችላሉ)', textAlign: TextAlign.center),
      );
    }

    return ListView.separated(
      padding: const EdgeInsets.all(12),
      itemCount: favs.length,
      separatorBuilder: (_, __) => const SizedBox(height: 10),
      itemBuilder: (context, i) => AzkarCard(item: favs[i]),
    );
  }
}
