package com.gkhnakbs.gcharts.charts.renderer

/**
 * Created by Gökhan Akbaş on 09/12/2025.
 */

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics. drawscope.DrawScope
import androidx.compose.ui.graphics. drawscope.Stroke

object PointRenderer {

    fun DrawScope.drawPoints(
        points: List<Offset>,
        pointColor: Color,
        pointRadius: Float,
        pointStrokeWidth: Float,
        pointFillColor: Color,
        animationProgress: Float = 1f
    ) {
        val visiblePointCount = (points. size * animationProgress).toInt()

        for (i in 0 until visiblePointCount) {
            val point = points[i]

            // İç dolgu (beyaz)
            drawCircle(
                color = pointFillColor,
                radius = pointRadius,
                center = point
            )

            // Dış çerçeve (renkli)
            drawCircle(
                color = pointColor,
                radius = pointRadius,
                center = point,
                style = Stroke(width = pointStrokeWidth)
            )
        }
    }
}