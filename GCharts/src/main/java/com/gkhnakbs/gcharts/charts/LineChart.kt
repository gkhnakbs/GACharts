package com.gkhnakbs.gcharts.charts


import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.gkhnakbs.gcharts.charts.animation.ChartAnimationConfig
import com.gkhnakbs.gcharts.charts.animation.rememberChartAnimationState
import com.gkhnakbs.gcharts.charts.config.LineChartConfig
import com.gkhnakbs.gcharts.charts.config.LineChartStyle
import com.gkhnakbs.gcharts.charts.core.AxisCalculator
import com.gkhnakbs.gcharts.charts.core.ChartPadding
import com.gkhnakbs.gcharts.charts.core.CoordinateMapper
import com.gkhnakbs.gcharts.charts.core.LineChartData
import com.gkhnakbs.gcharts.charts.renderer.AxisRenderer.drawAxes
import com.gkhnakbs.gcharts.charts.renderer.GridRenderer.drawGrid
import com.gkhnakbs.gcharts.charts.renderer.LabelRenderer.drawXAxisLabels
import com.gkhnakbs.gcharts.charts.renderer.LabelRenderer.drawYAxisLabels
import com.gkhnakbs.gcharts.charts.renderer.LineRenderer.drawLine
import com.gkhnakbs.gcharts.charts.renderer.PointRenderer.drawPoints
import com.gkhnakbs.gcharts.charts.renderer.PointRenderer.drawPopup
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * Created by Gökhan Akbaş on 09/12/2025.
 */

