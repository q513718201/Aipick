package com.hazz.aipick.ui.adapter


import android.widget.RelativeLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.BindCoinHouse


class CoinAdapter(layoutResId: Int, data: List<BindCoinHouse.ExchangesBean>?) : BaseQuickAdapter<BindCoinHouse.ExchangesBean, BaseViewHolder>(layoutResId, data) {

    private var currentTitle: BindCoinHouse.ExchangesBean? = null
    private var symbolsBean: BindCoinHouse.SymbolsBean? = null
    private var isfirst: Boolean = false
    private var curr: Int = 0

    override fun convert(helper: BaseViewHolder, item: BindCoinHouse.ExchangesBean) {
        //  helper.setText(R.id.tv_name, "")

        helper.setText(R.id.tv_name, item.exchange_code)

        if (curr == helper.adapterPosition) {
            currentTitle = item
            helper.getView<RelativeLayout>(R.id.rl_tv).setBackgroundResource(R.drawable.bg_blue_solid_5dp_coner)
        } else {
            helper.getView<RelativeLayout>(R.id.rl_tv).setBackgroundResource(R.drawable.bg_shaixuan_coner)
        }
        helper.itemView.setOnClickListener {
            currentTitle = item
            isfirst = true
            curr = helper.adapterPosition
            notifyDataSetChanged()

        }

    }

    fun getCurrent(): BindCoinHouse.ExchangesBean? {
        return currentTitle
    }

    fun getCurr(): Int {
        return curr

    }
}
