package com.gkhnakbs.gcharts.charts.renderer

/**
 * Created by Gökhan Akbaş on 09/12/2025.
 */

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.gkhnakbs.gcharts.charts.core.CoordinateMapper

object GridRenderer {

    fun DrawScope.drawGrid(
        mapper: CoordinateMapper,
        gridColor: Color,
        gridStrokeWidth: Float,
        horizontalLines: Int,
        verticalLines: Int,
    ) {
        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

        // Yatay çizgiler (horizontalLines + 1 tane çizgi çizmek için)
        for (i in 0..horizontalLines) {
            val y = mapper.drawableStartY + (mapper.drawableHeight / horizontalLines) * i

            drawLine(
                color = gridColor,
                start = androidx.compose.ui.geometry.Offset(mapper.drawableStartX, y),
                end = androidx.compose.ui.geometry.Offset(mapper.drawableEndX, y),
                strokeWidth = gridStrokeWidth,
                pathEffect = dashEffect
            )
        }

        // Dikey çizgiler (verticalLines + 1 tane çizgi çizmek için)
        for (i in 0..verticalLines) {
            val x = mapper.drawableStartX + (mapper.drawableWidth / verticalLines) * i

            drawLine(
                color = gridColor,
                start = androidx.compose.ui.geometry.Offset(x, mapper.drawableStartY),
                end = androidx.compose.ui.geometry.Offset(x, mapper.drawableEndY),
                strokeWidth = gridStrokeWidth,
                pathEffect = dashEffect
            )
        }
    }
}