package com.hazz.aipick.ui.adapter


import android.content.Intent
import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.TixianRecord


class TixianAdapter(layoutResId: Int, data: List<TixianRecord>?) : BaseQuickAdapter<TixianRecord, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: TixianRecord) {
        helper.setText(R.id.tv_price, item.amount)
        helper.setText(R.id.tv_num, item.create_at)
        helper.setText(R.id.tv_price, item.status)
        when(item.status){
            "已完成"-> helper.setTextColor(R.id.tv_price, Color.parseColor("#15F8D3"))
            else->   helper.setTextColor(R.id.tv_price, Color.parseColor("#F0BC33"))
        }
    }

}
