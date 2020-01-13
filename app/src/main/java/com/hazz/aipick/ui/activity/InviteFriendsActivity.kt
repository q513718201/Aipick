package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.widget.TextView
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.utils.ToolBarCustom
import com.hazz.aipick.widget.paydialog.PayPassDialog
import kotlinx.android.synthetic.main.invite_friend.*


class InviteFriendsActivity : BaseActivity() {


    override fun layoutId(): Int = R.layout.invite_friend


    override fun initData() {

    }

    private var dialog = PayPassDialog()
    private var tips: TextView? = null

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.invite_friend))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }


    }

    override fun start() {
        copy_invitation_btn.setOnClickListener {

        }
    }



}
