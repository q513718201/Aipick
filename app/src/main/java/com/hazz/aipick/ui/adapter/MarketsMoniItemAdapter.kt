package com.hazz.aipick.ui.adapter

import android.content.Intent
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.socket.CoinDetail
import com.hazz.aipick.ui.activity.CoinDescBuyOrSellActivity
import com.hazz.aipick.utils.BigDecimalUtil
import com.hazz.aipick.utils.CoinManager
import com.hazz.aipick.utils.SPUtil

class MarketsMoniItemAdapter//    private String formatPercent;
(layoutResId: Int, data: List<CoinDetail>?) : BaseQuickAdapter<CoinDetail, BaseViewHolder>(layoutResId, data) {
    private var sortByPrice: Boolean = false
    private var sortByUp: Boolean = false
    fun sortByPrice() {
        if (sortByPrice) {
            data.sortBy { BigDecimalUtil.mul(it.tick.close, "1").toDouble() }
        } else {
            data.sortByDescending { BigDecimalUtil.mul(it.tick.close, "1").toDouble() }
        }
        sortByPrice = !sortByPrice
        notifyDataSetChanged()
    }

    private var market: String = "--"
    fun setMarket(market: String) {
        this.market = market
    }

    fun sortByUp() {
        if (sortByUp) {
            data.sortBy {
                BigDecimalUtil.mulByDecimal(
                        BigDecimalUtil.div(BigDecimalUtil.sub(it.tick.close, it.tick.open, 6), it.tick.open, 6),
                        "1").toDouble()
            }
        } else {
            data.sortByDescending {
                BigDecimalUtil.mulByDecimal(
                        BigDecimalUtil.div(BigDecimalUtil.sub(it.tick.close, it.tick.open, 6), it.tick.open, 6),
                        "1").toDouble()
            }
        }
        sortByUp = !sortByUp
        notifyDataSetChanged()
    }

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

        val rate1 = SPUtil.getRate()
        val cyn = BigDecimalUtil.mul(close, rate1, 4)
        helper
                .setImageResource(R.id.ivIcon, CoinManager.getCoinIcon(coinName))
                .setText(R.id.tv_trade_a, coinName.toUpperCase())
                .setText(R.id.tv_price, if (cyn == "0") "--" else cyn)
                .setText(R.id.tv_num_tip, market)
                .setText(R.id.up_down, if (marketItem.isUp) "+$rate" else rate)
                .setText(R.id.tv_price_legal, "$ " + BigDecimalUtil.format(close, 2))
                .setText(R.id.trade_amount, if (volume == "--") "--" else BigDecimalUtil.format(volume, 2))
                .setTextColor(R.id.up_down, if (marketItem.isUp) mContext.resources.getColor(R.color.main_color_green) else mContext.resources.getColor(R.color.main_color_red))

        helper.itemView.setOnClickListener {
            mContext.startActivity(Intent(mContext, CoinDescBuyOrSellActivity::class.java).putExtra("name", marketItem.ch))
        }
    }
}
