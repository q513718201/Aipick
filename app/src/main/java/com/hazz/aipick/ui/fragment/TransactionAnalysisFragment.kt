package com.hazz.aipick.ui.fragment


import android.os.Bundle
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseFragment
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_chart.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.Legend.LegendOrientation
import com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment
import com.github.mikephil.charting.components.Legend.LegendVerticalAlignment
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import android.graphics.Color
import android.support.design.widget.BottomSheetDialog
import android.view.View
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import kotlinx.android.synthetic.main.dialog_choose.view.*
import kotlinx.android.synthetic.main.dialog_choose.view.tv_cancle
import kotlinx.android.synthetic.main.dialog_choose_day.view.*
import kotlinx.android.synthetic.main.dialog_choose_year.view.*


class TransactionAnalysisFragment : BaseFragment(), OnChartValueSelectedListener {


    override fun onNothingSelected() {

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
    }


    private var mTitle:String? =null

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

        initBarChart()
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

        chart.setOnChartValueSelectedListener(this)

        // no description text
        chart.description.isEnabled = false

        // enable touch gestures
        chart.setTouchEnabled(true)

        chart.dragDecelerationFrictionCoef = 0.9f

        // enable scaling and dragging
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setDrawGridBackground(false)
        chart.isHighlightPerDragEnabled = true

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true)
        val l = chart.legend
        l.setForm(LegendForm.LINE)

        l.textSize = 11f
        l.textColor = Color.WHITE
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
//        l.setYOffset(11f);

        val xAxis = chart.xAxis
        xAxis.textSize = 11f
        xAxis.textColor = Color.WHITE
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)

        val leftAxis = chart.axisLeft
        leftAxis.textColor = ColorTemplate.getHoloBlue()
        leftAxis.axisMaximum = 200f
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawGridLines(false)
        leftAxis.isGranularityEnabled = true
        chart.axisRight.isEnabled=false
        setData(10, 30F)
    }

    private fun setData(count: Int, range: Float) {

        val values1 = java.util.ArrayList<Entry>()

        for (i in 0 until count) {
            val `val` = (Math.random() * (range / 2f)).toFloat() + 50
            values1.add(Entry(i.toFloat(), `val`))
        }

        val values2 = java.util.ArrayList<Entry>()

        for (i in 0 until count) {
            val `val` = (Math.random() * range).toFloat() + 450
            values2.add(Entry(i.toFloat(), `val`))
        }

        val values3 = java.util.ArrayList<Entry>()

        for (i in 0 until count) {
            val `val` = (Math.random() * range).toFloat() + 500
            values3.add(Entry(i.toFloat(), `val`))
        }

        val set1: LineDataSet
        val set2: LineDataSet
        val set3: LineDataSet

        if (chart.data != null && chart.data.dataSetCount > 0) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set2 = chart.data.getDataSetByIndex(1) as LineDataSet
            set3 = chart.data.getDataSetByIndex(2) as LineDataSet
            set1.values = values1
            set2.values = values2
            set3.values = values3
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values1, "")

            set1.axisDependency = YAxis.AxisDependency.LEFT
            set1.color = ColorTemplate.getHoloBlue()
            set1.setCircleColor(Color.WHITE)
            set1.lineWidth = 2f
            set1.circleRadius = 3f
            set1.fillAlpha = 65
            set1.setDrawCircles(false)
            set1.mode=LineDataSet.Mode.CUBIC_BEZIER
            set1.fillColor = ColorTemplate.getHoloBlue()
            set1.highLightColor = Color.rgb(244, 117, 117)
            set1.setDrawCircleHole(false)
            //set1.setFillFormatter(new MyFillFormatter(0f));
            //set1.setDrawHorizontalHighlightIndicator(false);
            //set1.setVisible(false);
            //set1.setCircleHoleColor(Color.WHITE);

            // create a dataset and give it a type
            set2 = LineDataSet(values2, "")
            set2.axisDependency = YAxis.AxisDependency.RIGHT
            set2.color = Color.RED
            set2.setCircleColor(Color.WHITE)
            set2.lineWidth = 2f
            set2.mode=LineDataSet.Mode.CUBIC_BEZIER
            set2.setDrawCircles(true)
            set2.circleRadius = 3f
            set2.fillAlpha = 65
            set2.fillColor = Color.RED
            set2.setDrawCircleHole(true)
            set2.highLightColor = Color.rgb(244, 117, 117)
            //set2.setFillFormatter(new MyFillFormatter(900f));


            // create a data object with the data sets
            val data = LineData(set1, set2)
            data.setValueTextColor(Color.WHITE)
            data.setValueTextSize(9f)

            // set data
            chart.data = data
        }
    }

    private fun initBarChart() {

    }

    override fun lazyLoad() {

    }




}