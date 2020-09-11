package com.hazz.aipick.view.chart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.bigkoo.alertview.AlertView
import com.blankj.utilcode.util.LogUtils
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.InComing
import com.hazz.aipick.ui.fragment.TransactionAnalysisFragment
import kotlinx.android.synthetic.main.ai_view_benefit.view.*
import java.util.*
import kotlin.math.abs
import kotlin.math.round

class BenefitChartView(context: Context) : FrameLayout(context) {
    init {
        LayoutInflater.from(context).inflate(R.layout.ai_view_benefit, this)
        tv_year.setOnClickListener { showDialog(it) }
        tv_month.setOnClickListener { showDialog(it) }
        initChart()
    }

    private fun initChart() {


        val xAxis = chart.xAxis
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day

        xAxis.labelCount = 7
        xAxis.valueFormatter = IAxisValueFormatter { value, axis ->
            var index = round(abs(value)).toInt()
            when (index) {
                in 0..mLabelList.size -> mLabelList[index]
                else -> ""
            }
        }

        val leftAxis = chart.axisLeft
        leftAxis.setLabelCount(8, false)
        leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 15f
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
        leftAxis.valueFormatter = IAxisValueFormatter formatter@{ value: Float, axis: AxisBase? ->
            LogUtils.e(value)
            val temp = abs(value)
            when {
                temp < 10000 -> {
                    return@formatter String.format("$%.2f", value)
                }
                temp < 1000000 -> {
                    return@formatter String.format("$%.2fW", value / 10000)
                }
                else -> {
                    return@formatter String.format("$%.2fM", value / 1000000)
                }
            }
        }


        chart.axisRight.isEnabled = false
        chart.legend.isEnabled = false

    }

    private lateinit var mData: List<InComing>
    private lateinit var mLabelList: ArrayList<String>
    fun setData(data: List<InComing>) {
        mData = data
        dealData()
    }


    private fun dealData() {
        var mapBar = TreeMap<String, Float>(TransactionAnalysisFragment.MapKeyComparator())//天或者月的总收益
        var total = 0f
        for (datum in mData) {
            when (currentDayType) {
                "month" -> {
                    val month = getOrDefault(mapBar, datum.month_label, 0f)
                    mapBar[datum.month_label] = month + datum.self.toFloat()
                }
                else -> {
                    val month = getOrDefault(mapBar, datum.day_label, 0f)
                    mapBar[datum.day_label] = month + datum.self.toFloat()
                }
            }
        }
        mLabelList.clear()

//        xValue.clear()
//        barChartY.clear()
//        lineChartY.clear()
//        for (entry in mapBar.entries) {
//            mLabelList.add(entry.key)
//            xValue.add(entry.key)
//            barChartY.add(entry.value)
//            total += entry.value
//            lineChartY.add(total)
//        }
    }

    private fun getOrDefault(map: Map<String, Float>, key: String, defaultValue: Float): Float {
        return map[key] ?: defaultValue
    }

    private fun showDialog(v: View) {
        when (v.id) {
            R.id.tv_year -> showYear()
            R.id.tv_day -> showDay()
        }
    }

    private var currentYearType = "1month"
    private var currentDayType = "month"
    private fun showYear() {
        val itemYear = resources.getStringArray(R.array.item_year)
        AlertView.Builder()
                .setContext(context)
                .setStyle(AlertView.Style.ActionSheet)
                .setDestructive(*itemYear)
                .setCancelText(context.resources.getString(R.string.cancel))
                .setOnItemClickListener { any: Any, i: Int ->
                    currentYearType = when (i) {
                        0 -> "1month"
                        1 -> "3month"
                        else -> "1year"
                    }
                    tv_year.text = itemYear[i]
                    mOnDateSelect?.onSelect(currentDayType)
                }
                .build()
                .setCancelable(true)
                .show()
    }

    private fun showDay() {
        val itemDays = resources.getStringArray(R.array.item_day)
        AlertView.Builder()
                .setContext(context)
                .setStyle(AlertView.Style.ActionSheet)
                .setDestructive(*itemDays)
                .setCancelText(context.resources.getString(R.string.cancel))
                .setOnItemClickListener { _: Any, i: Int ->

                    currentDayType = when (i) {
                        1 -> {
                            "month"
                        }
                        else -> "day"
                    }
                    tv_month.text = itemDays[i]
                }
                .build()
                .setCancelable(true)
                .show()
    }

    interface OnDateSelect {
        fun onSelect(date: String)
    }

    fun setOnDateSelectListener(listener: OnDateSelect) {
        this.mOnDateSelect = listener
    }

    lateinit var mOnDateSelect: OnDateSelect
}