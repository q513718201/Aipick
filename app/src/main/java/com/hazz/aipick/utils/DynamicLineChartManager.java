package com.hazz.aipick.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointD;
import com.hazz.aipick.mvp.model.InComing;
import com.hazz.aipick.widget.XYMarkerView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DynamicLineChartManager {
    private LineChart lineChart;
    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;
    private LineData lineData;
    private LineDataSet lineDataSet;
    private List<ILineDataSet> lineDataSets = new ArrayList<>();
    private SimpleDateFormat df = new SimpleDateFormat("MM-dd");//设置日期格式  
    private List<String> timeList = new ArrayList<>(); //存储x轴的时间
    private LineDataSet mDataSetB;
    private LineDataSet mDataSetA;
    private Context mContext;
    List<InComing> list;

    //多条曲线
    public DynamicLineChartManager(Context context, LineChart mLineChart) {
        this.lineChart = mLineChart;
        this.mContext = context;
        leftAxis = lineChart.getAxisLeft();
        rightAxis = lineChart.getAxisRight();
        xAxis = lineChart.getXAxis();
        initLineChart();

    }

    /**
     * 初始化LineChar
     */
    private void initLineChart() {

        lineChart.setDrawGridBackground(false);
        //显示边界
        lineChart.setDrawBorders(false);
        lineChart.setScaleYEnabled(false);
        lineChart.setNoDataText("暂无收益记录");
        lineChart.setAutoScaleMinMaxEnabled(true);
        setDescription("");
        //折线图例 标签 设置
        Legend legend = lineChart.getLegend();
        legend.setTextSize(11f);
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setWordWrapEnabled(true);
        legend.setForm(Legend.LegendForm.NONE);

        leftAxis.setLabelCount(6, false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGridColor(Color.parseColor("#E4EEF9"));
        leftAxis.setAxisLineColor(Color.parseColor("#293559"));
        leftAxis.setDrawZeroLine(true);
        leftAxis.setGranularity(1);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setTextColor(Color.parseColor("#6371A0"));
        rightAxis.setEnabled(false);

        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);

        xAxis.setDrawGridLines(false);
        xAxis.setGridColor(Color.parseColor("#E4EEF9"));
        xAxis.setAxisLineColor(Color.parseColor("#293559"));
        xAxis.setTextColor(Color.parseColor("#6371A0"));
        xAxis.setValueFormatter((value, axis) -> timeList.get((int) value % timeList.size()));


        lineChart.setAutoScaleMinMaxEnabled(true);

        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // 获取Entry
                int iEntry = (int) e.getX();
                float valEntry = e.getY();

                // 获取选中value的坐标
                MPPointD p = lineChart.getPixelForValues(e.getX(), e.getY(), YAxis.AxisDependency.LEFT);
                double xValuePos = p.x;
                double yValuePos = p.y;

                XYMarkerView mv = new XYMarkerView(mContext, lineChart, iEntry, list);
                mv.setChartView(lineChart);
                lineChart.setMarker(mv);

            }

            @Override
            public void onNothingSelected() {

            }
        });

//        Matrix matrix = new Matrix();
//        // 根据数据量来确定 x轴缩放大倍
//
//        matrix.postScale(4.0f, 1.0f);
//
//        // 在图表动画显示之前进行缩放
//        lineChart.getViewPortHandler().refresh(matrix, lineChart, false);
//        // x轴执行动画
//        lineChart.animateX(500);
    }


    /**
     * 设置Y轴值
     *
     * @param max
     * @param min
     * @param labelCount
     */
