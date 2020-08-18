package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.widget.ImageView
import android.widget.LinearLayout
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
import com.hazz.aipick.ui.fragment.SubscribeFragment
import com.hazz.aipick.ui.fragment.TransactionAnalysisFragment
import com.hazz.aipick.utils.CoinManager
import com.hazz.aipick.utils.DpUtils
import com.hazz.aipick.utils.GsonUtil
import com.hazz.aipick.utils.ToastUtils
import com.hazz.aipick.widget.RecyclerViewSpacesItemDecoration
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
        mAccountPresenter.myAccount(id)
    }

    companion object {
        fun start(activity: Context, id: String, role: String, price: String) {
            val bundle = Bundle()
            bundle.putString("id", id)
            bundle.putString("role", role)
            bundle.putString("price", price)
            activity.startActivity(Intent(activity, RebotCategryActivity::class.java).putExtra("data", bundle))
        }
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
    private var price = ""

    private var mCoinAdapter: CoinAdapter? = null
    private var mCoinBiduiAdapter: CoinBiduiAdapter? = null
    private var mAccountPresenter: AccountPresenter = AccountPresenter(this)

    @SuppressLint("SetTextI18n")
    override fun initView() {
        id = intent.getBundleExtra("data").getString("id", "0")
        price = intent.getBundleExtra("data").getString("price", "0.01")
        if (intent.getBundleExtra("data").getString("role") != null) {
            role = intent.getBundleExtra("data").getString("role", "bot")
        }
        tv_price.text = "$$price"
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
                    transaction.replace(R.id.fl, OrderFragment.getInstance(id)).commitAllowingStateLoss()


                }
                R.id.rb3 -> {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fl, SubscribeFragment.getInstance(id)).commitAllowingStateLoss()
                }
            }


        }

        iv_back.setOnClickListener {
            finish()
        }

        tv_desc.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", id)
            bundle.putString("role", role)
            bundle.putString("title", username.text.toString())
            CategryDescActivity.start(this, bundle)
        }

        tv_suscribe.setOnClickListener {
            mAccountPresenter.coinList(id)
            showFirst()
        }
        ll_agree.setOnClickListener {
            TiaokuanActivity.start(this)
        }

        iv_follow_status.setOnClickListener {
            if (mMyAccount.is_following) {
                mAccountPresenter.attentionCancle(id)
            } else {
                mAccountPresenter.attention(id)
            }
        }
    }


    private fun showFirst() {

        // startActivity(Intent(this,PayActivity::class.java).putExtra("id",id).putExtra("price","0.01"))
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_coin, null)
        mAccountPresenter.coinList(id)
        view.recycleview1.layoutManager = GridLayoutManager(this, 4)
        view.iv_close.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
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

            if (mBindCoinHouse?.size == 0) {
                ToastUtils.showToast(this, getString(R.string.hint_coin_no_market_bind))
                return@setOnClickListener
            }
            current = mCoinAdapter?.getCurrent()
            if (current != null) {
                showNextBottom()
            }
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
        view.iv_close.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        view.iv_close.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
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
                        .putExtra("price", price)
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
        createSupportCoins(msg.coins)
        iv_follow_status.setImageResource(if (msg.is_following) R.drawable.ic_unfollow else R.drawable.ic_followed)
        currentName = msg.nickname
        username.text = msg.nickname
        tv_fans.text = msg.fans.toString()
        tv_renqi.text = msg.pageview.toString()

        tv_follow_benefit.text = msg.follow.toString()
        tv_total_benefit.text = msg.total.toString()
        tv_max_roll_back.text = msg.pullback
    }

    private var imageView: ImageView? = null

    //创建支持coin图标
    private fun createSupportCoins(coins: String) {
        val coinArray = coins.split(",")
        println(coins)
        ll_support_coins.removeAllViews()
        for (s in coinArray) {
            imageView = ImageView(this)
            var lp = LinearLayout.LayoutParams(DpUtils.dip2px(this, 20f), DpUtils.dip2px(this, 20f))
            lp.marginEnd = DpUtils.dip2px(this, 5f)
            imageView?.layoutParams = lp
            imageView?.setImageResource(CoinManager.getCoinIcon(s))
            imageView?.setBackgroundResource(R.drawable.bg_round_gray)
            ll_support_coins.addView(imageView)
        }
    }

    override fun coinList(msg: BindCoinHouse) {
        mCoinAdapter!!.setNewData(msg.exchanges)
        mBindCoinHouse = msg.symbols
    }

    override fun getPrice(msg: ChooseTime) {
        TODO("Not yet implemented")
    }

    override fun setFollow(msg: String) {
        ToastUtils.showToast(this, msg)
        mAccountPresenter.myAccount(id)
    }


}
