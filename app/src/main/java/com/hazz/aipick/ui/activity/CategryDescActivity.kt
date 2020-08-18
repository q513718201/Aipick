package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.GridLayoutManager
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.BindCoinHouse
import com.hazz.aipick.mvp.model.bean.ChooseTime
import com.hazz.aipick.mvp.model.bean.MyAccount
import com.hazz.aipick.mvp.presenter.AccountPresenter
import com.hazz.aipick.ui.adapter.CoinAdapter
import com.hazz.aipick.ui.adapter.CoinBiduiAdapter
import com.hazz.aipick.utils.GsonUtil
import com.hazz.aipick.utils.ToastUtils
import com.hazz.aipick.widget.RecyclerViewSpacesItemDecoration
import kotlinx.android.synthetic.main.activity_categry_desc.*
import kotlinx.android.synthetic.main.dialog_coin.view.*
import kotlinx.android.synthetic.main.view_subscibe.*


class CategryDescActivity : BaseActivity(), WaletContract.myaccountView {
    override fun initData() {
        mAccountPresenter.myAccount(id)
    }

    companion object {
        fun start(activity: Activity, bundle: Bundle) {
            val intent = Intent(activity, CategryDescActivity::class.java)

            intent.putExtra("data", bundle)
            activity.startActivity(intent)
        }
    }

    override fun layoutId(): Int = R.layout.activity_categry_desc

    private var role: String = ""
    private var id = ""
    private var title = ""
    private var mCoinAdapter: CoinAdapter? = null
    private var mCoinBiduiAdapter: CoinBiduiAdapter? = null
    private var mAccountPresenter: AccountPresenter = AccountPresenter(this)

    @SuppressLint("SetTextI18n")
    override fun initView() {
        if (intent != null) {
            id = intent.getStringExtra("id")
            role = intent.getStringExtra("role")
            title = intent.getStringExtra("title")
        }
        iv_back.setOnClickListener {
            finish()
        }
        tv_suscribe.setOnClickListener {
            mAccountPresenter.coinList(id)
            showFirst()
        }
    }

    override fun start() {

    }

    private fun showFirst() {

        // startActivity(Intent(this,PayActivity::class.java).putExtra("id",id).putExtra("price","0.01"))
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_coin, null)
        view.recycleview1.layoutManager = GridLayoutManager(this, 4)

        mCoinAdapter = CoinAdapter(R.layout.item_text, null)
        view.recycleview1.adapter = mCoinAdapter
        val stringIntegerHashMap: HashMap<String, Int>? = HashMap()
        stringIntegerHashMap?.put(RecyclerViewSpacesItemDecoration.BOTTOM_DECORATION, 15)//右间距
        view.recycleview1.addItemDecoration(RecyclerViewSpacesItemDecoration(stringIntegerHashMap))
        mCoinAdapter!!.bindToRecyclerView(view.recycleview1)
        mCoinAdapter!!.setEmptyView(R.layout.empty_view_coin)
        mCoinAdapter!!.emptyView.setOnClickListener {

            startActivity(Intent(this, CoinHouseActivity::class.java))
            bottomSheetDialog!!.dismiss()
        }

        view.tv_sure.setOnClickListener {

            showNextBottom()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog!!.setContentView(view)
        bottomSheetDialog.show()
    }

    private var mBindCoinHouse: MutableList<BindCoinHouse.SymbolsBean>? = mutableListOf()
    private var current: BindCoinHouse.ExchangesBean? = null
    private fun showNextBottom() {
        // startActivity(Intent(this,PayActivity::class.java).putExtra("id",id).putExtra("price","0.01"))
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_coin, null)

        view.recycleview1.layoutManager = GridLayoutManager(this, 4)
        view.tv_title.text = getString(R.string.choose_bidui)
        view.tv_sure.text = getString(R.string.confirm)
        mCoinBiduiAdapter = CoinBiduiAdapter(R.layout.item_text, mBindCoinHouse)
        view.recycleview1.adapter = mCoinBiduiAdapter
        val stringIntegerHashMap: HashMap<String, Int>? = HashMap()
        stringIntegerHashMap?.put(RecyclerViewSpacesItemDecoration.BOTTOM_DECORATION, 15)//右间距
        view.recycleview1.addItemDecoration(RecyclerViewSpacesItemDecoration(stringIntegerHashMap))
        view.tv_sure.setOnClickListener {

            if (mBindCoinHouse != null) {
                var baseCoin = mBindCoinHouse!![mCoinAdapter!!.getCurr()]
                startActivity(Intent(this, SettingFollowedActivity::class.java)
                        .putExtra("id", id)
                        .putExtra("price", "0.01")
                        .putExtra("bean", GsonUtil.toJson(current))
                        .putExtra("name", title)
                        .putExtra("SymbolsBean", GsonUtil.toJson(baseCoin))
                        .putExtra("role", role)
                )
                bottomSheetDialog.dismiss()

            } else {
                ToastUtils.showToast(this, "请选择投放平台")
                return@setOnClickListener
            }

        }

        bottomSheetDialog!!.setContentView(view)
        bottomSheetDialog.show()
    }

    override fun myaccount(msg: MyAccount) {
        TODO("Not yet implemented")
    }

    override fun coinList(msg: BindCoinHouse) {
        mCoinAdapter!!.setNewData(msg.exchanges)
        mBindCoinHouse = msg.symbols
    }

    override fun getPrice(msg: ChooseTime) {
        TODO("Not yet implemented")
    }

    override fun setFollow(msg: String) {
        TODO("Not yet implemented")
    }
}
