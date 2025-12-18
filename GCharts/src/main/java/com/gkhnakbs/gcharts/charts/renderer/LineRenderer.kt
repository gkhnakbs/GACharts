package com.gkhnakbs.gcharts.charts.renderer

/**
 * Created by Gökhan Akbaş on 09/12/2025.
 */

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect

object LineRenderer {

    fun DrawScope.drawLine(
        points: List<Offset>,
        color: Color,
        strokeWidth: Float,
        strokeCap: StrokeCap,
        animationProgress: Float = 1f,
        revealProgress: Float = 1f
    ) {
        if (points.size < 2) return

        val path = Path().apply {
            moveTo(points.first().x, points.first().y)

            // Animasyonlu çizim için kaç nokta gösterileceğini hesapla
            val visiblePointCount = (points.size * animationProgress).toInt().coerceIn(1, maximumValue = points.size)

            for (i in 1 until visiblePointCount) {
                lineTo(points[i].x, points[i].y)
            }

            // Son nokta için interpolasyon (smooth animasyon)
            if (animationProgress < 1f && visiblePointCount < points.size) {
                val fraction = (points.size * animationProgress) - visiblePointCount
                if (fraction > 0 && visiblePointCount < points.size) {
                    val currentPoint = points[visiblePointCount - 1]
                    val nextPoint = points[visiblePointCount]
                    val interpolatedX = currentPoint.x + (nextPoint.x - currentPoint.x) * fraction
                    val interpolatedY = currentPoint.y + (nextPoint.y - currentPoint.y) * fraction
                    lineTo(interpolatedX, interpolatedY)
                }
            }
        }

        // Reveal animasyonu için clip rect kullan
        if (revealProgress < 1f) {
            clipRect(
                left = 0f,
                top = 0f,
                right = size.width * revealProgress,
                bottom = size.height
            ) {
                drawPath(
                    path = path,
                    color = color,
                    style = Stroke(
                        width = strokeWidth,
                        cap = strokeCap
                    )
                )
            }
        } else {
            drawPath(
                path = path,
                color = color,
                style = Stroke(
                    width = strokeWidth,
                    cap = strokeCap
                )
            )
        }
    }

    fun DrawScope.drawSmoothLine(
        points: List<Offset>,
        color: Color,
        strokeWidth: Float,
        strokeCap: StrokeCap = StrokeCap.Round,
        animationProgress: Float = 1f
    ) {
        if (points.size < 2) return

        val fullPath = Path().apply {
            // İlk noktadan başla
            moveTo(points.first().x, points.first().y)

            for (i in 0 until points.size - 1) {
                val p0 = points.getOrElse(i - 1) { points[i] } // Önceki nokta (yoksa şimdiki)
                val p1 = points[i]     // Şu anki nokta
                val p2 = points[i + 1] // Hedef nokta
                val p3 = points.getOrElse(i + 2) { points[i + 1] } // Hedefin sonrası (yoksa hedef)

                // --- KONTROL NOKTASI HESAPLAMA (Cubic Spline) ---
                // Bu matematik, çizginin noktaya girerken ve çıkarkenki eğimini ayarlar.
                // 0.15f değeri "yumuşaklık" katsayısıdır. (0.1f - 0.3f arası idealdir)
                val smoothingFactor = 0.15f

                val dx1 = p2.x - p0.x
                val dy1 = p2.y - p0.y
                val controlPoint1X = p1.x + dx1 * smoothingFactor
                val controlPoint1Y = p1.y + dy1 * smoothingFactor

                val dx2 = p3.x - p1.x
                val dy2 = p3.y - p1.y
                val controlPoint2X = p2.x - dx2 * smoothingFactor
                val controlPoint2Y = p2.y - dy2 * smoothingFactor

                // Çizgiyi P1'den P2'ye, hesaplanan kontrol noktalarıyla götür
                cubicTo(
                    controlPoint1X, controlPoint1Y,
                    controlPoint2X, controlPoint2Y,
                    p2.x, p2.y // <-- Hedef kesinlikle p2 noktasıdır
                )
            }
        }

        // --- ANİMASYON KISMI (Değişmedi) ---
        val pathMeasure = PathMeasure()
        pathMeasure.setPath(fullPath, false)

        val pathLength = pathMeasure.length
        val animatedPath = Path()

        pathMeasure.getSegment(
            startDistance = 0f,
            stopDistance = pathLength * animationProgress,
            destination = animatedPath,
            startWithMoveTo = true
        )

        drawPath(
            path = animatedPath,
            color = color,
            style = Stroke(
                width = strokeWidth,
                cap = strokeCap,
                join = StrokeJoin.Round
            )
        )
    }
}