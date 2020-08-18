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
import com.hazz.aipick.ui.adapter.OrderAdapter
import com.hazz.aipick.utils.ToolBarCustom
import com.hazz.aipick.widget.TipsDialog
import kotlinx.android.synthetic.main.activity_subscribe_desc.*
import java.util.*


class SubscribeDescActivity : BaseActivity(), CompoundButton.OnCheckedChangeListener, CollectionContract.subscribeView, View.OnClickListener {

    override fun mySubscribe(msg: List<MySubscribe>) {

    }

    override fun mySubscribeDesc(msg: SubscribeDesc) {
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

        isfirst = true
    }

    override fun switchSucceed(msg: String) {
    }


    override fun layoutId(): Int = R.layout.activity_subscribe_desc

    override fun initData() {
        subId = intent.getStringExtra("subId")
        type = intent.getIntExtra("type", 0)
        mSubscribePresenter.mySubscribeDesc(subId, type, page, 10)

    }

    private var mOrderAdapter: OrderAdapter? = null
    private val titleList = ArrayList<String>()
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



        titleList.add("正在持仓")
        titleList.add("历史持仓")

        recycleview.layoutManager = LinearLayoutManager(this)
        mOrderAdapter = OrderAdapter(R.layout.item_order, null)
        recycleview.adapter = mOrderAdapter
        mOrderAdapter!!.bindToRecyclerView(recycleview)
        mOrderAdapter!!.setEmptyView(R.layout.empty_view)
        switchButton.setOnClickListener(this)
    }

    override fun start() {

    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

        if (isChecked && isfirst) {
            val tipsDialog = TipsDialog(this)

        } else {
            val tipsDialog = TipsDialog(this)
            tipsDialog.setContent(resources.getString(R.string.tips_subscribe_close))
                    .setConfirmListener {
                        mSubscribePresenter.mySubscribeSwitch(subId, "off")
                    }
                    .setCancleListener { tipsDialog.dismiss() }
                    .show()
        }

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


}
