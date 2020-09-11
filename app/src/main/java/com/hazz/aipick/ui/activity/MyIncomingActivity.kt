package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.IncomeBean
import com.hazz.aipick.mvp.presenter.WalletIncomePresenter
import com.hazz.aipick.ui.adapter.WalletIncomeAdapter
import com.hazz.aipick.utils.ToolBarCustom
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import kotlinx.android.synthetic.main.activity_my_incoming.*
import kotlinx.android.synthetic.main.activity_tixian_record.recycleview
import kotlinx.android.synthetic.main.activity_tixian_record.toolbar
import kotlinx.android.synthetic.main.dialog_choose.view.tv_cancle
import kotlinx.android.synthetic.main.dialog_choose_year.view.*


class MyIncomingActivity : BaseActivity(), WaletContract.IncomeView, OnRefreshLoadmoreListener {


    override fun layoutId(): Int = R.layout.activity_my_incoming


    override fun initData() {
        mWalletIncomePresenter.getIncomeList(filterType, page, 10)
    }

    private var mOrderAdapter: WalletIncomeAdapter? = null
    private var mWalletIncomePresenter: WalletIncomePresenter = WalletIncomePresenter(this)

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.mine_shouru)).setToolBarBg(Color.parseColor("#1E2742"))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setOnLeftIconClickListener { finish() }

        recycleview.layoutManager = LinearLayoutManager(this)
        mOrderAdapter = WalletIncomeAdapter(null)
        recycleview.adapter = mOrderAdapter
        mOrderAdapter!!.bindToRecyclerView(recycleview)
        mOrderAdapter!!.setEmptyView(R.layout.empty_view)
        refreshLayout.isEnableLoadmore = false
        refreshLayout.setOnRefreshLoadmoreListener(this)
    }

    private var page = 1

    override fun start() {
        tv_year.setOnClickListener {
            showBottomYear()
        }
    }

    private fun showBottomYear() {
        var bottomSheet = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_choose_year, null)
        bottomSheet.setContentView(view)
        view.tv_cancle.setOnClickListener {
            bottomSheet.dismiss()
        }
        view.tv_one_month.setOnClickListener {
            filterType = "days30"
            bottomSheet.dismiss()
        }
        view.tv_three_month.setOnClickListener {
            filterType = "days90"
            bottomSheet.dismiss()
        }
        view.tv_one_year.setOnClickListener {
            filterType = "days365"
            bottomSheet.dismiss()
        }
        bottomSheet.show()
    }

    private var filterType = "days30"

    override fun setIncomeList(list: List<IncomeBean>) {
        if (page == 1) {
            mOrderAdapter?.setNewData(list)
            refreshLayout.finishRefresh()
        } else {
            mOrderAdapter?.addData(list)
            refreshLayout.finishLoadmore()
        }
        if (list.size < 10)
            refreshLayout.finishLoadmoreWithNoMoreData()
    }

    override fun onLoadmore(refreshlayout: RefreshLayout?) {
        page++
        initData()
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        page = 1
        initData()
    }
}