//@Composable
//fun LineChart(
//    data: LineChartData,
//    modifier: Modifier = Modifier,
//    config: LineChartConfig = LineChartConfig(),
//    style: LineChartStyle = LineChartStyle(),
//    padding: ChartPadding = ChartPadding(),
//) {
//    val density = LocalDensity.current
//
//    // Animasyon config'i oluştur
//    val animationConfig = remember(config) {
//        ChartAnimationConfig(
//            enabled = config.animationEnabled,
//            duration = config.animationDuration,
//            type = config.animationType,
//            easing = config.animationEasing,
//            delayMs = config.animationDelayMs,
//            staggeredDelay = config.staggeredPointDelay
//        )
//    }
//
//    // Animasyon state'i
//    val animationState = rememberChartAnimationState(
//        key = data,
//        config = animationConfig
//    )
//
//    // Axis tick'lerini hesapla (sadece data değiştiğinde)
//    val yAxisTicks = remember(data) {
//        AxisCalculator.calculateYAxisTicks(
//            minValue = data.minY,
//            maxValue = data.maxY,
//            desiredTickCount = config.horizontalGridLines,
//            padding = with(density) { padding.start.toPx() }
//        )
//    }
//
//    val xAxisTicks = remember(data) {
//        AxisCalculator.calculateXAxisTicks(
//            minValue = data.minX,
//            maxValue = data.maxX,
//            desiredTickCount = config.verticalGridLines,
//            padding = with(density) { padding.bottom.toPx() }
//        )
//    }
//
//    var zoom by remember { mutableFloatStateOf(1f) }
//    // Yatay kaydırma miktarı
//    var offsetX by remember { mutableFloatStateOf(0f) }
//    // Maksimum zoom limiti (isteğe bağlı ayarlanabilir)
//    val maxZoom = 5f
//
//    Canvas(
//        modifier = modifier
//            .fillMaxSize()
//            .background(style.backgroundColor)
//            .pointerInput(Unit) {
//                detectTransformGestures { _, pan, zoomChange, _ ->
//                    val oldZoom = zoom
//                    zoom = (zoom * zoomChange).coerceIn(1f, maxZoom)
//                    offsetX += pan.x
//                }
//            }
//            .clipToBounds()
//    ) {
//
//        val virtualWidth = size.width * zoom
//
//        val maxScrollOffset = -(virtualWidth - size.width)
//        offsetX = offsetX.coerceIn(maxScrollOffset, 0f)
//
//        val actualMapper = CoordinateMapper.create(
//            canvasWidth = virtualWidth,
//            canvasHeight = size.height,
//            padding = padding,
//            density = density,
//            data = data,
//            yAxisTicks = yAxisTicks,
//            xAxisTicks = xAxisTicks,
//            axisLabelWidthDp = config.axisLabelWidthDp
//        )
//
//        // Noktaları canvas koordinatlarına çevir
//        val canvasPoints: List<Offset> = actualMapper.mapAllPoints()
//
//        // Değerleri pixel'e çevir
//        val lineWidthPx = with(density) { config.lineWidth.toPx() }
//        val pointRadiusPx = with(density) { config.pointRadius.toPx() }
//        val pointStrokeWidthPx = with(density) { config.pointStrokeWidth.toPx() }
//        val gridStrokeWidthPx = with(density) { config.gridStrokeWidth.toPx() }
//        val axisStrokeWidthPx = with(density) { config.axisStrokeWidth.toPx() }
//        val labelTextSizePx = with(density) { style.labelTextStyle.fontSize.toPx() }
//
//        // 1. Grid çiz (en altta)
//
//        clipRect(
//            left = actualMapper.drawableStartX,
//            top = actualMapper.drawableStartY,
//            right = actualMapper.drawableEndX,
//            bottom = actualMapper.drawableEndY
//        ) {
//            translate(left = offsetX) {
//                if (config.showGrid) {
//                    drawGrid(
//                        mapper = actualMapper,
//                        gridColor = config.gridColor,
//                        gridStrokeWidth = gridStrokeWidthPx,
//                        horizontalLines = yAxisTicks.labels.lastIndex,
//                        verticalLines = xAxisTicks.labels.lastIndex
//                    )
//                }
//            }
//        }
//
//        // 2. X Ekseni Label'ları
//        // X label'ları da Y ekseni label'larının üzerine binmesin diye soldan kırpılabilir
//        clipRect(
//            left = actualMapper.drawableStartX,
//            right = size.width
//        ) {
//            translate(left = offsetX) {
//                drawXAxisLabels(
//                    mapper = actualMapper,
//                    ticks = xAxisTicks,
//                    textColor = style.labelTextStyle.color,
//                    textSize = labelTextSizePx,
//                )
//            }
//        }
//
//        // 3. Eksen Çizgileri (Sabit kalırlar)
//        if (config.showAxes) {
//            drawAxes(
//                mapper = actualMapper,
//                axisColor = config.axisColor,
//                axisStrokeWidth = axisStrokeWidthPx
//            )
//        }
//
//        // 4. ve 5. ANA ÇİZGİ VE NOKTALAR (Esas istediğin kısım burası)
//        // Burada clipRect kullanarak çizimi sadece grafik alanına hapsediyoruz.
//        clipRect(
//            left = actualMapper.drawableStartX, // Y ekseni label'larının bittiği yer
//            top = 0f,             // Veya paddingTopPx
//            right = actualMapper.drawableEndX,
//            bottom = actualMapper.drawableEndY // X ekseni label'larının başladığı yer
//        ) {
//            translate(left = offsetX) {
//
//                // Ana Çizgi
//                drawSmoothLine(
//                    points = canvasPoints,
//                    color = config.lineColor.copy(alpha = animationState.alpha),
//                    strokeWidth = lineWidthPx,
//                    strokeCap = config.lineCap,
//                    animationProgress = animationState.progress
//                )
//
//                // Noktalar
//                if (config.showPoints) {
//                    drawCustomPoints(
//                        points = canvasPoints,
//                        animationProgress = animationState.progress,
//                        drawableHeight = size.height
//                    ) { index, center, scale ->
//                        val currentRadius = pointRadiusPx * scale
//
//                        // Görünürlük kontrolü (clipRect olsa da performans için bu kontrol kalmalı)
//                        val isVisible = (center.x + offsetX) > -50 && (center.x + offsetX) < size.width + 50
//
//                        if (isVisible) {
//                            drawCircle(color = Color.White, radius = currentRadius, center = center)
//                            drawCircle(
//                                color = Color.Blue,
//                                radius = currentRadius,
//                                center = center,
//                                style = Stroke(width = pointStrokeWidthPx)
//                            )
//                        }
//                    }
//                }
//            }
//        }
//
//        // Y Ekseni Label'ları (En üste çizilir, clipRect dışında olmalı)
//        drawYAxisLabels(
//            mapper = actualMapper,
//            ticks = yAxisTicks,
//            textColor = style.labelTextStyle.color,
//            textSize = labelTextSizePx,
//        )
//    }
//}

