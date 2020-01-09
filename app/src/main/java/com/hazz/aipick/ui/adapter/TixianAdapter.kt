package com.hazz.aipick.ui.adapter


import android.content.Intent
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.mvp.model.bean.TixianRecord
import com.hazz.aipick.ui.activity.MineSubscribeActivity
import com.hazz.aipick.ui.activity.SubscribeDescActivity

class TixianAdapter(layoutResId: Int, data: List<TixianRecord>?) : BaseQuickAdapter<TixianRecord, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: TixianRecord) {

    }
}
