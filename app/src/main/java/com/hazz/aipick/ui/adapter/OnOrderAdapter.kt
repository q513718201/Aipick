package com.hazz.aipick.ui.adapter


import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.BuyOrSell
import com.hazz.aipick.utils.BigDecimalUtil


class OnOrderAdapter(layoutResId: Int, data: List<BuyOrSell>?) : BaseQuickAdapter<BuyOrSell, BaseViewHolder>(layoutResId, data) {
    var MAX_COUNT = 10

    override fun convert(helper: BaseViewHolder, item: BuyOrSell) {
        helper.setText(R.id.tv_buy_amount, BigDecimalUtil.format(item.tick.askSize, 2))
        helper.setText(R.id.tv_buy_price, BigDecimalUtil.format(item.tick.ask, 4))
        helper.setText(R.id.tv_sell_price, BigDecimalUtil.format(item.tick.bid, 4))
        helper.setText(R.id.tv_sell_amount, BigDecimalUtil.format(item.tick.bidSize, 2))
    }

    override fun addData(data: BuyOrSell) {
        super.addData(data)
        if (itemCount >= 10) {
            remove(itemCount - 1)
        }
    }

}