@Composable
fun LineChart(
    data: LineChartData,
    modifier: Modifier = Modifier,
    config: LineChartConfig = LineChartConfig(),
    style: LineChartStyle = LineChartStyle(),
    padding: ChartPadding = ChartPadding(),
) {
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer() // Popup içine yazı yazmak için

    // --- STATE YÖNETİMİ ---

    // 1. Animasyon Config ve State
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
    val animationState = rememberChartAnimationState(key = data, config = animationConfig)

    // 2. Axis Hesaplamaları
    val yAxisTicks = remember(data) {
        AxisCalculator.calculateYAxisTicks(data.minY, data.maxY, config.horizontalGridLines)
    }
    val xAxisTicks = remember(data) {
        AxisCalculator.calculateXAxisTicks(data.minX, data.maxX, config.verticalGridLines)
    }

    // 3. Zoom ve Pan State'leri
    var zoom by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    val maxZoom = 5f

    // 4. Etkileşim State'leri (YENİ)
    // Hangi nokta seçili?
    var selectedPointIndex by remember { mutableIntStateOf(-1) }

    // Dokunma olaylarında kullanmak için hesaplanan noktaları saklamamız gerek
    // (Draw scope dışında erişebilmek için)
    var currentRenderedPoints by remember { mutableStateOf<List<Offset>>(emptyList()) }

    // Popup açılış animasyonu
    val popupAnimProgress by animateFloatAsState(
        targetValue = if (selectedPointIndex != -1) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "PopupAnim"
    )

    Spacer(
        modifier = modifier
            .fillMaxSize()
            .background(style.backgroundColor)
            .clipToBounds()
            .pointerInput(Unit) {
                coroutineScope {
                    // 1. Tıklama Algılayıcı (SADECE X EKSENİ KONTROLÜ)
                    launch {
                        detectTapGestures { tapOffset ->
                            var nearestIndex = -1
                            // En küçük X mesafesini takip etmek için
                            var minDistanceX = Float.MAX_VALUE

                            // X ekseninde ne kadar uzağı kabul edeceğiz? (Örn: Sağdan soldan 40dp)
                            val touchThresholdX = 40.dp.toPx()

                            currentRenderedPoints.forEachIndexed { index, point ->
                                // Noktanın ekrandaki kaydırılmış X konumu
                                val screenX = point.x + offsetX

                                // Yüksekliği önemsemiyoruz, sadece yatay mesafeye bakıyoruz
                                val distanceX = kotlin.math.abs(screenX - tapOffset.x)

                                // 1. Mesafe tolerans içinde mi?
                                // 2. Şu ana kadar bulduğumuz en yakın noktadan daha mı yakın?
                                if (distanceX < touchThresholdX && distanceX < minDistanceX) {
                                    minDistanceX = distanceX
                                    nearestIndex = index
                                }
                            }

                            // Seçili noktayı güncelle
                            selectedPointIndex =
                                if (selectedPointIndex == nearestIndex) -1 else nearestIndex
                        }
                    }

                    // 2. Zoom ve Pan Algılayıcı (Aynı kalıyor)
                    launch {
                        detectTransformGestures { _, pan, zoomChange, _ ->
                            zoom = (zoom * zoomChange).coerceIn(1f, maxZoom)
                            offsetX += pan.x
                        }
                    }
                }
            }
            .drawWithCache {
                val pointRadiusPx = config.pointRadius.toPx()
                val pointStrokeWidthPx = config.pointStrokeWidth.toPx()
                val gridStrokeWidthPx = config.gridStrokeWidth.toPx()
                val axisStrokeWidthPx = config.axisStrokeWidth.toPx()
                val labelTextSizePx = style.labelTextStyle.fontSize.toPx()

                onDrawBehind {
                    val virtualWidth = size.width * zoom  // Zoom'lu genişlik
                    val maxScrollOffset = -(virtualWidth - size.width)
                    val currentOffsetX = offsetX.coerceIn(maxScrollOffset, 0f)

                    val actualMapper = CoordinateMapper.create(
                        canvasWidth = virtualWidth,
                        canvasHeight = size.height,
                        padding = padding,
                        density = density,
                        data = data,
                        yAxisTicks = yAxisTicks,
                        xAxisTicks = xAxisTicks,
                        axisLabelWidthDp = config.axisLabelWidthDp
                    )

                    val canvasPoints = actualMapper.mapAllPoints()
                    currentRenderedPoints = canvasPoints

                    clipRect(
                        left = actualMapper.drawableStartX,
                        top = actualMapper.drawableStartY,
                        right = actualMapper.drawableEndX,
                        bottom = actualMapper.drawableEndY
                    ) {
                        translate(left = currentOffsetX) {
                            if (config.showGrid) {
                                drawGrid(
                                    mapper = actualMapper,
                                    gridColor = config.gridColor,
                                    gridStrokeWidth = gridStrokeWidthPx,
                                    horizontalLines = yAxisTicks.labels.lastIndex,
                                    verticalLines = xAxisTicks.labels.size
                                )
                            }
                        }
                    }

                    clipRect(
                        left = actualMapper.drawableStartX - (5.dp.toPx()), // Biraz pay bırak
                        right = actualMapper.drawableEndX + (5.dp.toPx())
                    ) {
                        translate(left = currentOffsetX) {
                            drawXAxisLabels(
                                mapper = actualMapper,
                                ticks = xAxisTicks,
                                textColor = style.labelTextStyle.color,
                                textSize = labelTextSizePx
                            )
                        }
                    }

                    if (config.showAxes) {
                        drawAxes(
                            mapper = actualMapper,
                            axisColor = config.axisColor,
                            axisStrokeWidth = axisStrokeWidthPx
                        )
                    }

                    clipRect(
                        left = actualMapper.drawableStartX,
                        top = 0f,
                        right = actualMapper.drawableEndX,
                        bottom = actualMapper.drawableEndY + pointRadiusPx
                    ) {
                        translate(left = currentOffsetX) {

                            drawLine(
                                points = canvasPoints,
                                config = config,
                                animationState = animationState,
                                mapper = actualMapper,
                            )


                            if (config.showPoints) {
                                drawPoints(
                                    points = canvasPoints,
                                    pointColor = config.pointColor,
                                    pointRadius = pointRadiusPx,
                                    pointStrokeWidth = pointStrokeWidthPx,
                                    pointFillColor = style.backgroundColor,
                                    animationProgress = animationState.progress
                                )
                            }

                            if (selectedPointIndex != -1 || popupAnimProgress > 0f) {
                                val activePoint = canvasPoints.getOrNull(selectedPointIndex)

                                val popupText = activePoint?.let {
                                    String.format(
                                        Locale.getDefault(),
                                        "%.1f",
                                        data.points.getOrNull(selectedPointIndex)?.y ?: 0f
                                    )
                                } ?: ""

                                drawPopup(
                                    selectedPoint = activePoint,
                                    popupValue = popupText,
                                    textMeasurer = textMeasurer,
                                    popupAnimationProgress = popupAnimProgress
                                )
                            }
                        }
                    }

                    drawYAxisLabels(
                        mapper = actualMapper,
                        ticks = yAxisTicks,
                        textColor = style.labelTextStyle.color,
                        textSize = labelTextSizePx
                    )
                }
            }
    )
}
