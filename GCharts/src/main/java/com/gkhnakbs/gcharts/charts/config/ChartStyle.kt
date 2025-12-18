package com.gkhnakbs.gcharts.charts.config


import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

/**
 * Created by Gökhan Akbaş on 09/12/2025.
 */
@Immutable
data class LineChartStyle(
    val backgroundColor: Color = Color.White,
    val labelTextStyle: TextStyle = TextStyle(
        color = Color(0xFF757575),
        fontSize = 10.sp
    ),
)