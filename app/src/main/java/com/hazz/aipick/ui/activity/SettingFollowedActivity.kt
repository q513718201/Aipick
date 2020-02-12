package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.CompoundButton
import android.widget.RadioGroup
import com.google.gson.Gson
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.BindCoinHouse
import com.hazz.aipick.mvp.model.bean.ChooseTime
import com.hazz.aipick.mvp.model.bean.MyAccount
import com.hazz.aipick.mvp.presenter.AccountPresenter
import com.hazz.aipick.utils.BigDecimalUtil
import com.hazz.aipick.utils.ToastUtils

import com.hazz.aipick.utils.ToolBarCustom
import kotlinx.android.synthetic.main.activity_setting_follow.*


class SettingFollowedActivity : BaseActivity(), WaletContract.myaccountView, RadioGroup.OnCheckedChangeListener {
    override fun setFollow(msg: String) {
        ToastUtils.showToast(this,msg)
        startActivity(Intent(this,ChooseTimeActivity::class.java).putExtra("id",id).putExtra("price","0.01")
                .putExtra("bean",bean).putExtra("name",currentNmae).putExtra("switch",currentSwitch)
                .putExtra("followType",followType) .putExtra("followFactor",tv_num.text.toString())
                .putExtra("role",role)
        )
    }


    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {

        when(checkedId){
            R.id.rb1->{
                followType="amount"

            }
            R.id.rb2->{
                followType="rate"
            }
        }
    }

    override fun myaccount(msg: MyAccount) {

    }

    override fun coinList(msg: BindCoinHouse) {
    }

    override fun getPrice(msg: ChooseTime) {
    }


    override fun layoutId(): Int = R.layout.activity_setting_follow

    override fun initData() {

    }

    private var id = ""
    private var price = ""
    private var mAccountPresenter: AccountPresenter = AccountPresenter(this)
    private var bean: BindCoinHouse.ExchangesBean? = null
    private var beanSymbl: BindCoinHouse.SymbolsBean? = null
    private var currentNmae = ""
    private var currentSwitch="on"
    private var followType="amount"
    private var role = ""

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.set_follow))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }
    }

    override fun start() {
        id = intent.getStringExtra("id")
        price = intent.getStringExtra("price")
        bean = intent.getSerializableExtra("bean") as BindCoinHouse.ExchangesBean
        beanSymbl = intent.getSerializableExtra("SymbolsBean") as BindCoinHouse.SymbolsBean
        currentNmae = intent.getStringExtra("name")
        role= intent.getStringExtra("role")

        tv_suscribe.setOnClickListener {
            Log.d("junjun",Gson().toJson(beanSymbl))
           // mAccountPresenter.setFollow(id,currentSwitch,followType,tv_num.text.toString())
            startActivity(Intent(this,ChooseTimeActivity::class.java).putExtra("id",id).putExtra("price","0.01")
                    .putExtra("bean",bean).putExtra("name",currentNmae)
                    .putExtra("SymbolsBean",beanSymbl)
                    .putExtra("switch",currentSwitch)
                    .putExtra("followType",followType) .putExtra("followFactor",tv_num.text.toString())
                    .putExtra("role",role))

            finish()

        }
        rg.setOnCheckedChangeListener(this)

        tv_jian.setOnClickListener {
            if(tv_num.text.toString()=="0.01"){
                ToastUtils.showToast(this,getString(R.string.zuixiao))
                return@setOnClickListener
            }
            tv_num.text=BigDecimalUtil.sub(tv_num.text.toString(),"0.01",2)


        }


        tv_jia.setOnClickListener {
            tv_num.text=BigDecimalUtil.add(tv_num.text.toString(),"0.01")

        }

        sw.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                currentSwitch="on"
            }else{
                currentSwitch="off"
            }
        }
    }




}
