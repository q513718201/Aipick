package com.hazz.aipick.ui.fragment

import android.support.v7.widget.DefaultItemAnimator
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseFragment
import com.hazz.aipick.mvp.model.bean.Trade
import com.hazz.aipick.socket.WsManager
import com.hazz.aipick.ui.adapter.OnSellAdapter
import com.hazz.aipick.utils.RxBus
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_on_order.*


/**
 * 成交列表
 */
class OnSellFragment : BaseFragment(), WsManager.onTrade {
    private lateinit var adapter: OnSellAdapter

    private var coinName = ""
    private var key = ""

    companion object {
        fun getInstance(coinName: String): OnSellFragment {
            val fragment = OnSellFragment()
            fragment.coinName = coinName
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_on_sell
    }

    var data = ArrayList<Trade>()
    override fun initView() {
        adapter = OnSellAdapter(R.layout.item_on_sell, data)
        recyclerView.adapter = adapter

        recyclerView.isEnabled = false;
        recyclerView.setHasFixedSize(true)
        (recyclerView.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
    }

    override fun lazyLoad() {
        val split = coinName?.split(".")

        key = getString(R.string.ws_trade, split[1])
        Logger.d(key)
        WsManager.getInstance().requestTrade(key)

        RxBus.get().observerOnMain(activity, Trade::class.java) {
            if (it?.ch == key) {
                it?.let {
                    if (data.size < 10) {
                        data.add(it)
                    } else {
                        data.add(0, it)
                        data.removeAt(data.size - 1)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        WsManager.getInstance().setOnTrade(this)
    }

    override fun onDetach() {
        super.onDetach()
        WsManager.getInstance().setOnTrade(null)
    }


    var index = 0
    override fun onTradeBack(trade: Trade?) {
        RxBus.get().send(trade)
        Logger.d("============" + trade.toString())
        if (trade?.ch == key) {
            trade?.let {
                if (adapter.itemCount < 10) {
                    adapter.addData(it)
                } else {
                    adapter.setData(index++ % 10, it)
                    if (index == 10) {
                        index = 0
                    }
                }
            }
        }
    }

}