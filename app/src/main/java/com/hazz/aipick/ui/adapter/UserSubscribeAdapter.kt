package com.hazz.aipick.ui.adapter


import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.UserSubscribeBean

class UserSubscribeAdapter(layoutResId: Int, data: List<UserSubscribeBean>?) : BaseQuickAdapter<UserSubscribeBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: UserSubscribeBean) {

        helper.setText(R.id.tv_name, item.subee_name)
        val view = helper.getView<ImageView>(R.id.civ_head)
        Glide.with(mContext).load(item.subee_avatar)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(view)

        helper.setText(R.id.tv_time, item.follow_at)


        helper.setText(R.id.tv_benefit, item.amount)
    }
}
