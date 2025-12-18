package com.gkhnakbs.gcharts.charts.core


import androidx.compose.runtime.Immutable

/**
 * Created by Gökhan Akbaş on 09/12/2025.
 */
@Immutable
data class DataPoint(
    val x: Float,
    val y: Float,
    val label: String? = null,
)