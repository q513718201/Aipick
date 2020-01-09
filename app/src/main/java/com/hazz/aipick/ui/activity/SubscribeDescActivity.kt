package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
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
import java.util.ArrayList



class SubscribeDescActivity : BaseActivity(), CompoundButton.OnCheckedChangeListener, CollectionContract.subscribeView {

    override fun mySubscribe(msg: List<MySubscribe>) {

    }

    override fun mySubscribeDesc(msg: SubscribeDesc) {
        tv_name.text=msg.subee_name
        when(msg.switchX){
            "on"->sw.isChecked=true
            "off"->sw.isChecked=false
        }
        tv_time.text = getString(R.string.youxiaoshijian,msg.end)
        when(msg.pay_method){
            "wechat"->tv_pay_type.text = getString(R.string.pay_type,getString(R.string.wechat_pay))
            "alipay"->tv_pay_type.text = getString(R.string.pay_type,getString(R.string.alipay_pay))
        }
        tv_pay_price.text=msg.price
    }

    override fun switchSucceed(msg: String) {
    }


    override fun layoutId(): Int = R.layout.activity_subscribe_desc

    override fun initData() {
        subId = intent.getStringExtra("subId")
        mSubscribePresenter.mySubscribeDesc(subId,page,10)

    }

    private var mOrderAdapter: OrderAdapter? = null
    private val titleList = ArrayList<String>()
    private  var  mSubscribePresenter: SubscribePresenter = SubscribePresenter(this)
    private  var page=1
    private  var subId=""
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
        mOrderAdapter= OrderAdapter(R.layout.item_order,titleList)
        recycleview.adapter = mOrderAdapter
        mOrderAdapter!!.bindToRecyclerView(recycleview)
        mOrderAdapter!!.setEmptyView(R.layout.empty_view)
        sw.setOnCheckedChangeListener(this)
    }

    override fun start() {

    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if(isChecked){
            val tipsDialog = TipsDialog(this)
            tipsDialog.setContent(resources.getString(R.string.tips_subscribe) )

                    .setConfirmListener {
                        mSubscribePresenter.mySubscribeSwitch(subId)
                    }
                    .show()
        }else{
            val tipsDialog = TipsDialog(this)
            tipsDialog.setContent(resources.getString(R.string.tips_subscribe_close) )
                    .setConfirmListener {
                        mSubscribePresenter.mySubscribeSwitch(subId)
                    }
                    .show()
        }

    }


}
