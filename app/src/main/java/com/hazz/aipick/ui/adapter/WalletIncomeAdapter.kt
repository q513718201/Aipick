package com.hazz.aipick.ui.adapter


import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.IncomeBean
import com.hazz.aipick.utils.BigDecimalUtil
import com.hazz.aipick.utils.GlideUtil


class WalletIncomeAdapter(data: List<IncomeBean>?) : BaseQuickAdapter<IncomeBean, BaseViewHolder>(R.layout.item_income, data) {

    override fun convert(helper: BaseViewHolder, item: IncomeBean) {

        var circleImageView = helper.getView<ImageView>(R.id.circleImageView)
        GlideUtil.showImage(item.from_avatar, circleImageView)

        helper.setText(R.id.tv_price, mContext.getString(R.string.text_income_title, item.from_name, "${BigDecimalUtil.div(item.days, "30", 0)}个月"))

        helper.setText(R.id.tv_time, item.create_at)
        helper.setText(R.id.tv_amount, mContext.getString(R.string.money_format, item.amount))

    }

}
