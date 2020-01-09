package com.hazz.aipick.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.model.bean.LoginBean
import com.hazz.aipick.mvp.model.bean.UserInfo
import com.hazz.aipick.mvp.presenter.LoginPresenter
import com.hazz.aipick.mvp.presenter.UserInfoPresenter
import com.hazz.aipick.ui.adapter.OrderAdapter
import com.hazz.aipick.utils.*
import com.hazz.aipick.widget.TipsDialog
import com.werb.pickphotoview.PickPhotoView
import com.werb.pickphotoview.util.PickConfig
import kotlinx.android.synthetic.main.activity_mine_set.*
import kotlinx.android.synthetic.main.head_dialog.view.*


import java.util.ArrayList
import java.util.ResourceBundle.clearCache


class SettingActivity : BaseActivity(), LoginContract.LoginView, LoginContract.updateView {


    override fun updateSuccess(msg: String) {
      SToast.showText(msg)
    }

    override fun loginSuccess(msg: LoginBean) {

    }

    override fun getUserInfo(msg: UserInfo) {
        if(!TextUtils.isEmpty(msg.avatar)){
            Glide.with(this).load(msg.avatar)
                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .into(iv_avatar)
        }

        tv_name.text=msg.nickname
        tv_location.text=msg.location

    }


    override fun layoutId(): Int = R.layout.activity_mine_set

    override fun initData() {
        mLoginPresenter.userInfo()
    }

    private var mOrderAdapter: OrderAdapter? = null
    private val titleList = ArrayList<String>()
    private var dialog: BottomSheetDialog? = null
    private var mClearTipsDialog: TipsDialog? = null
    private var mLoginPresenter: LoginPresenter = LoginPresenter(this)
    private var mUserInfoPresenter: UserInfoPresenter = UserInfoPresenter(this)
    private val REQUEST_AREACODE_CODE = 10005
    private var  imageToBase64=""

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.setting))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }


    }

    override fun start() {
        rl1.setOnClickListener {//头像
            showDialog()
        }

        rl3.setOnClickListener {//所在地
            startActivityForResult(Intent(this, CountryActivity::class.java), REQUEST_AREACODE_CODE)
        }
        rl4.setOnClickListener {//安全中心
            startActivity(Intent(this,SafeCenterActivity::class.java))
        }


        rl5.setOnClickListener {//关于
            startActivity(Intent(this,AboutActivity::class.java))
        }

        rl6.setOnClickListener {//清除缓存
            showClearCacheDialog()
        }

        rl7.setOnClickListener {//退出登录
            val tipsDialog = TipsDialog(this)
            tipsDialog.setContent(resources.getString(R.string.is_logot) )
                    .setCancleListener { }
                    .setConfirmListener {
                      SPUtil.putString("token","")
                        startActivity(Intent(this,LoginActivity::class.java))
                        finish()
                    }
                    .show()
        }
    }

    private fun showClearCacheDialog() {
        if (mClearTipsDialog == null) {
            mClearTipsDialog = TipsDialog(this)
                    .setTitle(getString(R.string.tips))
                    .setContent(getString(R.string.mine_clear_pic_content))
                    .setCancleText(getString(R.string.cancel))
                    .setConfirmText(getString(R.string.confirm))
                    .setCancleListener { }
                    .setConfirmListener {
                        clearCache()
                    }
        }
        mClearTipsDialog!!.show()

    }
    private fun showDialog() {
        dialog = BottomSheetDialog(this)
        val commentView = LayoutInflater.from(this).inflate(R.layout.head_dialog, null)
        dialog!!.setContentView(commentView)
        commentView.change_head.setOnClickListener { v ->
            permissionsnew!!.request(Manifest.permission.CAMERA
            ).subscribe { permission ->
                if (permission) {
                    PickPhotoView.Builder(this)
                            .setPickPhotoSize(2)
                            .setClickSelectable(true)
                            .setShowCamera(true)
                            .setSpanCount(3)
                            .setLightStatusBar(true)
                            .setStatusBarColor(R.color.color_white)
                            .setToolbarColor(R.color.color_white)
                            .setToolbarTextColor(R.color.color_black)
                            .setShowGif(false)
                            .start()
                    dialog!!.dismiss()
                } else {
                    showMissingPermissionDialog()
                }
            }

        }


        commentView.cancle.setOnClickListener({ v -> dialog!!.dismiss() })
        dialog!!.show()
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 0) {
            return
        }
        if (data == null) {
            return
        }

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_AREACODE_CODE) {
            val areaCode = data?.getStringExtra("countryName") ?: "中国大陆"
            tv_location.text = areaCode
            mUserInfoPresenter.update("location",areaCode,"","","","","","","","","","")
        }

        if (requestCode == PickConfig.PICK_PHOTO_DATA) {
            val paths = data.getSerializableExtra(PickConfig.INTENT_IMG_LIST_SELECT) as ArrayList<String>
            mUserInfoPresenter.update("avatar","","",RsaUtils.imageToBase64(PicUtil.compressImage(paths[0], ".png"))!!,"","","","","","","","")

            Glide.with(this).load(paths[0])
                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .into(iv_avatar)



        }
    }



}
