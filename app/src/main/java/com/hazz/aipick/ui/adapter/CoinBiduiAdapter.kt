package com.hazz.aipick.ui.adapter


import android.view.View
import android.widget.RelativeLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.BindCoinHouse


class CoinBiduiAdapter(layoutResId: Int, data: List<BindCoinHouse.SymbolsBean>?) : BaseQuickAdapter<BindCoinHouse.SymbolsBean, BaseViewHolder>(layoutResId, data) {

    private var currentTitle: BindCoinHouse.SymbolsBean? = null

    private var isfirst: Boolean = false
    private var curr: Int = 0

    override fun convert(helper: BaseViewHolder, item: BindCoinHouse.SymbolsBean) {
        //  helper.setText(R.id.tv_name, "")

        helper.setText(R.id.tv_name, """${item.base_coin}/${item.quote_coin}""")
        helper.getView<View>(R.id.tv_name).isSelected = curr == helper.adapterPosition

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

    fun getCurrent(): BindCoinHouse.SymbolsBean {
        return currentTitle!!

    }

    fun getCurr(): Int {
        return curr

    }
}
