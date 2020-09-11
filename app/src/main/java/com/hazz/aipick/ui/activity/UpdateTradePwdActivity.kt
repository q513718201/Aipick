package com.hazz.aipick.ui.activity

import android.graphics.Color
import android.support.v7.widget.Toolbar
import com.blankj.utilcode.util.ToastUtils
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.presenter.UserInfoPresenter
import com.hazz.aipick.utils.SPUtil
import com.hazz.aipick.utils.ToolBarCustom
import kotlinx.android.synthetic.main.activity_update_trade_pwd.*

class UpdateTradePwdActivity : BaseActivity(), LoginContract.updateView {

    override fun layoutId(): Int {
        return R.layout.activity_update_trade_pwd
    }

    override fun initData() {

    }

    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.update_trade_pwd))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { finish() }

        bt_confirm.setOnClickListener {
            var orig = et_trade_pwd_org.text.toString().trim()
            if (orig.isNullOrBlank()) {
                ToastUtils.showShort(R.string.please_input_old_trade_pwd)
                return@setOnClickListener
            }
            var et_trade_pwd = et_trade_pwd.text.toString().trim()
            if (et_trade_pwd.isNullOrBlank()) {
                ToastUtils.showShort(R.string.please_input_old_trade_pwd)
                return@setOnClickListener
            }
            var et_trade_pwd_again = et_trade_pwd_again.text.toString().trim()
            if (et_trade_pwd != et_trade_pwd_again) {
                ToastUtils.showShort(R.string.hint_pay_pwd_again)
                return@setOnClickListener
            }
            mUserInfoPresenter.update("modify_trade_password ", "", "", "", "", "", SPUtil.getUser().security.phone, "phone", "", "", orig, et_trade_pwd)
        }
    }

    var mUserInfoPresenter = UserInfoPresenter(this)

    override fun start() {

    }

    override fun updateSuccess(msg: String) {
        ToastUtils.showShort(msg)
    }
}