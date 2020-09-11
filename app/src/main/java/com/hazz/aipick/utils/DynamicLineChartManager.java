package com.hazz.aipick.utils;

import android.graphics.Color;

import com.blankj.utilcode.util.LogUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.hazz.aipick.mvp.model.InComing;
import com.hazz.aipick.widget.XYMarkerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DynamicLineChartManager {
    private LineChart lineChart;
    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;
    private List<String> timeList = new ArrayList<>(); //存储x轴的时间
    private LineDataSet mDataSetB;
    private LineDataSet mDataSetA;
    List<InComing> list = new ArrayList<>();

    //多条曲线
    public DynamicLineChartManager(LineChart mLineChart) {
        this.lineChart = mLineChart;
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
        lineChart.setNoDataText("暂无数据");
        lineChart.setAutoScaleMinMaxEnabled(true);
        setDescription("");
        //折线图例 标签 设置
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);

        leftAxis.setDrawGridLines(false);
        leftAxis.setGridColor(Color.parseColor("#E4EEF9"));
        leftAxis.setAxisLineColor(Color.parseColor("#293559"));
        leftAxis.setDrawZeroLine(true);
        leftAxis.setGranularity(1);
        leftAxis.setTextColor(Color.parseColor("#6371A0"));
        rightAxis.setEnabled(false);
        leftAxis.setValueFormatter((value, axis) -> {
            LogUtils.e(value);
            float temp = Math.abs(value);
            if (temp < 10000) {
                return String.format("$%.2f", value);
            } else if (temp < 1000000) {
                return String.format("$%.2fW", value / 10000);
            } else {
                return String.format("$%.2fM", value / 1000000);
            }
        });

        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);

        xAxis.setDrawGridLines(false);
        xAxis.setGridColor(Color.parseColor("#E4EEF9"));
        xAxis.setAxisLineColor(Color.parseColor("#293559"));
        xAxis.setTextColor(Color.parseColor("#6371A0"));
        xAxis.setValueFormatter((value, axis) -> timeList.get((int) value % timeList.size()));

        lineChart.setAutoScaleMinMaxEnabled(true);

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
    public void setYAxis(float max, float min, int labelCount) {
        if (max < min) {
            return;
        }
        leftAxis.setAxisMaximum(max);
        leftAxis.setAxisMinimum(min);
        leftAxis.setLabelCount(labelCount, false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGridColor(Color.parseColor("#E4EEF9"));
        leftAxis.setAxisLineColor(Color.parseColor("#AEB9CE"));

        rightAxis.setEnabled(false);

        lineChart.invalidate();
    }

    /**
     * 动态添加数据（多条折线图）
     */
    public void setXValue(List<String> xValue) {
        timeList.clear();
        timeList.addAll(xValue);
        xAxis.setLabelCount(timeList.size(), false);
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setAxisMaximum((float) (timeList.size() - 0.5));
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (xValue.size() == 0) return "";
                int pos = (int) Math.abs(value % xValue.size());
                String s = xValue.get(pos);
                LogUtils.e(value, xValue, s);
                return s;
            }
        });
        lineChart.invalidate();
    }


    public void setDoubleLineData(List<Integer> colour, List<InComing> list) {
        this.list.clear();
        this.list.addAll(list);

        //设置折线图横跨距离
        ArrayList<Entry> yValue1 = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            yValue1.add(new Entry(i, Float.parseFloat(list.get(i).buy)));
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
        List<ILineDataSet> lineDataSets = new ArrayList<>();
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


    public void setData(List<Integer> colour, @NotNull List<String> xValue, @NotNull List<Float> dealBuy, @NotNull List<Float> dealSell) {
        if (xValue.size() == 0) {
            lineChart.setData(new LineData(new ArrayList<>()));
            return;
        }
        setXValue(xValue);
        LineDataSet buy = createDateSet(dealBuy, "buy", colour.get(0));
        LineDataSet sell = createDateSet(dealSell, "sell", colour.get(1));

        List<ILineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(buy);
        lineDataSets.add(sell);

        LineData data = new LineData(lineDataSets);
        lineChart.setData(data);
        lineChart.setVisibleXRange(0, 5);
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // 获取Entry
                int iEntry = (int) e.getX();
                // 获取选中value的坐标

                XYMarkerView mv = new XYMarkerView(lineChart.getContext(), lineChart, iEntry, dealBuy, dealSell);
                lineChart.setMarker(mv);

            }

            @Override
            public void onNothingSelected() {
                lineChart.setMarker(null);
            }
        });

    }

    public LineDataSet createDateSet(List<Float> data, String label, int color) {
        ArrayList<Entry> yValue = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            yValue.add(new Entry(i, data.get(i)));
        }
        LineDataSet dataSet = new LineDataSet(yValue, label);
        dataSet.setLineWidth(1.5f);
        if (data.size() == 1) {
            dataSet.setDrawCircles(true);
        } else {
            dataSet.setDrawCircles(false);
        }

        dataSet.setDrawCircleHole(true);
        dataSet.setCircleColor(color);
        dataSet.setColor(color);
        dataSet.setFillColor(color);
        dataSet.setCircleColorHole(color);
        dataSet.setDrawValues(false);
//        dataSet.setHighLightColor(Color.parseColor("#AEB9CE"));
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setValueTextSize(10f);

        return dataSet;
    }
}
