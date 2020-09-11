package com.hazz.aipick.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bigkoo.alertview.AlertView
import com.bigkoo.alertview.OnItemClickListener
import com.blankj.utilcode.util.LogUtils
import com.github.mikephil.charting.data.Entry
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseFragment
import com.hazz.aipick.managers.ChartManager
import com.hazz.aipick.managers.GuideManager
import com.hazz.aipick.mvp.contract.HomeContract
import com.hazz.aipick.mvp.model.bean.Home
import com.hazz.aipick.mvp.model.bean.RateBean
import com.hazz.aipick.mvp.model.bean.ShaixuanBean
import com.hazz.aipick.mvp.presenter.HomePresenter
import com.hazz.aipick.ui.activity.FollowRobotDetailActivity
import com.hazz.aipick.ui.adapter.HomeAdapter
import com.hazz.aipick.ui.adapter.ShaiXuanAdapter
import com.hazz.aipick.ui.adapter.ShaiXuanAdapter2
import com.hazz.aipick.ui.adapter.ShaiXuanAdapter3
import com.hazz.aipick.ui.pop.HomePop
import com.hazz.aipick.utils.DpUtils
import com.hazz.aipick.utils.SPUtil
import com.hazz.aipick.widget.BezierView
import com.hazz.aipick.widget.RecyclerViewSpacesItemDecoration
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import easily.tech.guideview.lib.GuideViewBundle
import kotlinx.android.synthetic.main.dialog_shaixuan.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.collections.HashMap


@Suppress("DEPRECATION")
class HomeFragment : BaseFragment(), HomeContract.homeView, HomePop.OnClickListener, OnRefreshLoadmoreListener {


