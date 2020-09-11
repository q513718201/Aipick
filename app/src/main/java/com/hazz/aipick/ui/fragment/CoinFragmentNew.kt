package com.hazz.aipick.ui.fragment

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseFragment
import com.hazz.aipick.socket.WsManager
import com.hazz.aipick.ui.adapter.MarketsPagerItemAdapter
import kotlinx.android.synthetic.main.fragment_coin.tv_change_btn
import kotlinx.android.synthetic.main.fragment_coin.tv_price_btn
import kotlinx.android.synthetic.main.fragment_coin_new.*


class CoinFragmentNew : BaseFragment() {


    private var mTitle: String? = null

    private val mCoinList = arrayOf("market.btcusdt.detail", "market.ethusdt.detail",
            "market.htusdt.detail", "market.xrpusdt.detail", "market.ltcusdt.detail", "market.bchusdt.detail"
            , "market.eosusdt.detail", "market.etcusdt.detail", "market.bsvusdt.detail", "market.trxusdt.detail"

    )

    companion object {
        fun getInstance(title: String): CoinFragmentNew {
            val fragment = CoinFragmentNew()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }


    override fun getLayoutId(): Int = R.layout.fragment_coin_new
    lateinit var adapter: MarketsPagerItemAdapter

    override fun lazyLoad() {

    }

    override fun onResume() {
        super.onResume()
        WsManager.getInstance().reconnect()
        for (coin in mCoinList) {
            WsManager.getInstance().requestCoinDetail(coin, "detail")
        }
        WsManager.getInstance().setOnDetail {
            if (mCoinList.contains(it.ch))
                adapter?.update(it)
        }
    }

    override fun onDetach() {
        super.onDetach()
        WsManager.getInstance().setOnDetail(null)
    }

    override fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = MarketsPagerItemAdapter(R.layout.item_market, ArrayList())
        adapter.setMarket("Huobi")
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)//解决固定数目列表 单条目数据不刷新
        (recyclerView.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false//去掉默认动画,解决闪烁

        tv_price_btn.setOnClickListener {
            adapter.sortByPrice()
        }
        tv_change_btn.setOnClickListener {
            adapter.sortByUp()
        }

    }

}