package com.hazz.aipick.ui.activity


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import kotlinx.android.synthetic.main.activity_coin_trade.*


class CoinBuyAndSellActivity : BaseActivity() {

    companion object {
        fun start(activity: Activity, name: String, tradeType: Int) {
            val intent = Intent(activity, CoinBuyAndSellActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("type", tradeType)
            activity.startActivity(intent)
        }
    }

    override fun layoutId(): Int = R.layout.activity_coin_trade

    private var type: Int = 0//交易类型0 买 1 卖
    override fun initData() {


    }

    @SuppressLint("SetTextI18n", "ResourceType")
    override fun initView() {
        var coinName = intent.getStringExtra("name")
        type = intent.getIntExtra("type", 0)
        val split = coinName.split(".")
        var nameKine = split[0] + "." + split[1] + "."


        var name = split[1].substring(0, split[1].length - 4).toUpperCase()
        tv_title.text = "$name/USDT"

        btn_submit.text = "${if (type == 0) getString(R.string.text_buy_in) else getString(R.string.text_sell_out)}$name"
        initStage(type)
    }

    private fun initStage(type: Int) {
        when (type) {
            0 -> {

            }
            1 -> {

            }
        }
    }

    override fun start() {

    }

}
