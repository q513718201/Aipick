package com.hazz.aipick.ui.adapter


import android.content.Intent
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.Home
import com.hazz.aipick.ui.activity.MyAccountActivity


class HomeAdapter(layoutResId: Int, data: List<Home>?) : BaseQuickAdapter<Home, BaseViewHolder>(layoutResId, data) {

    private var subeeType="bot"
    override fun convert(helper: BaseViewHolder, item: Home) {
        helper.setText(R.id.tv_name, item.nickname)
        helper.setText(R.id.tv_rate,mContext.getString(R.string.ten_rate,item.pullback))
        helper.setText(R.id.tv_price,mContext.getString(R.string.home_price,item.price))

        helper.itemView.setOnClickListener {

            mContext.startActivity(Intent(mContext,MyAccountActivity::class.java).putExtra("id",item.id).putExtra("role",subeeType))
        }
    }

    fun  setRole(role:String){
        subeeType=role
    }
}
