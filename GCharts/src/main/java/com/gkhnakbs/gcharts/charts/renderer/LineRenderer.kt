package com.gkhnakbs.gcharts.charts.renderer

/**
 * Created by Gökhan Akbaş on 09/12/2025.
 */
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import com.gkhnakbs.gcharts.charts.animation.ChartAnimationState
import com.gkhnakbs.gcharts.charts.config.LineChartConfig
import com.gkhnakbs.gcharts.charts.config.LineShadowConfig
import com.gkhnakbs.gcharts.charts.core.CoordinateMapper

object LineRenderer {

    // Path nesnelerini tekrar tekrar oluşturmamak için bu fonksiyon içinde scope dahilinde oluşturuyoruz.
    // Ancak DrawScope stateless olduğu için her çağrıda new Path() kaçınılmazdır,
    // fakat logic'i temizleyerek gereksiz ara listeleri kaldırdık.

    fun DrawScope.drawLine(
        points: List<Offset>,
        config: LineChartConfig,
        animationState: ChartAnimationState,
        mapper: CoordinateMapper
    ) {
        if (points.size < 2) return

        val path = Path()

        // 1. Path Oluşturma Aşaması
        if (config.smoothLine) {
            buildSmoothPath(path, points, config.smoothLineCoefficient)
        } else {
            buildBasicPath(path, points)
        }

        // 2. Animasyon ve Çizim Aşaması
        drawAnimatedPath(
            originalPath = path,
            points = points, // Sadece başlangıç/bitiş X koordinatları için gerekli
            config = config,
            animationState = animationState,
            baselineY = mapper.drawableEndY
        )
    }

    private fun buildBasicPath(path: Path, points: List<Offset>) {
        path.moveTo(points.first().x, points.first().y)
        for (i in 1 until points.size) {
            path.lineTo(points[i].x, points[i].y)
        }
    }

    private fun buildSmoothPath(path: Path, points: List<Offset>, smoothingFactor: Float) {
        path.moveTo(points.first().x, points.first().y)
        for (i in 0 until points.lastIndex) {
            val p0 = points.getOrElse(i - 1) { points[i] }
            val p1 = points[i]
            val p2 = points[i + 1]
            val p3 = points.getOrElse(i + 2) { points[i + 1] }

            val dx1 = p2.x - p0.x
            val dy1 = p2.y - p0.y
            val controlPoint1X = p1.x + dx1 * smoothingFactor
            val controlPoint1Y = p1.y + dy1 * smoothingFactor

            val dx2 = p3.x - p1.x
            val dy2 = p3.y - p1.y
            val controlPoint2X = p2.x - dx2 * smoothingFactor
            val controlPoint2Y = p2.y - dy2 * smoothingFactor

            path.cubicTo(
                controlPoint1X, controlPoint1Y,
                controlPoint2X, controlPoint2Y,
                p2.x, p2.y
            )
        }
    }

    private fun DrawScope.drawAnimatedPath(
        originalPath: Path,
        points: List<Offset>,
        config: LineChartConfig,
        animationState: ChartAnimationState,
        baselineY: Float
    ) {
        val animationProgress = animationState.progress
        val revealProgress = animationState.revealProgress
        val color = config.lineColor.copy(alpha = animationState.alpha)
        val strokeWidth = config.lineWidth.toPx()

        // Çizilecek olan nihai path (Animasyon durumuna göre kesilmiş hali)
        val drawingPath = Path()

        // Animasyon mantığı:
        // Eğer progress < 1f ise, PathMeasure ile yolun sadece bir kısmını alıyoruz.
        // Bu "kalemle çiziliyormuş" efekti verir.
        var currentPathEndX = points.last().x // Varsayılan bitiş X

        if (animationProgress < 1f) {
            val pathMeasure = PathMeasure()
            pathMeasure.setPath(originalPath, false)
            val length = pathMeasure.length
            val distance = length * animationProgress

            // Segmenti al
            pathMeasure.getSegment(0f, distance, drawingPath, true)

            val position = pathMeasure.getPosition(distance)
            currentPathEndX = position.x
        } else {
            // Animasyon bittiyse orijinal yolu kullan (Daha performanslı)
            drawingPath.addPath(originalPath)
        }

        // --- GÖLGE ÇİZİMİ ---
        if (config.showLineShadow) {
            // Gölge path'i = (Çizgi Path'i) + (Bitişten aşağı in) + (Başlangıca dön) + (Kapat)
            val shadowPath = Path()
            shadowPath.addPath(drawingPath)

            // Son noktadan aşağıya (baseline) in
            // Not: drawingPath'in son noktasını kesin bilmek için currentPathEndX kullandık.
            // Y ekseni baselineY olacak.
            shadowPath.lineTo(currentPathEndX, baselineY)

            // Başlangıç noktasının altına git (X = ilk noktanın X'i, Y = baseline)
            shadowPath.lineTo(points.first().x, baselineY)

            // Şekli kapat
            shadowPath.close()

            // Gradient oluştur
            val brush = createShadowBrush(
                config = config,
                alpha = color.alpha,
                startX = points.first().x,
                endX = points.last().x,
                topY = points.minOf { it.y }, // Veya 0f (grafiğin en tepesi)
                bottomY = baselineY
            )

            // Reveal animasyonu (Wipe efekti) için clip
            if (revealProgress < 1f) {
                clipRect(right = size.width * revealProgress) {
                    drawPath(path = shadowPath, brush = brush)
                }
            } else {
                drawPath(path = shadowPath, brush = brush)
            }
        }

        // --- ÇİZGİ ÇİZİMİ ---
        if (revealProgress < 1f) {
            clipRect(right = size.width * revealProgress) {
                drawPath(
                    path = drawingPath,
                    color = color,
                    style = Stroke(
                        width = strokeWidth,
                        cap = config.lineCap,
                        join = if (config.smoothLine) StrokeJoin.Round else StrokeJoin.Miter
                    )
                )
            }
        } else {
            drawPath(
                path = drawingPath,
                color = color,
                style = Stroke(
                    width = strokeWidth,
                    cap = config.lineCap,
                    join = if (config.smoothLine) StrokeJoin.Round else StrokeJoin.Miter
                )
            )
        }
    }

    private fun createShadowBrush(
        config: LineChartConfig,
        alpha: Float,
        startX: Float,
        endX: Float,
        topY: Float,
        bottomY: Float
    ): Brush {
        val shadowColors = config.shadowConfig.shadowColors.map {
            it.copy(alpha = it.alpha * alpha)
        }.let {
            if (it.size < 2) it + Color.Transparent else it
        }

        return when (config.shadowConfig.shadowRotation) {
            LineShadowConfig.LineShadowRotation.TOP_TO_BOTTOM ->
                Brush.verticalGradient(colors = shadowColors, startY = topY, endY = bottomY)
            LineShadowConfig.LineShadowRotation.BOTTOM_TO_TOP ->
                Brush.verticalGradient(colors = shadowColors, startY = bottomY, endY = topY)
            LineShadowConfig.LineShadowRotation.LEFT_TO_RIGHT ->
                Brush.horizontalGradient(colors = shadowColors, startX = startX, endX = endX)
            LineShadowConfig.LineShadowRotation.RIGHT_TO_LEFT ->
                Brush.horizontalGradient(colors = shadowColors, startX = endX, endX = startX)
        }
    }
}