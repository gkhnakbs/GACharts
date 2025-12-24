package com.gkhnakbs.gcharts.charts.core


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp

/**
 * Created by Gökhan Akbaş on 09/12/2025.
 */
data class CoordinateMapper(
    private val canvasWidth: Float,
    private val canvasHeight: Float,
    val axisLabelWidthPx : Float,
    val paddingStartPx: Float,
    val paddingEndPx: Float,
    val paddingTopPx: Float,
    val paddingBottomPx: Float,
    private val data: LineChartData,
    val yAxisTicks: AxisCalculator.AxisTicks,
    val xAxisTicks: AxisCalculator.AxisTicks,
) {
    // Çizilebilir alan boyutları
    val drawableWidth: Float = canvasWidth - paddingStartPx - paddingEndPx - axisLabelWidthPx
    val drawableHeight: Float = canvasHeight - paddingTopPx - paddingBottomPx - axisLabelWidthPx

    // Çizilebilir alanın başlangıç noktası
    val drawableStartX: Float = paddingStartPx + axisLabelWidthPx
    val drawableStartY: Float = paddingTopPx
    val drawableEndX: Float = canvasWidth - paddingEndPx
    val drawableEndY: Float = canvasHeight - paddingBottomPx - axisLabelWidthPx

    val yAxisLabelValueRange by lazy {
        yAxisTicks.maxLabelValue - yAxisTicks.minLabelValue
    }
    val xAxisLabelValueRange by lazy {
        xAxisTicks.maxLabelValue - xAxisTicks.minLabelValue
    }

    /**
     * DataPoint'i Canvas koordinatına çevirir
     */
    fun dataToCanvas(point: DataPoint): Offset {
        // X: minLabelValue'dan başlayarak normalize et
        val x = drawableStartX + ((point.x - xAxisTicks.minLabelValue) / xAxisLabelValueRange) * drawableWidth

        // Canvas'ta Y ekseni ters (yukarı = 0, aşağı = max)
        // Veri koordinatında yukarı = max olmalı
        // minLabelValue -> drawableEndY (en aşağı), maxLabelValue -> drawableStartY (en yukarı)
        val y = drawableEndY - ((point.y - yAxisTicks.minLabelValue) / yAxisLabelValueRange) * drawableHeight

        return Offset(x, y)
    }

    /**
     * Tüm noktaları Canvas koordinatlarına çevirir
     */
    fun mapAllPoints(): List<Offset> {
        return data.points.map { dataToCanvas(it) }
    }

    /**
     * Y değerini Canvas Y koordinatına çevirir
     */
    fun yValueToCanvas(yValue: Float): Float {
        return drawableEndY - ((yValue - yAxisTicks.minLabelValue) / yAxisLabelValueRange) * drawableHeight
    }

    /**
     * X değerini Canvas X koordinatına çevirir
     */
    fun xValueToCanvas(xValue: Float): Float {
        return paddingStartPx + ((xValue - xAxisTicks.minLabelValue) / xAxisLabelValueRange) * drawableWidth
    }

    companion object {
        fun create(
            canvasWidth: Float,
            canvasHeight: Float,
            padding: ChartPadding,
            axisLabelWidthDp : Dp,
            density: Density,
            data: LineChartData,
            yAxisTicks: AxisCalculator.AxisTicks,
            xAxisTicks: AxisCalculator.AxisTicks,
        ): CoordinateMapper {
            with(density) {
                return CoordinateMapper(
                    canvasWidth = canvasWidth,
                    canvasHeight = canvasHeight,
                    paddingStartPx = padding.start.toPx(),
                    paddingEndPx = (padding.end + 10.dp).coerceAtLeast(10.dp).toPx(),
                    paddingTopPx = (padding.top + 10.dp).coerceAtLeast(10.dp).toPx(),
                    paddingBottomPx = padding.bottom.toPx(),
                    data = data,
                    yAxisTicks = yAxisTicks,
                    xAxisTicks = xAxisTicks,
                    axisLabelWidthPx = axisLabelWidthDp.toPx()
                )
            }
        }
    }
}