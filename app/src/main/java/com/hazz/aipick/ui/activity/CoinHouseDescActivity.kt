package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.widget.CompoundButton
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.CoinHouse
import com.hazz.aipick.mvp.model.bean.CoinHouseDesc
import com.hazz.aipick.mvp.presenter.CoinHousePresenter
import com.hazz.aipick.showToast
import com.hazz.aipick.ui.adapter.CoinHouseCategryAdapter
import com.hazz.aipick.ui.adapter.OrderAdapter
import com.hazz.aipick.utils.ToolBarCustom
import com.hazz.aipick.widget.TipsDialog
import kotlinx.android.synthetic.main.activity_coin_house_desc.*
import kotlinx.android.synthetic.main.activity_subscribe_desc.toolbar
import java.util.ArrayList



class CoinHouseDescActivity : BaseActivity(), WaletContract.coinHouseView {

    override fun coinHouseList(msg: List<CoinHouse>) {

    }

    override fun addCoinHouseSucceed(msg: String) {
    }

    override fun coinHouseDesc(msg: CoinHouseDesc) {
        tv_amount.text=msg.total_value
        tv_week_rate.text=msg.week_rate
        tv_all_incoming.text=msg.gain
        tv_leiji.text=msg.rate

        mOrderAdapter!!.setNewData(msg.recommends)
    }


    override fun layoutId(): Int = R.layout.activity_coin_house_desc


    override fun initData() {
        val exchangeId = intent.getIntExtra("exchangeId",0)

        mCoinHousePresenter.coinHouseDesc(exchangeId)
    }

    private var mOrderAdapter: CoinHouseCategryAdapter? = null
    private val titleList = ArrayList<String>()
    private var mCoinHousePresenter: CoinHousePresenter = CoinHousePresenter(this)

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle("Binance")
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }
                .setOnRightClickListener {
                    startActivity(Intent(this,TixianRecordActivity::class.java))
                }

        titleList.add("正在持仓")
        titleList.add("历史持仓")

        recycleview.layoutManager = LinearLayoutManager(this)
        mOrderAdapter= CoinHouseCategryAdapter(R.layout.item_coin_house_categry,null)
        recycleview.adapter = mOrderAdapter
        mOrderAdapter!!.bindToRecyclerView(recycleview)
        mOrderAdapter!!.setEmptyView(R.layout.empty_view)

    }

    override fun start() {
        mine_incoming.setOnClickListener {
            startActivity(Intent(this,MyIncomingActivity::class.java))
        }
    }



}
