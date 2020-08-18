package com.hazz.aipick.ui.fragment

import android.graphics.Point
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseFragment
import com.hazz.aipick.mvp.contract.HomeContract
import com.hazz.aipick.mvp.model.bean.Home
import com.hazz.aipick.mvp.model.bean.RateBean
import com.hazz.aipick.mvp.model.bean.ShaixuanBean
import com.hazz.aipick.mvp.presenter.HomePresenter
import com.hazz.aipick.ui.adapter.HomeAdapter
import com.hazz.aipick.ui.adapter.ShaiXuanAdapter
import com.hazz.aipick.ui.adapter.ShaiXuanAdapter2
import com.hazz.aipick.ui.adapter.ShaiXuanAdapter3
import com.hazz.aipick.utils.DpUtils
import com.hazz.aipick.utils.SPUtil
import com.hazz.aipick.widget.RecyclerViewSpacesItemDecoration
import com.scwang.smartrefresh.header.MaterialHeader
import kotlinx.android.synthetic.main.dialog_choose.view.*
import kotlinx.android.synthetic.main.dialog_shaixuan.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


@Suppress("DEPRECATION")
class HomeFragment : BaseFragment(), HomeContract.homeView {


    private var homeBean: Home? = null
    override fun getHomeMsg(msg: List<Home>) {
        if (msg.isEmpty()) return

        mHomeAdapter?.setNewData(msg)
        mHomeAdapter?.setRole(subeeType)
        if (homeBean == null) {
            homeBean = msg[0]
            setHead()
        }
        if (isRefresh)
            mRefreshLayout.finishRefresh()
    }

    private fun setHead() {
        homeBean?.let {
            tv_coin_name?.text = if (it.coin_name == null) getString(R.string.app_name) else it.coin_name

            tv_raise?.text = "${it.rate}"
            tv_pullback?.text = getString(R.string.ten_rate, it.pullback)
            price?.text = getString(R.string.home_price, it.price)
        }
    }

    override fun setRate(bean: RateBean) {
        bean.us_rmb?.let { SPUtil.setRate(it) }
    }


//    private val mPresenter by lazy { HomePresenter() }

    private var mTitle: String? = null

    private var num: Int = 1

    private var mHomeAdapter: HomeAdapter? = null
    private var mShaiXuanAdapter: ShaiXuanAdapter? = null
    private var mShaiXuanAdapter2: ShaiXuanAdapter2? = null
    private var mShaiXuanAdapter3: ShaiXuanAdapter3? = null
    private var loadingMore = false

    private var isRefresh = false
    private var mMaterialHeader: MaterialHeader? = null
    private var bottomSheet: BottomSheetDialog? = null
    var list: MutableList<String>? = mutableListOf()
    var list1: MutableList<ShaixuanBean>? = mutableListOf()
    var list2: MutableList<ShaixuanBean>? = mutableListOf()
    private var mHomePresenter: HomePresenter = HomePresenter(this)

    private var subeeType = "bot"
    private var page = 1

    private var rateStart = "0"
    private var rateEnd = "100"

    private var timesStart = "0"
    private var timesEnd = "100"

    private var pullbackStart = "0"
    private var pullbackEnd = "100"

    companion object {
        fun getInstance(title: String): HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    private val linearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }


    private val simpleDateFormat by lazy {
        SimpleDateFormat("- MMM. dd, 'Brunch' -", Locale.ENGLISH)
    }


    override fun getLayoutId(): Int = R.layout.fragment_home


    /**
     * 初始化 ViewI
     */
    override fun initView() {

        //内容跟随偏移
        mRefreshLayout.setEnableHeaderTranslationContent(true)
        mRefreshLayout.setOnRefreshListener {
            isRefresh = true
            getData()
        }
        mMaterialHeader = mRefreshLayout.refreshHeader as MaterialHeader?
        //打开下拉刷新区域块背景:
        mMaterialHeader?.setShowBezierWave(true)
        //设置下拉刷新主题颜色
        mRefreshLayout.setPrimaryColorsId(R.color.color_light_black, R.color.color_title_bg)

        mRefreshLayout.autoLoadmore()
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val childCount = mRecyclerView.childCount
                    val itemCount = mRecyclerView.layoutManager!!.itemCount
                    val firstVisibleItem = (mRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (firstVisibleItem + childCount == itemCount) {
                        if (!loadingMore) {
                            loadingMore = true
//                             mPresenter.loadMoreData()
                        }
                    }
                }

            }
        })
