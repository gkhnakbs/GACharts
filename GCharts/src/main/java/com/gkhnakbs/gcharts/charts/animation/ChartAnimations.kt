package com.gkhnakbs.gcharts.charts.animation


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember

/**
 * Created by Gökhan Akbaş on 09/12/2025.
 */
@Composable
fun rememberChartAnimationState(
    key: Any,
    enabled: Boolean,
    duration: Int
): Float {
    val animatable = remember { Animatable(0f) }

    LaunchedEffect(key) {
        if (enabled) {
            animatable.snapTo(0f)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = duration)
            )
        } else {
            animatable.snapTo(1f)
        }
    }

    return animatable.value
}