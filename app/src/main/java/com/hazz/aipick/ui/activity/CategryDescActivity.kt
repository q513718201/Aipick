package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import kotlinx.android.synthetic.main.activity_categry_desc.*
import kotlinx.android.synthetic.main.view_subscibe.*


class CategryDescActivity : BaseActivity() {
    override fun initData() {

    }

    override fun layoutId(): Int = R.layout.activity_categry_desc


    @SuppressLint("SetTextI18n")
    override fun initView() {

        iv_back.setOnClickListener {
            finish()
        }
        tv_suscribe.setOnClickListener {
            startActivity(Intent(this, SubscribeDescActivity::class.java))
        }
    }

    override fun start() {

    }
}
