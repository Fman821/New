import 'package:flutter/material.dart';

class IslamicGeometricDivider extends StatelessWidget {
  final Color color;

  const IslamicGeometricDivider({super.key, required this.color});

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: 20,
      child: Center(
        child: Row(
          children: [
            Expanded(child: Divider(color: color.withValues(alpha: 0.3), thickness: 1)),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 10),
              child: Icon(Icons.star, size: 10, color: color),
            ),
            Expanded(child: Divider(color: color.withValues(alpha: 0.3), thickness: 1)),
          ],
        ),
      ),
    );
  }
}
