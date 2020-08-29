package com.hazz.aipick.ui.adapter


import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.UserSubscribeBean
import com.hazz.aipick.utils.GlideUtil

class UserSubscribeAdapter(layoutResId: Int, data: List<UserSubscribeBean>?) : BaseQuickAdapter<UserSubscribeBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: UserSubscribeBean) {

        helper.setText(R.id.tv_name, item.suber_name)
        val view = helper.getView<ImageView>(R.id.civ_head)
        GlideUtil.showRound(item.subee_avatar, view, R.mipmap.ic_user)

        helper.setText(R.id.tv_time, item.follow_at)


        helper.setText(R.id.tv_benefit, item.amount)
    }
}
