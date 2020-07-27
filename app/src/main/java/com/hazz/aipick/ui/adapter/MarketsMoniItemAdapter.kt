package com.hazz.aipick.ui.adapter

import android.content.Context
import android.content.Intent
import android.util.Log

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.Coin
import com.hazz.aipick.mvp.model.bean.MarketItem
import com.hazz.aipick.socket.CoinDetail
import com.hazz.aipick.ui.activity.CoinDescActivity
import com.hazz.aipick.ui.activity.CoinDescBuyOrSellActivity
import com.hazz.aipick.utils.BigDecimalUtil

import java.math.BigDecimal
import java.math.RoundingMode

class MarketsMoniItemAdapter//    private String formatPercent;
( layoutResId: Int, data: List<CoinDetail>?) : BaseQuickAdapter<CoinDetail, BaseViewHolder>(layoutResId, data) {


    override fun convert(helper: BaseViewHolder, marketItem: CoinDetail?) {
        if (marketItem == null) {
            return
        }
        var close = ""
        val tick = marketItem.tick ?: return
//String changeString = marketItem.volumePercent;
        if (tick.close != null) {
            close = tick.close
        }
        var volume = ""
        if (tick.vol != null) {
            volume = tick.vol
        }
        val ch = marketItem.ch
        val split = ch.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val coinName = split[1].substring(0, split[1].length - 4)

        val compare = BigDecimalUtil.compare(tick.close, tick.open)
        marketItem.isUp = compare != -1
        val rate = BigDecimalUtil.mul(BigDecimalUtil.div(BigDecimalUtil.sub(tick.close, tick.open, 6), tick.open, 6), "100", 2) + "%"

        //        String cnyPrice = marketItem.cnyPrice;
        val cyn = BigDecimalUtil.mul(close, "7", 2)
        helper
                .setText(R.id.tv_trade_a, coinName.toUpperCase())
                .setText(R.id.tv_price, close)
                .setText(R.id.tv_volume, if (volume == "--") "--" else BigDecimal(volume).setScale(0, RoundingMode.HALF_UP).toPlainString())
                .setText(R.id.up_down, if (marketItem.isUp) "+$rate" else rate)
                .setText(R.id.tv_price_legal, "¥ " + if (cyn == "0") "--" else cyn)
                .setBackgroundRes(R.id.up_down, if (marketItem.isUp) R.drawable.shape_oval_green_3 else R.drawable.shape_oval_red_3)

        helper.itemView.setOnClickListener {
            mContext.startActivity(Intent(mContext, CoinDescBuyOrSellActivity::class.java).putExtra("name",marketItem.ch))
        }
    }
}
