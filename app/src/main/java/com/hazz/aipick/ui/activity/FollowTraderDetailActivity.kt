package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.base.BaseFragment
import com.hazz.aipick.managers.GuideManager
import com.hazz.aipick.mvp.contract.CollectionContract
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.*
import com.hazz.aipick.mvp.model.bean.Collection
import com.hazz.aipick.mvp.presenter.AccountPresenter
import com.hazz.aipick.ui.adapter.CoinAdapter
import com.hazz.aipick.ui.fragment.OrderFragment
import com.hazz.aipick.ui.fragment.SubscribeFragment
import com.hazz.aipick.ui.fragment.TransactionAnalysisFragment
import com.hazz.aipick.utils.CoinManager
import com.hazz.aipick.utils.DpUtils
import com.hazz.aipick.utils.ToastUtils
import easily.tech.guideview.lib.GuideViewBundle
import kotlinx.android.synthetic.main.activity_follow_trader_detail.*
import kotlinx.android.synthetic.main.activity_my_account.guide_point
import kotlinx.android.synthetic.main.activity_my_account.iv_avatar
import kotlinx.android.synthetic.main.activity_my_account.iv_back
import kotlinx.android.synthetic.main.activity_my_account.ll_support_coins
import kotlinx.android.synthetic.main.activity_my_account.rg
import kotlinx.android.synthetic.main.activity_my_account.tv_fans
import kotlinx.android.synthetic.main.activity_my_account.tv_follow_incoming
import kotlinx.android.synthetic.main.activity_my_account.tv_guanzhu_num
import kotlinx.android.synthetic.main.activity_my_account.tv_renqi
import kotlinx.android.synthetic.main.activity_my_account.tv_rest_time
import kotlinx.android.synthetic.main.activity_my_account.tv_shouyi_rate
import kotlinx.android.synthetic.main.activity_my_account.tv_ten
import kotlinx.android.synthetic.main.activity_my_account.username
import kotlinx.android.synthetic.main.view_subscibe.*


class FollowTraderDetailActivity : BaseActivity(), WaletContract.myaccountView, CollectionContract.collectionView {

    companion object {

        fun start(context: Context, id: String, role: String, price: String) {
            val intent = Intent(context, FollowTraderDetailActivity::class.java)
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
            Glide.with(this).load(msg.avatar)
                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .into(iv_avatar)
        }
        coin = msg.coins
        mMyAccount = msg
        currentName = msg.nickname
        username.text = msg.nickname

        createSupportCoins(msg.coins)

        tv_follow_status.text = if (msg.is_following) getString(R.string.yiguanzhu) else getString(R.string.guanzhu)

        tv_fans.text = msg.fans.toString()
        tv_guanzhu_num.text = msg.follow.toString()
        tv_renqi.text = msg.pageview.toString()
        tv_shouyi_rate.text = "${msg.gain_rate}%"
        tv_follow_incoming.text = getString(R.string.money_format, msg.follow.toString())
        tv_ten.text = "${msg.pullback}%"

        tv_total_sub.text = "${msg.follow_times}"
        tv_trade_weeks.text = "${msg.trade_weeks}"

        //没有订阅
        canSub = msg.is_sub == 0
        tv_suscribe.isEnabled = canSub
        tv_rest_time.visibility = if (canSub) View.GONE else View.VISIBLE
        tv_rest_time.text = getString(R.string.text_rest_service_time, "${msg.end_time}")
    }

    private var canSub = false
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

    override fun layoutId(): Int = R.layout.activity_follow_trader_detail
    override fun initData() {


        mAccountPresenter.myAccount(id)

    }


    private var mAccountPresenter: AccountPresenter = AccountPresenter(this)
    private var id = "-1"
    private var role = ""
    private var price = ""
    private var mCoinAdapter: CoinAdapter? = null
    private var currentName = ""
    private var coin = ""
    private lateinit var mMyAccount: MyAccount
    private var mBindCoinHouse: MutableList<BindCoinHouse.SymbolsBean>? = mutableListOf()

    private fun getFragment(index: Int): BaseFragment {
        return when (index) {
            0 -> TransactionAnalysisFragment.getInstance(id, role, "0")
            1 -> OrderFragment.getInstance(id, canSub)
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
        tv_price.text = getString(R.string.money_format, "$price")
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

        tv_follow_status.setOnClickListener {
            if (mMyAccount.is_following) {
                mAccountPresenter.attentionCancle(id)
            } else {
                mAccountPresenter.attention(id)
            }
        }
    }


    override fun start() {

    }


}
