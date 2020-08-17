package com.hazz.aipick.ui.adapter


import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.Trade
import com.hazz.aipick.utils.BigDecimalUtil
import com.vinsonguo.klinelib.util.DateUtils


class OnSellAdapter(layoutResId: Int, data: List<Trade>?) : BaseQuickAdapter<Trade, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: Trade) {
//
//
        helper.setText(R.id.tv_time, DateUtils.formatDate(item.tick.ts))
        if (item.tick.data != null) {
            val direction = helper.getView<TextView>(R.id.tv_direct)
            direction.text = if (item.tick.data[0]?.direction == "buy") "买入" else "卖出"
            if (item.tick.data[0]?.direction == "buy") {
                direction.setTextColor(mContext.resources.getColor(R.color.main_color_green))
            } else {
                direction.setTextColor(mContext.resources.getColor(R.color.main_color_red))
            }
            helper.setText(R.id.tv_price, BigDecimalUtil.formatMoney(item.tick.data[0].amount))
            helper.setText(R.id.tv_amount, BigDecimalUtil.format(item.tick.data[0].amount, 2))
        }

    }
}
