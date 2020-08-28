package com.hazz.aipick.ui.adapter


import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.Order
import com.hazz.aipick.utils.CoinManager
import com.hazz.aipick.utils.GlideUtil

class OrderAdapter(data: List<Order.ListBean>?) : BaseQuickAdapter<Order.ListBean, BaseViewHolder>(R.layout.item_order, data) {

    override fun convert(helper: BaseViewHolder, item: Order.ListBean) {
        val coinIcon = helper.getView<ImageView>(R.id.iv)
        GlideUtil.load(coinIcon, CoinManager.getCoinIcon(item.base_coin))
        helper.setText(R.id.tv_coin_name, "${item.base_coin}/${item.quote_coin}")
        if (item.gain_loss != null && item.gain_loss[0]?.isDigit()) {
            helper.setTextColor(R.id.tv_gain, mContext.resources.getColor(R.color.main_color_green))
        } else {
            helper.setTextColor(R.id.tv_gain, mContext.resources.getColor(R.color.main_color_red))
        }
        helper.setText(R.id.tv_gain, if (item.gain_loss == null) "--" else item.gain_loss)
        helper.setText(R.id.tv_changes, item.buy_price + "->" + item.sell_price)
        helper.setText(R.id.tv_num, mContext.getString(R.string.num_num, item.order_num))
        if (item.create_at.length > 10 && item.sell_time.length > 10) {
            helper.setText(R.id.tv_time, item.create_at.substring(0, 10) + item.sell_time.substring(0, 10))
        } else {
            helper.setText(R.id.tv_time, item.create_at + item.sell_time)
        }
        //买入
        helper.setText(R.id.tv_buy_time, item.create_at)
        helper.setText(R.id.tv_buy_price, item.buy_price)
        helper.setText(R.id.tv_buy_fee, item.buy_fee)
        helper.setText(R.id.tv_target_profit, if (item.gain == null) "--" else item.gain)// TODO: 2020/8/27 止盈价格
        helper.setText(R.id.tv_trade_coin, "${item.base_coin}/${item.quote_coin}")
        helper.setText(R.id.tv_trade_no, item.order_id)

        //卖出
        helper.setText(R.id.tv_sell_time, if (item.sell_time == null) "--" else item.sell_time)
        helper.setText(R.id.tv_sell_price, if (item.sell_price == null) "--" else item.sell_price)
        helper.setText(R.id.tv_sell_fee, if (item.sell_fee == null) "--" else item.sell_fee)
        helper.setText(R.id.tv_sell_loss, if (item.gain == null) "--" else item.loss)// TODO: 2020/8/27 止损价格
        helper.setText(R.id.tv_trade_amount, item.order_num)


        helper.itemView.setOnClickListener {

            val index = data.indexOf(item)
            item.isShowDetail = !item.isShowDetail
            notifyItemChanged(index)
            if (lastClick != -1 && lastClick != index) {
                data[lastClick].isShowDetail = false
                notifyItemChanged(lastClick)
            }
            lastClick = index
        }
        helper.setVisible(R.id.clOrderDetail, item.isShowDetail)
    }

    private var lastClick = -1
}
