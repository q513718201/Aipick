package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.managers.GuideManager
import com.hazz.aipick.mvp.contract.CollectionContract
import com.hazz.aipick.mvp.contract.SimulateContract
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.*
import com.hazz.aipick.mvp.model.bean.Collection
import com.hazz.aipick.mvp.presenter.AccountPresenter
import com.hazz.aipick.mvp.presenter.CollectionPresenter
import com.hazz.aipick.mvp.presenter.RobotSubscribePresenter
import com.hazz.aipick.ui.adapter.CoinAdapter
import com.hazz.aipick.ui.adapter.CoinBiduiAdapter
import com.hazz.aipick.ui.fragment.OrderFragment
import com.hazz.aipick.ui.fragment.SubscribeFragment
import com.hazz.aipick.ui.fragment.TransactionAnalysisFragment
import com.hazz.aipick.utils.*
import com.hazz.aipick.widget.RecyclerViewSpacesItemDecoration
import com.werb.pickphotoview.adapter.SpaceItemDecoration
import easily.tech.guideview.lib.GuideViewBundle.Direction.LEFT
import easily.tech.guideview.lib.GuideViewBundle.Direction.TOP
import kotlinx.android.synthetic.main.activity_my_account.iv_back
import kotlinx.android.synthetic.main.activity_my_account.rg
import kotlinx.android.synthetic.main.activity_my_account.tv_fans
import kotlinx.android.synthetic.main.activity_my_account.tv_renqi
import kotlinx.android.synthetic.main.activity_my_account.username
import kotlinx.android.synthetic.main.activity_robot_categry.*
import kotlinx.android.synthetic.main.activity_robot_categry.guide_point
import kotlinx.android.synthetic.main.activity_robot_categry.ll_support_coins
import kotlinx.android.synthetic.main.activity_robot_categry.tv_rest_time
import kotlinx.android.synthetic.main.dialog_coin.view.*
import kotlinx.android.synthetic.main.view_subscibe.*

/**
 * 订阅机器人
 */
class FollowRobotDetailActivity : BaseActivity(), WaletContract.myaccountView, SimulateContract.SubscribeView, CollectionContract.collectionView {


    override fun layoutId(): Int = R.layout.activity_robot_categry

    override fun initData() {
        if (id == "-1") {
            mAccountPresenter.moniAccount()
        } else {
            mAccountPresenter.myAccount(id)
        }
    }

    companion object {
        fun start(activity: Context, id: String, role: String, price: String) {
            val bundle = Bundle()
            bundle.putString("id", id)
            bundle.putString("role", role)
            bundle.putString("price", price)
            activity.startActivity(Intent(activity, FollowRobotDetailActivity::class.java).putExtra("data", bundle))
        }
    }


    private lateinit var currentName: String
    private lateinit var mMyAccount: MyAccount
    private var coin: String? = null
    private var baseCoin: BindCoinHouse.SymbolsBean? = null
    private var id = ""
    private var role = ""
    private var price = ""
    private var name = ""

    private var mCoinAdapter: CoinAdapter? = null
    private var mCoinBiduiAdapter: CoinBiduiAdapter? = null
    private var mAccountPresenter: AccountPresenter = AccountPresenter(this)
    private var mRobotSubscribePresenter: RobotSubscribePresenter = RobotSubscribePresenter(this)
    private var mCollectionPresenter: CollectionPresenter = CollectionPresenter(this)

