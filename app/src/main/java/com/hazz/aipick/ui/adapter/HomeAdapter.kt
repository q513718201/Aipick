package com.hazz.aipick.ui.adapter


import android.content.Intent
import android.os.Bundle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.Home
import com.hazz.aipick.ui.activity.MyAccountActivity
import com.hazz.aipick.ui.activity.RebotCategryActivity
import com.hazz.aipick.utils.CoinManager


class HomeAdapter(layoutResId: Int, data: List<Home>?) : BaseQuickAdapter<Home, BaseViewHolder>(layoutResId, data) {

    private var subeeType = "bot"
    override fun convert(helper: BaseViewHolder, item: Home) {


        helper.setText(R.id.tv_name, item.nickname)
        helper.setText(R.id.tv_rate, mContext.getString(R.string.ten_rate, item.pullback))
        helper.setText(R.id.tv_price, mContext.getString(R.string.home_price, item.price))
        helper.setText(R.id.totalRate, item.rate)
        helper.setImageResource(R.id.coinIcon, CoinManager.getCoinIcon(item.coin_name))

        helper.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", item.id)
            bundle.putString("role", subeeType)
            bundle.putString("price", item.price)
            when (subeeType) {
                "bot" -> {
                    RebotCategryActivity.start(mContext,item.id,subeeType,item.price)
                }
                "broker" -> {
                    mContext.startActivity(Intent(mContext, MyAccountActivity::class.java).putExtra("data", bundle))
                }
            }
        }
    }

    fun setRole(role: String) {
        subeeType = role
    }
}
