package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.blankj.utilcode.util.GsonUtils
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.CollectionContract
import com.hazz.aipick.mvp.model.bean.MySubscribe
import com.hazz.aipick.mvp.model.bean.SubscribeDesc
import com.hazz.aipick.mvp.presenter.SubscribePresenter
import com.hazz.aipick.ui.adapter.FollowBenefitAdapter
import com.hazz.aipick.utils.AssetsUtil
import com.hazz.aipick.utils.ToastUtils
import com.hazz.aipick.utils.ToolBarCustom
import com.hazz.aipick.widget.TipsDialog
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import kotlinx.android.synthetic.main.activity_subscribe_desc.*
import kotlinx.android.synthetic.main.refresh_layout.*


class SubscribeDescActivity : BaseActivity(), CollectionContract.subscribeView, View.OnClickListener, OnRefreshLoadmoreListener {

    override fun mySubscribe(msg: List<MySubscribe>) {

    }

    override fun mySubscribeDesc(msg: SubscribeDesc) {
// TODO: 2020/8/28 test data
        val assertsFileString = AssetsUtil.getAssertsFileString(this, "subscribe_detail.json")
        val msg: SubscribeDesc = GsonUtils.fromJson<SubscribeDesc>(assertsFileString, SubscribeDesc::class.java)

        tv_name.text = msg.subee_name
        when (msg.switchX) {
            "on" -> switchButton.isChecked = true
            "off" -> switchButton.isChecked = false
        }
        tv_time.text = getString(R.string.youxiaoshijian, msg.end)
        when (msg.pay_method) {
            "wechat" -> tv_pay_type.text = getString(R.string.pay_type, getString(R.string.wechat_pay))
            "alipay" -> tv_pay_type.text = getString(R.string.pay_type, getString(R.string.alipay_pay))
        }
        tv_pay_price.text = msg.price
        mOrderAdapter?.setNewData(msg.gains)
        isfirst = true
        refreshLayout.isEnableLoadmore = msg.gains.size == 10
        when (loadType) {
            0 -> {
                refreshLayout.finishRefresh()
                mOrderAdapter?.setNewData(msg.gains)
            }
            else -> {
                refreshLayout.finishLoadmore()
                mOrderAdapter?.addData(msg.gains)
            }
        }
    }

    private var loadType = 0

    override fun switchSucceed(msg: String) {
        ToastUtils.showToast(this, msg)
    }


    override fun layoutId(): Int = R.layout.activity_subscribe_desc

    override fun initData() {
        subId = intent.getStringExtra("subId")
        type = intent.getIntExtra("type", 0)
        mSubscribePresenter.mySubscribeDesc(subId, type, page, 10)

    }

    private var mOrderAdapter: FollowBenefitAdapter? = null
    private var mSubscribePresenter: SubscribePresenter = SubscribePresenter(this)
    private var page = 1
    private var subId = ""
    private var type = 0
    private var isfirst = false

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.subsciber_desc))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }

        recycleview.layoutManager = LinearLayoutManager(this)
        mOrderAdapter = FollowBenefitAdapter(null)

        mOrderAdapter!!.bindToRecyclerView(recycleview)
        mOrderAdapter!!.setEmptyView(R.layout.empty_view)
        switchButton.setOnClickListener(this)
        refreshLayout.isEnableLoadmore = false
        refreshLayout.setOnRefreshLoadmoreListener(this)
    }

    override fun start() {

    }


    override fun onClick(v: View?) {
        val tipsDialog = TipsDialog(this)
        when (switchButton.isChecked) {
            true -> {
                tipsDialog.setContent(resources.getString(R.string.tips_subscribe_close))
                        .setConfirmListener {
                            mSubscribePresenter.mySubscribeSwitch(subId, "off")
                        }
                        .setCancleListener { tipsDialog.dismiss() }
                        .show()
            }
            else -> {
                tipsDialog.setContent(resources.getString(R.string.tips_subscribe))

                        .setConfirmListener {
                            mSubscribePresenter.mySubscribeSwitch(subId, "on")
                        }
                        .setCancleListener { tipsDialog.dismiss() }
                        .show()
            }
        }
    }

    override fun onLoadmore(refreshlayout: RefreshLayout?) {
        page++
        mSubscribePresenter.mySubscribeDesc(subId, type, page, 10)
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        page = 1
        mSubscribePresenter.mySubscribeDesc(subId, type, page, 10)
    }


}
