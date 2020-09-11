package com.hazz.aipick.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseFragment
import com.hazz.aipick.mvp.contract.HomeContract
import com.hazz.aipick.mvp.model.bean.InfoBean
import com.hazz.aipick.mvp.presenter.InfoPresenter
import com.hazz.aipick.ui.adapter.InfoAdapter
import com.hazz.aipick.utils.DpUtils
import com.hazz.aipick.view.LinearItemDecoration
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import kotlinx.android.synthetic.main.refresh_layout.*

class InfoFragment : BaseFragment(), HomeContract.InfoView, OnRefreshLoadmoreListener {
    private lateinit var mInfoAdapter: InfoAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_info
    }

    override fun initView() {
        refreshLayout.setOnRefreshLoadmoreListener(this)
        refreshLayout.isEnableLoadmore = false
        recycleview.layoutManager = LinearLayoutManager(activity)
//        recycleview.addItemDecoration(LinearItemDecoration(DpUtils.dip2px(activity, 10f), activity?.resources!!.getColor(R.color.color_translucent)))
        mInfoAdapter = InfoAdapter(R.layout.item_info, null)

        mInfoAdapter.bindToRecyclerView(recycleview)
        mInfoAdapter.setEmptyView(R.layout.empty_view)
    }

    override fun lazyLoad() {
        getData()
    }

    var page = 1
    var refreshType = 0;
    override fun onLoadmore(refreshlayout: RefreshLayout?) {
        refreshType = 1
        page++
        getData()
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        refreshType = 0
        page = 1
        getData()
    }

    fun getData() {
        //数据满的时候可以加载更多
//        mPresenter.getInfoData(page, 10)
    }

    var mPresenter = InfoPresenter(this)

    override fun setInfoList(data: List<InfoBean>) {
        refreshLayout.isEnableLoadmore = data.size == 10
        when (refreshType) {
            1 -> {
                refreshLayout.finishRefresh()
                mInfoAdapter.addData(data)
            }
            else -> {
                mInfoAdapter.setNewData(data)
                refreshLayout.finishRefresh()
            }
        }
    }
}