package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.ui.fragment.*
import kotlinx.android.synthetic.main.activity_robot_categry.*
import android.support.v4.app.Fragment


class RebotCategryActivity : BaseActivity() {


    override fun layoutId(): Int = R.layout.activity_robot_categry

    override fun initData() {

    }


    private var mTransactionAnalysisFragment: TransactionAnalysisFragment? = null
    private var mOrderFragment: OrderFragment? = null
    private var mLastFragment: Fragment? = null


    @SuppressLint("SetTextI18n")
    override fun initView() {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl, TransactionAnalysisFragment()).commitAllowingStateLoss()
        rg.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                R.id.rb1 -> {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fl, TransactionAnalysisFragment()).commitAllowingStateLoss()
                }
                R.id.rb2 -> {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fl, OrderFragment()).commitAllowingStateLoss()


                }
                R.id.rb3 -> {

                }
            }


        }

        iv_back.setOnClickListener {
            finish()
        }

        tv_desc.setOnClickListener {
            startActivity(Intent(this, CategryDescActivity::class.java))
        }

        tv_suscribe.setOnClickListener {
//            startActivity(Intent(this, SubscribeDescActivity::class.java))
        }

    }


    override fun start() {

    }


}
