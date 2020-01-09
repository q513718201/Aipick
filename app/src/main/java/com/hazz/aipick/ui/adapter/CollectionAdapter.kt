package com.hazz.aipick.ui.adapter


import android.content.Intent
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.mvp.model.bean.Collection
import com.hazz.aipick.ui.activity.MineSubscribeActivity
import com.hazz.aipick.ui.activity.SubscribeDescActivity

class CollectionAdapter(layoutResId: Int, data: List<Collection>?) : BaseQuickAdapter<Collection, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: Collection) {

        helper.itemView.setOnClickListener {

        }
    }
}