//    public void setYAxis(float max, float min, int labelCount) {
//        if (max < min) {
//            return;
//        }
//        leftAxis.setAxisMaximum(max);
//        leftAxis.setAxisMinimum(min);
//        leftAxis.setLabelCount(labelCount, false);
//        leftAxis.setDrawGridLines(false);
//        leftAxis.setGridColor(Color.parseColor("#E4EEF9"));
//        leftAxis.setAxisLineColor(Color.parseColor("#AEB9CE"));
//
//        rightAxis.setEnabled(false);
//
//        lineChart.invalidate();
//    }

    /**
     * 动态添加数据（多条折线图）
     */
    public void setXValue(List<String> xValue) {
        timeList = xValue;
        xAxis.setLabelCount(timeList.size(), false);
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setAxisMaximum((float) (timeList.size() - 0.5));
        lineChart.invalidate();
    }


    public void setDoubleLineData(List<Integer> colour, List<InComing> list) {

        this.list = list;

        //设置折线图横跨距离
        ArrayList<Entry> yValue1 = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            yValue1.add(new Entry(i, Float.valueOf(list.get(i).buy)));
        }


        mDataSetA = new LineDataSet(yValue1, "");
        mDataSetA.setLineWidth(1.5f);
        if (list.size() == 1) {
            mDataSetA.setDrawCircles(true);
        } else {
            mDataSetA.setDrawCircles(false);
        }

        mDataSetA.setDrawCircleHole(true);
        mDataSetA.setCircleColor(colour.get(0));
        mDataSetA.setColor(colour.get(0));
        mDataSetA.setFillColor(colour.get(0));
        mDataSetA.setCircleColorHole(colour.get(0));

        mDataSetA.setDrawValues(false);
        mDataSetA.setHighLightColor(Color.parseColor("#AEB9CE"));
        mDataSetA.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        mDataSetA.setAxisDependency(YAxis.AxisDependency.LEFT);
        mDataSetA.setValueTextSize(10f);

        //设置折线数据 getChartData返回一个List<Entry>键值对集合标识 折线点的横纵坐标，"A"代表折线标识


        //设置折线图横跨距离
        ArrayList<Entry> yValue2 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
             yValue2.add(new Entry(i, Float.valueOf(list.get(i).sell)));
        }

        mDataSetB = new LineDataSet(yValue2, "");
        mDataSetB.setLineWidth(1.5f);
        if (list.size() == 1) {
            mDataSetB.setDrawCircles(true);
        } else {
            mDataSetB.setDrawCircles(false);
        }

        mDataSetB.setDrawCircleHole(true);
        mDataSetB.setCircleColor(colour.get(1));
        mDataSetB.setColor(colour.get(1));
        mDataSetB.setFillColor(colour.get(1));
        mDataSetB.setCircleColorHole(colour.get(1));
        mDataSetB.setDrawValues(false);
        mDataSetB.setHighLightColor(Color.parseColor("#AEB9CE"));
        mDataSetB.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        mDataSetB.setAxisDependency(YAxis.AxisDependency.LEFT);
        mDataSetB.setValueTextSize(10f);

        lineDataSets.add(mDataSetA);
        lineDataSets.add(mDataSetB);

        LineData data = new LineData(lineDataSets);
        lineChart.setData(data);

    }
//
//    public void setSingleLineData(List<Entry> list1, List<ChibiRate.ListBean> list) {
//        //设置折线图横跨距离
//        mDataSetA = new LineDataSet(list1, "");
//        mDataSetA.setLineWidth(1.5f);
//
//        mDataSetA.setDrawCircles(false);
//        mDataSetA.setDrawFilled(false);
//
//        mDataSetA.setColor(Color.parseColor("#00CAA9"));
//        mDataSetA.setDrawValues(false);
//        mDataSetA.setHighLightColor(Color.parseColor("#AEB9CE"));
//        mDataSetA.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        mDataSetA.setAxisDependency(YAxis.AxisDependency.LEFT);
//        mDataSetA.setValueTextSize(10f);
//        lineDataSets.add(mDataSetA);
//
//        LineData data = new LineData(lineDataSets);
//        lineChart.setData(data);
//
//    }


    /**
     * 设置高限制线
     *
     * @param high
     * @param name
     */
    public void setHightLimitLine(float high, String name, int color) {
        if (name == null) {
            name = "高限制线";
        }
        LimitLine hightLimit = new LimitLine(high, name);
        hightLimit.setLineWidth(4f);
        hightLimit.setTextSize(10f);
        hightLimit.setLineColor(color);
        hightLimit.setTextColor(color);
        leftAxis.addLimitLine(hightLimit);
        lineChart.invalidate();
    }

    /**
     * 设置低限制线
     *
     * @param low
     * @param name
     */
    public void setLowLimitLine(int low, String name) {
        if (name == null) {
            name = "低限制线";
        }
        LimitLine hightLimit = new LimitLine(low, name);
        hightLimit.setLineWidth(4f);
        hightLimit.setTextSize(10f);
        leftAxis.addLimitLine(hightLimit);
        lineChart.invalidate();
    }

    /**
     * 设置描述信息
     *
     * @param str
     */
    public void setDescription(String str) {
        Description description = new Description();
        description.setText(str);
        lineChart.setDescription(description);
        lineChart.invalidate();
    }


}
