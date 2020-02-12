package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.CountDownTimer
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.widget.TextView
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.model.bean.TraderSet
import com.hazz.aipick.mvp.presenter.RegistPresenter
import com.hazz.aipick.mvp.presenter.TraderAuthPresenter
import com.hazz.aipick.ui.adapter.TixianAdapter
import com.hazz.aipick.utils.ToastUtils
import com.hazz.aipick.utils.ToolBarCustom
import com.hazz.aipick.widget.paydialog.PayPassDialog
import com.hazz.aipick.widget.paydialog.PayPassView
import kotlinx.android.synthetic.main.activity_tixian.*
import kotlinx.android.synthetic.main.activity_tixian.tv_confirm
import kotlinx.android.synthetic.main.activity_tixian_record.*
import kotlinx.android.synthetic.main.activity_tixian_record.toolbar
import kotlinx.android.synthetic.main.trader_set.*
import java.util.*


class TraderSetActivity : BaseActivity(), LoginContract.tradeView {

    override fun traderAuthSuccess(msg: String) {


    }

    override fun getCoin(msg: List<String>) {

    }

    override fun traderSetSucceed(msg: String) {

    }

    override fun traderSetQuery(msg: TraderSet) {
        et1.setText(msg.days30)
        et2.setText(msg.days90)
        et3.setText(msg.days180)
        et4.setText(msg.strategy)
        ToastUtils.showToast(this,"设置成功")
    }


    override fun layoutId(): Int = R.layout.trader_set


    override fun initData() {

    }

    private var dialog = PayPassDialog()
    private var tips: TextView? = null
    private var mTraderAuthPresenter: TraderAuthPresenter = TraderAuthPresenter(this)

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.trader_set))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }


    }

    override fun start() {

        tv_confirm.setOnClickListener {
            mTraderAuthPresenter.traderSet( et1.text.toString(),et2.text.toString(),et3.text.toString(),et4.text.toString())
        }

    }



}
