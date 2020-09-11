package com.hazz.aipick.ui.adapter


import android.widget.CompoundButton
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.Collection
import com.hazz.aipick.utils.CoinManager

class CollectionAdapter(data: List<Collection>?) : BaseQuickAdapter<Collection, BaseViewHolder>(R.layout.item_collection, data) {

    fun setEditable(mode: Boolean) {
        this.editable = mode
        notifyDataSetChanged()
    }

    private var editable = false//0不可编辑 1，可编辑

    override fun convert(helper: BaseViewHolder, item: Collection) {
        helper.setVisible(R.id.cb, editable)
        helper.setImageResource(R.id.iv, CoinManager.getCoinIcon(item.base_coin))
        helper.setText(R.id.tv_coin_name, item.name)
        helper.setText(R.id.tv_date, item.create_at)
        helper.setOnCheckedChangeListener(R.id.cb, CompoundButton.OnCheckedChangeListener { compoundButton: CompoundButton, checked: Boolean ->
            item.select = checked
        })
    }

    fun getSelectIds(): ArrayList<Int> {
        var ids = ArrayList<Int>()
        for (item in data) {
            if (item.select) ids.add(item.id)
        }
        return ids
    }
}