//
//        iv_search.setOnClickListener { openSearchActivity() }


        val pointList = ArrayList<Point>()
        pointList.add(Point(-1, 50))
        pointList.add(Point(210, 200))
        pointList.add(Point(410, 30))
        pointList.add(Point(510, 250))
        pointList.add(Point(610, 80))
        pointList.add(Point(810, 250))
        pointList.add(Point(DpUtils.getDeviceWidthAndHeight(activity)[0] - 150, 60))

        mBezierView.setPointList(pointList)
        mBezierView.visibility = View.VISIBLE

        rl_choose.setOnClickListener {
            bottomSheet = BottomSheetDialog(activity!!)
            val view = layoutInflater.inflate(R.layout.dialog_choose, null)
            bottomSheet!!.setContentView(view)

            view.tv_cancle.setOnClickListener {
                bottomSheet!!.dismiss()
            }

            view.tv_rebot.setOnClickListener {
                bottomSheet!!.dismiss()
                subeeType = "bot"
                role.text = getString(R.string.robot_caty)
                homeBean = null
                getData()
            }

            view.tv_trader.setOnClickListener {
                bottomSheet!!.dismiss()
                subeeType = "broker"
                role.text = getString(R.string.robot_trader)
                homeBean = null
                getData()
            }
            val viewById = bottomSheet!!.delegate.findViewById<View>(android.support.design.R.id.design_bottom_sheet)
            //设置布局背景透明
            viewById?.setBackgroundColor(resources.getColor(android.R.color.transparent))
            bottomSheet!!.show()


        }

        iv_shaixuan.setOnClickListener {
            if (bottomSheet == null) {
                bottomSheet = BottomSheetDialog(activity!!)
            }
            val view = layoutInflater.inflate(R.layout.dialog_shaixuan, null)

            view.iv_close.setOnClickListener { bottomSheet?.dismiss() }//添加关闭按钮逻辑

            view.recycleview1.layoutManager = GridLayoutManager(activity, 4)
            mShaiXuanAdapter = ShaiXuanAdapter(list1!!)
            view.recycleview1.adapter = mShaiXuanAdapter

            view.recycleview2.layoutManager = GridLayoutManager(activity, 4)
            mShaiXuanAdapter2 = ShaiXuanAdapter2(list2!!)
            view.recycleview2.adapter = mShaiXuanAdapter2

            view.recycleview3.layoutManager = GridLayoutManager(activity, 4)
            mShaiXuanAdapter3 = ShaiXuanAdapter3(list1!!)
            view.recycleview3.adapter = mShaiXuanAdapter3
            val stringIntegerHashMap: HashMap<String, Int>? = HashMap()
            stringIntegerHashMap?.put(RecyclerViewSpacesItemDecoration.RIGHT_DECORATION, 20)//右间距
            stringIntegerHashMap?.put(RecyclerViewSpacesItemDecoration.BOTTOM_DECORATION, 20)//下间距
            view.recycleview1.addItemDecoration(RecyclerViewSpacesItemDecoration(stringIntegerHashMap))
            view.recycleview2.addItemDecoration(RecyclerViewSpacesItemDecoration(stringIntegerHashMap))
            view.recycleview3.addItemDecoration(RecyclerViewSpacesItemDecoration(stringIntegerHashMap))
            view.tv_sure.setOnClickListener {

                rateStart = mShaiXuanAdapter!!.getCurrentStart()
                rateEnd = mShaiXuanAdapter!!.getCurrentEnd()
                timesStart = mShaiXuanAdapter2!!.getCurrentStart()
                timesEnd = mShaiXuanAdapter2!!.getCurrentEnd()
                pullbackStart = mShaiXuanAdapter3!!.getCurrentStart()
                pullbackEnd = mShaiXuanAdapter3!!.getCurrentEnd()
                getData()
                bottomSheet!!.dismiss()
            }
            view.tv_reset.setOnClickListener {
                mShaiXuanAdapter!!.reset()
                mShaiXuanAdapter2!!.reset()
                mShaiXuanAdapter3!!.reset()
            }

            bottomSheet!!.setContentView(view)

            bottomSheet!!.show()


        }
    }

    override fun lazyLoad() {
//        mPresenter.requestHomeData(num)
        list1?.add(ShaixuanBean("不限", 1))
        list1?.add(ShaixuanBean("<10%", 1))
        list1?.add(ShaixuanBean("10%-20%", 1))
        list1?.add(ShaixuanBean("20%-30%", 1))
        list1?.add(ShaixuanBean(">30%", 1))
        list1?.add(ShaixuanBean("", 2))
        list1?.add(ShaixuanBean("", 2))

        list2?.add(ShaixuanBean("不限", 1))
        list2?.add(ShaixuanBean(">50%", 1))
        list2?.add(ShaixuanBean("", 3))
        list2?.add(ShaixuanBean("", 3))


        mHomeAdapter = HomeAdapter(R.layout.item_home, null)
        mRecyclerView.adapter = mHomeAdapter
        mRecyclerView.layoutManager = linearLayoutManager
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        val stringIntegerHashMap: HashMap<String, Int>? = HashMap()
        stringIntegerHashMap?.put(RecyclerViewSpacesItemDecoration.BOTTOM_DECORATION, 15)//右间距
        mRecyclerView.addItemDecoration(RecyclerViewSpacesItemDecoration(stringIntegerHashMap))

        getData()
    }

    private fun getData() {
        mHomePresenter.getRate()//获取汇率
        mHomePresenter.getHomeMsg(subeeType, page, 10, rateStart, rateEnd, timesStart, timesEnd, pullbackStart, pullbackEnd)
    }


}
