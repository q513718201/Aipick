package com.hazz.aipick.ui.adapter


import android.content.Intent
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.Message
import com.hazz.aipick.mvp.model.bean.TixianRecord
import com.hazz.aipick.ui.activity.MineSubscribeActivity
import com.hazz.aipick.ui.activity.MsgDescActivity
import com.hazz.aipick.ui.activity.SubscribeDescActivity

class MessageAdapter(layoutResId: Int, data: List<Message>?) : BaseQuickAdapter<Message, BaseViewHolder>(layoutResId, data) {


    lateinit var onConfirm: (View, Int) -> Unit

    override fun convert(helper: BaseViewHolder, item: Message) {

        helper.setText(R.id.mTvNoticeTitle,item.title)
        helper.setText(R.id.mTvTime,item.create_at)

        helper.itemView.setOnClickListener {

            mContext.startActivity(Intent(mContext,MsgDescActivity::class.java).putExtra("message",item))
        }
    }
}
