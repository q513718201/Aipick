package com.hazz.aipick.ui.adapter


import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.CoinHouseDesc

class CoinHouseCategryAdapter(layoutResId: Int, data: List<CoinHouseDesc.RecommendsBean>?) : BaseQuickAdapter<CoinHouseDesc.RecommendsBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: CoinHouseDesc.RecommendsBean) {
        helper.setText(R.id.tv_name, item.nickname)
        helper.setText(R.id.tv1, "${item.gain_rate}%")
        helper.setText(R.id.tv2, mContext.resources.getString(R.string.money_format_us, item.follower_gain))
        helper.setText(R.id.tv3, item.follow_times)
        helper.setText(R.id.tv4, "${item.max_pullback}%")
        helper.setText(R.id.tv5, "${mContext.resources.getString(R.string.money_format, item.price)}/月")
    }
}
