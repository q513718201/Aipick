package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.socket.WsManager
import com.hazz.aipick.ui.fragment.AboutCoinFragment
import com.hazz.aipick.utils.BigDecimalUtil
import kotlinx.android.synthetic.main.activity_coin_desc.*


class CoinDescActivity : BaseActivity() {


    override fun layoutId(): Int = R.layout.activity_coin_desc

    override fun initData() {
    }

    private var coinName = ""

    @SuppressLint("SetTextI18n", "ResourceType")
    override fun initView() {
        coinName = intent.getStringExtra("name")
        var market = intent.getStringExtra("market")
        val split = coinName.split(".")
        var nameKine = split[0] + "." + split[1] + "."
        var name = split[1].substring(0, split[1].length - 4).toUpperCase()

        tv_title.text = "$name/USDT"
        tv_sub_title.text = market
        chart.setSymbol(nameKine)

        initAbout()

        iv_back.setOnClickListener { finish() }

        WsManager.getInstance().setOnPing {
            if (it.ch == coinName) {
                val tick = it.tick
                val compare = BigDecimalUtil.compare(tick.close, tick.open)
                it.isUp = compare != -1
                val rate = BigDecimalUtil.mul(BigDecimalUtil.div(BigDecimalUtil.sub(tick.close, tick.open, 6), tick.open, 6), "100", 2) + "%"
                runOnUiThread {
                    tv_rate.text = if (it.isUp) "+$rate" else rate
                    if (!it.isUp) {
                        tv_name.setTextColor(resources.getColor(R.color.main_color_red))
                        tv_rate.setTextColor(resources.getColor(R.color.main_color_red))
                        tv_zj.setTextColor(resources.getColor(R.color.main_color_red))

                        tv_zj.text = "-" + BigDecimalUtil.sub(tick.open, tick.close, 4)
                        iv_up.setImageResource(R.mipmap.ic_down)
                    } else {
                        tv_zj.text = "+" + BigDecimalUtil.sub(tick.close, tick.open, 4)
                        tv_name.setTextColor(resources.getColor(R.color.main_color_green))
                        tv_rate.setTextColor(resources.getColor(R.color.main_color_green))
                        tv_zj.setTextColor(resources.getColor(R.color.main_color_green))
                        iv_up.setImageResource(R.mipmap.up)
                    }
                    tv_name.text = tick.close
                    tv_hight.text = tick.high
                    tv_low.text = tick.low
                    tv_vol.text = BigDecimalUtil.format(tick.vol, 4)

                }
            }


        }

    }

    private fun initAbout() {
        supportFragmentManager.beginTransaction().add(R.id.about_coin, AboutCoinFragment.getInstance(coinName)).commit()
    }

    override fun start() {

    }

}
