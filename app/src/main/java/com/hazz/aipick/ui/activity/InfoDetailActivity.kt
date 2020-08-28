package com.hazz.aipick.ui.activity

import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity

class InfoDetailActivity : BaseActivity() {

    override fun layoutId(): Int {
        return R.layout.activity_info_detail
    }

    private lateinit var id: String

    override fun initData() {
        intent?.let {
            id = it.getStringExtra("id")
        }
    }

    override fun initView() {

    }

    override fun start() {

    }
}