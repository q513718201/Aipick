package com.hazz.aipick.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.CountDownTimer
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.model.bean.LoginBean
import com.hazz.aipick.mvp.model.bean.SetTrade
import com.hazz.aipick.mvp.model.bean.TraderSet
import com.hazz.aipick.mvp.model.bean.UserInfo
import com.hazz.aipick.mvp.presenter.LoginPresenter
import com.hazz.aipick.mvp.presenter.RegistPresenter
import com.hazz.aipick.mvp.presenter.TraderAuthPresenter
import com.hazz.aipick.utils.RsaUtils
import com.hazz.aipick.utils.ToastUtils
import com.hazz.aipick.utils.ToolBarCustom
import com.werb.pickphotoview.PickPhotoView
import com.werb.pickphotoview.util.PickConfig
import kotlinx.android.synthetic.main.activity_apply_trader.*
import java.util.*


class SetTraderActivity : BaseActivity(), LoginContract.RegistView, LoginContract.tradeView, LoginContract.updateView, LoginContract.LoginView {

    override fun traderSetSucceed(msg: String) {

    }

    override fun traderSetQuery(msg: TraderSet) {
    }

    override fun getCoin(msg: List<String>) {

    }


    override fun loginSuccess(msg: LoginBean) {

    }

    override fun getUserInfo(msg: UserInfo) {
        val security = msg.security
        if(!TextUtils.isEmpty(security.email)&&!TextUtils.isEmpty(security.phone)){
            et_email.setText(security.email)
            edit_phone.setText(security.phone)
            rl_getcode.visibility= View.GONE
        }

        if(TextUtils.isEmpty(security.phone)&&!TextUtils.isEmpty(security.email)){//有邮箱无手机
            et_email.setText(security.email)
            currentCode=0
        }

        if(!TextUtils.isEmpty(security.phone)&&TextUtils.isEmpty(security.email)){//有手机无邮箱
            edit_phone.setText(security.phone)
            currentCode=1
        }
    }

    override fun updateSuccess(msg: String) {

    }


    override fun traderAuthSuccess(msg: String) {


    }


    override fun onRegistSuccess(msg: String) {

    }

    override fun onSendMesSuccess(msg: String) {
        ToastUtils.showToast(this,  getString(R.string.mine_send_success))
        showCountDownView()
    }
    /**
     * 展示获取验证码倒计时
     */
    private fun showCountDownView() {
        tv_get_code.isEnabled = false
        tv_get_code.isClickable = false
        countDownTimer = object : CountDownTimer(60 * 1000, 1000) {
            override fun onFinish() {
                tv_get_code.isEnabled = true
                tv_get_code.isClickable = true
                tv_get_code.setText(getString(R.string.get_verfycode))
            }

            override fun onTick(millisUntilFinished: Long) {
                tv_get_code.setText("${millisUntilFinished / 1000}s")
            }
        }.start()

    }



    override fun layoutId(): Int = R.layout.activity_apply_trader


    override fun initData() {
        mLoginPresenter.userInfo()
    }

    private var iv1_base64: String? = ""
    private var iv2_base64: String? = ""
    private var currentType = 1
    private var countDownTimer: CountDownTimer? = null
    private var mRegistPresenter: RegistPresenter = RegistPresenter(this)
    private var mTraderAuthPresenter: TraderAuthPresenter = TraderAuthPresenter(this)
    private var mLoginPresenter: LoginPresenter = LoginPresenter(this)
    private val REQUEST_AREACODE_CODE = 10005
    private var currentCode=0

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.apply_trader))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }
        tv_get_code.setOnClickListener {
            if(currentCode==0){
                mRegistPresenter.sendCodeLogin("phone",tv_quhao.text.toString(),edit_phone.text.toString(),
                        "bind_phone"
                )
            }else{
                mRegistPresenter.sendCodeLogin("email","",et_email.text.toString(),
                        "bind_email"
                )
            }
        }

        bt_login.setOnClickListener {
          val setTrade = SetTrade(tv_quhao.text.toString(), tv_getCode.text.toString(), et_email.text.toString(),
                  edit_phone.text.toString(), et_card.text.toString(), et_name.text.toString(), "idcard", iv1_base64!!, iv2_base64!!)
            startActivity(Intent(this,ApplyTraderActivity::class.java).putExtra("setTrade",setTrade))
//            mTraderAuthPresenter.traderAuth(tv_quhao.text.toString(),tv_getCode.text.toString(),et_email.text.toString(),
//                    edit_phone.text.toString(),et_card.text.toString(),et_name.text.toString(),"idcard",iv1_base64!!,iv2_base64!!,"")
    }


        tv_quhao.setOnClickListener {
            startActivityForResult(Intent(this, CountryActivity::class.java), REQUEST_AREACODE_CODE)
        }

    }

    override fun start() {
        iv1.setOnClickListener {
            currentType = 1
            permissionsnew!!.request(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            )!!.subscribe { aBoolean ->
                if (aBoolean!!) {
                    PickPhotoView.Builder(this)
                            .setPickPhotoSize(1)                  // select image size
                            .setClickSelectable(true)             // click one image immediately close and return image
                            .setShowCamera(true)                  // is show camera
                            .setSpanCount(3)                      // span count
                            .setLightStatusBar(true)              // lightStatusBar used in Android M or higher
                            .setStatusBarColor(R.color.color_white)     // statusBar color
                            .setToolbarColor(R.color.color_white)       // toolbar color
                            .setToolbarTextColor(R.color.color_black)   // toolbar text color// select icon color
                            .setShowGif(false)                    // is show gif
                            .start()
                } else {
                    showMissingPermissionDialog()
                }
            }

        }

        iv2.setOnClickListener {
            currentType = 2
            permissionsnew!!.request(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            )!!.subscribe { aBoolean ->
                if (aBoolean!!) {
                    PickPhotoView.Builder(this)
                            .setPickPhotoSize(1)                  // select image size
                            .setClickSelectable(true)             // click one image immediately close and return image
                            .setShowCamera(true)                  // is show camera
                            .setSpanCount(3)                      // span count
                            .setLightStatusBar(true)              // lightStatusBar used in Android M or higher
                            .setStatusBarColor(R.color.color_white)     // statusBar color
                            .setToolbarColor(R.color.color_white)       // toolbar color
                            .setToolbarTextColor(R.color.color_black)   // toolbar text color// select icon color
                            .setShowGif(false)                    // is show gif
                            .start()
                } else {
                    showMissingPermissionDialog()
                }
            }

        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_AREACODE_CODE) {
            val areaCode = data?.getStringExtra("countryNumber") ?: "+86"
            tv_quhao.text = areaCode
        }

        if (requestCode == PickConfig.PICK_PHOTO_DATA) {
            val paths = data.getSerializableExtra(PickConfig.INTENT_IMG_LIST_SELECT) as ArrayList<String>
            when (currentType) {
                1 -> {
                    Glide.with(this)
                            .load(paths[0])
                            .thumbnail(0.1f)
                            .into(iv1)
                    iv1_base64 = RsaUtils.imageToBase64(paths[0])
                }
                2 -> {
                    Glide.with(this)
                            .load(paths[0])
                            .thumbnail(0.1f)
                            .into(iv2)
                    iv2_base64 = RsaUtils.imageToBase64(paths[0])
                }
            }


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }

}
