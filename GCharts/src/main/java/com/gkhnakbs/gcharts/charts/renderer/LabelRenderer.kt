package com.gkhnakbs.gcharts.charts.renderer


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import com.gkhnakbs.gcharts.charts.core.AxisCalculator
import com.gkhnakbs.gcharts.charts.core.CoordinateMapper

/**
 * Created by Gökhan Akbaş on 10/12/2025.
 */
object LabelRenderer {

    fun DrawScope.drawYAxisLabels(
        mapper: CoordinateMapper,
        ticks: AxisCalculator. AxisTicks,
        textColor: Color,
        textSize: Float
    ) {
        val paint = android.graphics.Paint().apply {
            color = textColor. hashCode()
            this.textSize = textSize
            textAlign = android.graphics.Paint.Align. RIGHT
        }

        ticks.values.forEachIndexed { index, value ->
            val y = mapper.yValueToCanvas(value)
            val label = ticks.labels[index]

            drawContext.canvas.nativeCanvas.drawText(
                label,
                mapper.drawableStartX - 8f,
                y + textSize / 3, // Dikey ortalama
                paint
            )
        }
    }

    fun DrawScope.drawXAxisLabels(
        mapper:  CoordinateMapper,
        ticks: AxisCalculator. AxisTicks,
        textColor: Color,
        textSize:  Float
    ) {
        val paint = android.graphics.Paint().apply {
            color = textColor.hashCode()
            this.textSize = textSize
            textAlign = android.graphics.Paint. Align.CENTER
        }

        ticks.values. forEachIndexed { index, value ->
            val x = mapper.xValueToCanvas(value)
            val label = ticks. labels[index]

            drawContext.canvas.nativeCanvas.drawText(
                label,
                x,
                mapper. drawableEndY + textSize + 8f,
                paint
            )
        }
    }
}