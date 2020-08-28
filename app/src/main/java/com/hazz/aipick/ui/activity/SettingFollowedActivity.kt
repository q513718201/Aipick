package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.RadioGroup
import com.google.gson.Gson
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.managers.GuideManager
import com.hazz.aipick.mvp.model.bean.BindCoinHouse
import com.hazz.aipick.utils.BigDecimalUtil
import com.hazz.aipick.utils.GsonUtil
import com.hazz.aipick.utils.ToastUtils
import com.hazz.aipick.utils.ToolBarCustom
import easily.tech.guideview.lib.GuideViewBundle
import kotlinx.android.synthetic.main.activity_setting_follow.*


class SettingFollowedActivity : BaseActivity(), RadioGroup.OnCheckedChangeListener {

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (followType) {
            "amount" -> {
                followType = "rate"
                var amount = tv_num.text.toString()
                setIntroduce(amount)
            }
            "rate" -> {
                followType = "amount"
                var rate = tv_num.text.toString()
                setIntroduce(rate)
            }
        }
    }

    private fun setIntroduce(text: String) {
        when (followType) {
            "amount" -> {
                tv_intro.text = getString(R.string.setting_follow_amount, text, text)
            }
            "rate" -> {
                tv_intro.text = getString(R.string.setting_follow_rate, text, text)
            }
        }
    }

    override fun layoutId(): Int = R.layout.activity_setting_follow

    override fun initData() {

    }

    private var id = ""
    private var price = ""
    private var bean: BindCoinHouse.ExchangesBean? = null
    private var beanSymbl: BindCoinHouse.SymbolsBean? = null
    private var currentNmae = ""
    private var currentSwitch = "on"
    private var followType = "rate"
    private var role = ""
    private var default_str = "0.01"

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.set_follow))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }

        id = intent.getStringExtra("id")
        price = intent.getStringExtra("price")
        bean = GsonUtil.toBean(intent.getStringExtra("bean"), BindCoinHouse.ExchangesBean::class.java)
        beanSymbl = GsonUtil.toBean(intent.getStringExtra("SymbolsBean"), BindCoinHouse.SymbolsBean::class.java)
        currentNmae = intent.getStringExtra("name")
        role = intent.getStringExtra("role")

        tv_suscribe.setOnClickListener {
            Log.d("junjun", Gson().toJson(beanSymbl))
            // mAccountPresenter.setFollow(id,currentSwitch,followType,tv_num.text.toString())
            startActivity(Intent(this, ChooseTimeActivity::class.java).putExtra("id", id).putExtra("price", price)
                    .putExtra("bean", bean).putExtra("name", currentNmae)
                    .putExtra("SymbolsBean", beanSymbl)
                    .putExtra("switch", currentSwitch)
                    .putExtra("followType", followType)
                    .putExtra("followFactor", tv_num.text.toString())
                    .putExtra("role", role))

            finish()
        }
        rg.setOnCheckedChangeListener(this)
        setIntroduce(default_str)
        tv_jian.setOnClickListener {
            if (tv_num.text.toString() == default_str) {
                ToastUtils.showToast(this, getString(R.string.zuixiao))
                return@setOnClickListener
            }
            tv_num.text = BigDecimalUtil.sub(tv_num.text.toString(), default_str, 2)
            setIntroduce(tv_num.text.toString())
        }


        tv_jia.setOnClickListener {
            tv_num.text = BigDecimalUtil.add(tv_num.text.toString(), default_str)
            setIntroduce(tv_num.text.toString())
        }

        switchButton.setOnCheckedChangeListener { _, isChecked ->
            currentSwitch = if (isChecked) {
                "on"
            } else {
                "off"
            }
        }
        switchButton.isChecked = true
    }

    override fun start() {
        GuideManager.showGuide(supportFragmentManager, guide_target, R.mipmap.guide_setting, GuideViewBundle.Direction.BOTTOM, "guide_setting")
    }


}
