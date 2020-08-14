package com.hazz.aipick.ui.adapter


import android.content.Intent
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.Order
import com.hazz.aipick.ui.activity.MineSubscribeActivity
import com.hazz.aipick.ui.activity.SubscribeDescActivity

class OrderAdapter(layoutResId: Int, data: List<Order.ListBean>?) : BaseQuickAdapter<Order.ListBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: Order.ListBean) {

        helper.setText(R.id.tv_coin_name, item.base_coin + "/" + item.quote_coin)
        when (item.action) {
            "buy" -> helper.setText(R.id.tv_buy_or_sell, mContext.getString(R.string.buy))

            else -> helper.setText(R.id.tv_buy_or_sell, mContext.getString(R.string.sell))
        }
        helper.setText(R.id.tv_time, item.create_at)
        helper.setText(R.id.tv_num, mContext.getString(R.string.num_num,item.amount))
    }
}
