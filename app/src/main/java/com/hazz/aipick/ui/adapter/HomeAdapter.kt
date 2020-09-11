package com.hazz.aipick.ui.adapter


import android.graphics.drawable.Drawable
import android.os.Bundle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.hazz.aipick.R
import com.hazz.aipick.managers.ChartManager
import com.hazz.aipick.mvp.model.bean.Home
import com.hazz.aipick.ui.activity.FollowRobotDetailActivity
import com.hazz.aipick.ui.activity.FollowTraderDetailActivity
import com.hazz.aipick.ui.activity.MyAccountActivity
import com.hazz.aipick.utils.CoinManager
import com.hazz.aipick.utils.SPUtil
import com.hazz.aipick.widget.ColorTextView
import java.util.*


class HomeAdapter(data: List<Home>?) : BaseQuickAdapter<Home, BaseViewHolder>(R.layout.item_home, data) {

    private var subeeType = "bot"
    override fun convert(helper: BaseViewHolder, item: Home) {


        helper.setText(R.id.tv_name, item.nickname)
        helper.setText(R.id.tv_rate, "${mContext.getString(R.string.ten_rate, item.pullback)}%")
        helper.setText(R.id.tv_price, mContext.getString(R.string.home_item_price, item.price))

        var totalRate = helper.getView<ColorTextView>(R.id.totalRate)
        totalRate?.setText(item.rate, "%")

        helper.setImageResource(R.id.coinIcon, CoinManager.getCoinIcon(item.coin_name))

        helper.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", item.id)
            bundle.putString("role", subeeType)
            bundle.putString("price", item.price)
            when (subeeType) {
                "bot" -> {
                    FollowRobotDetailActivity.start(mContext, item.id, subeeType, item.price)
                }
                "broker" -> {
                    if (SPUtil.getUser().uid == item.id) {
                        MyAccountActivity.start(mContext, item.id, subeeType, item.price)
                    } else {
                        FollowTraderDetailActivity.start(mContext, item.id, subeeType, item.price)
                    }
                }
            }
        }
        val view = helper.getView<LineChart>(R.id.rate_chart)

        val split = item.gain_list.split(",")
        val entries = ArrayList<Entry>()
        var min = Float.MAX_VALUE
        for ((index, value) in split.withIndex()) {
            min = min.coerceAtMost(value.toFloat())
            entries.add(Entry(index.toFloat(), value.toFloat()))
        }

        if (min < 0) {
            for (entry in entries) {
                entry.y = entry.y + (min * -1)
            }
        }
        var lineColor = mContext.resources.getColor(R.color.home_text)
        var isUp = item.rate.toFloat()
        var fillDrawable: Drawable = mContext.resources.getDrawable(R.drawable.fill_drawable_zero)
        if (isUp > 0) {
            lineColor = mContext.resources.getColor(R.color.color_up)
            fillDrawable = mContext.resources.getDrawable(R.drawable.fill_drawable_up)
        } else if (isUp < 0) {
            lineColor = mContext.resources.getColor(R.color.color_down)
            fillDrawable = mContext.resources.getDrawable(R.drawable.fill_drawable_down)
        }
        ChartManager.showLineChart(view, entries, lineColor, 3f, fillDrawable)

    }


    fun setRole(role: String) {
        subeeType = role
    }
}
