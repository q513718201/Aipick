package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.CoinHouse
import com.hazz.aipick.mvp.model.bean.CoinHouseDesc
import com.hazz.aipick.mvp.presenter.CoinHousePresenter
import com.hazz.aipick.ui.adapter.CoinHouseCategryAdapter
import com.hazz.aipick.utils.BigDecimalUtil
import com.hazz.aipick.utils.ToolBarCustom
import kotlinx.android.synthetic.main.activity_coin_house_desc.*
import kotlinx.android.synthetic.main.activity_subscribe_desc.toolbar


class CoinHouseDescActivity : BaseActivity(), WaletContract.coinHouseView {

    override fun coinHouseList(msg: List<CoinHouse>) {

    }

    override fun addCoinHouseSucceed(msg: String) {
    }

    override fun coinHouseDesc(msg: CoinHouseDesc) {
        tv_amount.text = getString(R.string.money_format_us, msg.total_value)
        tv_week_rate.text = "${BigDecimalUtil.format(msg.week_rate, 2)}%"
        tv_data1_value.text = getString(R.string.money_format_us, "${BigDecimalUtil.format(msg.gain, 2)}")
        tv_leiji.text = "${BigDecimalUtil.format(msg.rate, 2)}%"

        mOrderAdapter!!.setNewData(msg.recommends)
    }


    override fun layoutId(): Int = R.layout.activity_coin_house_desc


    override fun initData() {
        val exchangeId = intent.getIntExtra("exchangeId", 0)

        mCoinHousePresenter.coinHouseDesc(exchangeId)
    }

    private var mOrderAdapter: CoinHouseCategryAdapter? = null
    private var mCoinHousePresenter: CoinHousePresenter = CoinHousePresenter(this)

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(intent.getStringExtra("title"))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }
                .setOnRightClickListener {
                    startActivity(Intent(this, TixianRecordActivity::class.java))
                }

        recycleview.layoutManager = LinearLayoutManager(this)
        mOrderAdapter = CoinHouseCategryAdapter(R.layout.item_coin_house_categry, null)
        recycleview.adapter = mOrderAdapter
        mOrderAdapter!!.bindToRecyclerView(recycleview)
        mOrderAdapter!!.setEmptyView(R.layout.empty_view)

    }

    override fun start() {
//        mine_incoming.setOnClickListener {
//            startActivity(Intent(this, MyIncomingActivity::class.java))
//        }
    }


}
