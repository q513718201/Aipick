package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.CompoundButton
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.CollectionContract
import com.hazz.aipick.mvp.model.bean.MySubscribe
import com.hazz.aipick.mvp.model.bean.SubscribeDesc
import com.hazz.aipick.mvp.presenter.SubscribePresenter
import com.hazz.aipick.ui.adapter.FollowBenefitAdapter
import com.hazz.aipick.utils.ToastUtils
import com.hazz.aipick.utils.ToolBarCustom
import com.hazz.aipick.widget.TipsDialog
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import kotlinx.android.synthetic.main.activity_subscribe_desc.*
import kotlinx.android.synthetic.main.refresh_layout.*


class SubscribeDescActivity : BaseActivity(), CollectionContract.subscribeView, View.OnClickListener, OnRefreshLoadmoreListener, CompoundButton.OnCheckedChangeListener {

    override fun mySubscribe(msg: List<MySubscribe>) {

    }

    override fun mySubscribeDesc(msg: SubscribeDesc) {

        tv_name.text = msg.subee_nickname
        when (msg.switchX) {
            "on" -> switchButton.isChecked = true
            "off" -> switchButton.isChecked = false
        }
        tv_time.text = getString(R.string.youxiaoshijian, msg.end)
        tv_pay_type.text = when (msg.pay_method) {
            "wechat" -> getString(R.string.pay_type, getString(R.string.wechat_pay))
            "alipay" -> getString(R.string.pay_type, getString(R.string.alipay_pay))
            else -> getString(R.string.pay_type, "未支付 -")
        }
        tv_pay_price.text = getString(R.string.money_format, msg.price)
        mOrderAdapter?.setNewData(msg.gains)
        isfirst = true

        when (page) {
            1 -> {
                refreshLayout.finishRefresh()
                mOrderAdapter?.setNewData(msg.gains)
            }
            else -> {
                refreshLayout.finishLoadmore()
                mOrderAdapter?.addData(msg.gains)
            }
        }
    }


    override fun switchSucceed(msg: String) {
        ToastUtils.showToast(this, msg)
    }


    override fun layoutId(): Int = R.layout.activity_subscribe_desc

    override fun initData() {
        mSubscribePresenter.mySubscribeDesc(subId, type, page, 10)
    }

    private var mOrderAdapter: FollowBenefitAdapter? = null
    private var mSubscribePresenter: SubscribePresenter = SubscribePresenter(this)
    private var page = 1
    private var subId = ""
    private var type = 1
    private var isfirst = false

    @SuppressLint("SetTextI18n")
    override fun initView() {
        subId = intent.getStringExtra("subId")
        type = intent.getIntExtra("type", 1)

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
        refreshLayout.isEnableLoadmore = false
        refreshLayout.setOnRefreshLoadmoreListener(this)
        //交易员没有订阅不显示开关
        switchButton.visibility = if (type == 2) View.INVISIBLE else View.VISIBLE
        switchButton.setOnClickListener(this)
    }

    override fun start() {

    }


    override fun onClick(v: View?) {
        val tipsDialog = TipsDialog(this)
        var isChecked = switchButton.isChecked
        when (!isChecked) {
            true -> {
                tipsDialog.setContent(resources.getString(R.string.tips_subscribe_close))
                        .setConfirmListener {
                            mSubscribePresenter.mySubscribeSwitch(subId, "off")
                        }
                        .setCancleListener {
                            switchButton?.isChecked = !isChecked
                            tipsDialog.dismiss()
                        }
                        .show()
            }
            else -> {
                tipsDialog.setContent(resources.getString(R.string.tips_subscribe))

                        .setConfirmListener {
                            mSubscribePresenter.mySubscribeSwitch(subId, "on")
                        }
                        .setCancleListener {
                            switchButton?.isChecked = !isChecked
                            tipsDialog.dismiss()
                        }
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

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

    }


}
