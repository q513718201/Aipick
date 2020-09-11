package com.hazz.aipick.ui.fragment


import android.graphics.Color
import android.os.Bundle
import com.bigkoo.alertview.AlertView
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseFragment
import com.hazz.aipick.events.ChangeEvent
import com.hazz.aipick.mvp.contract.InComingContract
import com.hazz.aipick.mvp.model.InComing
import com.hazz.aipick.mvp.presenter.InComingPresenter
import com.hazz.aipick.utils.CombinedChartManager
import com.hazz.aipick.utils.DynamicLineChartManager
import com.hazz.aipick.utils.RxBus
import com.vinsonguo.klinelib.util.DateUtils
import kotlinx.android.synthetic.main.fragment_chart.*
import java.util.*
import kotlin.collections.HashMap


class TransactionAnalysisFragment : BaseFragment(), InComingContract.incomingView {

    private var role: String? = null
    private var mInComingPresenter: InComingPresenter = InComingPresenter(this)

    private lateinit var dynamicLineChartManager: DynamicLineChartManager
    private lateinit var combinedChartManager: CombinedChartManager
    private val colour: MutableList<Int> = mutableListOf() //折线颜色集合
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

        fun getInstance(id: String, role: String, isDemo: String): TransactionAnalysisFragment {
            val fragment = TransactionAnalysisFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.role = role
            fragment.id = id
            fragment.isDemo = isDemo
            return fragment
        }
    }


    override fun getLayoutId(): Int = R.layout.fragment_chart

    override fun initView() {
        //折线颜色
        colour.add(resources.getColor(R.color.main_color_green))
        colour.add(resources.getColor(R.color.main_color_red))
        dynamicLineChartManager = DynamicLineChartManager(line_chart)
        combinedChartManager = CombinedChartManager(mChart)
        tv_year.setOnClickListener {
            dataType = 0
            showYear()
        }
        tv_month.setOnClickListener {
            dataType = 0
            showDay()
        }
        tv_year1.setOnClickListener {
            dataType = 1
            showYear()
        }
        tv_month1.setOnClickListener {
            dataType = 1
            showDay()
        }
        RxBus.get().observerOnMain(this, ChangeEvent::class.java) {
            isDemo = it.isDemo
            if (isVisible)
                getData()

        }
    }

    private var dataType = 0
    private var currentYearType = "1year"
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
                    if (dataType == 0) {
                        tv_year.text = itemYear[i]
                    } else {
                        tv_year1.text = itemYear[i]
                    }
                    getSubData()
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
                    if (dataType == 0) {
                        tv_month.text = itemDays[i]
                    } else {
                        tv_month1.text = itemDays[i]
                    }

                    getSubData()
                }
                .build()
                .setCancelable(true)
                .show()
    }

    fun getSubData() {
        if (id == "-1") {
            when (dataType) {
                0 -> mInComingPresenter.getBotTradeIncoming(currentYearType)
                else -> mInComingPresenter.getBotIncoming(currentYearType)
            }
        } else {
            when (dataType) {
                0 -> mInComingPresenter.getUserTradeIncoming(id, currentYearType, currentDayType, isDemo)
                else -> mInComingPresenter.getUserIncoming(id, currentYearType, currentDayType, isDemo)
            }
        }
    }

    private var isDemo = "0"
    var id = ""
    override fun lazyLoad() {
        getData()
    }

    private fun getData() {
        if (id == "-1") {
            mInComingPresenter.getBotTradeIncoming(currentYearType)
            mInComingPresenter.getBotIncoming(currentYearType)
        } else {
            mInComingPresenter.getUserTradeIncoming(id, currentYearType, currentDayType, isDemo)
            mInComingPresenter.getUserIncoming(id, currentYearType, currentDayType, isDemo)
        }
    }

    //设置买卖曲线
    override fun getIncoming(msg: List<InComing>) {
        var tempBuy = HashMap<String, Float>()
        var tempSell = HashMap<String, Float>()
        for (inComing in msg) {
            when (currentDayType) {
                "month" -> {
                    val month = getOrDefault(tempSell, inComing.month_label, 0f)
                    tempSell[inComing.month_label] = month + inComing.sell.toFloat()
                    var buy = getOrDefault(tempBuy, inComing.month_label, 0f)
                    tempBuy[inComing.month_label] = buy + inComing.buy.toFloat()

                }
                else -> {
                    val month = getOrDefault(tempSell, inComing.day_label, 0f)
                    tempSell[inComing.day_label] = month + inComing.sell.toFloat()
                    var buy = getOrDefault(tempBuy, inComing.day_label, 0f)
                    tempBuy[inComing.day_label] = buy + inComing.buy.toFloat()
                }
            }
        }

        xValue.clear()
        for (entry in tempBuy.entries) {
            xValue.add(entry.key)
        }
        xValue.sort()
        var dealBuy = mutableListOf<Float>()
        var dealSell = mutableListOf<Float>()

        for (entry in xValue) {
            tempBuy[entry]?.let { dealBuy.add(it) }
            tempSell[entry]?.let { dealSell.add(it) }
        }
        dynamicLineChartManager!!.setData(colour, xValue, dealBuy, dealSell)

    }

    override fun getTradeIncoming(msg: List<InComing>) {
        dealData(msg)
    }

    private fun getOrDefault(map: Map<String, Float>, key: String, defaultValue: Float): Float {
        return map[key] ?: defaultValue
    }


    fun dealData(data: List<InComing>) {
        var mapBar = HashMap<String, Float>()//天或者月的总收益
        var total = 0f
        for (datum in data) {
            when (currentDayType) {
                "month" -> {
                    val month = getOrDefault(mapBar, datum.month_label, 0f)
                    mapBar[datum.month_label] = month + datum.total.toFloat()
                }
                else -> {
                    val month = getOrDefault(mapBar, datum.day_label, 0f)
                    mapBar[datum.day_label] = month + datum.total.toFloat()
                }
            }
        }
        xValue.clear()
        barChartY.clear()
        lineChartY.clear()
        for (entry in mapBar.entries) {
            xValue.add(entry.key)
        }
        xValue.sort()
        for (entry in xValue) {
            var value = getOrDefault(mapBar, entry, 0f)
            barChartY.add(value)
            total += value
            lineChartY.add(total)
        }

        combinedChartManager.showCombinedChart(xValue, barChartY, lineChartY, "", "", currentDayType, Color.parseColor("#FFF0BC34"))
    }

    class MapKeyComparator : Comparator<String> {
        override fun compare(o1: String, o2: String): Int {
            return (DateUtils.date2Long(o1) - DateUtils.date2Long(o2)).toInt()
        }
    }
}