package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.Message
import com.hazz.aipick.mvp.presenter.MessagePresenter
import com.hazz.aipick.ui.adapter.MessageAdapter
import com.hazz.aipick.utils.ToastUtils
import com.hazz.aipick.utils.ToolBarCustom
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.activity_tixian_record.*
import java.util.*


class MessageActivity : BaseActivity(), WaletContract.messageView, OnRefreshListener, OnLoadmoreListener {
    override fun readAll(msg: String) {
        ToastUtils.showToast(this,msg)
    }

    override fun onLoadmore(refreshlayout: RefreshLayout?) {
        page++
        mWaletPresenter.messageList(page,10)
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        page=1
        refreshLayout.resetNoMoreData()
        mWaletPresenter.messageList(page,10)
    }

    override fun messageList(msg: List<Message>) {
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


    override fun layoutId(): Int = R.layout.activity_tixian_record


    override fun initData() {
        mWaletPresenter.messageList(page,10)
    }

    private var mOrderAdapter: MessageAdapter? = null
    private val titleList = ArrayList<String>()
    private var mWaletPresenter: MessagePresenter = MessagePresenter(this)
    private var page=1
    private var currentList:MutableList<Message>?= mutableListOf()

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.mine_message))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }
                .setRightText(getString(R.string.mine_all_read))
                .setRightTextIsShow(true)
                .setOnRightClickListener {
                    mWaletPresenter.readAll(0,"1")

                }


        titleList.add("正在持仓")
        titleList.add("历史持仓")

        recycleview.layoutManager = LinearLayoutManager(this)
        mOrderAdapter= MessageAdapter(R.layout.item_msg,null)
        recycleview.adapter = mOrderAdapter
        mOrderAdapter!!.bindToRecyclerView(recycleview)
        mOrderAdapter!!.setEmptyView(R.layout.empty_view)

    }

    override fun start() {
        refreshLayout.setOnRefreshListener(this)
        refreshLayout.setOnLoadmoreListener(this)
    }



}
