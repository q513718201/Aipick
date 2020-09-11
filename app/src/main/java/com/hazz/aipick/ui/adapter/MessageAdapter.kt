package com.hazz.aipick.ui.adapter


import android.content.Intent
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.Message
import com.hazz.aipick.ui.activity.MsgDescActivity
import com.hazz.aipick.utils.RxBus

class MessageAdapter(data: List<Message>?) : BaseQuickAdapter<Message, BaseViewHolder>(R.layout.item_msg, data) {

    override fun convert(helper: BaseViewHolder, item: Message) {
        helper.setText(R.id.mTvNoticeTitle, item.title)
        helper.setText(R.id.mTvTime, item.create_at)
        helper.setVisible(R.id.msg_status, item.status == 0)
        helper.itemView.setOnClickListener {
            mContext.startActivity(Intent(mContext, MsgDescActivity::class.java).putExtra("message", item))
            RxBus.get().send(item)
        }
    }
}
