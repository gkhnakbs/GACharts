package com.gkhnakbs.gcharts.charts.core


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Density

/**
 * Created by Gökhan Akbaş on 09/12/2025.
 */
class CoordinateMapper(
    private val canvasWidth: Float,
    private val canvasHeight: Float,
    private val paddingStartPx: Float,
    private val paddingEndPx: Float,
    private val paddingTopPx: Float,
    private val paddingBottomPx: Float,
    private val data: LineChartData,
    private val yAxisLabelCount : Int,
    private val xAxisLabelCount : Int,
    private val yAxisMaxValue : Float,
    private val yAxisMinValue : Float,
    private val xAxisMaxValue : Float,
    private val xAxisMinValue : Float,
) {
    // Çizilebilir alan boyutları
    val drawableWidth: Float = canvasWidth - paddingStartPx - paddingEndPx
    val drawableHeight: Float = canvasHeight - paddingTopPx - paddingBottomPx

    // Çizilebilir alanın başlangıç noktası
    val drawableStartX: Float = paddingStartPx
    val drawableStartY: Float = paddingTopPx
    val drawableEndX: Float = canvasWidth - paddingEndPx
    val drawableEndY: Float = canvasHeight - paddingBottomPx

    val yAxisLabelValueRange by lazy {
        yAxisMaxValue - yAxisMinValue
    }
    val xAxisLabelValueRange by lazy {
        xAxisMaxValue - xAxisMinValue
    }


    /**
     * DataPoint'i Canvas koordinatına çevirir
     */
    fun dataToCanvas(point: DataPoint): Offset {
        val x = paddingStartPx + ((point.x - data.minX) / data.xRange) * drawableWidth

        // Canvas'ta Y ekseni ters (yukarı = 0, aşağı = max)
        // Veri koordinatında yukarı = max olmalı
        // minY -> drawableEndY (en aşağı), maxY -> drawableStartY (en yukarı
        val y = drawableEndY - ((point.y / yAxisLabelValueRange) * drawableHeight)

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
        return drawableEndY - ((yValue - data.minY) / data.yRange) * drawableHeight
    }

    /**
     * X değerini Canvas X koordinatına çevirir
     */
    fun xValueToCanvas(xValue: Float): Float {
        return paddingStartPx + ((xValue - data.minX) / data.xRange) * drawableWidth
    }

    companion object {
        fun create(
            canvasWidth: Float,
            canvasHeight: Float,
            padding: ChartPadding,
            density: Density,
            data: LineChartData,
            yAxisLabelCount : Int,
            xAxisLabelCount : Int,
            yAxisMaxValue : Float,
            yAxisMinValue : Float,
            xAxisMaxValue : Float,
            xAxisMinValue : Float,
        ): CoordinateMapper {
            with(density) {
                return CoordinateMapper(
                    canvasWidth = canvasWidth,
                    canvasHeight = canvasHeight,
                    paddingStartPx = padding.start.toPx(),
                    paddingEndPx = padding.end.toPx(),
                    paddingTopPx = padding.top.toPx(),
                    paddingBottomPx = padding.bottom.toPx(),
                    data = data,
                    yAxisLabelCount=yAxisLabelCount,
                    xAxisLabelCount=xAxisLabelCount,
                    yAxisMaxValue=yAxisMaxValue,
                    yAxisMinValue=yAxisMinValue,
                    xAxisMaxValue=xAxisMaxValue,
                    xAxisMinValue=xAxisMinValue
                )
            }
        }
    }
}