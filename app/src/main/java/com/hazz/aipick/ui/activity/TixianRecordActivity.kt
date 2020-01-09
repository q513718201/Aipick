package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.TixianRecord
import com.hazz.aipick.mvp.model.bean.Walet
import com.hazz.aipick.mvp.presenter.WaletPresenter
import com.hazz.aipick.ui.adapter.TixianAdapter
import com.hazz.aipick.utils.ToolBarCustom
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.activity_tixian_record.*
import java.util.*


class TixianRecordActivity : BaseActivity(), WaletContract.waletView, OnRefreshListener, OnLoadmoreListener {

    override fun onLoadmore(refreshlayout: RefreshLayout?) {
        page++
        mWaletPresenter.tixianRecord(page,10)
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        page=1
        refreshLayout.resetNoMoreData()
        mWaletPresenter.tixianRecord(page,10)
    }

    override fun getWalet(msg: Walet) {

    }

    override fun tixianSucceed(msg: String) {
    }

    override fun tixianRecord(msg: List<TixianRecord>) {//提现记录
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadmore()
         if(page==1){
             currentList?.clear()
             currentList?.addAll(msg)
             mOrderAdapter!!.setNewData(currentList)
         }else{
             currentList!!.addAll(msg)
             mOrderAdapter!!.notifyDataSetChanged()
         }

        if(msg.size<10){
            refreshLayout.finishLoadmoreWithNoMoreData()
        }
    }


    override fun layoutId(): Int = R.layout.activity_message


    override fun initData() {
        mWaletPresenter.tixianRecord(page,10)
    }

    private var mOrderAdapter: TixianAdapter? = null
    private val titleList = ArrayList<String>()
    private var mWaletPresenter: WaletPresenter = WaletPresenter(this)
    private var page=1
    private var currentList:MutableList<TixianRecord>?= mutableListOf()

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.tixian_record))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }


        recycleview.layoutManager = LinearLayoutManager(this)
        mOrderAdapter= TixianAdapter(R.layout.item_msg,null)
        recycleview.adapter = mOrderAdapter
        mOrderAdapter!!.bindToRecyclerView(recycleview)
        mOrderAdapter!!.setEmptyView(R.layout.empty_view)

    }

    override fun start() {
        refreshLayout.setOnRefreshListener(this)
        refreshLayout.setOnLoadmoreListener(this)
    }



}
