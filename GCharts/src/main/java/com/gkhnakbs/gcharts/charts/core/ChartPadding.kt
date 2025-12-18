package com.gkhnakbs.gcharts.charts.core


import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Created by Gökhan Akbaş on 10/12/2025.
 */
@Immutable
data class ChartPadding(
    val start: Dp = 48.dp,   // Y ekseni label'ları için
    val end: Dp = 16.dp,
    val top: Dp = 16.dp,
    val bottom: Dp = 32.dp,   // X ekseni label'ları için
)