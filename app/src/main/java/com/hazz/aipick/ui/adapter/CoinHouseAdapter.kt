package com.hazz.aipick.ui.adapter


import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.CoinHouse
import com.hazz.aipick.ui.activity.CoinHouseDescActivity
import com.hazz.aipick.ui.activity.MineSubscribeActivity
import com.hazz.aipick.ui.activity.SubscribeDescActivity

class CoinHouseAdapter(layoutResId: Int, data: List<CoinHouse>?) : BaseQuickAdapter<CoinHouse, BaseViewHolder>(layoutResId, data) {

    lateinit var onConfirm: (View,Int) -> Unit

    override fun convert(helper: BaseViewHolder, item: CoinHouse) {

        if(item.bound){
            helper.setImageResource(R.id.iv_bind,R.mipmap.bt_miyao_press)
        }

        helper.getView<ImageView>(R.id.iv_bind).setOnClickListener {
            if(::onConfirm.isInitialized){
                onConfirm(it,item.exchange_id)
            }
        }
        helper.itemView.setOnClickListener {
            mContext.startActivity(Intent(mContext, CoinHouseDescActivity::class.java).putExtra("exchangeId",item.exchange_id))
        }
    }
}
