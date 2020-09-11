package com.hazz.aipick.ui.adapter


import android.content.Intent
import android.view.View
import android.widget.RelativeLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.BindCoinHouse
import com.hazz.aipick.ui.activity.CoinHouseActivity


class CoinAdapter(data: List<BindCoinHouse.ExchangesBean>?) : BaseQuickAdapter<BindCoinHouse.ExchangesBean, BaseViewHolder>(R.layout.item_text, data) {

    private var currentTitle: BindCoinHouse.ExchangesBean? = null
    private var isfirst: Boolean = false
    private var curr: Int = 0

    override fun convert(helper: BaseViewHolder, item: BindCoinHouse.ExchangesBean) {
        helper.setText(R.id.tv_name, item.exchange_code)
        helper.getView<View>(R.id.tv_name).isSelected = curr == helper.adapterPosition
        if (curr == helper.adapterPosition) {
            currentTitle = item
            helper.getView<RelativeLayout>(R.id.rl_tv).setBackgroundResource(R.drawable.bg_blue_solid_5dp_coner)
        } else {
            helper.getView<RelativeLayout>(R.id.rl_tv).setBackgroundResource(R.drawable.bg_shaixuan_coner)
        }
        helper.itemView.setOnClickListener {
            if (item.exchange_id == "-1") {
                mContext.startActivity(Intent(mContext, CoinHouseActivity::class.java))
            } else {
                currentTitle = item
                isfirst = true
                curr = helper.adapterPosition
            }
        }

    }

    override fun setNewData(data: MutableList<BindCoinHouse.ExchangesBean>?) {
        if (data.isNullOrEmpty()) {
            super.setNewData(data)
        } else {
            var newData = data
            val exchangesBean = BindCoinHouse.ExchangesBean()
            exchangesBean.exchange_id = "-1"
            exchangesBean.exchange_code = "+"
            newData.add(exchangesBean)
            super.setNewData(data)
        }
    }


    fun getCurrent(): BindCoinHouse.ExchangesBean? {
        return currentTitle
    }

    fun getCurr(): Int {
        return curr

    }
}
