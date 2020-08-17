package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.BindCoinHouse
import com.hazz.aipick.mvp.model.bean.ChooseTime
import com.hazz.aipick.mvp.model.bean.MyAccount
import com.hazz.aipick.mvp.presenter.AccountPresenter
import com.hazz.aipick.ui.adapter.CoinAdapter
import com.hazz.aipick.ui.adapter.CoinBiduiAdapter
import com.hazz.aipick.ui.fragment.OrderFragment
import com.hazz.aipick.ui.fragment.TransactionAnalysisFragment
import com.hazz.aipick.utils.GsonUtil
import com.hazz.aipick.utils.ToastUtils
import com.hazz.aipick.widget.RecyclerViewSpacesItemDecoration
import kotlinx.android.synthetic.main.activity_my_account.*
import kotlinx.android.synthetic.main.activity_my_account.iv_back
import kotlinx.android.synthetic.main.activity_my_account.rg
import kotlinx.android.synthetic.main.activity_my_account.tv_fans
import kotlinx.android.synthetic.main.activity_my_account.tv_renqi
import kotlinx.android.synthetic.main.activity_my_account.tv_suscribe
import kotlinx.android.synthetic.main.activity_my_account.username
import kotlinx.android.synthetic.main.activity_robot_categry.*
import kotlinx.android.synthetic.main.dialog_coin.view.*
import kotlinx.android.synthetic.main.view_subscibe.*


class RebotCategryActivity : BaseActivity(), WaletContract.myaccountView {


    override fun layoutId(): Int = R.layout.activity_robot_categry

    override fun initData() {

    }


    private var currentName: String? = null
    private lateinit var mMyAccount: MyAccount
    private var coin: String? = null
    private var baseCoin: BindCoinHouse.SymbolsBean? = null
    private var mTransactionAnalysisFragment: TransactionAnalysisFragment? = null
    private var mOrderFragment: OrderFragment? = null
    private var mLastFragment: Fragment? = null
    private var id = ""
    private var role = ""

    private var mCoinAdapter: CoinAdapter? = null
    private var mCoinBiduiAdapter: CoinBiduiAdapter? = null
    private var mAccountPresenter: AccountPresenter = AccountPresenter(this)

    @SuppressLint("SetTextI18n")
    override fun initView() {
        id = intent.getStringExtra("id")
        if (intent.getStringExtra("role") != null) {
            role = intent.getStringExtra("role")
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl, TransactionAnalysisFragment()).commitAllowingStateLoss()
        rg.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                R.id.rb1 -> {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fl, TransactionAnalysisFragment()).commitAllowingStateLoss()
                }
                R.id.rb2 -> {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fl, OrderFragment()).commitAllowingStateLoss()


                }
                R.id.rb3 -> {

                }
            }


        }

        iv_back.setOnClickListener {
            finish()
        }

        tv_desc.setOnClickListener {
            startActivity(Intent(this, CategryDescActivity::class.java))
        }

        tv_suscribe.setOnClickListener {
            mAccountPresenter.coinList(id)
            showFirst()
        }
        ll_agree.setOnClickListener {
            TiaokuanActivity.start(this)
        }

    }


    private fun showFirst() {

        // startActivity(Intent(this,PayActivity::class.java).putExtra("id",id).putExtra("price","0.01"))
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_coin, null)
        mAccountPresenter.coinList(id)
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
                baseCoin = mBindCoinHouse!![mCoinAdapter!!.getCurr()]
                startActivity(Intent(this, SettingFollowedActivity::class.java)
                        .putExtra("id", id)
                        .putExtra("price", "0.01")
                        .putExtra("bean", GsonUtil.toJson(current))
                        .putExtra("name", currentName)
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

    override fun start() {

    }

    override fun myaccount(msg: MyAccount) {

        coin = msg.coins
        mMyAccount = msg
        currentName = msg.nickname
        username.text = msg.nickname
        if (msg.is_following) {
            tv_yiguanzhu.visibility = View.VISIBLE
            tv_guanzhu.visibility = View.GONE
        }
        tv_fans.text = msg.fans.toString()
        tv_guanzhu_num.text = msg.follow.toString()
        tv_renqi.text = msg.pageview.toString()
        tv_shouyi_rate.text = msg.gain_rate
        tv_follow_incoming.text = msg.follow.toString()
        tv_all_incoming.text = msg.total.toString()
        tv_ten.text = msg.pullback
        tv_shouyi.text = msg.self
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
