package com.gkhnakbs.gcharts.charts.animation


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.EaseOutElastic
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by Gökhan Akbaş on 09/12/2025.
 */

/**
 * Animasyon easing tipleri
 */
enum class ChartAnimationEasing {
    Linear,
    EaseInOut,
    EaseOutCubic,
    EaseOutQuart,
    EaseOutBack,      // Hafif geri sekme efekti
    EaseOutBounce,    // Zıplama efekti
    EaseOutElastic,   // Elastik efekt
    FastOutSlowIn
}

/**
 * Animasyon tipleri
 */
enum class ChartAnimationType {
    Draw,           // Çizgi çizilir gibi (varsayılan)
    FadeIn,         // Opaklık ile görünür
    SlideUp,        // Aşağıdan yukarı kayar
    SlideDown,        // Yukarıdan aşağıya kayar
    Scale,          // Küçükten büyüğe
    Reveal          // Soldan sağa açılır
}

/**
 * Animasyon konfigürasyonu
 */
@Immutable
data class ChartAnimationConfig(
    val enabled: Boolean = true,
    val duration: Int = 1000,
    val type: ChartAnimationType = ChartAnimationType.Draw,
    val easing: ChartAnimationEasing = ChartAnimationEasing.EaseOutCubic,
    val delayMs: Int = 0,
    val staggeredDelay: Int = 0,  // Her nokta için ek gecikme (ms)
)

/**
 * Animasyon state holder
 */
@Immutable
data class ChartAnimationState(
    val progress: Float = 1f,
    val alpha: Float = 1f,
    val scale: Float = 1f,
    val slideOffset: Float = 0f,
    val revealProgress: Float = 1f,
)

/**
 * Ana animasyon composable - basit kullanım için
 */
@Composable
fun rememberChartAnimationState(
    key: Any,
    enabled: Boolean,
    duration: Int,
): Float {
    return rememberChartAnimationState(
        key = key,
        config = ChartAnimationConfig(
            enabled = enabled,
            duration = duration
        )
    ).progress
}

/**
 * Gelişmiş animasyon composable
 */
@Composable
fun rememberChartAnimationState(
    key: Any,
    config: ChartAnimationConfig,
): ChartAnimationState {
    val animatable = remember { Animatable(0f) }

    val easingFunction = when (config.easing) {
        ChartAnimationEasing.Linear -> LinearEasing
        ChartAnimationEasing.EaseInOut -> EaseInOut
        ChartAnimationEasing.EaseOutCubic -> EaseOutCubic
        ChartAnimationEasing.EaseOutQuart -> EaseOutQuart
        ChartAnimationEasing.EaseOutBack -> EaseOutBack
        ChartAnimationEasing.EaseOutBounce -> EaseOutBounce
        ChartAnimationEasing.EaseOutElastic -> EaseOutElastic
        ChartAnimationEasing.FastOutSlowIn -> FastOutSlowInEasing
    }

    LaunchedEffect(key) {
        if (config.enabled) {
            animatable.snapTo(0f)

            if (config.delayMs > 0) {
                delay(config.delayMs.toLong())
            }

            animatable.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = config.duration,
                    easing = easingFunction
                )
            )
        } else {
            animatable.snapTo(1f)
        }
    }

    val progress = animatable.value

    return when (config.type) {
        ChartAnimationType.Draw -> ChartAnimationState(
            progress = progress,
            alpha = 1f,
            scale = 1f,
            slideOffset = 0f,
            revealProgress = 1f
        )

        ChartAnimationType.FadeIn -> ChartAnimationState(
            progress = 1f,
            alpha = progress,
            scale = 1f,
            slideOffset = 0f,
            revealProgress = 1f
        )

        ChartAnimationType.SlideUp -> ChartAnimationState(
            progress = 1f,
            alpha = progress,
            scale = 1f,
            slideOffset = 1f - progress,  // 1 -> 0 (aşağıdan yukarı)
            revealProgress = 1f
        )

        ChartAnimationType.SlideDown -> ChartAnimationState(
            progress = 1f,
            alpha = progress,
            scale = 1f,
            slideOffset = 1f - progress,  // 1 -> 0 (aşağıdan yukarı)
            revealProgress = 1f
        )

        ChartAnimationType.Scale -> ChartAnimationState(
            progress = 1f,
            alpha = 1f,
            scale = progress,
            slideOffset = 0f,
            revealProgress = 1f
        )

        ChartAnimationType.Reveal -> ChartAnimationState(
            progress = 1f,
            alpha = 1f,
            scale = 1f,
            slideOffset = 0f,
            revealProgress = progress
        )
    }
}

