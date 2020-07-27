package com.hazz.aipick.ui.activity

import android.graphics.Color
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.socket.CoinDetail
import com.hazz.aipick.socket.WsManager
import com.hazz.aipick.ui.adapter.MarketsMoniItemAdapter
import com.hazz.aipick.ui.adapter.MarketsPagerItemAdapter
import com.hazz.aipick.utils.ToolBarCustom
import kotlinx.android.synthetic.main.activity_moni.*


class MonicpActivity : BaseActivity()  {

    override fun layoutId(): Int = R.layout.activity_moni

    private val mCoinList = arrayOf("market.btcusdt.detail", "market.ethusdt.detail",
            "market.htusdt.detail", "market.xrpusdt.detail", "market.ltcusdt.detail", "market.bchusdt.detail"
            , "market.eosusdt.detail", "market.etcusdt.detail", "market.bsvusdt.detail", "market.trxusdt.detail"

    )
    private var coinList: MutableList<CoinDetail>? = mutableListOf()
    private var adapter: MarketsMoniItemAdapter? = null
    private var index = 0

    override fun initData() {
        WsManager.getInstance().reconnect()
        for (coin in mCoinList) {
            WsManager.getInstance().requestCoinDetail(coin, "detail")
        }

        WsManager.getInstance().setOnMyClick {
            index++

            if (index == 1) {
                val entries = it.entries.iterator()
                coinList!!.clear()
                while (entries.hasNext()) {
                    val entry = entries.next()
                    val value = entry.value
                    coinList!!.add(value)

                }

                runOnUiThread {
                    adapter!!.setNewData(coinList)
                }

            }


        }

        WsManager.getInstance().setOnPing {
            if (coinList!!.size == 10) {
                for (index in coinList!!.indices) {
                    if (coinList!![index].ch == it.ch) {
                        runOnUiThread {
                            adapter!!.setData(index, it)
                        }
                    }
                }
            }

        }
    }

    override fun start() {
        ToolBarCustom.newBuilder(mToolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.mncp))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }

    }







    override fun initView() {
        recycleview.layoutManager = LinearLayoutManager(this)
        adapter = MarketsMoniItemAdapter( R.layout.item_market, ArrayList())

        recycleview.adapter = adapter
        recycleview.setHasFixedSize(true)//解决固定数目列表 单条目数据不刷新
        (recycleview.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false//去掉默认动画,解决闪烁
    }




}