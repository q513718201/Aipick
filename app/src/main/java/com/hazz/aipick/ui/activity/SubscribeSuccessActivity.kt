package com.hazz.aipick.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v7.widget.Toolbar
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.utils.ToolBarCustom
import kotlinx.android.synthetic.main.activity_subscribe_success.*
import kotlinx.android.synthetic.main.activity_time_choose.toolbar
import kotlinx.android.synthetic.main.activity_time_choose.tv1
import kotlinx.android.synthetic.main.activity_time_choose.tv2
import kotlinx.android.synthetic.main.activity_time_choose.tv3

class SubscribeSuccessActivity : BaseActivity() {

    override fun layoutId(): Int {
        return R.layout.activity_subscribe_success
    }

    companion object {
        fun start(context: Context, orderAccount: String, myAccount: String, time: String) {
            val intent = Intent(context, SubscribeSuccessActivity::class.java)
            intent.putExtra("oderAccount", orderAccount)
            intent.putExtra("myAccount", myAccount)
            intent.putExtra("time", time)
            context.startActivity(intent)
        }
    }

    private var oderAccount = ""
    private var myAccount = ""
    private var time = ""

    override fun initData() {
        intent?.let {
            oderAccount = it.getStringExtra("oderAccount")
            myAccount = it.getStringExtra("myAccount")
            time = it.getStringExtra("time")
            tv1.text = oderAccount
            tv2.text = myAccount
            tv3.text = time
        }
    }

    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setToolBarBgRescource(R.color.bg_common)
                .setOnLeftIconClickListener { finish() }
        tv_finish.setOnClickListener {
            finish()
        }
    }

    override fun start() {
    }
}