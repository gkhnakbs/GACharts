package com.gkhnakbs.gcharts.charts.core


import androidx.compose.runtime.Stable

/**
 * Created by Gökhan Akbaş on 09/12/2025.
 */
@Stable
data class LineChartData(
    val points: List<DataPoint>,
    val xAxisLabels: List<String> = emptyList(),
    val yAxisLabels: List<String> = emptyList()
) {
    init {
        require(points.isNotEmpty()) { "Points list cannot be empty" }
    }

    // Lazy ile değişken kullanılmak istendiğinde hesaplanır ve tekrar tekrar hesaplanmaz
    val minX: Float by lazy { points.minOf { it.x } }
    val maxX: Float by lazy { points. maxOf { it.x } }
    val minY: Float by lazy { points.minOf { it.y } }
    val maxY: Float by lazy { points. maxOf { it.y } }

    // Y ekseninde biraz padding bırakmak için
    val yRange: Float by lazy {
        val range = maxY - minY
        if (range == 0f) 1f else range
    }

    val xRange: Float by lazy {
        val range = maxX - minX
        if (range == 0f) 1f else range
    }
}