    @SuppressLint("SetTextI18n")
    override fun initView() {
        id = intent.getBundleExtra("data").getString("id", "-1")
        name = intent.getBundleExtra("data").getString("name", "0")
        price = intent.getBundleExtra("data").getString("price", "0")
        role = intent.getBundleExtra("data").getString("role", "")

        initStage()


        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl, TransactionAnalysisFragment.getInstance(id, role)).commitAllowingStateLoss()
        rg.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                R.id.rb1 -> {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fl, TransactionAnalysisFragment.getInstance(id, role)).commitAllowingStateLoss()
                }
                R.id.rb2 -> {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fl, OrderFragment.getInstance(id, canSub)).commitAllowingStateLoss()
                    GuideManager.showGuide(supportFragmentManager, guide_point,
                            View.inflate(this, R.layout.guide_order, null),
                            View.inflate(this, R.layout.guide_order_detail, null)
                            , TOP, "guide_order", "guide_order_detail")

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
            bundle.putString("price", price)
            bundle.putBoolean("isSub", canSub)
            bundle.putString("title", username.text.toString())
            CategryDescActivity.start(this, bundle)
        }


        tv_suscribe.setOnClickListener {
            if (id == "-1") {
                mRobotSubscribePresenter.subscribe()

            } else {
                mAccountPresenter.coinList(id)
                showFirst()
            }
        }
        ll_agree.setOnClickListener {
            TiaokuanActivity.start(this, 0)
        }

        iv_follow_status.setOnClickListener {
            if (!mMyAccount.in_collection) {//没有收藏 添加收藏，有收藏 删除收藏
                mCollectionPresenter.addCollection(role, id, "", "")
            } else {
                mCollectionPresenter.cancelCollection(role, id, "", "")
            }
        }
    }

    @SuppressLint("StringFormatInvalid")
    private fun initStage() {
        when (id) {
            "-1" -> {
                tv_price_name.visibility = View.GONE
                ll_agree.visibility = View.GONE
                llFans.visibility = View.INVISIBLE
                tv_price.text = getString(R.string.text_price_free)
                iv_follow_status.visibility = View.GONE
            }
            else -> {
                tv_price_name.visibility = View.VISIBLE
                ll_agree.visibility = View.VISIBLE
                llFans.visibility = View.VISIBLE
                iv_follow_status.visibility = View.VISIBLE
                tv_price.text = getString(R.string.money_format, "$price")
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
        mCoinAdapter = CoinAdapter(null)
        view.recycleview1.adapter = mCoinAdapter

        view.recycleview1.addItemDecoration(SpaceItemDecoration(DpUtils.dip2px(this, 10f), 4))

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
        view.recycleview1.addItemDecoration(SpaceItemDecoration(DpUtils.dip2px(this, 10f), 4))
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
        GuideManager.showGuide(supportFragmentManager, tv_suscribe, R.mipmap.guide_subscribe, LEFT, "guide_subscribe")
    }

    private var canSub = false

    @SuppressLint("StringFormatMatches", "SetTextI18n")
    override fun myaccount(msg: MyAccount) {
        coin = msg.coins
        mMyAccount = msg
        createSupportCoins(msg.coins)
        iv_follow_status.setImageResource(if (msg.in_collection) R.drawable.ic_followed else R.drawable.ic_unfollow)
        currentName = msg.nickname
        username.text = msg.nickname
        tv_fans.text = msg.fans.toString()
        tv_renqi.text = msg.pageview.toString()
        tv_total_follows.text = "${msg.follow_times}"
        tv_follow_benefit.text = getString(R.string.money_format_us, msg.follow)
        tv_total_benefit.text = "${BigDecimalUtil.format(msg.total.toString(), 2)}%"
        tv_max_roll_back.text = "${BigDecimalUtil.format(msg.pullback, 2)}%"
        canSub = msg.is_sub == 0
        tv_suscribe.isEnabled = canSub//是否订阅
        tv_rest_time.visibility = if (msg.end_time.toDouble() > 0) View.VISIBLE else View.GONE
        tv_rest_time.text = getString(R.string.text_rest_service_time, "${msg.end_time}")
        iv_follow_status.visibility = if (msg.is_self) View.GONE else View.VISIBLE //自己不可以订阅自己

    }

    @SuppressLint("StringFormatMatches")
    override fun moniaccount(msg: MoniAccount) {
        currentName = msg.nickname
        username.text = msg.nickname
        tv_total_follows.text = msg.total_subs

        tv_follow_benefit.text = getString(R.string.money_format_us, msg.follow_gain)
        tv_total_benefit.text = "${BigDecimalUtil.format(msg.gain_rate, 2)}%"
        tv_max_roll_back.text = "${BigDecimalUtil.format(msg.pullback, 2)}%"

        canSub = msg.is_sub == 0 && msg.end_time.toDouble() >= 0
        tv_suscribe.isEnabled = canSub//1 已订阅 0 未订阅
        tv_rest_time.visibility = if (msg.end_time.toDouble() > 0) View.VISIBLE else View.GONE
        tv_rest_time.text = getString(R.string.text_rest_service_time, "${msg.end_time}")
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

    override fun subscribeSuccess(bean: SubscribeBean) {
        SubscribeSuccessActivity.start(this, bean.nickname, SPUtil.getUser().nickname, bean.end_time)
    }

    override fun getCollection(msg: List<Collection>) {
        TODO("Not yet implemented")
    }

    override fun optionResult(msg: String) {
        ToastUtils.showToast(this, msg)
        mAccountPresenter.myAccount(id)
    }
}
