package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import kotlinx.android.synthetic.main.activity_coin_desc.*


class CoinDescActivity : BaseActivity() {




    override fun layoutId(): Int = R.layout.activity_coin_desc

    override fun initData() {

    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        iv_back.setOnClickListener {
            finish()
        }
    }

    override fun start() {

    }




}
