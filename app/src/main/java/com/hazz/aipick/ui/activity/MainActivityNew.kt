package com.hazz.aipick.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.widget.RadioGroup
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.socket.WsManager
import com.hazz.aipick.ui.fragment.CoinFragment
import com.hazz.aipick.ui.fragment.HomeFragment
import com.hazz.aipick.ui.fragment.InfoFragment
import com.hazz.aipick.ui.fragment.MineFragment
import com.tencent.bugly.Bugly
import kotlinx.android.synthetic.main.activity_main_new.*


class MainActivityNew : BaseActivity(), RadioGroup.OnCheckedChangeListener {
    override fun layoutId(): Int {
        return R.layout.activity_main_new
    }

    override fun initData() {
    }

    override fun initView() {
        WsManager.getInstance().init()
        initFragment()
        mRG.setOnCheckedChangeListener(this)
        Bugly.init(this, "2ccc3af99c", false)
    }

    override fun start() {
    }


    private lateinit var mFragments: ArrayList<Fragment>
    private var mLastSelect = 0


    private fun initFragment() {
        mFragments = ArrayList()
        mFragments.add(HomeFragment())
        mFragments.add(InfoFragment())
        mFragments.add(CoinFragment())
        mFragments.add(MineFragment())
        supportFragmentManager.beginTransaction()
                .replace(R.id.mFrame, mFragments[0], mFragments[0]::class.java.simpleName)
                .commitAllowingStateLoss()

        mRbMall.isChecked = true
        mLastSelect = 0

    }


    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {

            R.id.mRbMall -> {
                checkFragment(0)
            }
            R.id.mRbMining -> {
                checkFragment(1)
            }

            R.id.mRbShopCar -> {
                checkFragment(2)

            }
            R.id.mRbOtc -> {
                checkFragment(3)

            }


        }
    }

    private fun checkFragment(index: Int) {
        if (index == mLastSelect) {
            return
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.hide(mFragments[mLastSelect])
        val fragment = mFragments[index]
        if (!fragment.isAdded) {
            transaction.add(R.id.mFrame, fragment, fragment::class.java.simpleName).show(fragment)
        } else {
            transaction.show(fragment)
        }
        transaction.commitAllowingStateLoss()
        mLastSelect = index

    }

    fun setSelectRb(index: Int) {
        when (index) {

            0 -> mRbMall.isChecked = true
            1 -> mRbMining.isChecked = true
            // 2 -> mRbInformation.isChecked = true
            2 -> mRbShopCar.isChecked = true
            3 -> mRbOtc.isChecked = true

        }
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, MainActivityNew::class.java)
            context.startActivity(starter)
        }
    }

}
