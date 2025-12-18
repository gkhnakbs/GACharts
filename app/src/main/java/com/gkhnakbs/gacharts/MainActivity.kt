package com.gkhnakbs.gacharts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gkhnakbs.gacharts.ui.theme.GAChartsTheme
import com.gkhnakbs.gcharts.charts.LineChart
import com.gkhnakbs.gcharts.charts.animation.ChartAnimationEasing
import com.gkhnakbs.gcharts.charts.animation.ChartAnimationType
import com.gkhnakbs.gcharts.charts.config.LineChartConfig
import com.gkhnakbs.gcharts.charts.config.LineChartStyle
import com.gkhnakbs.gcharts.charts.core.ChartPadding
import com.gkhnakbs.gcharts.charts.core.DataPoint
import com.gkhnakbs.gcharts.charts.core.LineChartData

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GAChartsTheme {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(400.dp)
                            .height(400.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val sampleData = remember {
                            LineChartData(
                                points = listOf(
                                    DataPoint(0f, 90f, "test1"),
                                    DataPoint(1f, 250f, "test2"),
                                    DataPoint(2f, 180f, "test3"),
                                    DataPoint(3f, 350f, "test4"),
                                    DataPoint(4f, 280f, "test5"),
                                    DataPoint(5f, 450f, "test6 test6test6"),
                                    DataPoint(6f, 550f, "test6 test6test6"),
                                    DataPoint(7f, 650f, "test6 test6test6"),
                                    DataPoint(8f, 610f, "test6 test6test6"),
                                    DataPoint(9f, 623f, "test6 test6test6"),
                                    DataPoint(10f, 440f, "test6 test6test6"),
                                    DataPoint(11f, 640f, "test6 test6test6"),
                                    DataPoint(12f, 610f, "test6 test6test6"),
                                    DataPoint(13f, 620f, "test6 test6test6"),
                                    DataPoint(14f, 770f, "test6 test7test7test7"),
                                    DataPoint(15f, 990f, "test6 test7test7test7"),
                                    DataPoint(16f, 1550f, "test6 test7test7test7"),
                                    DataPoint(17f, 1250f, "test6 test7test7test7"),
                                    DataPoint(18f, 1760f, "test6 test6test6"),
                                    DataPoint(19f, 1760f, "test6 test6test6"),
                                ),
                                xAxisLabels = listOf(
                                    "test1",
                                    "test2",
                                    "test3",
                                    "test4",
                                    "test5",
                                    "test6 test6test6"
                                )
                            )
                        }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val data = remember { mutableStateOf(sampleData) }

                            val firstValue by remember {
                                derivedStateOf { data.value.points.firstOrNull()?.y }
                            }

                            Button(
                                onClick = {
                                    data.value = data.value.copy(
                                        points = data.value.points.map { it.copy(y = it.y + 50) }
                                    )
                                }
                            ) {
                                Text(
                                    text = firstValue?.toString() ?: "-",
                                    fontSize = 20.sp
                                )
                            }

                            LineChart(
                                data = data.value,
                                config = LineChartConfig(
                                    lineColor = Color(0xFFE91E63),
                                    pointColor = Color(0xFFE91E63),
                                    lineWidth = 3.dp,
                                    pointRadius = 3.dp,
                                    showGrid = true,
                                    showPoints = true,
                                    animationEnabled = true,
                                    animationDuration = 1000,
                                    lineCap = StrokeCap.Round,
                                    animationType = ChartAnimationType.Draw,
                                    animationEasing = ChartAnimationEasing.Linear,
                                ),
                                style = LineChartStyle(
                                    labelTextStyle = TextStyle(
                                        color = Color(0xFF757575),
                                        fontSize = 8.sp
                                    )
                                ),
                                padding = ChartPadding(0.dp, 0.dp, 0.dp, 0.dp),
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}
