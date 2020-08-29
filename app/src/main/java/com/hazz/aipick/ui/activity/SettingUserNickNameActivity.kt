package com.hazz.aipick.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.presenter.UserInfoPresenter
import com.hazz.aipick.utils.ToastUtils
import com.hazz.aipick.utils.ToolBarCustom
import kotlinx.android.synthetic.main.activity_setting_user_nick_name.*

class SettingUserNickNameActivity : BaseActivity(), LoginContract.updateView {

    override fun layoutId(): Int {
        return R.layout.activity_setting_user_nick_name
    }

    companion object {
        fun start(activity: BaseActivity, nickname: String) {
            val bundle = Bundle()
            activity.startActivity(Intent(activity, SettingUserNickNameActivity::class.java).putExtra("nick", nickname))
        }
    }

    private var mUserInfoPresenter: UserInfoPresenter = UserInfoPresenter(this)
    override fun initData() {
        tv_bt.setOnClickListener {
            mUserInfoPresenter.update("nickname", "", "", "", et_nick_name.text.toString(), "", "phone", "", "", "", "", "")
        }
    }

    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.set_nick_name))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { finish() }
        var nickname = intent.getStringExtra("nick")
        et_nick_name.setText(nickname)
    }

    override fun start() {
    }

    override fun updateSuccess(msg: String) {
        ToastUtils.showToast(this, msg)
    }
}