package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.model.bean.LoginBean
import com.hazz.aipick.mvp.model.bean.UserInfo
import com.hazz.aipick.mvp.presenter.LoginPresenter
import com.hazz.aipick.utils.ToolBarCustom
import kotlinx.android.synthetic.main.activity_safe_center.*


class SafeCenterActivity : BaseActivity(), LoginContract.LoginView {


    override fun loginSuccess(msg: LoginBean) {

    }

    private lateinit var security: UserInfo
    override fun setUserInfo(msg: UserInfo) {
        val security = msg.security
        if (security.bind_bankcard) {
            tv_bank_satate.text = getString(R.string.has_bind)
        }

        if (!TextUtils.isEmpty(security.email)) {
            tv_email_satate.text = getString(R.string.has_bind)
            isBindEmail = true
        }
        if (security.has_trade_password) {
            tv_trader_satate.text = getString(R.string.has_set)
        }
        if (TextUtils.isEmpty(msg.security.phone)) {
            tv_phone.text = getString(R.string.unbind)
        } else {
            tv_phone.text = "${msg.security.country_code} ${msg.security.phone}"
            phone = "${msg.security.country_code} ${msg.security.phone}"
            isBindPhone = true
        }

        if (TextUtils.isEmpty(msg.security.email)) {
            tv_email_satate.text = getString(R.string.unbind)
        } else {
            tv_email_satate.text = msg.security.email
            email = msg.security.email
        }
    }


    override fun layoutId(): Int = R.layout.activity_safe_center


    override fun initData() {


    }

    private var mLoginPresenter: LoginPresenter = LoginPresenter(this)
    private var isBindPhone = false
    private var isBindEmail = false
    private var phone = ""
    private var email = ""

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(mToolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.my_safe_center))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }

    }

    override fun start() {
        rl1.setOnClickListener {
            startActivity(Intent(this, PhoneModifyActivity::class.java).putExtra("type", "phone").putExtra("isBind", isBindPhone)
                    .putExtra("num", phone)
            )
        }
        rl2.setOnClickListener {
            startActivity(Intent(this, PhoneModifyActivity::class.java).putExtra("type", "email").putExtra("isBind", isBindEmail)
                    .putExtra("num", email)
            )
        }
        rl3.setOnClickListener {
            startActivity(Intent(this, PwdModifyActivity::class.java))
        }
        rl4.setOnClickListener {
            if (tv_trader_satate.text == getString(R.string.has_set)) {
                startActivity(Intent(this, UpdateTradePwdActivity::class.java))
            } else {
                startActivity(Intent(this, SetTradePwdActivity::class.java))
            }

        }
        rl5.setOnClickListener {
            startActivity(Intent(this, BindBankActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        mLoginPresenter.userInfo()
    }
}