/**
 * Staggered (sıralı) animasyon için - her nokta sırayla animasyon yapar
 */
@Composable
fun rememberStaggeredAnimationStates(
    key: Any,
    itemCount: Int,
    config: ChartAnimationConfig,
): List<Float> {
    val animatables = remember(itemCount) {
        List(itemCount) { Animatable(0f) }
    }

    val easingFunction = when (config.easing) {
        ChartAnimationEasing.Linear -> LinearEasing
        ChartAnimationEasing.EaseInOut -> EaseInOut
        ChartAnimationEasing.EaseOutCubic -> EaseOutCubic
        ChartAnimationEasing.EaseOutQuart -> EaseOutQuart
        ChartAnimationEasing.EaseOutBack -> EaseOutBack
        ChartAnimationEasing.EaseOutBounce -> EaseOutBounce
        ChartAnimationEasing.EaseOutElastic -> EaseOutElastic
        ChartAnimationEasing.FastOutSlowIn -> FastOutSlowInEasing
    }

    LaunchedEffect(key) {
        if (config.enabled) {
            animatables.forEach { it.snapTo(0f) }

            if (config.delayMs > 0) {
                delay(config.delayMs.toLong())
            }

            animatables.forEachIndexed { index, animatable ->
                delay((index * config.staggeredDelay).toLong())
                animatable.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = config.duration,
                        easing = easingFunction
                    )
                )
            }
        } else {
            animatables.forEach { it.snapTo(1f) }
        }
    }

    return animatables.map { it.value }
}

/**
 * Paralel staggered animasyon - noktalar sırayla başlar ama paralel çalışır
 */
@Composable
fun rememberParallelStaggeredAnimation(
    key: Any,
    itemCount: Int,
    config: ChartAnimationConfig,
): List<Float> {
    val animatables = remember(itemCount) {
        List(itemCount) { Animatable(0f) }
    }

    val easingFunction = when (config.easing) {
        ChartAnimationEasing.Linear -> LinearEasing
        ChartAnimationEasing.EaseInOut -> EaseInOut
        ChartAnimationEasing.EaseOutCubic -> EaseOutCubic
        ChartAnimationEasing.EaseOutQuart -> EaseOutQuart
        ChartAnimationEasing.EaseOutBack -> EaseOutBack
        ChartAnimationEasing.EaseOutBounce -> EaseOutBounce
        ChartAnimationEasing.EaseOutElastic -> EaseOutElastic
        ChartAnimationEasing.FastOutSlowIn -> FastOutSlowInEasing
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key) {
        if (config.enabled) {
            animatables.forEach { it.snapTo(0f) }

            if (config.delayMs > 0) {
                delay(config.delayMs.toLong())
            }

            // Paralel başlat, her biri kendi gecikmesiyle
            animatables.forEachIndexed { index, animatable ->
                scope.launch {
                    delay((index * config.staggeredDelay).toLong())
                    animatable.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(
                            durationMillis = config.duration,
                            easing = easingFunction
                        )
                    )
                }
            }
        } else {
            animatables.forEach { it.snapTo(1f) }
        }
    }

    return animatables.map { it.value }
}