package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.Toolbar
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.utils.ToolBarCustom
import kotlinx.android.synthetic.main.activity_tiaokuan.*


class TiaokuanActivity : BaseActivity() {
    override fun initData() {

    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, TiaokuanActivity::class.java))
        }
    }

    override fun layoutId(): Int = R.layout.activity_tiaokuan


    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(mToolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.fuwuxieyi))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { finish() }

    }

    override fun start() {

    }
}
