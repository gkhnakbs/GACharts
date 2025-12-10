package com.gkhnakbs.gcharts.charts.renderer

/**
 * Created by Gökhan Akbaş on 09/12/2025.
 */

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui. graphics.Path
import androidx.compose.ui. graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

object LineRenderer {

    fun DrawScope.drawLine(
        points: List<Offset>,
        color: Color,
        strokeWidth: Float,
        strokeCap: StrokeCap,
        animationProgress: Float = 1f
    ) {
        if (points.size < 2) return

        val path = Path().apply {
            moveTo(points. first().x, points.first().y)

            // Animasyonlu çizim için kaç nokta gösterileceğini hesapla
            val visiblePointCount = (points.size * animationProgress).toInt().coerceAtLeast(1)

            for (i in 1 until visiblePointCount) {
                lineTo(points[i].x, points[i].y)
            }

            // Son nokta için interpolasyon (smooth animasyon)
            if (animationProgress < 1f && visiblePointCount < points.size) {
                val fraction = (points.size * animationProgress) - visiblePointCount
                if (fraction > 0 && visiblePointCount < points.size) {
                    val currentPoint = points[visiblePointCount - 1]
                    val nextPoint = points[visiblePointCount]
                    val interpolatedX = currentPoint.x + (nextPoint.x - currentPoint. x) * fraction
                    val interpolatedY = currentPoint.y + (nextPoint.y - currentPoint.y) * fraction
                    lineTo(interpolatedX, interpolatedY)
                }
            }
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(
                width = strokeWidth,
                cap = strokeCap
            )
        )
    }
}