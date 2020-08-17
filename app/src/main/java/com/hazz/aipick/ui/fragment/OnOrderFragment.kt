package com.hazz.aipick.ui.fragment

import android.support.v7.widget.DefaultItemAnimator
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseFragment
import com.hazz.aipick.mvp.model.bean.BuyOrSell
import com.hazz.aipick.socket.WsManager
import com.hazz.aipick.ui.adapter.OnOrderAdapter
import com.hazz.aipick.utils.RxBus
import com.hazz.aipick.utils.ToastUtils
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_on_order.*


class OnOrderFragment : BaseFragment(), WsManager.onBbo {
    private lateinit var onSellAdapter: OnOrderAdapter
    private var coinName = ""
    private var key = ""

    companion object {
        fun getInstance(coinName: String): OnOrderFragment {
            val fragment = OnOrderFragment()
            fragment.coinName = coinName
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_on_order
    }

    override fun initView() {
        onSellAdapter = OnOrderAdapter(R.layout.item_on_order, data)
        recyclerView.adapter = onSellAdapter;
        recyclerView.isEnabled = false;
        recyclerView.setHasFixedSize(true)
        (recyclerView.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
    }

    override fun lazyLoad() {

        val split = coinName?.split(".")
        key = getString(R.string.ws_bbo, split[1])
        Logger.d(key)
        WsManager.getInstance().requestBbo(key)
        WsManager.getInstance().setOnBbo(this)
        RxBus.get().observerOnMain(activity, BuyOrSell::class.java) {
            if (it?.ch == key) {
                it?.let {
                    if (data.size < 10) {
                        data.add(it)
                    } else {
                        data.add(0, it)
                        data.removeAt(data.size - 1)
                    }
                    onSellAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDetach() {
        super.onDetach()
        WsManager.getInstance().setOnBbo(null)
    }

    var data = ArrayList<BuyOrSell>()


    override fun onBbo(coinDetails: BuyOrSell?) {
        RxBus.get().send(coinDetails)
    }

    var index = 0
}