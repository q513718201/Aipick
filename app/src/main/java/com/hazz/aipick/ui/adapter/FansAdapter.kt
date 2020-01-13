package com.hazz.aipick.ui.adapter


import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.Collection
import com.hazz.aipick.mvp.model.bean.Fans
import com.hazz.aipick.ui.activity.MineSubscribeActivity
import com.hazz.aipick.ui.activity.SubscribeDescActivity
import kotlinx.android.synthetic.main.item_fans.view.*

class FansAdapter(layoutResId: Int, data: List<Fans.ListBean>?) : BaseQuickAdapter<Fans.ListBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: Fans.ListBean) {

        val view = helper.getView<ImageView>(R.id.iv)
        Glide.with(mContext).load(item.follower_avatar).into(view)

        helper.setText(R.id.tv_name,item.follower_nickname)

        if(item.mutual){
            helper.getView<TextView>(R.id.tv_yiguanzhu).visibility= View.VISIBLE
        }else{
            helper.getView<TextView>(R.id.tv_guanzhu).visibility= View.VISIBLE
        }
    }
}
