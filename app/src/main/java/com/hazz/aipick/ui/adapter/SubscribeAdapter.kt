package com.hazz.aipick.ui.adapter


import android.annotation.SuppressLint
import android.content.Intent
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.MySubscribe
import com.hazz.aipick.ui.activity.SubscribeDescActivity
import com.hazz.aipick.utils.CoinManager
import com.hazz.aipick.utils.GlideUtil

class SubscribeAdapter(layoutResId: Int, data: List<MySubscribe>?) : BaseQuickAdapter<MySubscribe, BaseViewHolder>(layoutResId, data) {

    @SuppressLint("StringFormatInvalid")
    override fun convert(helper: BaseViewHolder, item: MySubscribe) {
        val view = helper.getView<ImageView>(R.id.iv)
        when (type) {
            1 -> {

                helper.setText(R.id.tv_name, item.subee_nickname)
                GlideUtil.showRound(item.subee_avatar, view, R.mipmap.ic_user)
            }
            else -> {
                helper.setText(R.id.tv_name, item.suber_nickname)
                GlideUtil.showRound(item.suber_avatar, view, R.mipmap.ic_user)
            }
        }

        helper.setText(R.id.tv_time, item.create_at)

        helper.setText(R.id.tv_price, mContext.getString(R.string.money_format, item.price))

        when (item.status) {
            "unpaid" -> helper.setText(R.id.tv_state, mContext.getString(R.string.unpaid))
            "paid" -> helper.setText(R.id.tv_state, mContext.getString(R.string.paid))
            "cancel" -> helper.setText(R.id.tv_state, mContext.getString(R.string.has_cancel))
        }
        helper.itemView.setOnClickListener {
            mContext.startActivity(Intent(mContext, SubscribeDescActivity::class.java).putExtra("subId", item.sub_id).putExtra("type", type))
        }
    }

    private var role = "bot"
    private var type = 1
    fun setRole(role: String) {
        this.role = role
    }

    fun setType(type: Int) {
        this.type = type
    }
}
