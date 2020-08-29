package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.Toolbar
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.presenter.UserInfoPresenter
import com.hazz.aipick.utils.RegexConstants
import com.hazz.aipick.utils.RegexUtils
import com.hazz.aipick.utils.ToastUtils
import com.hazz.aipick.utils.ToolBarCustom
import kotlinx.android.synthetic.main.activity_modify_pwd.*


class PwdModifyActivity : BaseActivity(), LoginContract.updateView {
    override fun updateSuccess(msg: String) {
        ToastUtils.showToast(this, msg)
        finish()
    }


    override fun layoutId(): Int = R.layout.activity_modify_pwd


    override fun initData() {

    }

    private var mUserInfoPresenter: UserInfoPresenter = UserInfoPresenter(this)


    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.modify_pwd))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }

    }

    override fun start() {
        bt_confirm.setOnClickListener {

            var pwdOld = et_old_pwd.text.toString().trim()
            if (pwdOld.isEmpty()) {
                ToastUtils.showToast(this, getString(R.string.hint_pay_pwd))
                return@setOnClickListener
            }
            var pwdNew = et_new_pwd.text.toString().trim()
            if (!RegexUtils.isMatch(RegexConstants.REGEX_USER_PASSWORD, pwdNew)) {
                ToastUtils.showToast(this, getString(R.string.hint_register_invalid_password))
                return@setOnClickListener
            }
            var pwdConfirm = et_again_pwd.text.toString().trim()
            if (pwdNew != pwdConfirm) {
                ToastUtils.showToast(this, getString(R.string.hint_pay_pwd_again))
                return@setOnClickListener
            }


            mUserInfoPresenter.update("password", "",
                    "", "", "",
                    "", "",
                    "", et_old_pwd.text.toString(), et_again_pwd.text.toString(), "", "")


        }

    }


}
