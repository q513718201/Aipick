package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import com.hazz.aipick.MyApplication
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.utils.AppUtils
import com.hazz.aipick.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_about.*

/**
 * Created by xuhao on 2017/12/6.
 * desc: 关于
 */
class AboutActivity : BaseActivity() {
    override fun initData() {

    }

    override fun layoutId(): Int = R.layout.activity_about



    @SuppressLint("SetTextI18n")
    override fun initView() {

        tv_version_name.text ="v${AppUtils.getVerName(MyApplication.context)}"
        //返回
        toolbar.setNavigationIcon(R.mipmap.back_white)
        toolbar.setNavigationOnClickListener { finish() }
        //访问 GitHub

    }

    override fun start() {
       tiaokuan.setOnClickListener {
            startActivity(Intent(this,TiaokuanActivity::class.java))
        }
    }
}
