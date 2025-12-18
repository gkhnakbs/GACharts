package com.gkhnakbs.gacharts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.Companion
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gkhnakbs.gacharts.ui.theme.GAChartsTheme
import com.gkhnakbs.gcharts.charts.LineChart
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
                        modifier = Modifier.width(400.dp).height(400.dp),
                        contentAlignment = Alignment.Center
                    ){
                        val sampleData = LineChartData(
                            points = listOf(
                                DataPoint(0f, 90f,"test1"),
                                DataPoint(1f, 250f,"test2"),
                                DataPoint(2f, 180f,"test3"),
                                DataPoint(3f, 350f,"test4"),
                                DataPoint(4f, 280f,"test5"),
                                DataPoint(5f, 450f,"test6 test6test6"),
                                DataPoint(5f, 550f,"test6 test7test7test7")
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

                        LineChart(
                            data = sampleData,
                            config = LineChartConfig(
                                lineColor = Color(0xFFE91E63),
                                pointColor = Color(0xFFE91E63),
                                lineWidth = 3.dp,
                                pointRadius = 3.dp,
                                showGrid = true,
                                animationEnabled = true
                            ),
                            style = LineChartStyle(
                                labelTextStyle = TextStyle(
                                    color = Color(0xFF757575),
                                    fontSize = 8.sp
                                )
                            ),
                            padding = ChartPadding(0.dp,0.dp,0.dp,0.dp),
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}