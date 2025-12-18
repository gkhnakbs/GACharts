package com.gkhnakbs.gcharts.charts.config


import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gkhnakbs.gcharts.charts.animation.ChartAnimationEasing
import com.gkhnakbs.gcharts.charts.animation.ChartAnimationType

/**
 * Created by Gökhan Akbaş on 09/12/2025.
 */
@Immutable
data class LineChartConfig(
    // Çizgi ayarları
    val lineColor: Color = Color(0xFF2196F3),
    val lineWidth: Dp = 2.dp,
    val lineCap: StrokeCap = StrokeCap.Round,

    // Nokta ayarları
    val showPoints: Boolean = true,
    val pointRadius: Dp = 4.dp,
    val pointColor: Color = Color(0xFF2196F3),
    val pointStrokeWidth: Dp = 2.dp,
    val pointFillColor: Color = Color.White,

    // Grid ayarları
    val showGrid: Boolean = true,
    val gridColor: Color = Color(0xFFE0E0E0),
    val gridStrokeWidth: Dp = 1.dp,
    val horizontalGridLines: Int = 5,
    val verticalGridLines: Int = 5,

    // Eksen ayarları
    val showAxes: Boolean = true,
    val axisColor: Color = Color(0xFF9E9E9E),
    val axisStrokeWidth: Dp = 1.dp,

    // Animasyon ayarları
    val animationEnabled: Boolean = true,
    val animationDuration: Int = 1000,
    val animationType: ChartAnimationType = ChartAnimationType.Draw,
    val animationEasing: ChartAnimationEasing = ChartAnimationEasing.EaseOutCubic,
    val animationDelayMs: Int = 0,
    val staggeredPointDelay: Int = 50,  // Her nokta için ek gecikme (ms)
)