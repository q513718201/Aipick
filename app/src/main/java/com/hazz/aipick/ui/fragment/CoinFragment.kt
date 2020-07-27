package com.hazz.aipick.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseFragment
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.model.bean.Coin
import com.hazz.aipick.mvp.model.bean.MarketItem
import com.hazz.aipick.mvp.presenter.CoinPresenter
import com.hazz.aipick.socket.CoinDetail
import com.hazz.aipick.socket.WsManager
import com.hazz.aipick.ui.activity.CoinDescActivity
import com.hazz.aipick.ui.activity.SearchHistoryActivity
import com.hazz.aipick.ui.adapter.MarketsPagerItemAdapter
import com.hazz.aipick.ui.adapter.MarketsViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_coin.*


class CoinFragment : BaseFragment()  {



    private var mTitle: String? = null
    private var mTitles: Array<String>? = null
    /**
     * 存放 tab 标题
     */
    private val mTabTitleList = ArrayList<String>()

    private val mCoinList = arrayOf("market.btcusdt.detail", "market.ethusdt.detail",
            "market.htusdt.detail", "market.xrpusdt.detail", "market.ltcusdt.detail", "market.bchusdt.detail"
            , "market.eosusdt.detail", "market.etcusdt.detail", "market.bsvusdt.detail", "market.trxusdt.detail"

    )
    private var coinList: MutableList<CoinDetail>? = mutableListOf()
    private var mViewPagerAdapter: MarketsViewPagerAdapter? = null
    private var index = 0

    companion object {
        fun getInstance(title: String): CoinFragment {
            val fragment = CoinFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }


    override fun getLayoutId(): Int = R.layout.fragment_coin


    override fun lazyLoad() {
        WsManager.getInstance().reconnect()
        for (coin in mCoinList) {
            WsManager.getInstance().requestCoinDetail(coin, "detail")
        }
        var adapter: MarketsPagerItemAdapter? = null
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
                adapter = mViewPagerAdapter!!.getItem(1).adapter as MarketsPagerItemAdapter

                activity!!.runOnUiThread {
                    adapter!!.setNewData(coinList)
                }

            }


        }

        WsManager.getInstance().setOnPing {
            if (coinList!!.size == 10) {
                for (index in coinList!!.indices) {
                    if (coinList!![index].ch == it.ch) {
                        activity!!.runOnUiThread {
                            adapter!!.setData(index, it)
                        }
                    }
                }
            }

    }

    }

//    override fun onHiddenChanged(hidden: Boolean) {
//        super.onHiddenChanged(hidden)
//        if (!hidden) {
//            Log.d("junjun",hidden.toString())
//            for (coin in mCoinList) {
//                WsManager.getInstance().requestCoinDetail(coin, "detail")
//            }
//
//        }
//    }

    override fun initView() {
        initTab()
        initViewPager()
        tv_add.setOnClickListener {
            startActivity(Intent(activity, SearchHistoryActivity::class.java))
        }


    }

    private fun initTab() {
        mTitles = resources.getStringArray(R.array.coin_list)
        mViewPagerAdapter = MarketsViewPagerAdapter(ArrayList<RecyclerView>(), mTitles)
        viewPager.adapter = mViewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {

                if (position == 0) {
                    tv_add.visibility = View.VISIBLE
                    mViewPagerAdapter!!.getItem(position).visibility = View.GONE
                } else {
                    tv_add.visibility = View.GONE
                    mViewPagerAdapter!!.getItem(position).visibility = View.VISIBLE

                }

            }

        })

    }

    @Synchronized
    private fun initViewPager() {
        val recyclerViewList = ArrayList<RecyclerView>()
        for (i in mTitles!!.indices) {  // 这里不是四个 Fragment 了，而是四个 recyclerView

            val recyclerView = RecyclerView(activity!!)
            recyclerView.layoutManager = LinearLayoutManager(activity)
            val adapter = MarketsPagerItemAdapter( R.layout.item_market, ArrayList())

            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)//解决固定数目列表 单条目数据不刷新
            (recyclerView.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false//去掉默认动画,解决闪烁
            recyclerViewList.add(recyclerView)
        }
        mViewPagerAdapter!!.updateList(recyclerViewList)
        viewPager.offscreenPageLimit = recyclerViewList.size
        viewPager.currentItem = 1

    }

}