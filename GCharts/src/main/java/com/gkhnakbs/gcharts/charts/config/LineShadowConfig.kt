package com.gkhnakbs.gcharts.charts.config

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class LineShadowConfig(
    val shadowColors : List<Color> = listOf(),
    val shadowRotation : LineShadowRotation,
){
    enum class LineShadowRotation{ LEFT_TO_RIGHT, RIGHT_TO_LEFT, TOP_TO_BOTTOM, BOTTOM_TO_TOP }
}
