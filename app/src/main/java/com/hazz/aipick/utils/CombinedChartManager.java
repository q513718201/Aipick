package com.hazz.aipick.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.blankj.utilcode.util.LogUtils;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.hazz.aipick.BuildConfig;
import com.hazz.aipick.R;
import com.hazz.aipick.widget.InfoMarkView;

import java.util.ArrayList;
import java.util.List;

public class CombinedChartManager {


    private CombinedChart mCombinedChart;
    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;

    public CombinedChartManager(CombinedChart combinedChart) {
        if (combinedChart == null) return;
        this.mCombinedChart = combinedChart;
        mCombinedChart.setLogEnabled(BuildConfig.DEBUG);
        leftAxis = mCombinedChart.getAxisLeft();
        rightAxis = mCombinedChart.getAxisRight();
        xAxis = mCombinedChart.getXAxis();
        initChart();
    }

    /**
     * 初始化Chart
     */
    public void initChart() {
        if (mCombinedChart == null) return;
        //不显示描述内容
        mCombinedChart.getDescription().setEnabled(false);

        mCombinedChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR,
                CombinedChart.DrawOrder.LINE
        });

        mCombinedChart.setNoDataText("暂无交易记录");
//        mCombinedChart.setBackgroundColor(Color.WHITE);
        mCombinedChart.setDrawGridBackground(false);
        mCombinedChart.setDrawBarShadow(false);
        mCombinedChart.setHighlightFullBarEnabled(false);
        //显示边界
        mCombinedChart.setDrawBorders(false);
        mCombinedChart.setScaleYEnabled(false);
        mCombinedChart.setAutoScaleMinMaxEnabled(true);

        //Y轴设置
//        rightAxis.setDrawGridLines(false);
        rightAxis.setEnabled(false);
        // 不从y轴发出横向直线
        leftAxis.setDrawGridLines(false);
        leftAxis.setTextColor(Color.parseColor("#718BA9"));
        leftAxis.setAxisLineColor(mCombinedChart.getContext().getResources().getColor(R.color.dilaog_btn_color));
        leftAxis.setDrawZeroLine(false);
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


        Transformer trans = mCombinedChart.getTransformer(YAxis.AxisDependency.LEFT);
        mCombinedChart.setXAxisRenderer(new CustomXAxisRenderer(mCombinedChart.getViewPortHandler(),
                mCombinedChart.getXAxis(), trans));

        //隐藏图例说明
        Legend legend = mCombinedChart.getLegend();
        legend.setEnabled(false);

////
//        Matrix matrix = new Matrix();
//        // 根据数据量来确定 x轴缩放大倍
//
//        matrix.postScale(4.0f, 1.0f);
//
//        // 在图表动画显示之前进行缩放
//        mCombinedChart.getViewPortHandler().refresh(matrix, mCombinedChart, false);
        // x轴执行动画
        mCombinedChart.animateY(500);
    }

    /**
     * 设置X轴坐标值
     *
     * @param xAxisValues x轴坐标集合
     */
    public void setXAxis(final List<String> xAxisValues) {

        //设置X轴在底部
        XAxis xAxis = mCombinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(0.01f);
        xAxis.setAvoidFirstLastClipping(false);
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (xAxisValues.size() == 0) return "";
                if ((int) Math.abs(value) > xAxisValues.size()) return "";
                int pos = (int) Math.abs(value % xAxisValues.size());
                String s = xAxisValues.get(pos);
                LogUtils.e(value, xAxisValues, s);
                return s;
            }
        });

        mCombinedChart.invalidate();
    }

    public class CustomXAxisRenderer extends XAxisRenderer {
        public CustomXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
            super(viewPortHandler, xAxis, trans);
        }

        @Override
        protected void drawLabel(Canvas c, String formattedLabel, float x, float y, MPPointF anchor, float angleDegrees) {

            float labelHeight = mXAxis.getTextSize();
            float labelInterval = 2f;
            String[] labels = formattedLabel.split(" ");
            LogUtils.e(x, y, labels);

            Paint mFirstLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mFirstLinePaint.setColor(Color.parseColor("#718BA9"));
            mFirstLinePaint.setTextAlign(Paint.Align.LEFT);
            mFirstLinePaint.setTextSize(Utils.convertDpToPixel(7f));
            mFirstLinePaint.setTypeface(mXAxis.getTypeface());

            Paint mSecondLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mSecondLinePaint.setColor(Color.parseColor("#718BA9"));
            mSecondLinePaint.setTextAlign(Paint.Align.LEFT);
            mSecondLinePaint.setTextSize(Utils.convertDpToPixel(7f));
            mSecondLinePaint.setTypeface(mXAxis.getTypeface());

            if (labels.length > 1) {
                Utils.drawXAxisValue(c, labels[0], x, y, mFirstLinePaint, anchor, angleDegrees);
                Utils.drawXAxisValue(c, labels[1], x, y + labelHeight + labelInterval, mSecondLinePaint, anchor, angleDegrees);
            } else {
                Utils.drawXAxisValue(c, formattedLabel, x, y, mFirstLinePaint, anchor, angleDegrees);
            }

        }
    }

    /**
     * 得到折线图(一条)
     *
     * @param lineChartY 折线Y轴值
     * @param lineName   折线图名字
     * @param lineColor  折线颜色
     * @return
     */
    private LineData getLineData(List<Float> lineChartY, String lineName, int lineColor) {
        LineData lineData = new LineData();

        ArrayList<Entry> yValue = new ArrayList<>();
        for (int i = 0; i < lineChartY.size(); i++) {
            yValue.add(new Entry(i, lineChartY.get(i)));
        }
        LineDataSet dataSet = new LineDataSet(yValue, lineName);

        dataSet.setColor(lineColor);
        dataSet.setCircleColor(lineColor);
        dataSet.setValueTextColor(lineColor);

        //显示值
        dataSet.setDrawValues(false);
        dataSet.setValueTextSize(10f);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setCubicIntensity(0.2f);
        dataSet.setCircleRadius(2f);
        dataSet.setCircleColorHole(Color.parseColor("#F5D54D"));
        dataSet.setDrawCircles(true);
        dataSet.setCircleColor(Color.parseColor("#F5D54D"));
        dataSet.setFillColor(Color.parseColor("#F5D54D"));
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineData.addDataSet(dataSet);
        return lineData;
    }

    /**
     * 得到柱状图
     *
     * @param barChartY Y轴值
     * @param barName   柱状图名字
     * @param barColor  柱状图颜色
     * @return
     */
    int green = Color.parseColor("#4cd964");
    int red = Color.parseColor("#ff3b30");

    private BarData getBarData(List<Float> barChartY, String barName, int barColor) {
        BarData barData = new BarData();
        ArrayList<BarEntry> yValues = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        for (int i = 0; i < barChartY.size(); i++) {
            yValues.add(new BarEntry(i, barChartY.get(i)));
            if (barChartY.get(i) < 0)
                colors.add(red);
            else
                colors.add(green);
        }

        BarDataSet barDataSet = new BarDataSet(yValues, barName);
        barDataSet.setDrawValues(false);
        barDataSet.setValueTextSize(10f);

        barDataSet.setColors(colors);
        barDataSet.setValueTextColors(colors);
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        barData.addDataSet(barDataSet);
        //以下是为了解决 柱状图 左右两边只显示了一半的问题 根据实际情况 而定
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setAxisMaximum(barChartY.size() - 0.5f);


        return barData;
    }

