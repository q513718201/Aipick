package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.model.bean.Message
import com.hazz.aipick.ui.adapter.ExchangeOrderAdapter
import com.hazz.aipick.utils.ToolBarCustom
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.activity_exchange_order.*
import java.util.*


class ExchangeOrderActivity : BaseActivity(), OnRefreshListener, OnLoadmoreListener {


    override fun onLoadmore(refreshlayout: RefreshLayout?) {
        page++
       // mWaletPresenter.messageList(page,10)
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        page=1
        refreshLayout.resetNoMoreData()
      //  mWaletPresenter.messageList(page,10)
    }


//        refreshLayout.finishRefresh()
//        refreshLayout.finishLoadmore()
//        if(page==1){
//            currentList?.clear()
//            currentList?.addAll(msg)
//            mOrderAdapter!!.setNewData(currentList)
//        }else{
//            currentList!!.addAll(msg)
//            mOrderAdapter!!.notifyDataSetChanged()
//        }
//
//        if(msg.size<10){
//            refreshLayout.finishLoadmoreWithNoMoreData()
//        }



    override fun layoutId(): Int = R.layout.activity_exchange_order


    override fun initData() {

    }

    private var mOrderAdapter: ExchangeOrderAdapter? = null
    private val titleList = ArrayList<String>()
 //  private var mWaletPresenter: MessagePresenter = MessagePresenter(this)
    private var page=1
    private var currentList:MutableList<Message>?= mutableListOf()

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.exchange_order))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }


        titleList.add("正在持仓")
        titleList.add("历史持仓")

        recycleview.layoutManager = LinearLayoutManager(this)
        mOrderAdapter= ExchangeOrderAdapter(R.layout.item_exchange,titleList)
        recycleview.adapter = mOrderAdapter
        mOrderAdapter!!.bindToRecyclerView(recycleview)
        mOrderAdapter!!.setEmptyView(R.layout.empty_view)

    }

    override fun start() {
        refreshLayout.setOnRefreshListener(this)
        refreshLayout.setOnLoadmoreListener(this)
    }



}
