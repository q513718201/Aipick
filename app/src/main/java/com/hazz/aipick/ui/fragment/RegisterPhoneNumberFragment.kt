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
import com.hazz.aipick.ui.activity.TiaokuanActivity
import com.hazz.aipick.utils.RegexConstants.REGEX_USER_PASSWORD
import com.hazz.aipick.utils.RegexUtils
import com.hazz.aipick.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_regist_phone.*


class RegisterPhoneNumberFragment : BaseFragment(), LoginContract.RegistView {
    override fun onSendMesSuccess(msg: String) {
        ToastUtils.showToast(activity, getString(R.string.mine_send_success))
        showCountDownView()
    }

    override fun onRegistSuccess(msg: String) {
        ToastUtils.showToast(activity, getString(R.string.regist_success))
        activity?.onBackPressed()
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

    override fun getLayoutId(): Int = R.layout.fragment_regist_phone


    override fun lazyLoad() {

    }


    companion object {
        fun getInstance(title: String): RegisterPhoneNumberFragment {
            val fragment = RegisterPhoneNumberFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun initView() {
        if (mTitle != null) {
            tv_bt.text = getString(R.string.next_step)
            ll_xieyi.visibility = View.GONE
            llPwd.visibility = View.GONE
        }

        tv_nation.setOnClickListener {
            startActivityForResult(Intent(activity, CountryActivity::class.java), REQUEST_AREACODE_CODE)
        }
        tv_bt.setOnClickListener {
            var phone = edit_text.text.toString()
            //检查手机号
            if (phone.isEmpty()) {
                ToastUtils.showToast(activity, getString(R.string.hint_register_empty_phone))
                return@setOnClickListener
            }
            // TODO: 2020/8/18 取消手机号码校验
//            if (!RegexUtils.isMobileSimple(phone)) {
//                ToastUtils.showToast(activity, getString(R.string.hint_register_invalid_phone))
//                return@setOnClickListener
//            }
            //如果没有获取验证码，无法校验 可用的时候说明没有获取验证码
            if (tv_getCode.isEnabled) {
                ToastUtils.showToast(activity, getString(R.string.hint_register_no_checkcode))
                return@setOnClickListener
            }
            //检查验证码
            if (verify_code_edit.text.toString().isEmpty()) {
                ToastUtils.showToast(activity, getString(R.string.hint_register_empty_checkcode))
                return@setOnClickListener
            }

            if (mTitle != null) {
                startActivity(Intent(activity, ForgetResetPwdActivity::class.java)
                        .putExtra("type", "phone")
                        .putExtra("account", edit_text.text.toString())
                        .putExtra("countryCode", tv_quhao.text.toString())
                        .putExtra("code", verify_code_edit.text.toString())
                )
                activity?.finish()

            } else {
                var password = pwd.text.toString()
                //检查密码
                if (password.isEmpty()) {
                    ToastUtils.showToast(activity, getString(R.string.hint_register_empty_password))
                    return@setOnClickListener
                }
                if (!RegexUtils.isMatch(REGEX_USER_PASSWORD, password)) {
                    ToastUtils.showToast(activity, getString(R.string.hint_register_invalid_password))
                    return@setOnClickListener
                }
                //检查服务条款
                if (!check_box.isChecked) {
                    ToastUtils.showToast(activity, getString(R.string.hint_register_checkbox))
                    return@setOnClickListener
                }
                mRegistPresenter.regist("phone", tv_quhao.text.toString(), edit_text.text.toString(), pwd.text.toString(),
                        verify_code_edit.text.toString()
                )
            }
        }
        service_rule.setOnClickListener {
            startActivity(Intent(activity, TiaokuanActivity::class.java))
        }
        tv_getCode.setOnClickListener {
            if (edit_text.text.toString().isEmpty()) {
                ToastUtils.showToast(activity, getString(R.string.hint_register_empty_phone))
                return@setOnClickListener
            }

            if (mTitle != null) {
                mRegistPresenter.sendCode("phone", tv_quhao.text.toString(), edit_text.text.toString(), "reset_password")


            } else {
                mRegistPresenter.sendCode("phone", tv_quhao.text.toString(), edit_text.text.toString(), "register")
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
            val area = data?.getStringExtra("countryName") ?: "中国"
            val areaCode = data?.getStringExtra("countryNumber") ?: "+86"
            tv_nation.text = area
            tv_quhao.text = areaCode
        }
    }
}