//
//    /**
//     * 得到柱状图(多条)
//     *
//     * @param barChartYs Y轴值
//     * @param barNames   柱状图名字
//     * @param barColors  柱状图颜色
//     * @return
//     */
//
//    private BarData getBarData(List<List<Float>> barChartYs, List<String> barNames, List<Integer> barColors) {
//        List<IBarDataSet> lists = new ArrayList<>();
//        for (int i = 0; i < barChartYs.size(); i++) {
//            ArrayList<BarEntry> entries = new ArrayList<>();
//
//            for (int j = 0; j < barChartYs.get(i).size(); j++) {
//                entries.add(new BarEntry(j, barChartYs.get(i).get(j)));
//            }
//            BarDataSet barDataSet = new BarDataSet(entries, barNames.get(i));
//
//            barDataSet.setColor(barColors.get(i));
//            barDataSet.setValueTextColor(barColors.get(i));
//            barDataSet.setValueTextSize(10f);
//            barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
//            lists.add(barDataSet);
//        }
//        BarData barData = new BarData(lists);
//
//        int amount = barChartYs.size(); //需要显示柱状图的类别 数量
//        float groupSpace = 0.12f; //柱状图组之间的间距
//        float barSpace = 20f; // x4 DataSet
//        float barWidth = 40f; // x4 DataSet
//
//        // (0.2 + 0.02) * 4 + 0.12 = 1.00 即100% 按照百分百布局
//        //柱状图宽度
//        barData.setBarWidth(barWidth);
//        //(起始点、柱状图组间距、柱状图之间间距)
//        barData.groupBars(0, groupSpace, barSpace);
//        return barData;
//    }

    /**
     * 显示混合图(柱状图+折线图)
     *
     * @param xAxisValues X轴坐标
     * @param barChartY   柱状图Y轴值
     * @param lineChartY  折线图Y轴值
     * @param barName     柱状图名字
     * @param lineName    折线图名字
     * @param type        类型
     * @param lineColor   折线图颜色
     */

    public void showCombinedChart(
            List<String> xAxisValues, List<Float> barChartY, List<Float> lineChartY
            , String barName, String lineName, String type, int lineColor) {

        int max;
        if (type.equals("month")) {
            max = 12;
        } else {
            max = 31;
        }
        initChart();
        LogUtils.e(barChartY, lineChartY);
        if (xAxisValues.size() == 0) return;

        CombinedData combinedData = new CombinedData();

        combinedData.setData(getBarData(barChartY, barName, max));
        combinedData.setData(getLineData(lineChartY, lineName, lineColor));

        setXAxis(xAxisValues);

        mCombinedChart.setVisibleXRange(0, 7);
//        mCombinedChart.setMaxVisibleValueCount(xAxisValues.size());
        mCombinedChart.setData(combinedData);
        mCombinedChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // 获取Entry
                int iEntry = (int) e.getX();
                // 获取选中value的坐标
                InfoMarkView maker = new InfoMarkView(mCombinedChart, iEntry, combinedData, type.equals("month") ? 0 : 1);
                mCombinedChart.setMarker(maker);
            }

            @Override
            public void onNothingSelected() {
                mCombinedChart.setMarker(null);
            }
        });
        mCombinedChart.invalidate();
    }

    private void setYAxis(float max, float min, int showCount) {
        leftAxis.setAxisMaximum(max);
        leftAxis.setAxisMinimum(min);
        leftAxis.setLabelCount(showCount);
    }

}
