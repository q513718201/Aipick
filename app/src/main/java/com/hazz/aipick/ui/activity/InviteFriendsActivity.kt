package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.Toolbar
import android.util.Base64
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils.showShort
import com.blankj.utilcode.util.Utils
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.UserContract
import com.hazz.aipick.mvp.model.bean.InviteInfoBean
import com.hazz.aipick.mvp.presenter.InvitePresenter
import com.hazz.aipick.utils.ToolBarCustom
import kotlinx.android.synthetic.main.invite_friend.*


class InviteFriendsActivity : BaseActivity(), UserContract.InviteView {


    override fun layoutId(): Int = R.layout.invite_friend

    private lateinit var bean: InviteInfoBean

    @ExperimentalStdlibApi
    override fun setInviteInfo(msg: InviteInfoBean) {
        LogUtils.e(msg)
        bean = msg
        val decodedString: ByteArray = Base64.decode(msg.invite_er.split(',')[1], Base64.DEFAULT)
        val decodedByte = ImageUtils.bytes2Bitmap(decodedString)
        qrCodeView.setImageBitmap(decodedByte)
    }

    private var mPresenter = InvitePresenter(this)
    override fun initData() {
        mPresenter.getInviteInfo()
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.invite_friend))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }
        copy_invitation_btn.setOnClickListener {
            val cm: ClipboardManager = Utils.getApp().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            try {
                cm.primaryClip = ClipData.newPlainText("inviteLink", bean.invite_link)
                showShort("邀请链接已复制到剪切板")
            } catch (e: Exception) {
                showShort("复制失败")
            }

        }
        tv_save_qr.setOnClickListener {
            InvitePicActivity.start(this, bean.invite_er)
        }
//        GlideUtil.load(qrCodeView, "https://avatars3.githubusercontent.com/u/13379927?s=480&v=4")

    }

    override fun start() {

    }


}
