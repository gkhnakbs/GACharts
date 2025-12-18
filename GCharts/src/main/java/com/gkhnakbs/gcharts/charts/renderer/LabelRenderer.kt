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
        ticks: AxisCalculator.AxisTicks,
        textColor: Color,
        textSize: Float,
        defaultDistanceToYAxis : Float = 30f
    ) {
        val paint = android.graphics.Paint().apply {
            color = textColor.hashCode()
            this.textSize = textSize
            textAlign = android.graphics.Paint.Align.CENTER
        }

        // Metin metriklerini kullanarak doğru dikey ortalama hesapla
        val fontMetrics = paint.fontMetrics
        val textVerticalOffset = (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent

        val gridGapY = mapper.drawableHeight / (ticks.labels.size-1)
        ticks.values.forEachIndexed { index, value ->
            val y = mapper.yValueToCanvas(value)
            val label = ticks.labels[index]

            // Label'ın grafiğin dışına çıkmaması için sınırla
            val clampedY = mapper.drawableEndY - (index * gridGapY).coerceAtMost(mapper.drawableHeight)

            drawContext.canvas.nativeCanvas.drawText(
                label,
                mapper.drawableStartX - defaultDistanceToYAxis,
                clampedY+textVerticalOffset,
                paint
            )
        }
    }

    fun DrawScope.drawXAxisLabels(
        mapper:  CoordinateMapper,
        ticks: AxisCalculator.AxisTicks,
        textColor: Color,
        textSize:  Float
    ) {
        val paint = android.graphics.Paint().apply {
            color = textColor.hashCode()
            this.textSize = textSize
            textAlign = android.graphics.Paint.Align.CENTER
        }

        ticks.values.forEachIndexed { index, value ->
            val x = mapper.xValueToCanvas(value)
            val label = ticks.labels[index]

            drawContext.canvas.nativeCanvas.drawText(
                label,
                x,
                mapper.drawableEndY + textSize + 8f,
                paint
            )
        }
    }
}