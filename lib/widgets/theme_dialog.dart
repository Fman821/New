import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../models/azkar_models.dart';
import '../providers/azkar_provider.dart';

void showThemeDialog(BuildContext context) {
  showDialog(
    context: context,
    builder: (ctx) {
      final provider = ctx.watch<AzkarProvider>();
      return AlertDialog(
        title: const Text('የመተግበሪያውን ገጽታ ይምረጡ'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: AppThemeMode.values.map((mode) {
            final isSelected = mode == provider.currentTheme;
            return RadioListTile<AppThemeMode>(
              value: mode,
              groupValue: provider.currentTheme,
              title: Text(mode.titleAm, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14)),
              subtitle: Text(mode.descAm, style: const TextStyle(fontSize: 12)),
              onChanged: (val) {
                if (val != null) {
                  provider.setTheme(val);
                  Navigator.pop(ctx);
                }
              },
            );
          }).toList(),
        ),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx), child: const Text('ዝጋ')),
        ],
      );
    },
  );
}
