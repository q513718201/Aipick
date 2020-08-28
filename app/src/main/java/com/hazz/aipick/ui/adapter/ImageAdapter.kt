package com.hazz.aipick.ui.adapter


import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R

class ImageAdapter(layoutResId: Int, data: List<String>?) : BaseQuickAdapter<String, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: String) {
        item?.let {
            var iv = helper.getView<ImageView>(R.id.iv)
            Glide.with(iv).load(it).into(iv)
        }

    }
}
