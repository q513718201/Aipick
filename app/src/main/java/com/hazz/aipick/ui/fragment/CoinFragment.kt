package com.hazz.aipick.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseFragment
import com.hazz.aipick.mvp.model.bean.MarketItem
import com.hazz.aipick.socket.WebSocket
import com.hazz.aipick.ui.activity.CoinDescActivity
import com.hazz.aipick.ui.activity.SearchHistoryActivity
import com.hazz.aipick.ui.adapter.MarketsPagerItemAdapter
import com.hazz.aipick.ui.adapter.MarketsViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_coin.*

/**
 * Created by xuhao on 2017/11/9.
 * 热门
 */
class CoinFragment : BaseFragment(), BaseQuickAdapter.OnItemClickListener {

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        startActivity(Intent(activity, CoinDescActivity::class.java))
    }


    private var mTitle: String? = null
    private var mTitles: Array<String>? = null
    /**
     * 存放 tab 标题
     */
    private val mTabTitleList = ArrayList<String>()

    private val mFragmentList = ArrayList<Fragment>()
    private var mViewPagerAdapter: MarketsViewPagerAdapter? = null

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

    }

    override fun initView() {
//        StatusBarUtil.setPaddingSmart(activity!!, rl)
        initTab()
        initViewPager()
        tv_add.setOnClickListener {
            startActivity(Intent(activity, SearchHistoryActivity::class.java))
        }

        WebSocket.getInstance().tryToConnect()
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
//
                    tv_add.visibility = View.VISIBLE
                }else{
                    tv_add.visibility = View.GONE
                }
                var adapter:MarketsPagerItemAdapter = mViewPagerAdapter!!.getItem(position).adapter as MarketsPagerItemAdapter
                 var list:MutableList<MarketItem>?= mutableListOf()
                list!!.add(MarketItem())
                list!!.add(MarketItem())
                list!!.add(MarketItem())
                list!!.add(MarketItem())
                list!!.add(MarketItem())
                list!!.add(MarketItem())
                list!!.add(MarketItem())
                list!!.add(MarketItem())
                list!!.add(MarketItem())
                list!!.add(MarketItem())

                adapter.setNewData(list)
            }

        })
    }

    @Synchronized
    private fun initViewPager() {
        val recyclerViewList = ArrayList<RecyclerView>()
        for (i in mTitles!!.indices) {  // 这里不是四个 Fragment 了，而是四个 recyclerView

            val recyclerView = RecyclerView(activity!!)
            recyclerView.layoutManager = LinearLayoutManager(activity)
            val adapter = MarketsPagerItemAdapter(activity, R.layout.item_market, ArrayList())
            adapter.onItemClickListener = this
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