package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.CollectionContract
import com.hazz.aipick.mvp.model.bean.MySubscribe
import com.hazz.aipick.mvp.model.bean.SubscribeDesc
import com.hazz.aipick.mvp.presenter.SubscribePresenter
import com.hazz.aipick.ui.adapter.OrderAdapter
import com.hazz.aipick.ui.adapter.SubscribeAdapter
import com.hazz.aipick.utils.ToolBarCustom
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.activity_mine_subscriber.*
import kotlinx.android.synthetic.main.activity_mine_subscriber.recycleview
import kotlinx.android.synthetic.main.activity_mine_subscriber.tabLayout
import kotlinx.android.synthetic.main.activity_subscribe_desc.toolbar
import kotlinx.android.synthetic.main.dialog_choose.view.*
import kotlinx.android.synthetic.main.dialog_choose.view.tv_cancle
import kotlinx.android.synthetic.main.dialog_choose_year.view.*
import kotlinx.android.synthetic.main.fragment_order.*

import java.util.ArrayList


class MineSubscribeActivity : BaseActivity(), CollectionContract.subscribeView, OnRefreshListener, OnLoadmoreListener, TabLayout.OnTabSelectedListener {
    override fun mySubscribeDesc(msg: SubscribeDesc) {

    }

    override fun switchSucceed(msg: String) {
    }


    override fun onTabReselected(p0: TabLayout.Tab?) {

    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {
    }

    override fun onTabSelected(p0: TabLayout.Tab?) {
        val position = p0!!.position

        when(position){
            0->{
                tv_month.visibility=View.VISIBLE
                currentDirect="out"
                requestData()
            }
            1->{
                tv_month.visibility=View.GONE
                currentDirect="in"
                requestData()
            }
        }

    }

    override fun onLoadmore(refreshlayout: RefreshLayout?) {
        page=1
        refreshLayout.resetNoMoreData()
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        page++
    }

    override fun mySubscribe(msg: List<MySubscribe>) {
        mOrderAdapter!!.setNewData(msg)
    }


    override fun layoutId(): Int = R.layout.activity_mine_subscriber

    override fun initData() {
        requestData()
    }

    private fun requestData() {
        page=1
        mSubscribePresenter.getCollection(currentCategry, currentDirect, currentDay, page, 10)
    }

    private var mOrderAdapter: SubscribeAdapter? = null
    private val titleList = ArrayList<String>()
    private  var bottomSheet:BottomSheetDialog?=null
    private  var  mSubscribePresenter: SubscribePresenter =SubscribePresenter(this)
    private  var page=1
    private  var currentDay="days0"
    private  var currentCategry="bot"
    private  var currentDirect="out"

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.subsciber))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }


        titleList.add("我的订阅")
        titleList.add("订阅我的")
        for (i in titleList.indices) {
            tabLayout!!.addTab(tabLayout!!.newTab().setText(titleList[i]))
        }
        recycleview.layoutManager = LinearLayoutManager(this)
        mOrderAdapter= SubscribeAdapter(R.layout.item_mysubscribe,null)

        recycleview.adapter = mOrderAdapter
        mOrderAdapter!!.bindToRecyclerView(recycleview)
        mOrderAdapter!!.setEmptyView(R.layout.empty_view)
        tv_year.setOnClickListener {
            showBottomYear()
        }
        tv_month.setOnClickListener {
            showBottomDay()
        }

    }

    override fun start() {
        refreshLayout.setOnRefreshListener(this)
        refreshLayout.setOnLoadmoreListener(this)
        tabLayout.addOnTabSelectedListener(this)
    }

    private fun showBottomDay() {
        bottomSheet= BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_choose, null)
        bottomSheet!!.setContentView(view)

        view.tv_cancle.setOnClickListener {
            bottomSheet!!.dismiss()
        }

        view.tv_rebot.setOnClickListener {
            bottomSheet!!.dismiss()
            currentCategry="bot"
            tv_month.text=getString(R.string.robot_caty)
            requestData()
        }

        view.tv_trader.setOnClickListener {
            bottomSheet!!.dismiss()
            currentCategry="broker"
            tv_month.text=getString(R.string.jiaoyiyuan)
            requestData()
        }
        val viewById = bottomSheet!!.delegate.findViewById<View>(android.support.design.R.id.design_bottom_sheet)
        //设置布局背景透明
        viewById?.setBackgroundColor(resources.getColor(android.R.color.transparent))
        bottomSheet!!.show()


    }


    private fun showBottomYear() {
        var bottomSheet= BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_choose_year, null)
        bottomSheet.setContentView(view)
        view.tv_cancle.setOnClickListener {
            bottomSheet.dismiss()
        }
        view.tv_one_month.setOnClickListener {
            bottomSheet.dismiss()
            currentDay="days30"
            tv_year.text=getString(R.string.jinyiyue)
            requestData()
        }
        view.tv_three_month.setOnClickListener {
            bottomSheet.dismiss()
            currentDay="days90"
            tv_year.text=getString(R.string.jin_sanyue)
            requestData()
        }
        view.tv_one_year.setOnClickListener {
            bottomSheet.dismiss()
            currentDay="days7"
            tv_year.text=getString(R.string.jin_seven_day)
            requestData()
        }

        view.tv_all.setOnClickListener {
            bottomSheet.dismiss()
            currentDay="daya0"
            tv_year.text=getString(R.string.all)
            requestData()
        }


        val viewById = bottomSheet!!.delegate.findViewById<View>(android.support.design.R.id.design_bottom_sheet)
        //设置布局背景透明
        viewById?.setBackgroundColor(resources.getColor(android.R.color.transparent))
        bottomSheet.show()

    }



}
