package com.gkhnakbs.gcharts.charts.renderer

/**
 * Created by Gökhan Akbaş on 09/12/2025.
 */

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

object PointRenderer {

    fun DrawScope.drawPoints(
        points: List<Offset>,
        pointColor: Color,
        pointRadius: Float,
        pointStrokeWidth: Float,
        pointFillColor: Color,
        animationProgress: Float = 1f,
        slideOffset: Float = 0f,
        drawableHeight: Float = 0f
    ) {
        val visiblePointCount = (points.size * animationProgress).toInt().coerceIn(minimumValue = 1, maximumValue = points.size)

        for (i in 0 until visiblePointCount) {
            val point = points[i]

            // SlideUp animasyonu için Y offset hesapla
            val animatedY = if (slideOffset > 0f && drawableHeight > 0f) {
                point.y + (drawableHeight * slideOffset)
            } else {
                point.y
            }

            val animatedPoint = Offset(point.x, animatedY)

            // İç dolgu (beyaz)
            drawCircle(
                color = pointFillColor,
                radius = pointRadius,
                center = animatedPoint
            )

            // Dış çerçeve (renkli)
            drawCircle(
                color = pointColor,
                radius = pointRadius,
                center = animatedPoint,
                style = Stroke(width = pointStrokeWidth)
            )
        }
    }

    fun DrawScope.drawCustomPoints(
        points: List<Offset>,
        animationProgress: Float = 1f, // 0f ile 1f arası (X ekseni ilerlemesi)
        slideOffset: Float = 0f,       // 0f ile 1f arası (Y ekseni alttan geliş)
        drawableHeight: Float = 0f,
        // ÖZELLEŞTİRME BURADA: Her nokta için ne çizileceğine sen karar verirsin
        onDrawPoint: DrawScope.(index: Int, center: Offset, scale: Float) -> Unit
    ) {
        // Toplam kaç nokta olduğu
        val totalPoints = points.size

        // Animasyon ilerlemesine göre şu an kaçıncı noktanın çizileceği (veya çizilmekte olduğu)
        val currentProgressIndex = (totalPoints * animationProgress)

        points.forEachIndexed { index, point ->
            // Eğer bu noktanın sırası henüz gelmediyse döngüyü pas geç (Performans)
            if (index > currentProgressIndex) return@forEachIndexed

            // --- ANIMASYON HESAPLAMALARI ---

            // 1. Scale (Büyüme) Efekti:
            // Nokta sırası geldikçe 0'dan 1'e büyüsün.
            // Örneğin: currentProgressIndex 5.5 ise; 5. nokta tam boyutta(1f), 6. nokta yarım boyutta(0.5f) olur.
            val pointScale = (currentProgressIndex - index).coerceIn(0f, 1f)

            // Eğer çok küçükse çizme (boşuna GPU yorma)
            if (pointScale <= 0f) return@forEachIndexed

            // 2. Slide (Alttan Kayma) Efekti:
            val animatedY = if (slideOffset > 0f && drawableHeight > 0f) {
                // slideOffset 1 ise en altta, 0 ise orijinal yerinde
                point.y + (drawableHeight * slideOffset)
            } else {
                point.y
            }

            val centerPos = Offset(point.x, animatedY)

            // Hesaplanan değerleri çiziciye gönder
            onDrawPoint(index, centerPos, pointScale)
        }
    }
}