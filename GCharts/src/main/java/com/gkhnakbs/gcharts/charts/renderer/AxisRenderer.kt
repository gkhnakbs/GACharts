package com.gkhnakbs.gcharts.charts.renderer

/**
 * Created by Gökhan Akbaş on 09/12/2025.
 */

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.gkhnakbs.gcharts.charts.core.CoordinateMapper

object AxisRenderer {

    fun DrawScope.drawAxes(
        mapper: CoordinateMapper,
        axisColor: Color,
        axisStrokeWidth: Float,
    ) {
        // Y ekseni (sol)
        drawLine(
            color = axisColor,
            start = Offset(mapper.drawableStartX, mapper.drawableStartY),
            end = Offset(mapper.drawableStartX, mapper.drawableEndY),
            strokeWidth = axisStrokeWidth
        )

        // X ekseni (alt)
        drawLine(
            color = axisColor,
            start = Offset(mapper.drawableStartX, mapper.drawableEndY),
            end = Offset(mapper.drawableEndX, mapper.drawableEndY),
            strokeWidth = axisStrokeWidth
        )
    }
}