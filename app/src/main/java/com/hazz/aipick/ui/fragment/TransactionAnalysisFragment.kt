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

        // set an alternative background color
        chart.setBackgroundColor(Color.LTGRAY)
        val l = chart.legend
        l.setForm(LegendForm.LINE)

        l.setTextSize(11f)
        l.setTextColor(Color.WHITE)
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT)
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        l.setDrawInside(false)
//        l.setYOffset(11f);

        val xAxis = chart.xAxis
        xAxis.textSize = 11f
        xAxis.textColor = Color.WHITE
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        val leftAxis = chart.axisLeft
        leftAxis.textColor = ColorTemplate.getHoloBlue()
        leftAxis.axisMaximum = 200f
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawGridLines(true)
        leftAxis.isGranularityEnabled = true

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
            set1 = LineDataSet(values1, "DataSet 1")

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
            set2 = LineDataSet(values2, "DataSet 2")
            set2.axisDependency = YAxis.AxisDependency.RIGHT
            set2.color = Color.RED
            set2.setCircleColor(Color.WHITE)
            set2.lineWidth = 2f
            set2.mode=LineDataSet.Mode.CUBIC_BEZIER
            set2.setDrawCircles(false)
            set2.circleRadius = 3f
            set2.fillAlpha = 65
            set2.fillColor = Color.RED
            set2.setDrawCircleHole(false)
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
        mChart.setOnChartValueSelectedListener(this)
        mChart.setDrawBarShadow(false)
        mChart.setDrawValueAboveBar(true)
        mChart.getDescription().setEnabled(false)
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60)
        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false)
        mChart.setDrawGridBackground(false)

        //        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

        //自定义坐标轴适配器，配置在X轴，xAxis.setValueFormatter(xAxisFormatter);
        //   val xAxisFormatter = XAxisValueFormatter()

        val xAxis = mChart.getXAxis()
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        // xAxis.setTypeface(mTfLight)//可以去掉，没什么用
        xAxis.setDrawAxisLine(false)
        xAxis.setGranularity(1f)
        //  xAxis.setValueFormatter(xAxisFormatter)


        //自定义坐标轴适配器，配置在Y轴。leftAxis.setValueFormatter(custom);
        // val custom = MyAxisValueFormatter()

        //设置限制临界线
        val limitLine = LimitLine(3f, "临界点")
        limitLine.lineColor = Color.GREEN
        limitLine.lineWidth = 1f
        limitLine.textColor = Color.GREEN

        //获取到图形左边的Y轴
        val leftAxis = mChart.getAxisLeft()
        leftAxis.addLimitLine(limitLine)

        leftAxis.setLabelCount(8, false)
        //  leftAxis.setValueFormatter(custom)
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.setSpaceTop(15f)
        leftAxis.setAxisMinimum(0f)

        //获取到图形右边的Y轴，并设置为不显示
        mChart.getAxisRight().setEnabled(false)

        //图例设置
        val legend = mChart.getLegend()
        legend.setVerticalAlignment(LegendVerticalAlignment.BOTTOM)
        legend.setHorizontalAlignment(LegendHorizontalAlignment.LEFT)
        legend.setOrientation(LegendOrientation.HORIZONTAL)
        legend.setDrawInside(false)
        legend.setForm(LegendForm.SQUARE)
        legend.setFormSize(9f)
        legend.setTextSize(11f)
        legend.setXEntrySpace(4f)

        //如果点击柱形图，会弹出pop提示框.XYMarkerView为自定义弹出框
        //        val mv = XYMarkerView(this, xAxisFormatter)
        //        mv.setChartView(mChart)
        //        mChart.setMarker(mv)
        setBarChartData()
    }

    private fun setBarChartData() {

        val yVals1 = ArrayList<BarEntry>()


        //在这里设置自己的数据源,BarEntry 只接收float的参数，
        //图形横纵坐标默认为float形式，如果想展示文字形式，需要自定义适配器，
        yVals1.add(BarEntry(0f, 4f))
        yVals1.add(BarEntry(1f, 2f))
        yVals1.add(BarEntry(2f, 6f))
        yVals1.add(BarEntry(3f, 1f))
        yVals1.add(BarEntry(4f, 3f))
        yVals1.add(BarEntry(5f, 5f))
        val set1: BarDataSet

        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set1 = mChart.getData().getDataSetByIndex(0) as BarDataSet
            set1.values = yVals1
            mChart.getData().notifyDataChanged()
            mChart.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(yVals1, "The year 2017")
            set1.setDrawIcons(false)

            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)

            val data = BarData(dataSets)
            data.setValueTextSize(10f)
            data.barWidth = 0.9f

            mChart.setData(data)
        }
    }

    override fun lazyLoad() {

    }




}