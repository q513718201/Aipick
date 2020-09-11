package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.SimulateContract
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.BindCoinHouse
import com.hazz.aipick.mvp.model.bean.CategoryDetailBean
import com.hazz.aipick.mvp.model.bean.UserInfo
import com.hazz.aipick.mvp.presenter.CategoryDetailPresenter
import com.hazz.aipick.mvp.presenter.CoinPresenter
import com.hazz.aipick.ui.adapter.CoinAdapter
import com.hazz.aipick.ui.adapter.CoinBiduiAdapter
import com.hazz.aipick.utils.DpUtils
import com.hazz.aipick.utils.GsonUtil
import com.hazz.aipick.utils.SPUtil
import com.hazz.aipick.utils.ToastUtils
import com.hazz.aipick.widget.RecyclerViewSpacesItemDecoration
import com.vinsonguo.klinelib.util.DateUtils
import com.werb.pickphotoview.adapter.SpaceItemDecoration
import kotlinx.android.synthetic.main.activity_categry_desc.*
import kotlinx.android.synthetic.main.activity_categry_desc.iv_back
import kotlinx.android.synthetic.main.activity_robot_categry.*
import kotlinx.android.synthetic.main.dialog_coin.view.*
import kotlinx.android.synthetic.main.view_subscibe.*


class CategryDescActivity : BaseActivity(), WaletContract.CoinView, SimulateContract.CategoryDetailView {
    override fun initData() {
        mCategoryDetailPresenter.getDetail(id)
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
    private var price = ""
    private var isSub = false
    private var mCoinAdapter: CoinAdapter? = null
    private var mCoinBiduiAdapter: CoinBiduiAdapter? = null
    private var mCoinPresenter: CoinPresenter = CoinPresenter(this)
    private var mCategoryDetailPresenter: CategoryDetailPresenter = CategoryDetailPresenter(this)

    @SuppressLint("SetTextI18n")
    override fun initView() {
        if (intent != null) {
            id = intent.getBundleExtra("data").getString("id", "-1")
            role = intent.getBundleExtra("data").getString("role", "")
            title = intent.getBundleExtra("data").getString("title", "")
            price = intent.getBundleExtra("data").getString("price", "")
            isSub = intent.getBundleExtra("data").getBoolean("isSub", false)
        }
        tv_suscribe.isEnabled = isSub
        tv_subject_name.text = title
        iv_back.setOnClickListener {
            finish()
        }
        tv_suscribe.setOnClickListener {
            if (id == "-1") {
                val obj = SPUtil.getObj("userinfo", UserInfo::class.java)
                SubscribeSuccessActivity.start(this, title, obj.nickname, DateUtils.getDay(30))
            } else {
                mCoinPresenter.coinList(id)
                showFirst()
            }
        }
        initStage()
    }

    @SuppressLint("StringFormatInvalid")
    private fun initStage() {
        when (id) {
            "-1" -> {
                tv_price_name.visibility = View.GONE
                ll_agree.visibility = View.GONE
                llFans?.visibility = View.INVISIBLE
                tv_price.text = getString(R.string.text_price_free)
                iv_follow_status?.visibility = View.GONE
            }
            else -> {
                tv_price_name.visibility = View.VISIBLE
                ll_agree.visibility = View.VISIBLE
                llFans?.visibility = View.VISIBLE
                iv_follow_status?.visibility = View.VISIBLE
                tv_price.text = getString(R.string.money_format, "$price")
            }
        }
    }

    override fun start() {

    }

    private fun showFirst() {

        // startActivity(Intent(this,PayActivity::class.java).putExtra("id",id).putExtra("price","0.01"))
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_coin, null)
        view.recycleview1.layoutManager = GridLayoutManager(this, 4)

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

    override fun coinList(msg: BindCoinHouse) {
        mCoinAdapter!!.setNewData(msg.exchanges)
        mBindCoinHouse = msg.symbols
    }

    override fun setDetail(bean: CategoryDetailBean) {
        tv_detail.text = bean.detail.replace("\\n", "\n")
        tv_create_time.text = getString(R.string.text_create_time, bean.create_time?.substring(0, 10))
        tv_modify_time.text = getString(R.string.text_last_modify, bean.update_time?.substring(0, 10))
    }
}
