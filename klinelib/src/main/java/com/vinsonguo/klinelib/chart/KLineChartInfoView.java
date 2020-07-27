package com.vinsonguo.klinelib.chart;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.vinsonguo.klinelib.ChartSettings;
import com.vinsonguo.klinelib.R;
import com.vinsonguo.klinelib.model.HisData;
import com.vinsonguo.klinelib.util.DateUtils;
import com.vinsonguo.klinelib.util.DoubleUtil;
import com.vinsonguo.klinelib.util.StringUtils;

import java.util.Locale;

/**
 * Created by dell on 2017/9/25.
 */

public class KLineChartInfoView extends ChartInfoView {

    private TextView mTvOpenPrice;
    private TextView mTvClosePrice;
    private TextView mTvHighPrice;
    private TextView mTvLowPrice;
    private TextView mTvChangeRate;
    private TextView mChange;
    private TextView mTvVol;
    private TextView mTvTime;
    private View mVgChangeRate;

    public KLineChartInfoView(Context context) {
        this(context, null);
    }

    public KLineChartInfoView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KLineChartInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_kline_chart_info, this);
        mTvTime =  findViewById(R.id.tv_time);
        mTvOpenPrice =  findViewById(R.id.tv_open_price);
        mTvClosePrice =  findViewById(R.id.tv_close_price);
        mTvHighPrice =  findViewById(R.id.tv_high_price);
        mTvLowPrice =  findViewById(R.id.tv_low_price);
        mTvChangeRate =  findViewById(R.id.tv_change_rate);
        mTvVol =  findViewById(R.id.tv_vol);
        mVgChangeRate = findViewById(R.id.vg_change_rate);
        mChange = findViewById(R.id.tv_change);
    }

    @Override
    public void setData(double lastClose, HisData data) {

        Log.d("junjun",data.getClose()+"***"+data.getOpen());

        mTvTime.setText(DateUtils.formatDate(data.getDate()));
        mTvClosePrice.setText(DoubleUtil.formatDecimal(data.getClose()));
        mTvOpenPrice.setText(DoubleUtil.formatDecimal(data.getOpen()));
        mTvHighPrice.setText(DoubleUtil.formatDecimal(data.getHigh()));
        mTvLowPrice.setText(DoubleUtil.formatDecimal(data.getLow()));
        double change = data.getClose() - data.getOpen();
        String changeString = DoubleUtil.formatDecimal(change);
        mChange.setText(changeString);
        if (lastClose == 0) {
            mVgChangeRate.setVisibility(GONE);
        } else {
            mTvChangeRate.setText(String.format(Locale.getDefault(), "%.2f%%", (data.getClose() - data.getOpen()) / lastClose * 100));
        }
        if (change >= 0) {
            mChange.setTextColor(getResources().getColor(R.color.increasing_color));
            mTvChangeRate.setTextColor(getResources().getColor(R.color.increasing_color));
        } else {
            mChange.setTextColor(getResources().getColor(R.color.decreasing_color));
            mTvChangeRate.setTextColor(getResources().getColor(R.color.decreasing_color));

        }
//        if (ChartSettings.isChinese()) {
//            mTvVol.setText(StringUtils.amountToStringByTenThousand(data.getVol()));
//        } else {
//            mTvVol.setText(StringUtils.amountToStringByThousand(data.getVol()));
//        }
        removeCallbacks(mRunnable);
        postDelayed(mRunnable, 2000);
    }

}
