package com.gkhnakbs.gcharts.charts.core


import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Created by Gökhan Akbaş on 10/12/2025.
 */
@Immutable
data class ChartPadding(
    val start: Dp = 10.dp,   // For Y-axis labels
    val end: Dp = 10.dp,
    val top: Dp = 10.dp,
    val bottom: Dp = 10.dp,   // For X-axis labels
)