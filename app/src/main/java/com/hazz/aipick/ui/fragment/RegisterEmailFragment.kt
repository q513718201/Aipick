package com.hazz.aipick.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.view.View
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseFragment
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.presenter.RegistPresenter
import com.hazz.aipick.ui.activity.CountryActivity
import com.hazz.aipick.ui.activity.ForgetResetPwdActivity
import com.hazz.aipick.ui.activity.GorgetPwdActivity
import com.hazz.aipick.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_regist_email.*
import kotlinx.android.synthetic.main.fragment_regist_email.edit_text
import kotlinx.android.synthetic.main.fragment_regist_email.pwd
import kotlinx.android.synthetic.main.fragment_regist_email.tv_bt
import kotlinx.android.synthetic.main.fragment_regist_email.tv_getCode
import kotlinx.android.synthetic.main.fragment_regist_email.tv_nation
import kotlinx.android.synthetic.main.fragment_regist_email.tv_quhao
import kotlinx.android.synthetic.main.fragment_regist_email.verify_code_edit



class RegisterEmailFragment : BaseFragment(), LoginContract.RegistView {
    override fun onSendMesSuccess(msg: String) {
        ToastUtils.showToast(activity,  getString(R.string.mine_send_success))
        showCountDownView()
    }

    override fun onRegistSuccess(msg: String) {
        ToastUtils.showToast(activity,  getString(R.string.regist_success))
        activity?.onBackPressed()
    }

    companion object {
        fun getInstance(title: String): RegisterEmailFragment {
            val fragment = RegisterEmailFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    /**
     * 展示获取验证码倒计时
     */
    private fun showCountDownView() {
        tv_getCode.isEnabled = false
        tv_getCode.isClickable = false
        countDownTimer = object : CountDownTimer(60 * 1000, 1000) {
            override fun onFinish() {
                tv_getCode.isEnabled = true
                tv_getCode.isClickable = true
                tv_getCode.text = getString(R.string.get_verfycode)
            }

            override fun onTick(millisUntilFinished: Long) {
                tv_getCode.text = "${millisUntilFinished / 1000}s"
            }
        }.start()

    }

    private var countDownTimer: CountDownTimer? = null
    private var mTitle: String? = null
    private var mRegistPresenter: RegistPresenter = RegistPresenter(this)
    /**
     * 存放 tab 标题
     */
    private val mTabTitleList = ArrayList<String>()

    private val mFragmentList = ArrayList<Fragment>()
    private val REQUEST_AREACODE_CODE = 10005

    override fun getLayoutId(): Int = R.layout.fragment_regist_email


    override fun lazyLoad() {

    }

    override fun initView() {
        if(mTitle!=null){
            tv_bt.text=getString(R.string.next_step)
            ll_xieyi.visibility= View.GONE
            pwd.visibility= View.GONE
        }

        tv_nation.setOnClickListener {
            startActivityForResult(Intent(activity, CountryActivity::class.java), REQUEST_AREACODE_CODE)
        }

        tv_bt.setOnClickListener {

            if(mTitle!=null){
                startActivity(Intent(activity, ForgetResetPwdActivity::class.java).putExtra("type","email").putExtra("account",edit_text.text.toString())
                        .putExtra("countryCode",tv_nation.text.toString())
                        .putExtra("code", verify_code_edit.text.toString())
                )
                activity?.finish()
            }else{
                mRegistPresenter.regist("email", tv_nation.text.toString(), edit_text.text.toString(), pwd.text.toString(),
                        verify_code_edit.text.toString()
                )
            }

        }
        tv_getCode.setOnClickListener {
            if(mTitle!=null){
                mRegistPresenter.sendCode("email", tv_nation.text.toString(), edit_text.text.toString(), "reset_password"
                )
            }else{
                mRegistPresenter.sendCode("email", tv_nation.text.toString(), edit_text.text.toString(), "register"

                )
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
            val areaCode = data?.getStringExtra("countryName") ?: "中国"
            tv_nation.text = areaCode
        }
    }
}