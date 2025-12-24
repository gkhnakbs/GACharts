package com.gkhnakbs.gcharts.charts.renderer

/**
 * Created by Gökhan Akbaş on 09/12/2025.
 */

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object PointRenderer {

    fun DrawScope.drawPoints(
        points: List<Offset>,
        pointColor: Color,
        pointRadius: Float,
        pointStrokeWidth: Float,
        pointFillColor: Color,
        animationProgress: Float = 1f,
        slideOffset: Float = 0f,
        drawableHeight: Float = 0f,
    ) {
        if (points.isEmpty()) return

        val slideDelta = if (slideOffset > 0f && drawableHeight > 0f) {
            drawableHeight * slideOffset
        } else {
            0f
        }

        // Çizilecek nokta sayısı
        val visiblePointCount = (points.size * animationProgress).toInt()
            .coerceIn(0, points.size)

        for (i in 0 until visiblePointCount) {
            val point = points[i]

            val centerPos = point.copy(y = point.y + slideDelta)

            // 1. İç dolgu
            drawCircle(
                color = pointFillColor,
                radius = pointRadius,
                center = centerPos
            )
            // 2. Çerçeve
            drawCircle(
                color = pointColor,
                radius = pointRadius,
                center = centerPos,
                style = Stroke(width = pointStrokeWidth)
            )
        }
    }

    fun DrawScope.drawPopup(
        selectedPoint: Offset?,
        popupValue: String,
        textMeasurer: TextMeasurer,
        popupAnimationProgress: Float, // 0f (yok) -> 1f (tam)
        slideOffset: Float = 0f,
        drawableHeight: Float = 0f,
    ) {
        if (selectedPoint == null || popupAnimationProgress <= 0f) return

        // Noktanın slide animasyonundaki gerçek yerini bul
        val slideDelta =
            if (slideOffset > 0f && drawableHeight > 0f) drawableHeight * slideOffset else 0f
        val anchorPoint = selectedPoint.copy(y = selectedPoint.y + slideDelta)

        // Animasyon parametreleri
        val scale = popupAnimationProgress // Büyüme efekti
        val alpha = popupAnimationProgress.coerceIn(0f, 1f) // Opaklık efekti

        // Popup Stil Ayarları
        val padding = 12.dp.toPx()
        val cornerRadius = 8.dp.toPx()
        val arrowHeight = 8.dp.toPx()
        val backgroundColor = Color(0xFF333333).copy(alpha = 0.9f * alpha)
        val textColor = Color.White.copy(alpha = alpha)

        // Metni ölç (TextMeasurer Compose 1.3+ ile geldi)
        val textLayoutResult: TextLayoutResult = textMeasurer.measure(
            text = popupValue,
            style = TextStyle(color = textColor, fontSize = 14.sp)
        )

        val boxWidth = textLayoutResult.size.width + (padding * 2)
        val boxHeight = textLayoutResult.size.height + (padding * 2)

        // Popup'ın konumu (Noktanın tam üstü)
        // Scale efektini merkezden uygulamak için math işlemleri:
        val targetBottomY = anchorPoint.y - arrowHeight - 10f // Noktanın 10px yukarısı
        val currentBoxWidth = boxWidth * scale
        val currentBoxHeight = boxHeight * scale

        val left = anchorPoint.x - (currentBoxWidth / 2)
        val top = targetBottomY - currentBoxHeight
        val right = left + currentBoxWidth
        val bottom = targetBottomY

        // --- BALONCUK ŞEKLİ (Path) ---
        val path = Path().apply {
            // Yuvarlak dikdörtgen
            addRoundRect(
                RoundRect(
                    rect = Rect(left, top, right, bottom),
                    cornerRadius = CornerRadius(cornerRadius * scale)
                )
            )
            // Altındaki üçgen ok (Arrow)
            if (scale > 0.8f) { // Ok sadece kutu yeterince büyüyünce çıksın
                moveTo(anchorPoint.x - (6.dp.toPx() * scale), bottom)
                lineTo(anchorPoint.x, bottom + (arrowHeight * scale))
                lineTo(anchorPoint.x + (6.dp.toPx() * scale), bottom)
                close()
            }
        }

        // Baloncuğu çiz
        drawPath(path = path, color = backgroundColor)

        // Yazıyı çiz (sadece kutu belli bir boyuta gelince)
        if (scale > 0.5f) {
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    x = left + padding * scale, // Padding'i de scale et
                    y = top + padding * scale
                )
            )
        }
    }
}