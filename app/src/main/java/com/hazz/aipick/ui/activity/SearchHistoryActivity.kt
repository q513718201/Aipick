package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.model.bean.TraderSet
import com.hazz.aipick.mvp.presenter.TraderAuthPresenter
import com.hazz.aipick.ui.adapter.CoinListAdapter
import kotlinx.android.synthetic.main.activity_coin_search.*


class SearchHistoryActivity : BaseActivity(), LoginContract.tradeView {
    override fun traderSetSucceed(msg: String) {

    }

    override fun traderSetQuery(msg: TraderSet) {
    }


    override fun traderAuthSuccess(msg: String) {

    }

    override fun getCoin(msg: List<String>) {
        mCoinAdapter!!.setNewData(msg)
    }


    override fun layoutId(): Int = R.layout.activity_coin_search

    override fun initData() {
        mTraderAuthPresenter.getCoinList()
    }

    private var mCoinAdapter: CoinListAdapter? = null
    private var mTraderAuthPresenter: TraderAuthPresenter = TraderAuthPresenter(this)

    @SuppressLint("SetTextI18n")
    override fun initView() {
        recycleview.layoutManager = LinearLayoutManager(this)
        mCoinAdapter= CoinListAdapter(R.layout.item_coin_search,null)
        recycleview.adapter = mCoinAdapter
        mCoinAdapter!!.bindToRecyclerView(recycleview)
        mCoinAdapter!!.setEmptyView(R.layout.empty_view)
        mCoinAdapter!!.onConfirm={ view: View, s: String ->

            val intent = Intent()
            intent.putExtra("coinName", s)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun start() {
        tv_cancel.setOnClickListener {
            finish()
        }
    }




}
