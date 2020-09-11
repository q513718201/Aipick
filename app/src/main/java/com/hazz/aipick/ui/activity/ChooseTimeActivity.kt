package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.Toolbar
import android.view.View
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.HomeContract
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.*
import com.hazz.aipick.mvp.presenter.AccountPresenter
import com.hazz.aipick.mvp.presenter.PayPresenter
import com.hazz.aipick.utils.ToolBarCustom
import kotlinx.android.synthetic.main.activity_time_choose.*


class ChooseTimeActivity : BaseActivity(), WaletContract.myaccountView, HomeContract.payView {

    override fun setFollow(msg: String) {

    }

    override fun myaccount(msg: MyAccount) {

    }

    override fun moniaccount(msg: MoniAccount) {
    }

    override fun coinList(msg: BindCoinHouse) {

    }

    @SuppressLint("SetTextI18n")
    override fun getPrice(msg: ChooseTime) {
        tv1.text = getString(R.string.money_format, msg.days30)
        tv2.text = getString(R.string.money_format, msg.days90)
        tv3.text = getString(R.string.money_format, msg.days180)
        tv4.text = getString(R.string.money_format, msg.days365)
    }

    private var mPayPresenter: PayPresenter = PayPresenter(this)

    override fun layoutId(): Int = R.layout.activity_time_choose
    private val rls by lazy { arrayOf(rl1, rl2, rl3, rl4) }
    override fun initData() {
        chose(rl1)
        rl1.setOnClickListener {
            currentDays = "30days"
            chose(it)
            tv_price.text = tv1.text
        }
        rl2.setOnClickListener {
            currentDays = "90days"
            chose(it)
            tv_price.text = tv2.text
        }
        rl3.setOnClickListener {
            currentDays = "180days"
            chose(it)
            tv_price.text = tv3.text
        }
        rl4.setOnClickListener {
            currentDays = "365days"
            chose(it)
            tv_price.text = tv4.text
        }
    }

    private fun chose(v: View) {
        for (rl in rls) {
            rl.isSelected = false
        }
        v.isSelected = true;
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
        tv_price.text = getString(R.string.money_format, price)
        tv_name.text = "${getString(R.string.subsciber)}/$currentNmae"
        mAccountPresenter.getPrice(id)

        tv_suscribe.setOnClickListener {
            val payBean = PayBean(id, bean?.exchange_id, switch, followType, followFactor, currentDays, beanSymbl!!.base_coin, beanSymbl!!.quote_coin)
            mPayPresenter.createId(role, payBean)

        }

        tv_agreement.setOnClickListener {
            TiaokuanActivity.start(this, 0)
        }
    }

    override fun payResult(msg: PayResultMine) {
    }

    override fun createId(msg: CreateId) {
        msg?.let {
            PayActivity.start(this, id, role, tv_price.text.toString(), msg.sub_id, msg.timer)
            finish()
        }

    }

    override fun payCancle(msg: String) {
    }

    override fun paySucceed(msg: PaySucceed) {
    }


}
