package com.hazz.aipick.ui.adapter


import android.content.Intent
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.CoinHouseDesc
import com.hazz.aipick.ui.activity.MineSubscribeActivity
import com.hazz.aipick.ui.activity.SubscribeDescActivity
import kotlinx.android.synthetic.main.item_coin_house_categry.view.*

class CoinHouseCategryAdapter(layoutResId: Int, data: List<CoinHouseDesc.RecommendsBean>?) : BaseQuickAdapter<CoinHouseDesc.RecommendsBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: CoinHouseDesc.RecommendsBean) {
          helper.setText(R.id.tv_name,item.nickname)
        helper.setText(R.id.tv1,item.gain_rate)
        helper.setText(R.id.tv2,item.follower_gain)
        helper.setText(R.id.tv3,item.follow_times)
        helper.setText(R.id.tv4,item.max_pullback)
        helper.setText(R.id.tv5,item.price)
    }
}