    private var homeBean: Home? = null
    override fun getHomeMsg(msg: List<Home>) {
        mHomeAdapter?.setRole(subeeType)
        if (msg.isNotEmpty()) {
            homeBean = msg[0]
            setHead()
        }
        mRefreshLayout?.isEnableLoadmore = msg.size == 10
        when (page) {
            1 -> {
                mRefreshLayout?.finishRefresh()
                mHomeAdapter?.setNewData(msg)
            }
            else -> {
                mHomeAdapter?.addData(msg)
                mRefreshLayout?.finishLoadmore()
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun setHead() {
        homeBean?.let { home ->
            tv_coin_name?.text = if (home.coin_name.isEmpty()) getString(R.string.app_name) else home.coin_name

            tv_raise?.text = if (home.all_rate.toFloat() >= 0) "+${home.all_rate}%" else "${home.all_rate}%"
            tv_total_rate?.setText(home.all_rate, "%")


            tv_pullback?.text = "${getString(R.string.ten_rate, home.rate)}%"
            price?.text = getString(R.string.home_price, home.price)
//            var pos = "90.00000000,66.00000000,-30.00000000,1100.00000000,-66.00000000,1110.00000000"
//
//            val split = /*home.gain_list*/pos.split(",")
            val split = home.gain_list.split(",")
            chart?.let {
                val entries = ArrayList<Entry>()
                var min = 0f
                for ((index, value) in split.withIndex()) {
                    min = min.coerceAtMost(value.toFloat())
                    entries.add(Entry(index.toFloat(), value.toFloat()))
                }

                if (min < 0) {
                    for (entry in entries) {
                        entry.y = entry.y + (min * -1)
                    }
                }
                context?.resources?.let {
                    var lineColor = it.getColor(R.color.home_line)
                    var fillDrawable = it.getDrawable(R.color.bg_common)
                    ChartManager.showLineChart(chart, entries, lineColor, 5f, fillDrawable)
                }
            }

            bezierView?.let {
                val dp_15 = DpUtils.dip2px(context, 15f)

                var entries = ArrayList<BezierView.Point>()
                var min = -0.01f
                var max = 0.01f
                for (value in split) {
                    min = min.coerceAtMost(value.toFloat())
                    max = max.coerceAtLeast(value.toFloat())
                }
                var width = bezierView.measuredWidth
                var height = bezierView.measuredHeight


                for ((index, value) in split.withIndex()) {
                    var x = width / (split.size - 1) * index.toFloat()
                    var y = if (min < 0) value.toFloat() + (min * -1f) else value.toFloat()
                    y = height - y * (bezierView.measuredHeight - dp_15 ) / (max - min)
                    entries.add(BezierView.Point(x, y))
                }

                LogUtils.e(entries.toString(), it.measuredHeight)
                it.setPointList(entries)
            }

            chart?.visibility = View.GONE
            bezierView?.visibility = View.VISIBLE

        }
    }


    override fun setRate(bean: RateBean) {
        bean.us_rmb?.let { SPUtil.setRate(it) }
    }

    private var mTitle: String? = null


    private var mHomeAdapter: HomeAdapter? = null
    private var mShaiXuanAdapter: ShaiXuanAdapter? = null
    private var mShaiXuanAdapter2: ShaiXuanAdapter2? = null
    private var mShaiXuanAdapter3: ShaiXuanAdapter3? = null
    private var mMaterialHeader: MaterialHeader? = null
    private var bottomSheet: BottomSheetDialog? = null
    var list: MutableList<String>? = mutableListOf()
    var list1: MutableList<ShaixuanBean>? = mutableListOf()
    var list2: MutableList<ShaixuanBean>? = mutableListOf()
    private var mHomePresenter: HomePresenter = HomePresenter(this)

    private var subeeType = "bot"
    private var page = 1

    private var rateStart = "-1000000"
    private var rateEnd = "1000000"

    private var timesStart = "-1000000"
    private var timesEnd = "1000000"

    private var pullbackStart = "-1000000"
    private var pullbackEnd = "1000000"

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

    override fun getLayoutId(): Int = R.layout.fragment_home


    /**
     * 初始化 ViewI
     */
    override fun initView() {
        mHomePresenter.getRate()//获取汇率
        //内容跟随偏移
        mRefreshLayout.setEnableHeaderTranslationContent(true)
        mRefreshLayout.setOnRefreshLoadmoreListener(this)

        mMaterialHeader = mRefreshLayout.refreshHeader as MaterialHeader?
        //打开下拉刷新区域块背景:
        mMaterialHeader?.setShowBezierWave(true)
        //设置下拉刷新主题颜色
        mRefreshLayout.setPrimaryColorsId(R.color.color_light_black, R.color.color_title_bg)


        rl_choose.setOnClickListener {
            val arrayOf = arrayOf(getString(R.string.robot_caty), getString(R.string.robot_trader))
            AlertView.Builder().setContext(context)
                    .setStyle(AlertView.Style.ActionSheet)
                    .setCancelText(getString(R.string.cancel))
                    .setDestructive(*arrayOf)
                    .setOnItemClickListener(OnItemClickListener { any: Any, pos: Int ->
                        when (pos) {
                            0 -> {
                                subeeType = "bot"
                                role.text = getString(R.string.robot_caty)
                                homeBean = null
                                getData()
                            }
                            else -> {
                                subeeType = "broker"
                                role.text = getString(R.string.robot_trader)
                                homeBean = null
                                getData()
                            }
                        }
                    }).build()
                    .show()

            tv_coin_name?.text = getString(R.string.app_name)
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

    override fun onResume() {
        super.onResume()
//        var isfirstMine = SPUtil.getBoolean("isfirstHome")
//        activity?.let {
//            if (isfirstMine) {
//                var pop = HomePop(it)
//                pop.setOutSideDismiss(false)
//                pop.setPopupWindowFullScreen(true)
//                pop.setBlurBackgroundEnable(true)
//                pop.mOnClickListener = this
//                pop.showPopupWindow()
//            }
//        }
        GuideManager.showGuide(childFragmentManager, guide_point, View.inflate(context, R.layout.guide_home, null), GuideViewBundle.Direction.BOTTOM, "ic_guide_home")
    }

    //弹窗点击之后的事件
    override fun onClick(v: View) {
        FollowRobotDetailActivity.start(context!!, "-1", "bot", "")
//        ToastUtils.showToast(activity, "ni xiang gansha!")
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


        mHomeAdapter = HomeAdapter(null)
        mRecyclerView.adapter = mHomeAdapter
        mRecyclerView.layoutManager = linearLayoutManager
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        val stringIntegerHashMap: HashMap<String, Int>? = HashMap()
        stringIntegerHashMap?.put(RecyclerViewSpacesItemDecoration.BOTTOM_DECORATION, 15)//右间距
        mRecyclerView.addItemDecoration(RecyclerViewSpacesItemDecoration(stringIntegerHashMap))

        getData()
    }

    private fun getData() {
        mHomePresenter.getHomeMsg(subeeType, page, 10, rateStart, rateEnd, timesStart, timesEnd, pullbackStart, pullbackEnd)
    }

    override fun onLoadmore(refreshlayout: RefreshLayout?) {
        page++
        getData()
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        page = 1
        getData()
    }


}
