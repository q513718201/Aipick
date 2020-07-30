package com.hazz.aipick.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.hazz.aipick.R;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import java.util.List;


public class XYMarkerView extends MarkerView {

    private TextView tvData;
    private TextView tv1;
    private TextView tv2;

    private int iEntry;
    private LineChart lineChart;
   // private List<Suanli.ListBean> list;

    public XYMarkerView(Context context, LineChart lineChart, int iEntry, List<String> list) {
        super(context, R.layout.custom_marker_view);
       // this.list = list;
        this.iEntry = iEntry;
        this.lineChart = lineChart;
        tvData = (TextView) findViewById(R.id.tv_data);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);


    }

    //回调函数每次MarkerView重绘,可以用来更新内容(用户界面)
    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
       // tvData.setText(list.get(iEntry).day_label);

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -(getHeight() + DensityUtil.dp2px(100)));
    }
}

