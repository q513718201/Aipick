package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.Toolbar
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.utils.ToolBarCustom
import kotlinx.android.synthetic.main.activity_categry_desc.*


class CategryDescActivity : BaseActivity() {
    override fun initData() {

    }

    override fun layoutId(): Int = R.layout.activity_categry_desc



    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(mToolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.categry_desc))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }

    }

    override fun start() {

    }
}
