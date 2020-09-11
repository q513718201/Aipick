package com.hazz.aipick.ui.adapter


import android.annotation.SuppressLint
import android.content.Intent
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.Order
import com.hazz.aipick.socket.CoinDetail
import com.hazz.aipick.ui.activity.MineSubscribeActivity
import com.hazz.aipick.ui.activity.SubscribeDescActivity
import com.hazz.aipick.utils.BigDecimalUtil
import java.math.BigDecimal
import java.math.RoundingMode

class CoinHanqingAdapter(layoutResId: Int, data: List<CoinDetail>?) : BaseQuickAdapter<CoinDetail, BaseViewHolder>(layoutResId, data) {

    @SuppressLint("StringFormatInvalid")
    override fun convert(helper: BaseViewHolder, marketItem:CoinDetail) {

        var close = "";
       var tick = marketItem.tick;
        //String changeString = marketItem.volumePercent;
        if (tick.close != null) {
            close = tick.close
        }

         var  volume = tick.vol

        var ch = marketItem.ch
        val split = ch.split(".")
        var coinName = split[1].substring(0, split[1].length - 4)

//        String cnyPrice = marketItem.cnyPrice;
        var cyn = BigDecimalUtil.mul(close, "7", 2)
        helper
                .setText(R.id.tv_trade_a,coinName.toUpperCase())
                .setText(R.id.tv_price, close)
                .setText(R.id.tv_volume, BigDecimal(volume).setScale(0, RoundingMode.HALF_UP).toPlainString())
                // .setText(R.id.up_down, changeString == null ? "0.00%" : changeString)
                .setText(R.id.tv_price_legal, mContext.getString(R.string.money_format,"$cyn"))

   }
}
