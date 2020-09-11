package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.Toolbar
import com.hazz.aipick.R
import com.hazz.aipick.base.Agree
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.utils.ToolBarCustom
import kotlinx.android.synthetic.main.activity_tiaokuan.*


class TiaokuanActivity : BaseActivity() {
    override fun initData() {

    }

    companion object {
        fun start(activity: Context, type: Int) {
            activity.startActivity(Intent(activity, TiaokuanActivity::class.java).putExtra("type", type))
        }
    }

    override fun layoutId(): Int = R.layout.activity_tiaokuan


    @SuppressLint("SetTextI18n")
    override fun initView() {
        val type = intent.getIntExtra("type", -1)
        content.text = when (type) {
            0 -> Agree.service_for_pay
            1 -> Agree.user_agree
            2 -> Agree.wind_control
            else->
                Agree.user_agree
        }
        var title = when (type) {
            0 -> "用户服务付费协议"
            1 -> "用户使用协议"
            2 -> "AI PICK风控协议"
            else -> getString(R.string.fuwuxieyi)
        }
        ToolBarCustom.newBuilder(mToolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(title)
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { finish() }


    }

    override fun start() {

    }
}
