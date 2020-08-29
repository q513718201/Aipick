package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.widget.TextView
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.model.bean.SetTrade
import com.hazz.aipick.mvp.model.bean.TraderSet
import com.hazz.aipick.mvp.presenter.TraderAuthPresenter
import com.hazz.aipick.ui.adapter.TraderCoinAddAdapter
import com.hazz.aipick.utils.GsonUtil
import com.hazz.aipick.utils.RsaUtils
import com.hazz.aipick.utils.ToastUtils
import com.hazz.aipick.utils.ToolBarCustom
import com.hazz.aipick.widget.RecyclerViewSpacesItemDecoration
import com.hazz.aipick.widget.paydialog.PayPassDialog
import kotlinx.android.synthetic.main.activity_tixian_record.toolbar
import kotlinx.android.synthetic.main.trader_apply.*
import java.util.*


class ApplyTraderActivity : BaseActivity(), LoginContract.tradeView {

    companion object {
        fun start(context: Activity, trade: String) {
            context.startActivityForResult(Intent(context, ApplyTraderActivity::class.java).putExtra("trade", trade), 1)
        }
    }

    override fun traderSetSucceed(msg: String) {

    }

    override fun traderSetQuery(msg: TraderSet) {
    }

    override fun traderAuthSuccess(msg: String) {
        ToastUtils.showToast(this, msg)
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun getCoin(msg: List<String>) {

    }


    override fun layoutId(): Int = R.layout.trader_apply


    override fun initData() {
        mTraderAuthPresenter.getCoinList()
    }

    private var dialog = PayPassDialog()
    private var tips: TextView? = null
    private var setTrade: SetTrade? = null
    private var mTraderAuthPresenter: TraderAuthPresenter = TraderAuthPresenter(this)
    private var mCoinAdapter: TraderCoinAddAdapter? = null
    private val selectedCoins = ArrayList<String>()

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.trader_apply))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }
        var trade = intent.getStringExtra("trade")
        setTrade = GsonUtil.toBean(trade, SetTrade::class.java)
        recycleview.layoutManager = GridLayoutManager(this, 3)
        mCoinAdapter = TraderCoinAddAdapter(this, selectedCoins)
        recycleview.adapter = mCoinAdapter
        val stringIntegerHashMap: HashMap<String, Int>? = HashMap()
        stringIntegerHashMap?.put(RecyclerViewSpacesItemDecoration.RIGHT_DECORATION, 15)//右间距
        stringIntegerHashMap?.put(RecyclerViewSpacesItemDecoration.BOTTOM_DECORATION, 15)//右间距
        recycleview.addItemDecoration(RecyclerViewSpacesItemDecoration(stringIntegerHashMap))
        mCoinAdapter!!.setOnMyClick { position ->
            if (mCoinAdapter!!.getItemViewType(position) == 1) {
                startActivityForResult(Intent(this, SearchHistoryActivity::class.java), 0)
            }
        }
    }

    override fun start() {

        tv_confirm.setOnClickListener {
            if (TextUtils.isEmpty(et_huizhe.text.toString())) {
                ToastUtils.showToast(this, getString(R.string.please_huizhe))
                return@setOnClickListener
            }
//            if(selectedCoins.isEmpty()){
//                ToastUtils.showToast(this,getString(R.string.please_choose_coin))
//                return@setOnClickListener
//            }
            mTraderAuthPresenter.traderAuth(setTrade!!.countryCode, setTrade!!.code, setTrade!!.email,
                    setTrade!!.phone, setTrade!!.cardNumber, setTrade!!.name,
                    setTrade!!.cardType, RsaUtils.imageToBase64(setTrade!!.front)!!, RsaUtils.imageToBase64(setTrade!!.back)!!, et_huizhe.text.toString(), selectedCoins)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
            val coinName = data?.getStringExtra("coinName") ?: ""
            if (!selectedCoins.contains(coinName)) {
                selectedCoins.add(coinName.replace("/", "_"))
            }
            mCoinAdapter!!.notifyDataSetChanged()

        }
    }

}
