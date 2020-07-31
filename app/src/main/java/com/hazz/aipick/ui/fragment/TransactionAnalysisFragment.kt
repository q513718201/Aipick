package com.hazz.aipick.ui.fragment


import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.view.View
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseFragment
import com.hazz.aipick.mvp.contract.InComingContract
import com.hazz.aipick.mvp.model.InComing
import com.hazz.aipick.mvp.presenter.InComingPresenter
import com.hazz.aipick.utils.DynamicLineChartManager
import kotlinx.android.synthetic.main.dialog_choose.view.tv_cancle
import kotlinx.android.synthetic.main.dialog_choose_day.view.*
import kotlinx.android.synthetic.main.dialog_choose_year.view.*
import kotlinx.android.synthetic.main.fragment_chart.*


class TransactionAnalysisFragment : BaseFragment(), OnChartValueSelectedListener, InComingContract.incomingView {


    override fun onNothingSelected() {

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
    }


    private var mTitle:String? =null
    private var mInComingPresenter: InComingPresenter =InComingPresenter(this)

    private var dynamicLineChartManager: DynamicLineChartManager? = null
    private val colour: MutableList<Int> = mutableListOf() //折线颜色集合

    private val xValue: MutableList<String> = mutableListOf() //X轴坐标

    private val yValue: MutableList<Float> = mutableListOf() //y轴坐标


    companion object {
        fun getInstance(title:String): TransactionAnalysisFragment {
            val fragment = TransactionAnalysisFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }


    override fun getLayoutId(): Int= R.layout.fragment_chart

    override fun initView() {


        //折线颜色
        colour.add(Color.parseColor("#15F8D3"))
        colour.add(Color.parseColor("#FF7C95"))
        dynamicLineChartManager = DynamicLineChartManager(activity, line_chart)


        mInComingPresenter.getIncoming("1month")

        initLineChart()
        tv_year.setOnClickListener {
            showBottomYear("shouyi")
        }
        tv_month.setOnClickListener {
            showBottomDay("shouyi")
        }
        tv_year1.setOnClickListener {
            showBottomDay("yingkui")
        }
        tv_month1.setOnClickListener {
            showBottomYear("yingkui")
        }
    }

    private fun showBottomDay(s: String) {
        var bottomSheet= BottomSheetDialog(activity!!)
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


    private fun showBottomYear(type:String) {
       var bottomSheet= BottomSheetDialog(activity!!)
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




    override fun lazyLoad() {

    }

    override fun getIncoming(msg: List<InComing>) {
        if(!msg.isNullOrEmpty()){
            for(a in msg){
                xValue.add(a.day_label)
            }
            dynamicLineChartManager!!.setXValue(xValue)
            dynamicLineChartManager!!.setDoubleLineData(colour, msg)
        }

    }


}