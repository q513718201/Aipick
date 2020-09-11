package com.hazz.aipick.ui.adapter


import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.Fans
import com.hazz.aipick.utils.GlideUtil
import com.hazz.aipick.utils.RxBus

class FansAdapter(layoutResId: Int, data: List<Fans.ListBean>?) : BaseQuickAdapter<Fans.ListBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: Fans.ListBean) {

        val view = helper.getView<ImageView>(R.id.iv)


        val guan_zhu = helper.getView<TextView>(R.id.tv_guanzhu)

        when (type) {
            "fans" -> {
                GlideUtil.showRound(item.follower_avatar, view, R.mipmap.ic_user)
                helper.setText(R.id.tv_name, item.follower_nickname)

                guan_zhu.setBackgroundResource(R.drawable.btn_solid_zi_bg)
                guan_zhu.setTextColor(mContext.resources.getColor(R.color.dilaog_btn_color))
                //粉丝 如果关注了 这个mutual 为true 展示取消关注，反之展示为 + 关注
                if (item.mutual) {
                    guan_zhu.text = mContext.resources.getString(R.string.hasguanzhu)
                    guan_zhu.setBackgroundResource(R.drawable.btn_solid_follow_bg)
                    guan_zhu.setTextColor(mContext.resources.getColor(R.color.white))
                } else {
                    guan_zhu.text = "+ ${mContext.resources.getString(R.string.guanzhu)}"
                    guan_zhu.setBackgroundResource(R.drawable.btn_solid_zi_bg)
                    guan_zhu.setTextColor(mContext.resources.getColor(R.color.dilaog_btn_color))
                }
            }
            else -> {
                helper.setText(R.id.tv_name, item.followee_nickname)
                GlideUtil.showRound(item.followee_avatar, view, R.mipmap.ic_user)
                guan_zhu.setBackgroundResource(R.drawable.btn_solid_follow_bg)
                guan_zhu.setTextColor(mContext.resources.getColor(R.color.white))
                if (item.mutual) {
                    guan_zhu.text = mContext.resources.getString(R.string.hasguanzhu)
                } else {
                    guan_zhu.text = mContext.resources.getString(R.string.yiguanzhu)
                }

            }
        }


        guan_zhu.setOnClickListener {
            RxBus.get().send(item)
        }

    }

    private var type = "fans"
    fun setType(type: String) {
        this.type = type
    }
}
