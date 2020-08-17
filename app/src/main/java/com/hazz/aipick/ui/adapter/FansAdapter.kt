package com.hazz.aipick.ui.adapter


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.Fans
import com.hazz.aipick.utils.RxBus

class FansAdapter(layoutResId: Int, data: List<Fans.ListBean>?) : BaseQuickAdapter<Fans.ListBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: Fans.ListBean) {

        val view = helper.getView<ImageView>(R.id.iv)
        Glide.with(mContext).load(item.follower_avatar).into(view)

        helper.setText(R.id.tv_name, item.follower_nickname)

        val guan_zhu = helper.getView<TextView>(R.id.tv_guanzhu)

        if (item.mutual) {
            guan_zhu.text = mContext.resources.getString(R.string.hasguanzhu)
            guan_zhu.setBackgroundResource(R.drawable.btn_solid_follow_bg)
            guan_zhu.setTextColor(mContext.resources.getColor(R.color.white))
        } else {
            guan_zhu.text = """+ ${mContext.resources.getString(R.string.guanzhu)}"""
            guan_zhu.setBackgroundResource(R.drawable.btn_solid_zi_bg)
            guan_zhu.setTextColor(mContext.resources.getColor(R.color.dilaog_btn_color))
        }

        guan_zhu.setOnClickListener {
            RxBus.get().send(item)
        }

    }
}
