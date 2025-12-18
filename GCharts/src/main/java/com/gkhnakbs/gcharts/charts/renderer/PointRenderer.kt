package com.gkhnakbs.gcharts.charts.renderer

/**
 * Created by Gökhan Akbaş on 09/12/2025.
 */

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

object PointRenderer {

    fun DrawScope.drawPoints(
        points: List<Offset>,
        pointColor: Color,
        pointRadius: Float,
        pointStrokeWidth: Float,
        pointFillColor: Color,
        animationProgress: Float = 1f,
        slideOffset: Float = 0f,
        drawableHeight: Float = 0f
    ) {
        val visiblePointCount = (points.size * animationProgress).toInt()

        for (i in 0 until visiblePointCount) {
            val point = points[i]

            // SlideUp animasyonu için Y offset hesapla
            val animatedY = if (slideOffset > 0f && drawableHeight > 0f) {
                point.y + (drawableHeight * slideOffset)
            } else {
                point.y
            }

            val animatedPoint = Offset(point.x, animatedY)

            // İç dolgu (beyaz)
            drawCircle(
                color = pointFillColor,
                radius = pointRadius,
                center = animatedPoint
            )

            // Dış çerçeve (renkli)
            drawCircle(
                color = pointColor,
                radius = pointRadius,
                center = animatedPoint,
                style = Stroke(width = pointStrokeWidth)
            )
        }
    }
}