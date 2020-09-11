package com.hazz.aipick.widget

import android.annotation.SuppressLint
import android.widget.TextView
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.hazz.aipick.R
import com.scwang.smartrefresh.layout.util.DensityUtil
import kotlin.math.abs

class InfoMarkView : MarkerView {

    constructor(chart: Chart<*>, pos: Int, list: CombinedData, type: Int) : super(chart.context, R.layout.maker_benefit) {
        super.setChartView(chart)
        this.pos = pos
        this.data = list
        this.type = type
    }

    private var type = 0
    private var pos = 0
    private var data: CombinedData
    private var tvData: TextView = findViewById(R.id.tv_data)
    private var tv1: TextView = findViewById(R.id.tv1)
    private var tv2: TextView = findViewById(R.id.tv2)

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-(height + DensityUtil.dp2px(100f))).toFloat())
    }

    //回调函数每次MarkerView重绘,可以用来更新内容(用户界面)
    @SuppressLint("SetTextI18n", "StringFormatMatches")
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        println(e)
        tv1.text = context.getString(R.string.text_total_benefit, "${dealData((data.lineData.dataSets[0] as LineDataSet).values[pos].y)}")
        tv2.text = when (type) {
            0 -> {
                context.getString(R.string.text_month_total, "${dealData((data.barData.dataSets[0] as BarDataSet).values[pos].y)}")
            }
            else -> {
                context.getString(R.string.text_day_total, "${dealData((data.barData.dataSets[0] as BarDataSet).values[pos].y)}")
            }
        }
        tvData.text = chartView.xAxis.getFormattedLabel(pos)
        super.refreshContent(e, highlight)
    }

    fun dealData(data: Float): String {
        val temp: Float = abs(data)
        return when {
            temp < 10000 -> {
                String.format("%.2f", data)
            }
            temp < 1000000 -> {
                String.format("%.2fW", data / 10000)
            }
            else -> {
                String.format("%.2fM", data / 1000000)
            }
        }
    }
}