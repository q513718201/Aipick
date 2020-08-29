package com.hazz.aipick.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseFragment
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.presenter.RegistPresenter
import com.hazz.aipick.ui.activity.ForgetResetPwdActivity
import com.hazz.aipick.ui.activity.TiaokuanActivity
import com.hazz.aipick.utils.RegexUtils
import com.hazz.aipick.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_regist_email.*


class RegisterEmailFragment : BaseFragment(), LoginContract.RegistView {
    override fun onSendMesSuccess(msg: String) {
        ToastUtils.showToast(activity, getString(R.string.mine_send_success))
        showCountDownView()
    }

    override fun onRegistSuccess(msg: String) {
        ToastUtils.showToast(activity, getString(R.string.regist_success))
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

    override fun getLayoutId(): Int = R.layout.fragment_regist_email


    override fun lazyLoad() {

    }

    override fun initView() {
        if (mTitle != null) {
            tv_bt.text = getString(R.string.next_step)
            ll_xieyi.visibility = View.GONE
            llPwd.visibility = View.GONE
        }
        service_rule.setOnClickListener {
            context?.let { TiaokuanActivity.start(it, 1) }
        }

        tv_bt.setOnClickListener {
            if (edit_text.text.toString().isEmpty()) {
                ToastUtils.showToast(activity, getString(R.string.hint_register_empty_email))
                return@setOnClickListener
            }
            if (!RegexUtils.isEmail(edit_text.text)) {
                ToastUtils.showToast(activity, getString(R.string.hint_register_empty_email))
                return@setOnClickListener
            }
            //如果没有获取验证码，无法校验 可用的时候说明没有获取验证码
            if (tv_getCode.isEnabled) {
                ToastUtils.showToast(activity, getString(R.string.hint_register_no_checkcode))
                return@setOnClickListener
            }
            var code = verify_code_edit.text.toString()
            if (code.isEmpty()) {
                ToastUtils.showToast(activity, getString(R.string.hint_register_empty_checkcode))
                return@setOnClickListener
            }
            if (mTitle != null) {//忘记密码流程
                startActivity(Intent(activity, ForgetResetPwdActivity::class.java).putExtra("type", "email").putExtra("account", edit_text.text.toString())
                        .putExtra("code", code)
                )
                activity?.finish()
            } else {
                mRegistPresenter.regist("email", "", edit_text.text.toString(), pwd.text.toString(), code)
            }

        }
        tv_getCode.setOnClickListener {
            if (edit_text.text.toString().isEmpty()) {
                ToastUtils.showToast(activity, getString(R.string.hint_register_empty_email))
                return@setOnClickListener
            }
            if (!RegexUtils.isEmail(edit_text.text)) {
                ToastUtils.showToast(activity, getString(R.string.hint_register_invalid_email))
                return@setOnClickListener
            }
            if (mTitle != null) {
                mRegistPresenter.sendCode("email", "", edit_text.text.toString(), "reset_password"
                )
            } else {
                mRegistPresenter.sendCode("email", "", edit_text.text.toString(), "register"

                )
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}