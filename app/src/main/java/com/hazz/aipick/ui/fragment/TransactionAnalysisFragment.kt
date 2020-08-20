package com.hazz.aipick.ui.fragment


import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.view.View
import com.bigkoo.alertview.AlertView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.gson.Gson
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseFragment
import com.hazz.aipick.mvp.contract.InComingContract
import com.hazz.aipick.mvp.model.InComing
import com.hazz.aipick.mvp.presenter.InComingPresenter
import com.hazz.aipick.utils.AssetsUtil
import com.hazz.aipick.utils.BigDecimalUtil
import com.hazz.aipick.utils.CombinedChartManager
import com.hazz.aipick.utils.DynamicLineChartManager
import kotlinx.android.synthetic.main.dialog_choose.view.tv_cancle
import kotlinx.android.synthetic.main.dialog_choose_day.view.*
import kotlinx.android.synthetic.main.dialog_choose_year.view.*
import kotlinx.android.synthetic.main.fragment_chart.*
import java.nio.charset.Charset
import kotlin.random.Random


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


    override fun getLayoutId(): Int = R.layout.fragment_chart

    override fun initView() {

        //折线颜色
        colour.add(Color.parseColor("#15F8D3"))
        colour.add(Color.parseColor("#FF7C95"))
        dynamicLineChartManager = DynamicLineChartManager(activity, line_chart)



        initLineChart()
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
    }

    private var dataType = 0


    var current_year_type = "1month"
    var current_day_type = "month"
    private fun showYear() {
        val item_year = resources.getStringArray(R.array.item_year)
        AlertView.Builder()
                .setContext(context)
                .setStyle(AlertView.Style.ActionSheet)
                .setDestructive(*item_year)
                .setCancelText(getString(R.string.cancel))
                .setOnItemClickListener { any: Any, i: Int ->
                    current_year_type = when (i) {
                        0 -> {
                            "1month"
                        }
                        1 -> {
                            "2month"
                        }
                        else -> "1year"
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
                    {
                        current_day_type = when (i) {
                            1 -> {
                                "month"
                            }
                            else -> "day"
                        }

                    }

                }
                .build()
                .setCancelable(true)
                .show()
    }

    private fun showBottomDay(s: String) {
        var bottomSheet = BottomSheetDialog(activity!!)
        val view = layoutInflater.inflate(R.layout.dialog_choose_day, null)
        bottomSheet.setContentView(view)
        view.tv_cancle.setOnClickListener {
            bottomSheet.dismiss()
        }
        view.tv_day.setOnClickListener {
            bottomSheet.dismiss()
        }
        view.tv_month.setOnClickListener {
            bottomSheet.dismiss()
        }


        val viewById = bottomSheet!!.delegate.findViewById<View>(android.support.design.R.id.design_bottom_sheet)
        //设置布局背景透明
        viewById?.setBackgroundColor(resources.getColor(android.R.color.transparent))
        bottomSheet.show()

    }

    private fun showBottomYear(type: String) {
        var bottomSheet = BottomSheetDialog(activity!!)
        val view = layoutInflater.inflate(R.layout.dialog_choose_year, null)
        bottomSheet.setContentView(view)
        view.tv_cancle.setOnClickListener {
            bottomSheet.dismiss()
        }
        view.tv_one_month.setOnClickListener {
            bottomSheet.dismiss()
        }
        view.tv_three_month.setOnClickListener {
            bottomSheet.dismiss()
        }
        view.tv_one_year.setOnClickListener {
            bottomSheet.dismiss()
        }


        val viewById = bottomSheet!!.delegate.findViewById<View>(android.support.design.R.id.design_bottom_sheet)
        //设置布局背景透明
        viewById?.setBackgroundColor(resources.getColor(android.R.color.transparent))
        bottomSheet.show()

    }

    private fun initLineChart() {

    }

    var isDemo = "0"
    var id = ""
    override fun lazyLoad() {
        getData()

    }

    fun getData() {
        when (role) {
            "bot" -> {
                mInComingPresenter.getBotIncoming(current_year_type)
                mInComingPresenter.getBotTradeIncoming(current_year_type)
            }
            else -> {
                mInComingPresenter.getUserIncoming(id, current_year_type, current_day_type, isDemo)
                mInComingPresenter.getUserTradeIncoming(id, current_year_type, current_day_type, isDemo)
            }
        }
    }

    override fun getIncoming(msg: List<InComing>) {
        var assertsFileInBytes = AssetsUtil.getAssertsFileInBytes(context, "data.json")
        val json = String(assertsFileInBytes, Charset.defaultCharset())
        var msg: List<InComing> = Gson().fromJson<Array<InComing>>(json, Array<InComing>::class.java).toMutableList()

        if (!msg.isNullOrEmpty()) {
            for (a in msg) {
                xValue.add(a.month_label)
                a.gain = BigDecimalUtil.format(Random(1).nextInt(1000).toString(), 2)
                a.follow = BigDecimalUtil.format(Random(1).nextInt(100).toString(), 2)
                a.self = BigDecimalUtil.format(Random(1).nextInt(150).toString(), 2)
                a.total = BigDecimalUtil.format(Random(1).nextInt(500).toString(), 2)
            }
            dynamicLineChartManager!!.setXValue(xValue)
            dynamicLineChartManager!!.setDoubleLineData(colour, msg)
        }

    }

    override fun getTradeIncoming(msg: List<InComing>) {

        for (a in msg) {
            xValue.add(a.month_label)
            a.follow = BigDecimalUtil.format(Random(1000).nextInt(1000).toString(), 2)
            a.self = BigDecimalUtil.format(Random(1000).nextInt(1500).toString(), 2)
            a.total = BigDecimalUtil.format(Random(1000).nextInt(5000).toString(), 2)
            barChartY.add(a.self.toFloat())
            lineChartY.add(a.follow.toFloat())
        }


        val combinedChartManager = CombinedChartManager(mChart)
        combinedChartManager.initChart()
        combinedChartManager.showCombinedChart(xValue, barChartY, lineChartY, "", "",
                Color.parseColor("#1BAC8F"), Color.parseColor("#F0BC33"))
    }


}