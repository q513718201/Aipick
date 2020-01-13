package com.hazz.aipick.ui.adapter


import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.ui.activity.MineSubscribeActivity
import com.hazz.aipick.ui.activity.SubscribeDescActivity
import kotlinx.android.synthetic.main.item_home.view.*

class CoinListAdapter(layoutResId: Int, data: List<String>?) : BaseQuickAdapter<String, BaseViewHolder>(layoutResId, data) {

    lateinit var onConfirm:(View, String)->Unit

    override fun convert(helper: BaseViewHolder, item: String) {

     helper.setText(R.id.tv_name,item)
        helper.getView<ImageView>(R.id.iv_insert).setOnClickListener {
            if(::onConfirm.isInitialized){
                onConfirm(it,item)
            }
        }
    }
}
