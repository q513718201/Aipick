package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.CompoundButton
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.CoinHouse
import com.hazz.aipick.mvp.model.bean.CoinHouseDesc
import com.hazz.aipick.mvp.presenter.CoinHousePresenter
import com.hazz.aipick.showToast
import com.hazz.aipick.ui.adapter.CoinHouseAdapter
import com.hazz.aipick.ui.adapter.OrderAdapter
import com.hazz.aipick.utils.DisplayManager
import com.hazz.aipick.utils.ToastUtils
import com.hazz.aipick.utils.ToolBarCustom
import com.hazz.aipick.widget.DialogInsertCoinHouse
import com.hazz.aipick.widget.RecyclerViewSpacesItemDecoration
import com.hazz.aipick.widget.Tools.DialogMishiCoinHouse
import kotlinx.android.synthetic.main.activity_coin_house.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.ArrayList



class CoinHouseActivity : BaseActivity(), WaletContract.coinHouseView {

    override fun coinHouseDesc(msg: CoinHouseDesc) {

    }

    override fun addCoinHouseSucceed(msg: String) {
        ToastUtils.showToast(this,msg)
        mCoinHousePresenter.coinHouseList()
    }


    override fun coinHouseList(msg: List<CoinHouse>) {
        mOrderAdapter!!.setNewData(msg)
    }


    override fun layoutId(): Int = R.layout.activity_coin_house


    override fun initData() {
        mCoinHousePresenter.coinHouseList()
    }

    private var mOrderAdapter: CoinHouseAdapter? = null
    private val titleList = ArrayList<String>(10)
    private var mCoinHousePresenter: CoinHousePresenter =CoinHousePresenter(this)

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.bind_coin_house))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }


        recycleview.layoutManager = GridLayoutManager(this,2)
        mOrderAdapter= CoinHouseAdapter(R.layout.item_coin_house,null)
        val stringIntegerHashMap:HashMap<String,Int>?= HashMap()
        stringIntegerHashMap?.put(RecyclerViewSpacesItemDecoration.LEFT_DECORATION, DisplayManager.dip2px(20.0f)!!)//右间距
        stringIntegerHashMap?.put(RecyclerViewSpacesItemDecoration.TOP_DECORATION, DisplayManager.dip2px(20.0f)!!)//右间距
        recycleview.addItemDecoration(RecyclerViewSpacesItemDecoration(stringIntegerHashMap))
        recycleview.adapter = mOrderAdapter
        mOrderAdapter!!.bindToRecyclerView(recycleview)
        mOrderAdapter!!.setEmptyView(R.layout.empty_view)
        mOrderAdapter!!.onConfirm={ view: View, i: Int ->
            val dialogInsertCoinHouse = DialogMishiCoinHouse(this)
            dialogInsertCoinHouse.onConfirm={ s: String, s1: String, s2: String ->
                mCoinHousePresenter.bindCoinHouse(i.toString(),s,s1,s2)
            }
            dialogInsertCoinHouse.show()
        }

    }

    override fun start() {
        tv_add.setOnClickListener {
            val dialogInsertCoinHouse = DialogInsertCoinHouse(this)
            dialogInsertCoinHouse.onConfirm={ s: String, s1: String, s2: String ->
                mCoinHousePresenter.addCoinHouse(s,s1,s2)
            }
            dialogInsertCoinHouse.show()
        }
    }



}
