package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.Toolbar
import android.widget.TextView
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.TixianRecord
import com.hazz.aipick.mvp.model.bean.Walet
import com.hazz.aipick.mvp.presenter.WaletPresenter
import com.hazz.aipick.utils.SPUtil
import com.hazz.aipick.utils.ToastUtils
import com.hazz.aipick.utils.ToolBarCustom
import com.hazz.aipick.widget.paydialog.PayPassDialog
import com.hazz.aipick.widget.paydialog.PayPassView
import kotlinx.android.synthetic.main.activity_tixian.*
import kotlinx.android.synthetic.main.activity_tixian_record.toolbar


class TixianActivity : BaseActivity(), WaletContract.waletView {
    override fun tixianRecord(msg: List<TixianRecord>) {

    }

    override fun getWalet(msg: Walet) {
        tv_msg.text = getString(R.string.tixian_title, msg.cond.max, "${msg.cond.fee_rate}%", msg.cond.start, msg.cond.end)
        SPUtil.getUser()?.let {
            if (it.security.bind_bankcard) {
                tv_bank_card.text = "${msg.bankcard_list[0].bank}(${msg.bankcard_list[0].number})"
            } else {
                tv_bank_card.text = getString(R.string.not_bind_card)
                tv_bank_card.setOnClickListener { }
                startActivity(Intent(this, BindBankActivity::class.java))
            }
        }
        tv_fee.text = "手续费 ${msg.cond.fee_rate}%"
        tv_can_withdraw.text = "可提现余额 $${msg.withdrawable}"

    }

    override fun tixianSucceed(msg: String) {
        ToastUtils.showToast(this, msg)
        finish()
    }


    override fun layoutId(): Int = R.layout.activity_tixian


    override fun initData() {
        mWaletPresenter.waletDesc()
    }

    private var dialog = PayPassDialog()
    private var tips: TextView? = null
    private var mWaletPresenter: WaletPresenter = WaletPresenter(this)

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.tixian))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }
        tv_all.setOnClickListener {
            val substring = tv_can_withdraw.text.toString().substring(tv_can_withdraw.text.indexOf("$") + 1, tv_can_withdraw.text.length)
            et_amount.setText(substring)
        }

    }

    override fun start() {
        tv_confirm.setOnClickListener {
            payDialog()
        }
    }


    private fun payDialog() {
        dialog.init(this)
        dialog.payViewPass.setPayClickListener(object : PayPassView.OnPayClickListener {
            override fun onPassFinish(passContent: String?) {
                mWaletPresenter.tixian(et_amount.text.toString(), passContent!!)
            }

            override fun onPayClose() {
                dialog.dismiss()
            }


        })
        dialog.show()
        tips = dialog.payTips

    }

}
