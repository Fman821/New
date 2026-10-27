package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/**
 * Custom Shape that draws a classic Islamic Mihrab Arch at the top.
 */
val IslamicArchShape = GenericShape { size, _ ->
    val w = size.width
    val h = size.height
    val archHeight = h * 0.25f

    // Start bottom-left
    moveTo(0f, h)
    // Up left edge
    lineTo(0f, archHeight)
    // Left shoulder
    cubicTo(0f, archHeight * 0.5f, w * 0.25f, 0f, w * 0.5f, 0f)
    // Right shoulder
    cubicTo(w * 0.75f, 0f, w, archHeight * 0.5f, w, archHeight)
    // Down right edge
    lineTo(w, h)
    close()
}

/**
 * Islamic Star Pattern / Geometric divider for cards and headers.
 */
@Composable
fun IslamicGeometricDivider(
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(18.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val midY = size.height / 2
            val midX = size.width / 2

            // Center small 8-pointed star
            val starSize = 7f
            val starPath = Path().apply {
                moveTo(midX, midY - starSize)
                lineTo(midX + starSize * 0.4f, midY - starSize * 0.4f)
                lineTo(midX + starSize, midY)
                lineTo(midX + starSize * 0.4f, midY + starSize * 0.4f)
                lineTo(midX, midY + starSize)
                lineTo(midX - starSize * 0.4f, midY + starSize * 0.4f)
                lineTo(midX - starSize, midY)
                lineTo(midX - starSize * 0.4f, midY - starSize * 0.4f)
                close()
            }
            drawPath(path = starPath, color = color)

            // Left line with fade
            drawLine(
                color = color.copy(alpha = 0.5f),
                start = androidx.compose.ui.geometry.Offset(size.width * 0.15f, midY),
                end = androidx.compose.ui.geometry.Offset(midX - starSize - 12f, midY),
                strokeWidth = 1.2f
            )

            // Right line with fade
            drawLine(
                color = color.copy(alpha = 0.5f),
                start = androidx.compose.ui.geometry.Offset(midX + starSize + 12f, midY),
                end = androidx.compose.ui.geometry.Offset(size.width * 0.85f, midY),
                strokeWidth = 1.2f
            )
        }
    }
}
