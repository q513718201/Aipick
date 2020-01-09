package com.hazz.aipick.ui.fragment


import android.os.Bundle
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseFragment
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_chart.*
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
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
        lineChart.setDrawGridBackground(false);
        //是否显示边界
        lineChart.setDrawBorders(true);
        //是否可以拖动
        lineChart.setDragEnabled(false);
        //是否有触摸事件
        lineChart.setTouchEnabled(true);
        //设置XY轴动画效果
        lineChart.animateY(2500);
        lineChart.animateX(1500);

        /***XY轴的设置***/
         var xAxis = lineChart.getXAxis();
        var leftYAxis = lineChart.getAxisLeft();
        var rightYaxis = lineChart.getAxisRight();
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        //保证Y轴从0开始，不然会上移一点
        leftYAxis.setAxisMinimum(0f);
        rightYaxis.setAxisMinimum(0f);

        /***折线图例 标签 设置***/
        var legend = lineChart.getLegend();
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
         legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(12f);
        //显示位置 左下方
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false)

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