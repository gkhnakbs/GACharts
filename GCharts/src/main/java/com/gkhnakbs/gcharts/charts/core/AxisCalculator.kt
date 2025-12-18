package com.gkhnakbs.gcharts.charts.core


import java.util.Locale
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

/**
 * Created by Gökhan Akbaş on 10/12/2025.
 */
object AxisCalculator {

    data class AxisTicks(
        val values: List<Float>,
        val labels: List<String>,
        val maxLabelValue: Float,
        val minLabelValue: Float,
    )

    /**
     * Y ekseni için uygun tick değerlerini hesaplar
     */
    fun calculateYAxisTicks(
        minValue: Float,
        maxValue: Float,
        desiredTickCount: Int = 5,
    ): AxisTicks {
        if (minValue == maxValue) {
            return AxisTicks(
                values = listOf(minValue),
                labels = listOf(formatLabel(minValue)),
                maxLabelValue = maxValue,
                minLabelValue = minValue
            )
        }

        val range = maxValue - minValue
        val roughStep = range / (desiredTickCount - 1)

        // Güzel bir step değeri bul (1, 2, 5, 10, 20, 50, 100...)
        val niceStep = calculateNiceStep(roughStep)

        val niceMin = floor(minValue / niceStep) * niceStep
        val niceMax = ceil(maxValue / niceStep) * niceStep

        val values = mutableListOf<Float>()
        var current = niceMin
        while (current <= niceMax + niceStep / 2) {
            values.add(current)
            current += niceStep
        }

        return AxisTicks(
            values = values,
            labels = values.map { formatLabel(it) },
            maxLabelValue = niceMax,
            minLabelValue = niceMin
        )
    }

    /**
     * X ekseni için tick değerlerini hesaplar
     */
    fun calculateXAxisTicks(
        minValue: Float,
        maxValue: Float,
        desiredTickCount: Int = 5,
    ): AxisTicks {
        return calculateYAxisTicks(minValue, maxValue, desiredTickCount)
    }

    private fun calculateNiceStep(roughStep: Float): Float {
        val exponent = floor(log10(roughStep))
        val fraction = roughStep / 10f.pow(exponent)

        val niceFraction = when {
            fraction <= 1.5 -> 1f
            fraction <= 3 -> 2f
            fraction <= 7 -> 5f
            else -> 10f
        }

        return niceFraction * 10f.pow(exponent)
    }

    private fun formatLabel(value: Float): String {
        return if (value == value.toLong().toFloat()) {
            value.toLong().toString()
        } else {
            String.format(Locale.getDefault(), "%.1f", value)
        }
    }
}