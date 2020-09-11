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
        val icon = CoinManager.getCoinIcon(if (hideData) "" else item.base_coin)
        GlideUtil.showImage(icon, coinIcon)

        helper.setText(R.id.tv_coin_name, dealData(item.base_coin))
        helper.setText(R.id.tv_market, "/${dealData(item.quote_coin)}")
        if (hideData) {
            helper.setTextColor(R.id.tv_gain, mContext.resources.getColor(R.color.home_text))
        } else {
            if (item.gain_loss != null && item.gain_loss[0]?.isDigit()) {
                helper.setTextColor(R.id.tv_gain, mContext.resources.getColor(R.color.main_color_green))
            } else {
                helper.setTextColor(R.id.tv_gain, mContext.resources.getColor(R.color.main_color_red))
            }
        }
        helper.setText(R.id.tv_gain, dealData(mContext.getString(R.string.money_format_us, item.gain_loss)))
        helper.setText(R.id.tv_changes, dealData(item.buy_price) + "->" + dealData(item.sell_price))
        helper.setText(R.id.tv_num, mContext.getString(R.string.num_num, dealData(item.order_num)))
        if (item.create_at.length > 10 && item.sell_time.length > 10) {
            helper.setText(R.id.tv_time, dealData(item.create_at.substring(0, 10)) + "-" + dealData(item.sell_time.substring(0, 10)))
        } else {
            helper.setText(R.id.tv_time, dealData(item.create_at) + "-" + dealData(item.sell_time))
        }
        //买入
        helper.setText(R.id.tv_buy_time, dealData(item.create_at))
        helper.setText(R.id.tv_buy_price, dealData(mContext.getString(R.string.money_format_us, item.buy_price)))
        helper.setText(R.id.tv_buy_fee, dealData("${item.buy_fee}/${item.base_coin}"))
        helper.setText(R.id.tv_target_profit, dealData(item.gain))
        helper.setText(R.id.tv_trade_coin, dealData("${item.base_coin}/${item.quote_coin}"))
        helper.setText(R.id.tv_trade_no, dealData(item.order_id))

        //卖出
        helper.setText(R.id.tv_sell_time, dealData(item.sell_time))
        helper.setText(R.id.tv_sell_price, dealData(mContext.getString(R.string.money_format_us, item.sell_price)))
        helper.setText(R.id.tv_sell_fee, dealData(mContext.getString(R.string.money_format_us, item.sell_fee)))
        helper.setText(R.id.tv_sell_loss, dealData(item.loss))
        helper.setText(R.id.tv_trade_amount, dealData(item.order_num))


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

    private fun dealData(dealDate: String?): String {
        if (hideData) return "******"
        if (dealDate.isNullOrBlank()) return "/"
        return dealDate
    }

    private var lastClick = -1
    private var hideData = false

    //是否显示数据
    fun isHideData(isHide: Boolean) {
        hideData = isHide
        notifyDataSetChanged()
    }
}
