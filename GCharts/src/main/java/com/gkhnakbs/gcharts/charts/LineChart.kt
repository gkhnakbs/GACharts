package com.gkhnakbs.gcharts.charts


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gkhnakbs.gcharts.charts.animation.ChartAnimationConfig
import com.gkhnakbs.gcharts.charts.animation.rememberChartAnimationState
import com.gkhnakbs.gcharts.charts.config.LineChartConfig
import com.gkhnakbs.gcharts.charts.config.LineChartStyle
import com.gkhnakbs.gcharts.charts.core.AxisCalculator
import com.gkhnakbs.gcharts.charts.core.ChartPadding
import com.gkhnakbs.gcharts.charts.core.CoordinateMapper
import com.gkhnakbs.gcharts.charts.core.DataPoint
import com.gkhnakbs.gcharts.charts.core.LineChartData
import com.gkhnakbs.gcharts.charts.renderer.AxisRenderer.drawAxes
import com.gkhnakbs.gcharts.charts.renderer.GridRenderer.drawGrid
import com.gkhnakbs.gcharts.charts.renderer.LabelRenderer.drawXAxisLabels
import com.gkhnakbs.gcharts.charts.renderer.LabelRenderer.drawYAxisLabels
import com.gkhnakbs.gcharts.charts.renderer.LineRenderer.drawLine
import com.gkhnakbs.gcharts.charts.renderer.PointRenderer.drawPoints

/**
 * Created by Gökhan Akbaş on 09/12/2025.
 */

@Composable
fun LineChart(
    data: LineChartData,
    modifier: Modifier = Modifier,
    config: LineChartConfig = LineChartConfig(),
    style: LineChartStyle = LineChartStyle(),
    padding: ChartPadding = ChartPadding(),
) {
    val density = LocalDensity.current

    // Animasyon config'i oluştur
    val animationConfig = remember(config) {
        ChartAnimationConfig(
            enabled = config.animationEnabled,
            duration = config.animationDuration,
            type = config.animationType,
            easing = config.animationEasing,
            delayMs = config.animationDelayMs,
            staggeredDelay = config.staggeredPointDelay
        )
    }

    // Animasyon state'i
    val animationState = rememberChartAnimationState(
        key = data,
        config = animationConfig
    )

    // Axis tick'lerini hesapla (sadece data değiştiğinde)
    val yAxisTicks = remember(data) {
        AxisCalculator.calculateYAxisTicks(
            minValue = data.minY,
            maxValue = data.maxY,
            desiredTickCount = config.horizontalGridLines
        )
    }

    val xAxisTicks = remember(data) {
        AxisCalculator.calculateXAxisTicks(
            minValue = data.minX,
            maxValue = data.maxX,
            desiredTickCount = config.verticalGridLines
        )
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(style.backgroundColor)
    ) {
        // Canvas boyutlarıyla mapper'ı oluştur (data değiştiğinde yeniden hesapla)
        val actualMapper = CoordinateMapper.create(
            canvasWidth = size.width,
            canvasHeight = size.height,
            padding = padding,
            density = density,
            data = data,
            yAxisTicks = yAxisTicks,
            xAxisTicks = xAxisTicks
        )

        // Noktaları canvas koordinatlarına çevir
        val canvasPoints: List<Offset> = actualMapper.mapAllPoints()

        // Değerleri pixel'e çevir
        val lineWidthPx = with(density) { config.lineWidth.toPx() }
        val pointRadiusPx = with(density) { config.pointRadius.toPx() }
        val pointStrokeWidthPx = with(density) { config.pointStrokeWidth.toPx() }
        val gridStrokeWidthPx = with(density) { config.gridStrokeWidth.toPx() }
        val axisStrokeWidthPx = with(density) { config.axisStrokeWidth.toPx() }
        val labelTextSizePx = with(density) { style.labelTextStyle.fontSize.toPx() }

        // 1. Grid çiz (en altta)
        if (config.showGrid) {
            drawGrid(
                mapper = actualMapper,
                gridColor = config.gridColor,
                gridStrokeWidth = gridStrokeWidthPx,
                horizontalLines = yAxisTicks.labels.lastIndex,
                verticalLines = xAxisTicks.labels.lastIndex
            )
        }

        // 2. Eksenleri çiz
        if (config.showAxes) {
            drawAxes(
                mapper = actualMapper,
                axisColor = config.axisColor,
                axisStrokeWidth = axisStrokeWidthPx
            )
        }

        // 3. Y ekseni label'ları
        drawYAxisLabels(
            mapper = actualMapper,
            ticks = yAxisTicks,
            textColor = style.labelTextStyle.color,
            textSize = labelTextSizePx
        )

        // 4. X ekseni label'ları
        drawXAxisLabels(
            mapper = actualMapper,
            ticks = xAxisTicks,
            textColor = style.labelTextStyle.color,
            textSize = labelTextSizePx
        )

        // 5. Çizgiyi çiz
        drawLine(
            points = canvasPoints,
            color = config.lineColor.copy(alpha = animationState.alpha),
            strokeWidth = lineWidthPx,
            strokeCap = config.lineCap,
            animationProgress = animationState.progress,
            revealProgress = animationState.revealProgress
        )

        // 6. Noktaları çiz (en üstte)
        if (config.showPoints) {
            drawPoints(
                points = canvasPoints,
                pointColor = config.pointColor.copy(alpha = animationState.alpha),
                pointRadius = pointRadiusPx * animationState.scale,
                pointStrokeWidth = pointStrokeWidthPx,
                pointFillColor = config.pointFillColor.copy(alpha = animationState.alpha),
                animationProgress = animationState.progress,
                slideOffset = animationState.slideOffset,
                drawableHeight = actualMapper.drawableHeight
            )
        }
    }
}

// ==================== PREVIEW ====================

@Preview(showBackground = true, widthDp = 350, heightDp = 250)
@Composable
private fun LineChartPreview() {
    val sampleData = LineChartData(
        points = listOf(
            DataPoint(0f, 10f),
            DataPoint(1f, 25f),
            DataPoint(2f, 18f),
            DataPoint(3f, 35f),
            DataPoint(4f, 28f),
            DataPoint(5f, 45f),
            DataPoint(6f, 38f)
        )
    )

    LineChart(
        data = sampleData,
        config = LineChartConfig(
            animationEnabled = false // Preview'da animasyon kapalı
        ),
        modifier = Modifier.fillMaxSize()
    )
}

@Preview(showBackground = true, widthDp = 350, heightDp = 250)
@Composable
private fun LineChartCustomStylePreview() {
    val sampleData = LineChartData(
        points = listOf(
            DataPoint(0f, 100f),
            DataPoint(1f, 250f),
            DataPoint(2f, 180f),
            DataPoint(3f, 350f),
            DataPoint(4f, 280f),
            DataPoint(5f, 450f)
        )
    )

    LineChart(
        data = sampleData,
        config = LineChartConfig(
            lineColor = Color(0xFFE91E63),
            pointColor = Color(0xFFE91E63),
            lineWidth = 3.dp,
            pointRadius = 6.dp,
            showGrid = true,
            animationEnabled = false
        ),
        modifier = Modifier.fillMaxSize()
    )
}