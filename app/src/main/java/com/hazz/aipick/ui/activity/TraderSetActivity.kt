package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.CountDownTimer
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.widget.TextView
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.presenter.RegistPresenter
import com.hazz.aipick.ui.adapter.TixianAdapter
import com.hazz.aipick.utils.ToolBarCustom
import com.hazz.aipick.widget.paydialog.PayPassDialog
import com.hazz.aipick.widget.paydialog.PayPassView
import kotlinx.android.synthetic.main.activity_tixian.*
import kotlinx.android.synthetic.main.activity_tixian_record.*
import kotlinx.android.synthetic.main.activity_tixian_record.toolbar
import java.util.*


class TraderSetActivity : BaseActivity() {


    override fun layoutId(): Int = R.layout.trader_set


    override fun initData() {

    }

    private var dialog = PayPassDialog()
    private var tips: TextView? = null


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
            payDialog()
        }


    }


    private fun payDialog() {
        dialog.init(this)
        dialog.payViewPass.setPayClickListener(object : PayPassView.OnPayClickListener {
            override fun onPassFinish(passContent: String?) {

            }

            override fun onPayClose() {
                dialog.dismiss()
            }


        })
        dialog.show()
        tips = dialog.payTips

    }

}
