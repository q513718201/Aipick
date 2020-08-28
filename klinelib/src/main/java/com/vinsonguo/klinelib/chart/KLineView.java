package com.vinsonguo.klinelib.chart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.vinsonguo.klinelib.BuildConfig;
import com.vinsonguo.klinelib.R;
import com.vinsonguo.klinelib.model.HisData;
import com.vinsonguo.klinelib.model.Index;
import com.vinsonguo.klinelib.util.DataUtils;
import com.vinsonguo.klinelib.util.DisplayUtils;
import com.vinsonguo.klinelib.util.DoubleUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * kline
 * Created by guoziwei on 2017/10/26.
 */
public class KLineView extends BaseView implements CoupleChartGestureListener.OnAxisChangeListener {


    public static final int NORMAL_LINE = 0;
    /**
     * average line1
     */
    public static final int AVE_LINE = 1;
    /**
     * hide line1
     */
    public static final int INVISIABLE_LINE = 6;


    public static final int MA5 = 5;
    public static final int MA10 = 10;
    public static final int MA20 = 20;
    public static final int MA30 = 30;

    public static final int K = 31;
    public static final int D = 32;
    public static final int J = 33;

    public static final int DIF = 34;
    public static final int DEA = 35;
    public static final String TYPE_PREFIX = "ma";


    protected AppCombinedChart mChartPrice;
    protected AppCombinedChart mChartVolume;
    protected AppCombinedChart mChartMacd;
    protected AppCombinedChart mChartKdj;

    protected ChartInfoView mChartInfoView;
    protected Context mContext;


    /**
     * last price
     */
    private double mLastPrice;

    /**
     * yesterday close price
     */
    private double mLastClose;

    /**
     * the digits of the symbol
     */
    private int mDigits = 2;
    private ILineDataSet mTempAvg;
    private LineData mTempKLineData;


    public enum Mode {
        LINE, K
    }

    private State mState;


    public KLineView(Context context) {
        this(context, null);
    }

