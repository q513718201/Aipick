package com.hazz.aipick.ui.adapter


import android.os.Bundle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.Home
import com.hazz.aipick.ui.activity.MyAccountActivity
import com.hazz.aipick.ui.activity.RebotCategryActivity
import com.hazz.aipick.utils.CoinManager
import com.hazz.aipick.view.BezierCurveChart
import kotlin.random.Random


class HomeAdapter(layoutResId: Int, data: List<Home>?) : BaseQuickAdapter<Home, BaseViewHolder>(layoutResId, data) {

    private var subeeType = "bot"
    override fun convert(helper: BaseViewHolder, item: Home) {


        helper.setText(R.id.tv_name, item.nickname)
        helper.setText(R.id.tv_rate, "${mContext.getString(R.string.ten_rate, item.pullback)}%")
        helper.setText(R.id.tv_price, mContext.getString(R.string.home_price, item.price))
        helper.setText(R.id.totalRate, item.rate)
        helper.setImageResource(R.id.coinIcon, CoinManager.getCoinIcon(item.coin_name))

        helper.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", item.id)
            bundle.putString("role", subeeType)
            bundle.putString("price", item.price)
            when (subeeType) {
                "bot" -> {
                    RebotCategryActivity.start(mContext, item.id, subeeType, item.price)
                }
                "broker" -> {
                    MyAccountActivity.start(mContext, item.id, subeeType, item.price, "home")
                }
            }
        }
        val view = helper.getView<BezierCurveChart>(R.id.rate_chart)
        var points = ArrayList<BezierCurveChart.Point>()
        points.add(BezierCurveChart.Point(1f, 10f));
        points.add(BezierCurveChart.Point(2f, 55f));
        points.add(BezierCurveChart.Point(3f, 7f));
        points.add(BezierCurveChart.Point(4f, 16f));
        points.add(BezierCurveChart.Point(5f, 6f));
        points.add(BezierCurveChart.Point(6f, 26f));
        points.add(BezierCurveChart.Point(7f, 66f));
        points.add(BezierCurveChart.Point(8f, 76f));
        var title = arrayOf<String>("0:00", "6:00", "12:00", "18:00", "24:00")
        view.init(points, title, "tipText");
    }

    fun setRole(role: String) {
        subeeType = role
    }
}
