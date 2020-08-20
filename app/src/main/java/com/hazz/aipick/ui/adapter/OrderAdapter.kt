package com.hazz.aipick.ui.adapter


import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.Order
import com.hazz.aipick.utils.BigDecimalUtil
import com.hazz.aipick.utils.CoinManager
import com.hazz.aipick.utils.GlideUtil

class OrderAdapter(layoutResId: Int, data: List<Order.ListBean>?) : BaseQuickAdapter<Order.ListBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: Order.ListBean) {

        helper.setText(R.id.tv_coin_name, item.base_coin + "/" + item.quote_coin)
        when (item.action) {
            "buy" -> {
                helper.setText(R.id.tv_buy_or_sell, mContext.getString(R.string.buy))
                helper.setTextColor(R.id.tv_buy_or_sell, mContext.resources.getColor(R.color.main_color_green))
                helper.setTextColor(R.id.tv_price, mContext.resources.getColor(R.color.main_color_green))
            }

            else -> {
                helper.setText(R.id.tv_buy_or_sell, mContext.getString(R.string.sell))
                helper.setTextColor(R.id.tv_buy_or_sell, mContext.resources.getColor(R.color.main_color_red))
                helper.setTextColor(R.id.tv_price, mContext.resources.getColor(R.color.main_color_red))
            }
        }
        helper.setText(R.id.tv_price, "$${BigDecimalUtil.formatMoney(item.price)}")
        helper.setText(R.id.tv_time, item.create_at)
        helper.setText(R.id.tv_num, mContext.getString(R.string.num_num, item.amount))
        val coinIcon = helper.getView<ImageView>(R.id.iv)
//        Glide.with(mContext).load(CoinManager.getCoinIcon(item.base_coin)).into(coinIcon)
        GlideUtil.load(coinIcon, CoinManager.getCoinIcon(item.base_coin))
    }
}
