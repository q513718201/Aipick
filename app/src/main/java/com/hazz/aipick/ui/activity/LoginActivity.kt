package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.model.bean.LoginBean
import com.hazz.aipick.mvp.model.bean.UserInfo
import com.hazz.aipick.mvp.presenter.LoginPresenter
import com.hazz.aipick.utils.SPUtil
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity(), LoginContract.LoginView {

    override fun setUserInfo(msg: UserInfo) {

    }


    override fun loginSuccess(msg: LoginBean) {
        SPUtil.putString("token",msg.token)
        val intent = Intent(this, MainActivityNew::class.java)
        startActivity(intent)
        finish()
    }

    private val REQUEST_AREACODE_CODE = 10005
    private var mLoginPresenter: LoginPresenter = LoginPresenter(this)
    private var currentType=0

    override fun layoutId(): Int = R.layout.activity_login

    override fun initData() {
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {


        ll_area.setOnClickListener {
            startActivityForResult(Intent(this, CountryActivity::class.java), REQUEST_AREACODE_CODE)
        }

        tv_regist.setOnClickListener {
            startActivity(Intent(this, RegistActivity::class.java))

        }
        bt_confirm.setOnClickListener {

            when(currentType){
                0->  mLoginPresenter.login("phone",mTvAreaCode.text.toString(),et_phone.text.toString(),et_pwd.text.toString())
                1->  mLoginPresenter.login("email",mTvAreaCode.text.toString(),et_phone.text.toString(),et_pwd.text.toString())

            }

        }
        tv_login_type.setOnClickListener {
            if(currentType==0){
                currentType=1
                tv_login_type.text=getString(R.string.pwd_login)
                ll_area.visibility= View.GONE
                tv_email.visibility= View.VISIBLE
                et_phone.hint=getString(R.string.please_input_email)
            }else{
                currentType=0
                tv_login_type.text=getString(R.string.mail_login)
                ll_area.visibility= View.VISIBLE
                tv_email.visibility= View.GONE
                et_phone.hint=getString(R.string.please_input_phone)
            }
        }

        tv_forget.setOnClickListener {
            startActivity(Intent(this, ForgetPwdActivity::class.java))
        }

    }

    override fun start() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_AREACODE_CODE) {
            val areaCode = data?.getStringExtra("countryNumber") ?: "+86"
            mTvAreaCode.text = areaCode
        }
    }
}
