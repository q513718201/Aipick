package com.hazz.aipick.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hazz.aipick.R;
import com.hazz.aipick.mvp.model.bean.Coin;
import com.hazz.aipick.mvp.model.bean.MarketItem;
import com.hazz.aipick.utils.BigDecimalUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class MarketsPagerItemAdapter extends BaseQuickAdapter<Coin.DataBean, BaseViewHolder> {

    //    private String formatPercent;
    public MarketsPagerItemAdapter(Context context, int layoutResId, @Nullable List<Coin.DataBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, Coin.DataBean marketItem) {
        if (marketItem == null) {
            return;
        }
      Log.d("ceshi",marketItem.priceUsd);
//
      String changeString = marketItem.volumePercent;
        String close = marketItem.priceUsd;
        String volume = marketItem.volumeUsd24Hr;
//        String cnyPrice = marketItem.cnyPrice;
        String cyn= BigDecimalUtil.mul(close,"7",2);
        helper
                .setText(R.id.tv_trade_a, marketItem.baseSymbol)
                .setText(R.id.tv_trade_b, marketItem.quoteSymbol)
                .setText(R.id.tv_price, close == null ? "--" : close)
                .setText(R.id.tv_volume, (volume == null || volume.equals("--")) ? "--" : new BigDecimal(volume).setScale(0, RoundingMode.HALF_UP).toPlainString())
                .setText(R.id.up_down, changeString == null ? "0.00%" : changeString)
                .setText(R.id.tv_price_legal, "¥ " + (cyn.equals("0") ? "--" : cyn));
//                .setBackgroundRes(R.id.up_down, marketItem.isUp ? R.drawable.shape_oval_green_3 : R.drawable.shape_oval_red_3);
////        if("USDT".equals(marketItem.tradeB) && !TextUtils.isEmpty(close)){
////            //Log.d("YUDAN", "convert: change to ￥");
////            String res = new DecimalFormat("########.####").format( 6.9*Float.valueOf(close) );
////            helper.setText(R.id.tv_price_legal,"¥ "+res);
////        }
    }
}
