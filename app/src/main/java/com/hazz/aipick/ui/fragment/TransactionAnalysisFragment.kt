package com.hazz.aipick.ui.fragment


import android.graphics.Color
import android.os.Bundle
import com.bigkoo.alertview.AlertView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.gson.Gson
import com.hazz.aipick.BuildConfig
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseFragment
import com.hazz.aipick.mvp.contract.InComingContract
import com.hazz.aipick.mvp.model.InComing
import com.hazz.aipick.mvp.presenter.InComingPresenter
import com.hazz.aipick.utils.AssetsUtil
import com.hazz.aipick.utils.CombinedChartManager
import com.hazz.aipick.utils.DynamicLineChartManager
import kotlinx.android.synthetic.main.fragment_chart.*
import java.nio.charset.Charset


class TransactionAnalysisFragment : BaseFragment(), OnChartValueSelectedListener, InComingContract.incomingView {


    override fun onNothingSelected() {
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
    }


    private var role: String? = null
    private var mInComingPresenter: InComingPresenter = InComingPresenter(this)

    private var dynamicLineChartManager: DynamicLineChartManager? = null
    private val colour: MutableList<Int> = mutableListOf() //折线颜色集合

    private val xValueTrade: MutableList<String> = mutableListOf() //X轴坐标
    private val xValue: MutableList<String> = mutableListOf() //X轴坐标


    var barChartY: MutableList<Float> = mutableListOf()
    var lineChartY: MutableList<Float> = mutableListOf()


    companion object {
        fun getInstance(id: String, role: String): TransactionAnalysisFragment {
            val fragment = TransactionAnalysisFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.role = role
            fragment.id = id
            return fragment
        }

        fun getInstance(role: String): TransactionAnalysisFragment {
            val fragment = TransactionAnalysisFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.role = role
            return fragment
        }
    }

    fun setIsDemo(demo: String) {
        isDemo = demo
        getData()
    }


    override fun getLayoutId(): Int = R.layout.fragment_chart

    override fun initView() {

        //折线颜色

        colour.add(resources.getColor(R.color.main_color_red))
        colour.add(resources.getColor(R.color.main_color_green))
        dynamicLineChartManager = DynamicLineChartManager(activity, line_chart)

        tv_year.setOnClickListener {
            dateType = 0
            showYear()
        }
        tv_month.setOnClickListener {
            dateType = 0
            showDay()
        }
        tv_year1.setOnClickListener {
            dateType = 1
            showYear()
        }
        tv_month1.setOnClickListener {
            dateType = 1
            showDay()
        }
    }

    private var dateType = 0
    private var currentYearType = "1month"
    private var currentDayType = "month"
    private fun showYear() {
        val itemYear = resources.getStringArray(R.array.item_year)
        AlertView.Builder()
                .setContext(context)
                .setStyle(AlertView.Style.ActionSheet)
                .setDestructive(*itemYear)
                .setCancelText(getString(R.string.cancel))
                .setOnItemClickListener { any: Any, i: Int ->
                    currentYearType = when (i) {
                        0 -> "1month"
                        1 -> "3month"
                        else -> "1year"
                    }
                    if (dateType == 0) {
                        tv_year.text = itemYear[i]
                    } else {
                        tv_year1.text = itemYear[i]
                    }
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
                .setCancelText(getString(R.string.cancel))
                .setOnItemClickListener { _: Any, i: Int ->

                    currentDayType = when (i) {
                        1 -> {
                            "month"
                        }
                        else -> "day"
                    }
                    if (dateType == 0) {
                        tv_month.text = itemDays[i]
                    } else {
                        tv_month1.text = itemDays[i]
                    }
                    getData()

                }
                .build()
                .setCancelable(true)
                .show()
    }


    private var isDemo = "0"
    var id = ""
    override fun lazyLoad() {
        getData()
    }

    private fun getData() {
        when (role) {
            "bot" -> {
                mInComingPresenter.getBotIncoming(currentYearType)
                mInComingPresenter.getBotTradeIncoming(currentYearType)
            }
            else -> {
                mInComingPresenter.getUserIncoming(id, currentYearType, currentDayType, isDemo)
                mInComingPresenter.getUserTradeIncoming(id, currentYearType, currentDayType, isDemo)
            }
        }
    }

    //设置买卖曲线
    override fun getIncoming(msg: List<InComing>) {
        if (BuildConfig.DEBUG) {
            var assertsFileInBytes = AssetsUtil.getAssertsFileInBytes(context?.applicationContext, "income.json")
            val json = String(assertsFileInBytes, Charset.defaultCharset())
            var msg: List<InComing> = Gson().fromJson<Array<InComing>>(json, Array<InComing>::class.java).toMutableList()
            xValue.clear()
            for (a in msg) {
                xValue.add(getLabel(a.month_label, a.day_label))
            }
            dynamicLineChartManager!!.setXValue(xValue)
            dynamicLineChartManager!!.setDoubleLineData(colour, msg)
            return
        }

        if (!msg.isNullOrEmpty()) {
            for (a in msg) {
                xValue.add(getLabel(a.month_label, a.day_label))
            }
            dynamicLineChartManager!!.setXValue(xValue)
            dynamicLineChartManager!!.setDoubleLineData(colour, msg)
        }

    }

    fun getLabel(month: String, day: String): String {
        if (currentDayType == "month") {
            return month
        }
        return day
    }

    override fun getTradeIncoming(msg: List<InComing>) {

        if (BuildConfig.DEBUG) {
            var assertsFileInBytes = AssetsUtil.getAssertsFileInBytes(context?.applicationContext, "income.json")
            val json = String(assertsFileInBytes, Charset.defaultCharset())
            var msg: List<InComing> = Gson().fromJson<Array<InComing>>(json, Array<InComing>::class.java).toMutableList()
            xValue.clear()
            barChartY.clear()
            lineChartY.clear()
            for (a in msg) {
                xValue.add(getLabel(a.month_label, a.day_label))
                barChartY.add(a.gain.toFloat())
                lineChartY.add(a.total.toFloat())
            }

        }
//        for (a in msg) {
//            xValue.add(getLabel(a.month_label, a.day_label))
//            barChartY.add(a.buy.toFloat())
//            lineChartY.add(a.total.toFloat())
//        }


        val combinedChartManager = CombinedChartManager(mChart)

        combinedChartManager.showCombinedChart(xValue, barChartY, lineChartY, "", "",
                Color.parseColor("#1BAC8F"), Color.parseColor("#FFF0BC34"))
    }


}