package com.hazz.aipick.ui.adapter


import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.Order
import com.hazz.aipick.mvp.model.bean.SubscribeDesc
import com.hazz.aipick.utils.CoinManager
import com.hazz.aipick.utils.GlideUtil

class FollowBenefitAdapter(data: List<SubscribeDesc.GainsBean>?) : BaseQuickAdapter<SubscribeDesc.GainsBean, BaseViewHolder>(R.layout.item_follow_benefit, data) {

    override fun convert(helper: BaseViewHolder, item: SubscribeDesc.GainsBean) {
        val coinIcon = helper.getView<ImageView>(R.id.iv)
        GlideUtil.showImage(CoinManager.getCoinIcon(item.base_coin),coinIcon)
        helper.setText(R.id.tv_coin_name, item.base_coin)
        helper.setText(R.id.tv_market, "/${item.exchange_code}")
        if (item.amount != null && item.amount[0]?.isDigit()) {
            helper.setTextColor(R.id.tv_gain, mContext.resources.getColor(R.color.main_color_green))
        } else {
            helper.setTextColor(R.id.tv_gain, mContext.resources.getColor(R.color.main_color_red))
        }
        helper.setText(R.id.tv_gain, if (item.amount == null) "--" else item.amount)
        helper.setText(R.id.tv_time, item.create_at)

    }

}
