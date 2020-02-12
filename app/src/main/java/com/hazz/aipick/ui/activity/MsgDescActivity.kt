package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.support.v7.widget.Toolbar
import com.hazz.aipick.MyApplication
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.model.bean.Message
import com.hazz.aipick.utils.AppUtils
import com.hazz.aipick.utils.StatusBarUtil
import com.hazz.aipick.utils.ToolBarCustom
import kotlinx.android.synthetic.main.activity_msg_desc.*
import kotlinx.android.synthetic.main.activity_tiaokuan.*
import kotlinx.android.synthetic.main.activity_tiaokuan.mToolbar


class MsgDescActivity : BaseActivity() {
    override fun initData() {

    }

    override fun layoutId(): Int = R.layout.activity_msg_desc



    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(mToolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.msg_desc))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }

    }

    override fun start() {
        val message = intent.getSerializableExtra("message") as Message

        tv_title.text=message.title
        tv_time.text=getString(R.string.coin_fabu_time,message.create_at)
        tv_intro.text=message.content
    }
}
