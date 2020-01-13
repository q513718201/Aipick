package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.presenter.BindCarPresenter
import com.hazz.aipick.utils.ToastUtils
import com.hazz.aipick.utils.ToolBarCustom
import kotlinx.android.synthetic.main.activity_bind_bank.*
import kotlinx.android.synthetic.main.activity_bind_phone.*
import kotlinx.android.synthetic.main.activity_bind_phone.toolbar


class BindBankActivity : BaseActivity(), LoginContract.bindCarView {


    override fun bindSuccess(msg: String) {
        ToastUtils.showToast(this,  msg)
        finish()
    }


    override fun layoutId(): Int = R.layout.activity_bind_bank


    override fun initData() {

    }

    private var mBindCarPresenter: BindCarPresenter = BindCarPresenter(this)

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.bind_card))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { finish() }

    }

    override fun start() {
        tv_save.setOnClickListener {
            if(TextUtils.isEmpty(edit_name.text)){
                ToastUtils.showToast(this,"请输入真实姓名")
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(edit_bank.text)){
                ToastUtils.showToast(this,"请输入开户银行")
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(edit_zhihang.text)){
                ToastUtils.showToast(this,"请输入开户支行")
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(edit_card.text)){
                ToastUtils.showToast(this,"请输入银行卡号")
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(edit_pwd.text)){
                ToastUtils.showToast(this,"请输入资金密码")
                return@setOnClickListener
            }
            mBindCarPresenter.bindCard(edit_name.text.toString(),edit_bank.text.toString(),edit_zhihang.text.toString(),
                    edit_card.text.toString(),edit_pwd.text.toString()
                    )

        }
    }


}
