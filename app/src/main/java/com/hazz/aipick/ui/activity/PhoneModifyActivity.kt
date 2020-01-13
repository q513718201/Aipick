package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.CountDownTimer
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.presenter.RegistPresenter
import com.hazz.aipick.mvp.presenter.UserInfoPresenter
import com.hazz.aipick.utils.ToastUtils
import com.hazz.aipick.utils.ToolBarCustom
import kotlinx.android.synthetic.main.activity_bind_phone.*



class PhoneModifyActivity : BaseActivity(), LoginContract.RegistView, LoginContract.updateView {

    override fun updateSuccess(msg: String) {
        ToastUtils.showToast(this,  msg)
        finish()
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
                tv_get_code.text = getString(R.string.get_verfycode)
            }

            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                tv_get_code.text = "${millisUntilFinished / 1000}s"
            }
        }.start()

    }


    override fun layoutId(): Int = R.layout.activity_bind_phone


    override fun initData() {

    }
    private var isBind=false

    private var num=""
    private var type=""
    private var onLeftIconClickListener:ToolBarCustom.Builder?=null
    private var mRegistPresenter: RegistPresenter = RegistPresenter(this)
    private var mUserInfoPresenter: UserInfoPresenter = UserInfoPresenter(this)

    private var countDownTimer: CountDownTimer? = null
    private val REQUEST_AREACODE_CODE = 10005


    @SuppressLint("SetTextI18n")
    override fun initView() {
          onLeftIconClickListener = ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)

                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view ->
                    finish()
                }

    }

    override fun start() {
        type= intent.getStringExtra("type")
        num= intent.getStringExtra("num")
        isBind= intent.getBooleanExtra("isBind",false)

        Log.d("junjun",type+"---"+num+"--"+isBind)

        if(type == "phone"){
            if(isBind){
                onLeftIconClickListener?.setTitle(getString(R.string.modify_phone))
                tv1.text=getString(R.string.current_bind_phone,num)
            }else{
                rl.visibility= View.GONE
                onLeftIconClickListener?.setTitle(getString(R.string.bind_phone))

            }

        }else{
            tv_quhao.visibility=View.GONE
            tv_email.visibility=View.VISIBLE
            edit_text.hint=getString(R.string.please_input_email)
            if(isBind){
                onLeftIconClickListener?.setTitle(getString(R.string.modify_email))
                tv1.text=getString(R.string.current_bind_email,num)

            }else{
                rl.visibility= View.GONE
                onLeftIconClickListener?.setTitle(getString(R.string.bind_email))
            }

        }
        tv_quhao.setOnClickListener {
            startActivityForResult(Intent(this, CountryActivity::class.java), REQUEST_AREACODE_CODE)
        }
        tv_get_code.setOnClickListener {
            if(type == "phone"){
                mRegistPresenter.sendCodeLogin("phone",tv_quhao.text.toString(),edit_text.text.toString(),
                        "bind_phone")

            }else{

                mRegistPresenter.sendCodeLogin("email","",edit_text.text.toString(),
                        "bind_email")

            }
        }

        bt_login.setOnClickListener {

            if(type == "phone"){
                mUserInfoPresenter.update("phone","",et_verfycode.text.toString(),"","",tv_quhao.text.toString(),edit_text.text.toString(),"phone","","","","")

            }else{
                mUserInfoPresenter.update("email","",et_verfycode.text.toString(),"","","",edit_text.text.toString(),"email","","","","")


            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_AREACODE_CODE) {
            val areaCode = data?.getStringExtra("countryNumber") ?: "+86"
            tv_quhao.text = areaCode
        }
    }
}
