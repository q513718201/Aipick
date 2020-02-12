package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.Toolbar
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.HomeContract
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.*
import com.hazz.aipick.mvp.presenter.AccountPresenter
import com.hazz.aipick.utils.ToolBarCustom
import kotlinx.android.synthetic.main.activity_time_choose.*


class ChooseTimeActivity : BaseActivity(),  WaletContract.myaccountView {

    override fun setFollow(msg: String) {

    }

    override fun myaccount(msg: MyAccount) {

    }

    override fun coinList(msg: BindCoinHouse) {

    }

    override fun getPrice(msg: ChooseTime) {
        tv1.text = msg.days30
        tv2.text = msg.days90
        tv3.text = msg.days180
    }



    override fun layoutId(): Int = R.layout.activity_time_choose

    override fun initData() {
        rl1.setOnClickListener {
            currentDays = "30days"
            rl1.setBackgroundResource(R.drawable.pay_select_bg)
            rl2.setBackgroundResource(R.drawable.pay_gradient_bg)
            rl3.setBackgroundResource(R.drawable.pay_gradient_bg)
        }
        rl2.setOnClickListener {
            currentDays = "90days"
            rl2.setBackgroundResource(R.drawable.pay_select_bg)
            rl1.setBackgroundResource(R.drawable.pay_gradient_bg)
            rl3.setBackgroundResource(R.drawable.pay_gradient_bg)
        }
        rl3.setOnClickListener {
            currentDays = "180days"
            rl3.setBackgroundResource(R.drawable.pay_select_bg)
            rl2.setBackgroundResource(R.drawable.pay_gradient_bg)
            rl1.setBackgroundResource(R.drawable.pay_gradient_bg)
        }
    }

    private var id = ""
    private var price = ""
    private var mAccountPresenter: AccountPresenter = AccountPresenter(this)
    private var bean: BindCoinHouse.ExchangesBean? = null
    private var currentNmae = ""
    private var currentDays = "30days"
    private var switch = ""
    private var followType = ""
    private var followFactor = ""
    private var role = ""
    private var beanSymbl: BindCoinHouse.SymbolsBean? = null

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.choose_time))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }
        rl1.setBackgroundResource(R.drawable.pay_select_bg)
    }

    @SuppressLint("SetTextI18n")
    override fun start() {
        id = intent.getStringExtra("id")
        price = intent.getStringExtra("price")
        bean = intent.getSerializableExtra("bean") as BindCoinHouse.ExchangesBean?
        currentNmae = intent.getStringExtra("name")
        switch = intent.getStringExtra("switch")
        followType = intent.getStringExtra("followType")
        followFactor = intent.getStringExtra("followFactor")
        role = intent.getStringExtra("role")
        beanSymbl = intent.getSerializableExtra("SymbolsBean") as BindCoinHouse.SymbolsBean
        tv_price.text = "$$price"
        tv_name.text = currentNmae
        mAccountPresenter.getPrice(id)

        tv_suscribe.setOnClickListener {
            val payBean = PayBean(id, bean?.exchange_id, switch, followType, followFactor, currentDays,beanSymbl!!.base_coin,beanSymbl!!.quote_coin)
            startActivity(Intent(this, PayActivity::class.java).putExtra("payBean", payBean)
                    .putExtra("role", role) .putExtra("price", price))

            finish()

        }
    }


}
