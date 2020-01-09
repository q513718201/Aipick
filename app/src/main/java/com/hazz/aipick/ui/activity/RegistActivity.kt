package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.presenter.RegistPresenter
import com.hazz.aipick.net.BaseView
import com.hazz.aipick.ui.fragment.RegisterEmailFragment
import com.hazz.aipick.ui.fragment.RegisterPhoneNumberFragment
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_regist.*


class RegistActivity : BaseActivity() {



    private var mCurrentType = 0

    override fun layoutId(): Int = R.layout.activity_regist

    override fun initData() {

    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        view_pager.adapter = mFragmentPagerAdapter
        tab_layout.setupWithViewPager(view_pager)
        iv_back.setOnClickListener {
            finish()
        }
    }

    override fun start() {

    }


    private val mFragmentPagerAdapter = object : FragmentPagerAdapter(supportFragmentManager) {
        override fun getItem(position: Int): Fragment {
            return if (position == 0) {
                RegisterPhoneNumberFragment()
            } else {
                RegisterEmailFragment()
            }
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return if (position == 0) {
                resources.getText(R.string.phone_register)
            } else {
                resources.getText(R.string.email_register)
            }
        }
    }


}
