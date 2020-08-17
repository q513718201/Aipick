package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.Toolbar
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.presenter.ResetPwdPresenter
import com.hazz.aipick.utils.ToastUtils
import com.hazz.aipick.utils.ToolBarCustom
import kotlinx.android.synthetic.main.activity_bind_phone.bt_login
import kotlinx.android.synthetic.main.activity_bind_phone.toolbar
import kotlinx.android.synthetic.main.activity_bind_phone.tv1
import kotlinx.android.synthetic.main.activity_forget_reset_phone.*


class ForgetResetPwdActivity : BaseActivity(), LoginContract.updateView {

    override fun updateSuccess(msg: String) {
        ToastUtils.showToast(this, msg)
        finish()

    }


    override fun layoutId(): Int = R.layout.activity_forget_reset_phone


    override fun initData() {

    }

    private var mResetPwdPresenter: ResetPwdPresenter = ResetPwdPresenter(this)
    private var type = ""
    private var account = ""
    private var countryCode = ""
    private var code = ""

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.set_new_pwd))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { finish() }

    }

    override fun start() {
        type = intent.getStringExtra("type")
        account = intent.getStringExtra("account")
        if(type=="phone"){
            countryCode = intent.getStringExtra("countryCode")
        }
        code = intent.getStringExtra("code")
        when (type) {
            "phone" -> tv1.text = getString(R.string.current_bind, getString(R.string.phone_num), account)
            "email" -> tv1.text = getString(R.string.current_bind, getString(R.string.email), account)
        }
        bt_login.setOnClickListener {
            mResetPwdPresenter.reset(type, countryCode, account, et_new_pwd.text.toString(), code)
        }
    }


}