    public KLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_kline, this);
        mChartPrice = findViewById(R.id.price_chart);
        mChartVolume = findViewById(R.id.vol_chart);
        mChartMacd = findViewById(R.id.macd_chart);
        mChartKdj = findViewById(R.id.kdj_chart);
        mChartInfoView = findViewById(R.id.k_info);
        mChartInfoView.setChart(mChartPrice, mChartVolume, mChartMacd, mChartKdj);

        mChartPrice.setNoDataText(context.getString(R.string.loading));
        mState = new State();
        initChartPrice();
        initBottomChart(mChartVolume);
        initBottomChart(mChartMacd);
        initBottomChart(mChartKdj);
        setOffset();
        initChartListener();
    }

    public void showKdj() {
        mChartVolume.setVisibility(GONE);
        mChartMacd.setVisibility(GONE);
        mChartKdj.setVisibility(VISIBLE);
    }

    public void showMacd() {
        mChartVolume.setVisibility(GONE);
        mChartMacd.setVisibility(VISIBLE);
        mChartKdj.setVisibility(GONE);
    }

    public void showVolume() {
        mChartMacd.setVisibility(GONE);
        mChartKdj.setVisibility(GONE);
        mChartVolume.setVisibility(VISIBLE);
    }


    protected void initChartPrice() {
        mChartPrice.setScaleEnabled(true);
        mChartPrice.setDrawBorders(false);
        mChartPrice.setDragEnabled(true);
        mChartPrice.setScaleYEnabled(false);
        mChartPrice.setAutoScaleMinMaxEnabled(true);
        mChartPrice.setDragDecelerationEnabled(false);
        mChartPrice.setLogEnabled(BuildConfig.DEBUG);
        LineChartXMarkerView mvx = new LineChartXMarkerView(mContext, mData);
        mvx.setChartView(mChartPrice);
        mChartPrice.setXMarker(mvx);
        Legend lineChartLegend = mChartPrice.getLegend();
        lineChartLegend.setEnabled(false);

        XAxis xAxisPrice = mChartPrice.getXAxis();
        xAxisPrice.setDrawLabels(false);
        xAxisPrice.setDrawAxisLine(false);
        xAxisPrice.setDrawGridLines(true);
        xAxisPrice.setGranularity(1f);
        xAxisPrice.setAxisMinimum(-0.5f);
        xAxisPrice.setGridColor(getResources().getColor(R.color.grid_line_color));
        xAxisPrice.setGridLineWidth(getResources().getDimension(R.dimen.grid_line_width));


        YAxis axisRightPrice = mChartPrice.getAxisRight();
        axisRightPrice.setLabelCount(5, true);
        axisRightPrice.setDrawLabels(true);
        axisRightPrice.setDrawGridLines(true);
        axisRightPrice.setDrawAxisLine(false);
        axisRightPrice.setGridColor(getResources().getColor(R.color.grid_line_color));
        axisRightPrice.setGridLineWidth(getResources().getDimension(R.dimen.grid_line_width));

        axisRightPrice.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisRightPrice.setTextColor(mAxisColor);
        axisRightPrice.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return DoubleUtil.getStringByDigits(value, mDigits);
            }
        });

        //左侧
        YAxis axisLeft = mChartPrice.getAxisLeft();
        axisLeft.setDrawAxisLine(false);
        axisLeft.setDrawLabels(false);
        axisLeft.setDrawGridLines(false);

    }


    private void initChartListener() {
        mChartPrice.setOnChartGestureListener(new CoupleChartGestureListener(this, mChartPrice, mChartVolume, mChartMacd, mChartKdj));
        mChartVolume.setOnChartGestureListener(new CoupleChartGestureListener(this, mChartVolume, mChartPrice, mChartMacd, mChartKdj));
        mChartMacd.setOnChartGestureListener(new CoupleChartGestureListener(this, mChartMacd, mChartPrice, mChartVolume, mChartKdj));
        mChartKdj.setOnChartGestureListener(new CoupleChartGestureListener(this, mChartKdj, mChartPrice, mChartVolume, mChartMacd));
        mChartPrice.setOnChartValueSelectedListener(new InfoViewListener(mContext, mLastClose, mData, mChartInfoView, mChartVolume, mChartMacd, mChartKdj));
        mChartVolume.setOnChartValueSelectedListener(new InfoViewListener(mContext, mLastClose, mData, mChartInfoView, mChartPrice, mChartMacd, mChartKdj));
        mChartMacd.setOnChartValueSelectedListener(new InfoViewListener(mContext, mLastClose, mData, mChartInfoView, mChartPrice, mChartVolume, mChartKdj));
        mChartKdj.setOnChartValueSelectedListener(new InfoViewListener(mContext, mLastClose, mData, mChartInfoView, mChartPrice, mChartVolume, mChartMacd));

        mChartPrice.setOnTouchListener(new ChartInfoViewHandler(mChartPrice));
        mChartVolume.setOnTouchListener(new ChartInfoViewHandler(mChartVolume));
        mChartMacd.setOnTouchListener(new ChartInfoViewHandler(mChartMacd));
        mChartKdj.setOnTouchListener(new ChartInfoViewHandler(mChartKdj));
    }

    public void initData(List<HisData> hisDatas) {

         resetChart();
        mData.addAll(hisDatas);

        CombinedData combinedData;
        if (getState().getMode() == Mode.K) {
            combinedData = getKCombinedData();
        } else {

            combinedData = getLineCombinedData();
        }


        initChartPriceData(combinedData);
        initChartVolumeData();
        initChartMacdData();
        initChartKdjData();

        scaleChart();


        HisData hisData = getLastData();
        if (getState().getMode() == Mode.K) {
            mChartPrice.setDrawMADescription(true);
            if (getState().getMainIndex() != Index.NONE) {
                mChartPrice.getDescription().setEnabled(true);
                combinedData.getLineData().removeDataSet(0);
            } else {
                combinedData.setData(new LineData());
            }
        } else {
            mChartPrice.getDescription().setEnabled(false);
        }
        updateDescription(hisData);

    }

    private void scaleChart() {
        mChartPrice.zoom(MAX_COUNT * 1f / INIT_COUNT, 0, 0, 0);
        mChartVolume.zoom(MAX_COUNT * 1f / INIT_COUNT, 0, 0, 0);
        mChartMacd.zoom(MAX_COUNT * 1f / INIT_COUNT, 0, 0, 0);
        mChartKdj.zoom(MAX_COUNT * 1f / INIT_COUNT, 0, 0, 0);
    }

    private void updateDescription(HisData hisData) {
        String ma = String.format(Locale.getDefault(), "MA5:%.2f  MA10:%.2f  MA20:%.2f  MA30:%.2f",
                hisData.getMa5(), hisData.getMa10(), hisData.getMa20(), hisData.getMa30());
        setDescription(mChartPrice, ma, getResources().getColor(R.color.text_color), Paint.Align.LEFT);
        setDescription(mChartVolume, "VOL:  " + hisData.getVol(),
                getResources().getColor(R.color.text_color), Paint.Align.LEFT);
        setDescription(mChartMacd, String.format(Locale.getDefault(), "MACD:%.2f  DEA:%.2f  DIF:%.2f",
                hisData.getMacd(), hisData.getDea(), hisData.getDif()));
        setDescription(mChartKdj, String.format(Locale.getDefault(), "K:%.2f  D:%.2f  J:%.2f",
                hisData.getK(), hisData.getD(), hisData.getJ()));
    }

    private void initChartPriceData(CombinedData combinedData) {
        mChartPrice.getXAxis().setAxisMaximum(combinedData.getXMax() + 0.5f);
        mChartPrice.setData(combinedData);
        mChartPrice.setVisibleXRange(MAX_COUNT, MIN_COUNT);
        mChartPrice.notifyDataSetChanged();
        moveToLast(mChartPrice);
    }

    private void resetChart() {
        mTempAvg = null;
        mTempKLineData = null;
        mData.clear();
//        mChartPrice.clear();
//        mChartVolume.clear();
//        mChartKdj.clear();
//        mChartMacd.clear();

        mChartPrice.notifyDataSetChanged();
        mChartVolume.notifyDataSetChanged();
        mChartPrice.notifyDataSetChanged();
        mChartMacd.notifyDataSetChanged();

        mChartPrice.fitScreen();
        mChartVolume.fitScreen();
        mChartKdj.fitScreen();
        mChartMacd.fitScreen();
    }


    private BarDataSet setBar(ArrayList<BarEntry> barEntries, int type) {
        BarDataSet barDataSet = new BarDataSet(barEntries, "vol");
        barDataSet.setHighLightAlpha(120);
        barDataSet.setHighLightColor(getResources().getColor(R.color.highlight_color));
        barDataSet.setDrawValues(false);
        barDataSet.setVisible(type != INVISIABLE_LINE);
        barDataSet.setHighlightEnabled(type != INVISIABLE_LINE);
        barDataSet.setColors(getResources().getColor(R.color.increasing_color), getResources().getColor(R.color.decreasing_color));
        return barDataSet;
    }


    @android.support.annotation.NonNull
    private LineDataSet setLine(int type, ArrayList<Entry> lineEntries) {
        LineDataSet lineDataSetMa = new LineDataSet(lineEntries, TYPE_PREFIX + type);
        lineDataSetMa.setDrawValues(false);
        if (type == NORMAL_LINE) {
            lineDataSetMa.setColor(getResources().getColor(R.color.normal_line_color));
            lineDataSetMa.setCircleColor(ContextCompat.getColor(mContext, R.color.normal_line_color));
            lineDataSetMa.setDrawFilled(true);
            lineDataSetMa.setFillDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_fade_blue));
        } else if (type == K) {
            lineDataSetMa.setColor(getResources().getColor(R.color.k));
            lineDataSetMa.setCircleColor(mTransparentColor);
        } else if (type == D) {
            lineDataSetMa.setColor(getResources().getColor(R.color.d));
            lineDataSetMa.setCircleColor(mTransparentColor);
            lineDataSetMa.setHighlightEnabled(false);
        } else if (type == J) {
            lineDataSetMa.setColor(getResources().getColor(R.color.j));
            lineDataSetMa.setCircleColor(mTransparentColor);
            lineDataSetMa.setHighlightEnabled(false);
        } else if (type == DIF) {
            lineDataSetMa.setColor(getResources().getColor(R.color.dif));
            lineDataSetMa.setCircleColor(mTransparentColor);
            lineDataSetMa.setHighlightEnabled(false);
        } else if (type == DEA) {
            lineDataSetMa.setColor(getResources().getColor(R.color.dea));
            lineDataSetMa.setCircleColor(mTransparentColor);
            lineDataSetMa.setHighlightEnabled(false);
        } else if (type == AVE_LINE) {
            lineDataSetMa.setColor(getResources().getColor(R.color.ave_color));
            lineDataSetMa.setCircleColor(mTransparentColor);
            lineDataSetMa.setHighlightEnabled(false);
        } else if (type == MA5) {
            lineDataSetMa.setColor(getResources().getColor(R.color.ma5));
            lineDataSetMa.setCircleColor(mTransparentColor);
            lineDataSetMa.setHighlightEnabled(false);
        } else if (type == MA10) {
            lineDataSetMa.setColor(getResources().getColor(R.color.ma10));
            lineDataSetMa.setCircleColor(mTransparentColor);
            lineDataSetMa.setHighlightEnabled(false);
        } else if (type == MA20) {
            lineDataSetMa.setColor(getResources().getColor(R.color.ma20));
            lineDataSetMa.setCircleColor(mTransparentColor);
            lineDataSetMa.setHighlightEnabled(false);
        } else if (type == MA30) {
            lineDataSetMa.setColor(getResources().getColor(R.color.ma30));
            lineDataSetMa.setCircleColor(mTransparentColor);
            lineDataSetMa.setHighlightEnabled(false);
        } else {
            lineDataSetMa.setVisible(false);
            lineDataSetMa.setHighlightEnabled(false);
        }
        lineDataSetMa.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSetMa.setLineWidth(1f);
        lineDataSetMa.setCircleRadius(1f);

        lineDataSetMa.setDrawCircles(false);
        lineDataSetMa.setDrawCircleHole(false);

        return lineDataSetMa;
    }

    @android.support.annotation.NonNull
    public CandleDataSet setKLine(int type, ArrayList<CandleEntry> lineEntries) {
        CandleDataSet set = new CandleDataSet(lineEntries, "KLine" + type);
        set.setDrawIcons(false);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setShadowColor(Color.DKGRAY);
        set.setShadowWidth(0.75f);
        set.setDecreasingColor(mDecreasingColor);
        set.setDecreasingPaintStyle(Paint.Style.FILL);
        set.setShadowColorSameAsCandle(true);
        set.setIncreasingColor(mIncreasingColor);
        set.setIncreasingPaintStyle(Paint.Style.FILL);
        set.setNeutralColor(ContextCompat.getColor(getContext(), R.color.increasing_color));
        set.setDrawValues(true);
        set.setValueTextSize(10);
        set.setHighlightEnabled(true);
        if (type != NORMAL_LINE) {
            set.setVisible(false);
        }
        return set;
    }

    private void initChartVolumeData() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<BarEntry> paddingEntries = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            HisData t = mData.get(i);
            barEntries.add(new BarEntry(i, t.getVol(), t));
        }
        int maxCount = MAX_COUNT;
        if (!mData.isEmpty() && mData.size() < maxCount) {
            for (int i = mData.size(); i < maxCount; i++) {
                paddingEntries.add(new BarEntry(i, 0));
            }
        }

        BarData barData = new BarData(setBar(barEntries, NORMAL_LINE), setBar(paddingEntries, INVISIABLE_LINE));
        barData.setBarWidth(0.75f);
        CombinedData combinedData = new CombinedData();
        combinedData.setData(barData);
        mChartVolume.getXAxis().setAxisMaximum(combinedData.getXMax() + 0.5f);
        mChartVolume.setData(combinedData);
        mChartVolume.setVisibleXRange(MAX_COUNT, MIN_COUNT);
        YAxis axisLeft = mChartVolume.getAxisLeft();

        axisLeft.setDrawLabels(false);

        mChartVolume.notifyDataSetChanged();
        moveToLast(mChartVolume);

    }

    private void initChartMacdData() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<BarEntry> paddingEntries = new ArrayList<>();
        ArrayList<Entry> difEntries = new ArrayList<>();
        ArrayList<Entry> deaEntries = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            HisData t = mData.get(i);
            barEntries.add(new BarEntry(i, (float) t.getMacd()));
            difEntries.add(new Entry(i, (float) t.getDif()));
            deaEntries.add(new Entry(i, (float) t.getDea()));
        }
        int maxCount = MAX_COUNT;
        if (!mData.isEmpty() && mData.size() < maxCount) {
            for (int i = mData.size(); i < maxCount; i++) {
                paddingEntries.add(new BarEntry(i, 0));
            }
        }

        BarData barData = new BarData(setBar(barEntries, NORMAL_LINE), setBar(paddingEntries, INVISIABLE_LINE));
        barData.setBarWidth(0.75f);
        CombinedData combinedData = new CombinedData();
        combinedData.setData(barData);
        LineData lineData = new LineData(setLine(DIF, difEntries), setLine(DEA, deaEntries));
        combinedData.setData(lineData);
        mChartMacd.getXAxis().setAxisMaximum(combinedData.getXMax() + 0.5f);
        mChartMacd.setData(combinedData);
        mChartMacd.setVisibleXRange(MAX_COUNT, MIN_COUNT);

        mChartMacd.notifyDataSetChanged();
        moveToLast(mChartMacd);
    }

    private void initChartKdjData() {
        ArrayList<Entry> kEntries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> dEntries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> jEntries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> paddingEntries = new ArrayList<>(INIT_COUNT);

        for (int i = 0; i < mData.size(); i++) {
            kEntries.add(new Entry(i, (float) mData.get(i).getK()));
            dEntries.add(new Entry(i, (float) mData.get(i).getD()));
            jEntries.add(new Entry(i, (float) mData.get(i).getJ()));
        }
        if (!mData.isEmpty() && mData.size() < MAX_COUNT) {
            for (int i = mData.size(); i < MAX_COUNT; i++) {
                paddingEntries.add(new Entry(i, (float) mData.get(mData.size() - 1).getK()));
            }
        }
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(setLine(K, kEntries));
        sets.add(setLine(D, dEntries));
        sets.add(setLine(J, jEntries));
        sets.add(setLine(INVISIABLE_LINE, paddingEntries));
        LineData lineData = new LineData(sets);

        CombinedData combinedData = new CombinedData();
        combinedData.setData(lineData);
        mChartKdj.getXAxis().setAxisMaximum(combinedData.getXMax() + 0.5f);
        mChartKdj.setData(combinedData);
        mChartKdj.setVisibleXRange(MAX_COUNT, MIN_COUNT);

        mChartKdj.notifyDataSetChanged();
        moveToLast(mChartKdj);
    }


    /**
     * according to the price to refresh the last data of the chart
     */
    public void refreshData(float price) {
        if (price <= 0 || price == mLastPrice) {
            return;
        }
        mLastPrice = price;
        CombinedData data = mChartPrice.getData();
        if (data == null) return;
        LineData lineData = data.getLineData();
        if (lineData != null) {
            ILineDataSet set = lineData.getDataSetByIndex(0);
            if (set.removeLast()) {
                set.addEntry(new Entry(set.getEntryCount(), price));
            }
        }
        CandleData candleData = data.getCandleData();
        if (candleData != null) {
            ICandleDataSet set = candleData.getDataSetByIndex(0);
            if (set.removeLast()) {
                HisData hisData = mData.get(mData.size() - 1);
                hisData.setClose(price);
                hisData.setHigh(Math.max(hisData.getHigh(), price));
                hisData.setLow(Math.min(hisData.getLow(), price));
                set.addEntry(new CandleEntry(set.getEntryCount(), (float) hisData.getHigh(), (float) hisData.getLow(), (float) hisData.getOpen(), price));

            }
        }
        mChartPrice.notifyDataSetChanged();
        mChartPrice.invalidate();
    }


    public void addData(HisData hisData) {
        hisData = DataUtils.calculateHisData(hisData, mData);

        CombinedData combinedData = mChartPrice.getData();

        LineData priceData = combinedData.getLineData();
        ILineDataSet ma5Set = priceData.getDataSetByIndex(1);
        ILineDataSet ma10Set = priceData.getDataSetByIndex(2);
        ILineDataSet ma20Set = priceData.getDataSetByIndex(3);
        ILineDataSet ma30Set = priceData.getDataSetByIndex(4);

        CandleData kData = combinedData.getCandleData();
        ICandleDataSet klineSet = kData.getDataSetByIndex(0);

        IBarDataSet volSet = mChartVolume.getData().getBarData().getDataSetByIndex(0);
        IBarDataSet macdSet = mChartMacd.getData().getBarData().getDataSetByIndex(0);
        ILineDataSet difSet = mChartMacd.getData().getLineData().getDataSetByIndex(0);
        ILineDataSet deaSet = mChartMacd.getData().getLineData().getDataSetByIndex(1);
        LineData kdjData = mChartKdj.getData().getLineData();
        ILineDataSet kSet = kdjData.getDataSetByIndex(0);
        ILineDataSet dSet = kdjData.getDataSetByIndex(1);
        ILineDataSet jSet = kdjData.getDataSetByIndex(2);

        if (mData.contains(hisData)) {
            int index = mData.indexOf(hisData);
            klineSet.removeEntry(index);
            // ma比较特殊，entry数量和k线的不一致，移除最后一个
            ma5Set.removeLast();
            ma10Set.removeLast();
            ma20Set.removeLast();
            ma30Set.removeLast();
            volSet.removeEntry(index);
            macdSet.removeEntry(index);
            difSet.removeEntry(index);
            deaSet.removeEntry(index);
            kSet.removeEntry(index);
            dSet.removeEntry(index);
            jSet.removeEntry(index);
            mData.remove(index);
        }
        mData.add(hisData);
        int klineCount = klineSet.getEntryCount();
        klineSet.addEntry(new CandleEntry(klineCount, (float) hisData.getHigh(), (float) hisData.getLow(), (float) hisData.getOpen(), (float) hisData.getClose()));
        volSet.addEntry(new BarEntry(volSet.getEntryCount(), hisData.getVol(), hisData));

        macdSet.addEntry(new BarEntry(macdSet.getEntryCount(), (float) hisData.getMacd()));
        difSet.addEntry(new Entry(difSet.getEntryCount(), (float) hisData.getDif()));
        deaSet.addEntry(new Entry(deaSet.getEntryCount(), (float) hisData.getDea()));

        kSet.addEntry(new Entry(kSet.getEntryCount(), (float) hisData.getK()));
        dSet.addEntry(new Entry(dSet.getEntryCount(), (float) hisData.getD()));
        jSet.addEntry(new Entry(jSet.getEntryCount(), (float) hisData.getJ()));

        // 因为ma的数量会少，所以这里用kline的set数量作为x
        if (!Double.isNaN(hisData.getMa5())) {
            ma5Set.addEntry(new Entry(klineCount, (float) hisData.getMa5()));
        }
        if (!Double.isNaN(hisData.getMa10())) {
            ma10Set.addEntry(new Entry(klineCount, (float) hisData.getMa10()));
        }
        if (!Double.isNaN(hisData.getMa20())) {
            ma20Set.addEntry(new Entry(klineCount, (float) hisData.getMa20()));
        }
        if (!Double.isNaN(hisData.getMa30())) {
            ma30Set.addEntry(new Entry(klineCount, (float) hisData.getMa30()));
        }


        mChartPrice.getXAxis().setAxisMaximum(combinedData.getXMax() + 1.5f);
        mChartVolume.getXAxis().setAxisMaximum(mChartVolume.getData().getXMax() + 1.5f);
        mChartMacd.getXAxis().setAxisMaximum(mChartMacd.getData().getXMax() + 1.5f);
        mChartKdj.getXAxis().setAxisMaximum(mChartKdj.getData().getXMax() + 1.5f);


        mChartPrice.setVisibleXRange(MAX_COUNT, MIN_COUNT);
        mChartVolume.setVisibleXRange(MAX_COUNT, MIN_COUNT);
        mChartMacd.setVisibleXRange(MAX_COUNT, MIN_COUNT);
        mChartKdj.setVisibleXRange(MAX_COUNT, MIN_COUNT);

        mChartPrice.notifyDataSetChanged();
        mChartPrice.invalidate();
        mChartVolume.notifyDataSetChanged();
        mChartVolume.invalidate();
        mChartMacd.notifyDataSetChanged();
        mChartMacd.invalidate();
        mChartKdj.notifyDataSetChanged();
        mChartKdj.invalidate();


        setDescription(mChartPrice, String.format(Locale.getDefault(), "MA5:%.2f  MA10:%.2f  MA20:%.2f  MA30:%.2f",
                hisData.getMa5(), hisData.getMa10(), hisData.getMa20(), hisData.getMa30()));
        setDescription(mChartVolume, "VOL: " + hisData.getVol());
        setDescription(mChartMacd, String.format(Locale.getDefault(), "MACD:%.2f  DEA:%.2f  DIF:%.2f",
                hisData.getMacd(), hisData.getDea(), hisData.getDif()));
        setDescription(mChartKdj, String.format(Locale.getDefault(), "K:%.2f  D:%.2f  J:%.2f",
                hisData.getK(), hisData.getD(), hisData.getJ()));

    }


    public void addData1(HisData hisData) {

        HisData lastData = mData.get(mData.size() - 1);
        hisData.setOpen(lastData.getOpen());
        setLastClose(lastData.getClose());

        hisData = DataUtils.calculateHisData(hisData, mData);
        CombinedData combinedData = mChartPrice.getData();
        LineData priceData = combinedData.getLineData();
        ILineDataSet priceSet = priceData.getDataSetByIndex(0);
        ILineDataSet aveSet = priceData.getDataSetByIndex(1);
        IBarDataSet volSet = mChartVolume.getData().getBarData().getDataSetByIndex(0);
        if (mData.contains(hisData)) {
            int index = mData.indexOf(hisData);
            priceSet.removeEntry(index);
            aveSet.removeEntry(index);
            volSet.removeEntry(index);
            mData.remove(index);
        }
        mData.add(hisData);
        priceSet.addEntry(new Entry(priceSet.getEntryCount(), (float) hisData.getClose()));
        aveSet.addEntry(new Entry(aveSet.getEntryCount(), (float) hisData.getAvePrice()));
        volSet.addEntry(new BarEntry(volSet.getEntryCount(), hisData.getVol(), hisData));

        mChartPrice.setVisibleXRange(MAX_COUNT, MIN_COUNT);
        mChartVolume.setVisibleXRange(MAX_COUNT, MIN_COUNT);

        mChartPrice.getXAxis().setAxisMaximum(combinedData.getXMax() + 1.5f);
        mChartVolume.getXAxis().setAxisMaximum(mChartVolume.getData().getXMax() + 1.5f);

        mChartPrice.notifyDataSetChanged();
        mChartPrice.invalidate();
        mChartVolume.notifyDataSetChanged();
        mChartVolume.invalidate();

        updateDescription(hisData);

       // setDescription(mChartVolume, "成交量 " + hisData.getVol());
    }


    /**
     * align two chart
     */
    private void setOffset() {
        int chartHeight = getResources().getDimensionPixelSize(R.dimen.bottom_chart_height);
        mChartPrice.setViewPortOffsets(0, 0, 0, chartHeight);
        int bottom = DisplayUtils.dip2px(mContext, 20);
        int top = DisplayUtils.dip2px(mContext, 10);
        mChartVolume.setViewPortOffsets(0, top, 0, bottom);
        mChartMacd.setViewPortOffsets(0, top, 0, bottom);
        mChartKdj.setViewPortOffsets(0, top, 0, bottom);
    }


    /**
     * add limit line1 to chart
     */
    public void setLimitLine(double lastClose) {
        LimitLine limitLine = new LimitLine((float) lastClose);
        limitLine.enableDashedLine(5, 10, 0);
        limitLine.setLineColor(getResources().getColor(R.color.limit_color));
        mChartPrice.getAxisLeft().addLimitLine(limitLine);
    }

    public void setLimitLine() {
        setLimitLine(mLastClose);
    }

    public void setLastClose(double lastClose) {
        mLastClose = lastClose;
        mChartPrice.setOnChartValueSelectedListener(new InfoViewListener(mContext, mLastClose, mData, mChartInfoView, mChartVolume, mChartMacd, mChartKdj));
        mChartVolume.setOnChartValueSelectedListener(new InfoViewListener(mContext, mLastClose, mData, mChartInfoView, mChartPrice, mChartMacd, mChartKdj));
        mChartMacd.setOnChartValueSelectedListener(new InfoViewListener(mContext, mLastClose, mData, mChartInfoView, mChartPrice, mChartVolume, mChartKdj));
        mChartKdj.setOnChartValueSelectedListener(new InfoViewListener(mContext, mLastClose, mData, mChartInfoView, mChartPrice, mChartVolume, mChartMacd));

    }


    @Override
    public void onAxisChange(BarLineChartBase chart) {
        float lowestVisibleX = chart.getLowestVisibleX();
        if (lowestVisibleX <= chart.getXAxis().getAxisMinimum()) return;
        int maxX = (int) chart.getHighestVisibleX();
        int x = Math.min(maxX, mData.size() - 1);
        HisData hisData = mData.get(x < 0 ? 0 : x);
        setDescription(mChartPrice, String.format(Locale.getDefault(), "MA5:%.2f  MA10:%.2f  MA20:%.2f  MA30:%.2f",
                hisData.getMa5(), hisData.getMa10(), hisData.getMa20(), hisData.getMa30()));
        setDescription(mChartVolume, "VOL: " + hisData.getVol());
        setDescription(mChartMacd, String.format(Locale.getDefault(), "MACD:%.2f  DEA:%.2f  DIF:%.2f",
                hisData.getMacd(), hisData.getDea(), hisData.getDif()));
        setDescription(mChartKdj, String.format(Locale.getDefault(), "K:%.2f  D:%.2f  J:%.2f",
                hisData.getK(), hisData.getD(), hisData.getJ()));
    }

    public void showIndex() {
        showSecondIndex();
        showSecondIndex();
    }

    public void showSecondIndex() {
        switch (getState().getSecondIndex()) {
            case VOL:
                showVolume();
                break;
            case KDJ:
                showKdj();
                break;
            case MACD:
                showMacd();
                break;
        }
    }

    public void showMainIndex() {
        switch (getState().getMainIndex()) {
            case NONE:
                clearMainIndex();
                break;
            case MA:
                showMAIndex();
                break;
            case BOLL:
                break;
        }
    }

    private void showMAIndex() {
        CombinedData data = mChartPrice.getData();
        if (data == null) {
            return;
        }
        if (getState().getMode() == Mode.LINE) {
            LineData lineData = data.getLineData();
            lineData.addDataSet(mTempAvg);
        } else if (getState().getMode() == Mode.K) {
            data.setData(mTempKLineData);
            mChartPrice.getDescription().setEnabled(true);
        }
        mChartPrice.notifyDataSetChanged();
        mChartPrice.invalidate();
    }

    private void clearMainIndex() {
        if (getState().getMode() == Mode.LINE) {
            CombinedData data = mChartPrice.getData();
            if (data != null) {
                LineData lineData = data.getLineData();
                lineData.removeDataSet(mTempAvg);
            }
        } else if (getState().getMode() == Mode.K) {
            CombinedData data = mChartPrice.getData();
            if (data != null) {
                data.setData(new LineData());
            }
            mChartPrice.getDescription().setEnabled(false);
        }
        mChartPrice.notifyDataSetChanged();
        mChartPrice.invalidate();
    }

    public void setMainIndex(Index mainIndex) {
        getState().setMainIndex(mainIndex);
        showMainIndex();
    }

    public void setSecondIndex(Index secondIndex) {
        getState().setSecondIndex(secondIndex);
        showSecondIndex();
    }


    private CombinedData getKCombinedData() {
        ArrayList<CandleEntry> lineCJEntries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> ma5Entries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> ma10Entries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> ma20Entries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> ma30Entries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> paddingEntries = new ArrayList<>(INIT_COUNT);

        for (int i = 0; i < mData.size(); i++) {
            HisData hisData = mData.get(i);
            lineCJEntries.add(new CandleEntry(i, (float) hisData.getHigh(), (float) hisData.getLow(), (float) hisData.getOpen(), (float) hisData.getClose()));

            if (!Double.isNaN(hisData.getMa5())) {
                ma5Entries.add(new Entry(i, (float) hisData.getMa5()));
            }

            if (!Double.isNaN(hisData.getMa10())) {
                ma10Entries.add(new Entry(i, (float) hisData.getMa10()));
            }

            if (!Double.isNaN(hisData.getMa20())) {
                ma20Entries.add(new Entry(i, (float) hisData.getMa20()));
            }

            if (!Double.isNaN(hisData.getMa30())) {
                ma30Entries.add(new Entry(i, (float) hisData.getMa30()));
            }
        }

        if (!mData.isEmpty() && mData.size() < MAX_COUNT) {
            for (int i = mData.size(); i < MAX_COUNT; i++) {
                paddingEntries.add(new Entry(i, (float) mData.get(mData.size() - 1).getClose()));
            }
        }

        mTempKLineData = new LineData(
                setLine(INVISIABLE_LINE, paddingEntries),
                setLine(MA5, ma5Entries),
                setLine(MA10, ma10Entries),
                setLine(MA20, ma20Entries),
                setLine(MA30, ma30Entries));
        CandleData candleData = new CandleData(setKLine(NORMAL_LINE, lineCJEntries));
        candleData.setValueTextColor(getResources().getColor(R.color.text_color));
        CombinedData combinedData = new CombinedData();
        combinedData.setData(mTempKLineData);
        combinedData.setData(candleData);
        return combinedData;
    }

    private CombinedData getLineCombinedData() {

        ArrayList<Entry> priceEntries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> aveEntries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> paddingEntries = new ArrayList<>(INIT_COUNT);

        for (int i = 0; i < mData.size(); i++) {
            priceEntries.add(new Entry(i, (float) mData.get(i).getClose()));
            aveEntries.add(new Entry(i, (float) mData.get(i).getAvePrice()));
        }
        if (!mData.isEmpty() && mData.size() < MAX_COUNT) {
            for (int i = mData.size(); i < MAX_COUNT; i++) {
                paddingEntries.add(new Entry(i, (float) mData.get(mData.size() - 1).getClose()));
            }
        }
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(setLine(NORMAL_LINE, priceEntries));
        mTempAvg = setLine(AVE_LINE, aveEntries);
        if (getState().getMainIndex() != Index.NONE) {
            sets.add(mTempAvg);
        }
        sets.add(setLine(INVISIABLE_LINE, paddingEntries));
        LineData lineData = new LineData(sets);

        CombinedData combinedData = new CombinedData();
        combinedData.setData(lineData);
        return combinedData;
    }

    public void setMode(Mode mode) {
        getState().setMode(mode);
    }

    public class State {

        public Index mainIndex = Index.MA;

        public Index secondIndex = Index.VOL;

        private Mode mode = Mode.K;

        public Index getMainIndex() {
            return mainIndex;
        }

        public void setMainIndex(Index mainIndex) {
            this.mainIndex = mainIndex;
        }

        public Index getSecondIndex() {
            return secondIndex;
        }

        public void setSecondIndex(Index secondIndex) {
            this.secondIndex = secondIndex;
        }

        public Mode getMode() {
            return mode;
        }

        public void setMode(Mode mode) {
            this.mode = mode;
        }
    }

    public State getState() {
        return mState;
    }

    public void setState(State state) {
        mState = state;
    }
}
