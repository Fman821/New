import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';

import 'package:nur_azkar/main.dart';
import 'package:nur_azkar/providers/azkar_provider.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    SharedPreferences.setMockInitialValues({});
  });

  testWidgets('application loads its main navigation', (tester) async {
    await tester.pumpWidget(
      ChangeNotifierProvider(
        create: (_) => AzkarProvider(),
        child: const AmharicAzkarApp(),
      ),
    );

    // Allow asynchronous preferences and bundled JSON assets to load.
    await tester.pump(const Duration(milliseconds: 100));
    await tester.pump(const Duration(milliseconds: 500));
    await tester.pump(const Duration(milliseconds: 500));

    expect(find.byType(Scaffold), findsOneWidget);
    expect(find.text('Amharic Azkar'), findsOneWidget);
    expect(find.text('ዋና ገጽ'), findsOneWidget);
  });
}
