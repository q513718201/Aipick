package com.hazz.aipick.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hazz.aipick.R;
import com.hazz.aipick.socket.CoinDetail;
import com.hazz.aipick.utils.BigDecimalUtil;
import com.hazz.aipick.utils.SPUtil;

import java.math.BigDecimal;

public class MarketInfoView extends RelativeLayout {

    //当前价格
    private TextView mPrice;
    //当前人民币价格
    private TextView mCurrencyPrice;
    //涨幅百分比
    private TextView mChange;
    //涨幅值
    private TextView mChangeValue;

    private TextView mVolume;

    private TextView mHighest;

    private TextView mLowest;
    private String mSymbol;


    public MarketInfoView(Context context) {
        this(context, null);
    }

    public MarketInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_market_info, this);
        mPrice = findViewById(R.id.price);
        mCurrencyPrice = findViewById(R.id.currency_price);
        mChange = findViewById(R.id.change);
        mChangeValue = findViewById(R.id.change_value);
        mVolume = findViewById(R.id.volume);
        mHighest = findViewById(R.id.highest);
        mLowest = findViewById(R.id.lowest);


    }


    public void bindView(CoinDetail coinDetail) {

        post(() -> updateView(coinDetail));

    }

    private void updateView(CoinDetail coinDetail) {
        String ch = coinDetail.ch;

        CoinDetail.TickBean market = coinDetail.tick;
        mPrice.setText(market.close);

        int compare = BigDecimalUtil.compare(market.close, market.open);
        coinDetail.isUp = compare != -1;
        if (coinDetail.isUp) {
            mChange.setTextColor(getContext().getResources().getColor(R.color.green));
            mPrice.setTextColor(getContext().getResources().getColor(R.color.green));
        } else {
            mChange.setTextColor(getContext().getResources().getColor(R.color.redF4));
            mPrice.setTextColor(getContext().getResources().getColor(R.color.redF4));
        }
        String changeAmount = "--";
        if (market.open != null && market.close != null) {
            BigDecimal open = new BigDecimal(market.open);
            BigDecimal close = new BigDecimal(market.close);
            changeAmount = close.subtract(open).stripTrailingZeros().toPlainString();
            String upPercent = BigDecimalUtil.div(changeAmount, market.open, 2);
            mChange.setText(String.format("%s%s", coinDetail.isUp ? "+" : "-", upPercent + "%"));
        }


        mChangeValue.setText(coinDetail.isUp ? "+" + changeAmount : changeAmount);

        mHighest.setText(market.high);
        mLowest.setText(market.low);
        String volume = market.vol;
//            String volumeString = volume + " " + market.tradeA;
//            if (!TextUtils.isEmpty(volume) && !volume.equals("--")) {
//                String s = new BigDecimal(volume).setScale(0, RoundingMode.HALF_UP).toPlainString();
//                volumeString = s + " " + market.tradeA;
//            }
        mVolume.setText(BigDecimalUtil.formatRaise(volume));
        String rate = SPUtil.INSTANCE.getRate();
        String currency = String.format(getResources().getString(R.string.market_yuan), BigDecimalUtil.mul(market.close, rate, 2));
        mCurrencyPrice.setText(currency);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }
}
