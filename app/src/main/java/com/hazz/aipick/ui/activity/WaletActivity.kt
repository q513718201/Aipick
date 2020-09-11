package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.Toolbar
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.TixianRecord
import com.hazz.aipick.mvp.model.bean.Walet
import com.hazz.aipick.mvp.presenter.WaletPresenter
import com.hazz.aipick.ui.adapter.OrderAdapter
import com.hazz.aipick.utils.ToolBarCustom
import kotlinx.android.synthetic.main.activity_subscribe_desc.toolbar
import kotlinx.android.synthetic.main.activity_walet.*
import java.util.*


class WaletActivity : BaseActivity(), WaletContract.waletView {
    override fun tixianRecord(msg: List<TixianRecord>) {

    }

    override fun tixianSucceed(msg: String) {

    }

    override fun getWalet(msg: Walet) {

        tv_amount.text = getString(R.string.money_format, msg.withdrawable)
        tv_leiji.text = getString(R.string.money_format, msg.total_amount)
        tv_shenqing.text = getString(R.string.money_format, msg.withdrawing)
        tv_yitiqu.text = getString(R.string.money_format, msg.withdrawed)
    }


    override fun layoutId(): Int = R.layout.activity_walet


    override fun initData() {
        mWaletPresenter.waletDesc()
    }

    private var mWaletPresenter: WaletPresenter = WaletPresenter(this)

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.my_walet_title))
                .setRightText(getString(R.string.tixian_record))
                .setRightTextIsShow(true)
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }
                .setOnRightClickListener {
                    startActivity(Intent(this,TixianRecordActivity::class.java))
                }
    }

    override fun start() {
        mine_incoming.setOnClickListener {
            startActivity(Intent(this,MyIncomingActivity::class.java))
        }

        tv_go_tiqu.setOnClickListener {
            startActivity(Intent(this,TixianActivity::class.java))
        }
    }
}
