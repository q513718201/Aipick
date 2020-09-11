package com.hazz.aipick.ui.adapter


import android.content.Intent
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.CoinHouse
import com.hazz.aipick.ui.activity.CoinHouseDescActivity

class CoinHouseAdapter(layoutResId: Int, data: List<CoinHouse>?) : BaseQuickAdapter<CoinHouse, BaseViewHolder>(layoutResId, data) {

    lateinit var onConfirm: (View, Int) -> Unit

    override fun convert(helper: BaseViewHolder, item: CoinHouse) {

        if (item.bound) {
            helper.setImageResource(R.id.iv_bind, R.mipmap.bt_miyao_press)
        } else {
            helper.setImageResource(R.id.iv_bind, R.mipmap.bt_miyao)
        }

        helper.itemView.setOnClickListener {
            if (item.bound) {
                mContext.startActivity(Intent(mContext, CoinHouseDescActivity::class.java)
                        .putExtra("exchangeId", item.exchange_id)
                        .putExtra("title", item.name))
            } else {
                if (::onConfirm.isInitialized) {
                    onConfirm(it, item.exchange_id)
                }
            }


        }
        helper.setOnClickListener(R.id.iv_bind) {
            if (::onConfirm.isInitialized) {
                onConfirm(it, item.exchange_id)
            }
        }
        helper.setText(R.id.tv_coin_name, item.name)
        val tvCoinName = helper.getView<TextView>(R.id.tv_coin_name)
//        tvCoinName.setBackgroundResource(CoinManager.getCoinHouseIcon("huobi"))// TODO: 2020/8/17
    }
}
