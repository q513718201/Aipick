package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.bigkoo.alertview.AlertView
import com.bigkoo.alertview.OnItemClickListener
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.base.BaseFragment
import com.hazz.aipick.events.ChangeEvent
import com.hazz.aipick.managers.GuideManager
import com.hazz.aipick.mvp.contract.CollectionContract
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.*
import com.hazz.aipick.mvp.model.bean.Collection
import com.hazz.aipick.mvp.presenter.AccountPresenter
import com.hazz.aipick.ui.adapter.CoinAdapter
import com.hazz.aipick.ui.adapter.CoinBiduiAdapter
import com.hazz.aipick.ui.fragment.OrderFragment
import com.hazz.aipick.ui.fragment.SubscribeFragment
import com.hazz.aipick.ui.fragment.TransactionAnalysisFragment
import com.hazz.aipick.utils.*
import easily.tech.guideview.lib.GuideViewBundle
import kotlinx.android.synthetic.main.activity_my_account.*


class MyAccountActivity : BaseActivity(), WaletContract.myaccountView, CollectionContract.collectionView, OnItemClickListener {

    companion object {
        /**
         * form 从哪里来 home 来自主页，mine 个人中心
         */
        fun start(context: Context, id: String, role: String, price: String) {
            val intent = Intent(context, MyAccountActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("role", role)
            intent.putExtra("price", price)
            context.startActivity(intent)
        }
    }

    override fun getCollection(msg: List<Collection>) {

    }

    override fun optionResult(msg: String) {
        ToastUtils.showToast(this, msg)
    }

    override fun setFollow(msg: String) {
        mAccountPresenter.myAccount(id)

        ToastUtils.showToast(this, msg)
    }

    override fun getPrice(msg: ChooseTime) {

    }

    override fun coinList(msg: BindCoinHouse) {
        mCoinAdapter!!.setNewData(msg.exchanges)
        mBindCoinHouse = msg.symbols
    }


    @SuppressLint("SetTextI18n", "StringFormatMatches")
    override fun myaccount(msg: MyAccount) {
        if (!TextUtils.isEmpty(msg.avatar)) {
            GlideUtil.showRound(msg.avatar, iv_avatar, R.mipmap.ic_user)
        }
        coin = msg.coins
        mMyAccount = msg
        currentName = msg.nickname
        username.text = msg.nickname

        createSupportCoins(msg.coins)


        tv_fans.text = msg.fans.toString()
        tv_guanzhu_num.text = msg.follow.toString()
        tv_renqi.text = msg.pageview.toString()
        tv_shouyi_rate.text = "${msg.gain_rate}%"
        tv_follow_incoming.text = getString(R.string.money_format_us, msg.follow.toString())
        tv_ten.text = "${msg.pullback}%"

        tv_stage.text = "$currentName(跟随者)"
        tv_total_benefit.text = getString(R.string.money_format_us, msg.total)
        tv_self_benefit.text = getString(R.string.money_format_us, msg.self)
    }

    override fun moniaccount(msg: MoniAccount) {
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

    override fun layoutId(): Int = R.layout.activity_my_account
    override fun initData() {
        mAccountPresenter.myAccount(id)
    }


    private var mAccountPresenter: AccountPresenter = AccountPresenter(this)
    private var id = "-1"
    private var role = ""
    private var price = ""
    private var mCoinAdapter: CoinAdapter? = null
    private var mCoinBiduiAdapter: CoinBiduiAdapter? = null
    private var currentName = ""
    private var coin = ""
    private lateinit var mMyAccount: MyAccount
    private var mBindCoinHouse: MutableList<BindCoinHouse.SymbolsBean>? = mutableListOf()
    private var current: BindCoinHouse.ExchangesBean? = null
    private var baseCoin: BindCoinHouse.SymbolsBean? = null

    private fun getFragment(index: Int): BaseFragment {
        return when (index) {
            0 -> TransactionAnalysisFragment.getInstance(id, role, isDemo.toString())
            1 -> OrderFragment.getInstance(id, false)
            else -> SubscribeFragment.getInstance(id)
        }
    }

    private var pos = 0

    @SuppressLint("SetTextI18n")
    override fun initView() {
        if (intent.getStringExtra("id") != null)
            id = intent.getStringExtra("id")
        role = intent.getStringExtra("role")
        price = intent.getStringExtra("price")

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl, getFragment(0)).commitAllowingStateLoss()
        rg.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {
                R.id.rb1 -> {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fl, getFragment(0)).commitAllowingStateLoss()
                    pos = 0
                }
                R.id.rb2 -> {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fl, getFragment(1)).commitAllowingStateLoss()
                    pos = 1
                    GuideManager.showGuide(supportFragmentManager, guide_point,
                            View.inflate(this, R.layout.guide_order, null),
                            View.inflate(this, R.layout.guide_order_detail, null)
                            , GuideViewBundle.Direction.TOP, "guide_order", "guide_order_detail")
                }
                R.id.rb3 -> {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fl, getFragment(2)).commitAllowingStateLoss()
                    pos = 2
                }
            }


        }

        iv_back.setOnClickListener {
            finish()
        }

        tv_stage.setOnClickListener {
            AlertView.Builder()
                    .setContext(this)
                    .setStyle(AlertView.Style.ActionSheet)
                    .setDestructive("模拟账户(跟随者)", "$currentName(跟随者)")
                    .setCancelText(getString(R.string.cancel))
                    .setOnItemClickListener(this)
                    .build()
                    .setCancelable(true)
                    .show()
        }

    }


    override fun start() {

    }


    @SuppressLint("SetTextI18n")
    override fun onItemClick(o: Any?, position: Int) {
        when (position) {
            1 -> {
                isDemo = 0
                tv_stage.text = "$currentName(跟随者)"
            }
            0 -> {
                isDemo = 1
                tv_stage.text = "模拟账户(跟随者)"
            }
        }
        RxBus.get().send(ChangeEvent(isDemo))
    }

    var isDemo = 0

}
