package com.hazz.aipick.managers

import android.graphics.drawable.Drawable
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


object ChartManager {
    fun showLineChart(chart: LineChart, points: ArrayList<Entry>, lineColor: Int, lineWidth: Float, fillDrawable: Drawable) {
        initChart(chart)

        // 每一个LineDataSet代表一条线
        val lineDataSet = LineDataSet(points, "")
        initLineDataSet(lineDataSet, lineColor, fillDrawable, lineWidth, LineDataSet.Mode.CUBIC_BEZIER)
        val lineData = LineData(lineDataSet)
        chart.data = lineData
        chart.invalidate()
    }

    /**
     * 初始化图表
     */
    private fun initChart(lineChart: LineChart) {
        /***图表设置 */
        //是否展示网格线
        lineChart.setDrawGridBackground(false)
        //是否显示边界
        lineChart.setDrawBorders(false)
        //是否可以拖动
        lineChart.isDragEnabled = false
        //是否有触摸事件
        lineChart.setTouchEnabled(false)
        //设置XY轴动画效果
        lineChart.animateY(0)
        lineChart.animateX(0)
        /***XY轴的设置 */

        // enable touch gestures
        lineChart.setTouchEnabled(false)

        // enable scaling and dragging

        // enable scaling and dragging
        lineChart.isDragEnabled = false
        lineChart.setScaleEnabled(false)

        // if disabled, scaling can be done on x- and y-axis separately

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false)

        lineChart.setDrawGridBackground(false)

        lineChart.xAxis.isEnabled = false

        val leftAxis: YAxis = lineChart.axisLeft
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        leftAxis.setDrawAxisLine(false)
        leftAxis.setDrawLabels(false)
        leftAxis.setDrawGridLines(false)
        leftAxis.isGranularityEnabled = true
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawTopYLabelEntry(false)
        leftAxis.setDrawZeroLine(false)

        lineChart.axisRight.isEnabled = false
        lineChart.legend.isEnabled = false
        lineChart.description.isEnabled = false
        lineChart.setViewPortOffsets(0f, 0f, 0f, 0f)

    }

    /**
     * 曲线初始化设置 一个LineDataSet 代表一条曲线
     *
     * @param lineDataSet 线条
     * @param color       线条颜色
     * @param mode
     */
    private fun initLineDataSet(lineDataSet: LineDataSet, color: Int, fillDrawable: Drawable, width: Float, mode: LineDataSet.Mode?) {
        lineDataSet.color = color
//        lineDataSet.setCircleColor(color)
        lineDataSet.lineWidth = width
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawValues(false)
        lineDataSet.valueTextSize = 10f
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        //设置折线图填充
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillDrawable = fillDrawable
        lineDataSet.fillAlpha = 30
        lineDataSet.formLineWidth = 2f
        lineDataSet.cubicIntensity = 0.05f
        lineDataSet.formSize = 15f
        if (mode == null) {
            //设置曲线展示为圆滑曲线（如果不设置则默认折线）
            lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        } else {
            lineDataSet.mode = mode
        }
    }
}