package com.hazz.aipick.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.hazz.aipick.R;
import com.hazz.aipick.mvp.model.InComing;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.List;

import static com.hazz.aipick.utils.BigDecimalUtil.formatFloat;


public class XYMarkerView extends MarkerView {

    private TextView tvData;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private int iEntry;
    private LineChart lineChart;
    private List<InComing> list;

    public XYMarkerView(Context context, Chart<?> lineChart, int iEntry, List<InComing> list) {
        super(context, R.layout.custom_marker_view);
        this.list = list;
        this.iEntry = iEntry;
        setChartView(lineChart);
        tvData = (TextView) findViewById(R.id.tv_data);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);

    }

    private List<Float> dealBuy;
    private List<Float> dealSell;

    public XYMarkerView(Context mContext, LineChart lineChart, int iEntry, List<Float> dealBuy, List<Float> dealSell) {
        super(mContext, R.layout.custom_marker_view);
        this.iEntry = iEntry;
        this.dealBuy = dealBuy;
        this.dealSell = dealSell;
        setChartView(lineChart);
        tvData = (TextView) findViewById(R.id.tv_data);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
    }

    //回调函数每次MarkerView重绘,可以用来更新内容(用户界面)
    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (getChartView() == null) return;
        tvData.setText(getChartView().getXAxis().getFormattedLabel(iEntry));
        tv1.setText("买入$" + formatFloat(dealBuy.get(iEntry)));
        tv2.setText("卖出$" + formatFloat(dealSell.get(iEntry)));
        tv3.setText("盈亏$" + formatFloat(dealSell.get(iEntry) - dealBuy.get(iEntry)));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -(getHeight() + DensityUtil.dp2px(100)));
    }
}

