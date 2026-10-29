package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

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

/**
 * Elegant Islamic Emblem Motif:
 * Displays a delicate Islamic geometric 8-pointed star and crescent moon emblem.
 */
@Composable
fun IslamicEmblemMotif(
    primaryColor: Color,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val center = androidx.compose.ui.geometry.Offset(size.width / 2f, size.height / 2f)
        val radius = size.minDimension / 2f

        // Outer ornate circle
        drawCircle(
            color = accentColor.copy(alpha = 0.25f),
            radius = radius * 0.95f,
            style = Stroke(width = 1.5f)
        )

        // Inner dashed ring
        drawCircle(
            color = accentColor.copy(alpha = 0.4f),
            radius = radius * 0.78f,
            style = Stroke(width = 1f)
        )

        // 8-Pointed Star (Rub el Hizb)
        val starPath = Path()
        val numPoints = 8
        for (i in 0 until numPoints * 2) {
            val angle = i * PI / numPoints - PI / 2
            val r = if (i % 2 == 0) radius * 0.65f else radius * 0.40f
            val x = center.x + r * cos(angle).toFloat()
            val y = center.y + r * sin(angle).toFloat()
            if (i == 0) starPath.moveTo(x, y) else starPath.lineTo(x, y)
        }
        starPath.close()

        drawPath(
            path = starPath,
            color = accentColor,
            style = Stroke(width = 1.8f)
        )

        // Center jewel dot
        drawCircle(
            color = accentColor,
            radius = radius * 0.14f
        )
    }
}

/**
 * Islamic Number Badge (Rub el Hizb) used for Ayah numbers or Surah indexes.
 */
@Composable
fun IslamicNumberBadge(
    number: Int,
    badgeColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(36.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val center = androidx.compose.ui.geometry.Offset(size.width / 2f, size.height / 2f)
            val radius = size.minDimension / 2f

            val starPath = Path()
            val numPoints = 8
            for (i in 0 until numPoints * 2) {
                val angle = i * PI / numPoints - PI / 2
                val r = if (i % 2 == 0) radius * 0.9f else radius * 0.65f
                val x = center.x + r * cos(angle).toFloat()
                val y = center.y + r * sin(angle).toFloat()
                if (i == 0) starPath.moveTo(x, y) else starPath.lineTo(x, y)
            }
            starPath.close()

            drawPath(
                path = starPath,
                color = badgeColor.copy(alpha = 0.16f)
            )
            drawPath(
                path = starPath,
                color = badgeColor,
                style = Stroke(width = 1.2f)
            )
        }
        Text(
            text = "$number",
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}
