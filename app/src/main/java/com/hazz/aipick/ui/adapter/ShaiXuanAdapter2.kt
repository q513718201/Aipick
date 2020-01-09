package com.hazz.aipick.ui.adapter

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.ShaixuanBean


class ShaiXuanAdapter2(data: List<ShaixuanBean>) :
        BaseMultiItemQuickAdapter<ShaixuanBean, BaseViewHolder>(data) {

    private var currentTitle = "不限"
    private var currentStart = "0"
    private var currentEnd = "100"

    private var isfirst: Boolean = false
    private var isEdit: Boolean = false
    private var curr: Int = 0

    init {
        addItemType(1, R.layout.item_text)
        addItemType(3, R.layout.item_editnum)
    }


    @SuppressLint("WrongConstant")
    override fun convert(helper: BaseViewHolder, item: ShaixuanBean) {

        when (helper.itemViewType) {
            1 -> {
                helper.setText(R.id.tv_name, item.title)
                if (curr == helper.adapterPosition && isfirst) {
                    helper.getView<RelativeLayout>(R.id.rl_tv).setBackgroundResource(R.drawable.bg_blue_solid_5dp_coner)
                } else {
                    helper.getView<RelativeLayout>(R.id.rl_tv).setBackgroundResource(R.drawable.bg_shaixuan_coner)
                }
                helper.itemView.setOnClickListener {
                    currentTitle = item.title
                    isEdit = false
                    isfirst = true
                    curr = helper.adapterPosition
                    notifyDataSetChanged()

                }
            }
            3 -> {

                if (isEdit) {
                    helper.getView<RelativeLayout>(R.id.rl_edit).setBackgroundResource(R.drawable.bg_blue_solid_5dp_coner)
                } else {
                    helper.getView<RelativeLayout>(R.id.rl_edit).setBackgroundResource(R.drawable.bg_shaixuan_coner)
                }

                helper.getView<EditText>(R.id.tv_edit).addTextChangedListener(object : TextWatcher {

                    override fun afterTextChanged(s: Editable?) {
                        Log.d("junjun", helper.adapterPosition.toString())
                        if (helper.adapterPosition == data.size - 2) {
                            currentStart = s.toString()

                        } else {
                            currentEnd = s.toString()
                        }
                        isEdit = true
                        isfirst = false
                        currentTitle=""
                        helper.getView<RelativeLayout>(R.id.rl_edit).setBackgroundResource(R.drawable.bg_blue_solid_5dp_coner)

                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    }

                })


            }

        }
    }

    fun getCurrentStart(): String {
        transfer()
        return currentStart

    }

    private fun transfer() {
        when (currentTitle) {
            "不限" -> {
                currentStart = "0"
                currentEnd = "100"
            }

            ">50%" -> {
                currentStart = "50"
                currentEnd = "100"
            }
        }
    }

    fun getCurrentEnd(): String {
        transfer()
        return currentEnd

    }

    fun reset() {
        isfirst = false
        isEdit = false
        notifyDataSetChanged()
        currentTitle = "全部"
        currentStart = "0"
        currentEnd = "100"
    }


    private var mBackHomeListener: Onclick? = null

    interface Onclick {
        fun click()
    }

    fun setOnclick(mBackHomeListener: Onclick) {//评论回调
        this.mBackHomeListener = mBackHomeListener
    }


}
