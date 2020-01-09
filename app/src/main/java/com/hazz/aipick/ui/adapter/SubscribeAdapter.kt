package com.hazz.aipick.ui.adapter


import android.content.Intent
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.mvp.model.bean.MySubscribe
import com.hazz.aipick.ui.activity.MineSubscribeActivity
import com.hazz.aipick.ui.activity.SubscribeDescActivity

class SubscribeAdapter(layoutResId: Int, data: List<MySubscribe>?) : BaseQuickAdapter<MySubscribe, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: MySubscribe) {



        helper.itemView.setOnClickListener {
            mContext.startActivity(Intent(mContext, SubscribeDescActivity::class.java).putExtra("subId",item.sub_id))
        }
    }
}
