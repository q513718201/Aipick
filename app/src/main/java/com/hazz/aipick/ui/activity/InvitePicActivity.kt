package com.hazz.aipick.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.View
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.ToastUtils
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import kotlinx.android.synthetic.main.activity_invite_pic.*

class InvitePicActivity : BaseActivity() {

    private var url = ""

    companion object {
        fun start(context: Context, url: String) {
            context.startActivity(Intent(context, InvitePicActivity::class.java).putExtra("url", url))
        }
    }

    override fun layoutId(): Int {
        return R.layout.activity_invite_pic
    }

    override fun initData() {

    }

    @ExperimentalStdlibApi
    override fun initView() {
        url = intent.getStringExtra("url")
        val decodedString: ByteArray = Base64.decode(url.split(',')[1], Base64.DEFAULT)
        val decodedByte = ImageUtils.bytes2Bitmap(decodedString)
        iv_qr_code.setImageBitmap(decodedByte)
        iv_back.setOnClickListener { finish() }
        tv_save.setOnClickListener {
            iv_back.visibility = View.GONE
            val file = ImageUtils.save2Album(ImageUtils.view2Bitmap(pic), Bitmap.CompressFormat.JPEG)
            if (file != null) {
                ToastUtils.showShort("二维码以保存到相册")
            } else {
                ToastUtils.showShort("保存失败")
            }
            iv_back.visibility = View.VISIBLE
        }
    }

    override fun start() {

    }
}