package com.hazz.aipick.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseFragment
import com.hazz.aipick.managers.GuideManager
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.model.bean.Fans
import com.hazz.aipick.mvp.model.bean.LoginBean
import com.hazz.aipick.mvp.model.bean.UserInfo
import com.hazz.aipick.mvp.presenter.FansPresenter
import com.hazz.aipick.mvp.presenter.LoginPresenter
import com.hazz.aipick.ui.activity.*
import com.hazz.aipick.ui.adapter.FansAdapter
import com.hazz.aipick.utils.RxBus
import com.hazz.aipick.utils.SPUtil
import com.hazz.aipick.utils.StatusBarUtil
import com.hazz.aipick.utils.ToastUtils
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import easily.tech.guideview.lib.GuideViewBundle.Direction.BOTTOM
import kotlinx.android.synthetic.main.dialog_fans.view.*
import kotlinx.android.synthetic.main.fragment_mine.*


class MineFragment : BaseFragment(), View.OnClickListener, LoginContract.LoginView, LoginContract.FansView, OnRefreshLoadmoreListener {

    override fun funsList(msg: Fans) {
        mOrderAdapter!!.setNewData(msg.list)
        //数据满的时候可以加载更多
        refreshLayout.isEnableLoadmore = msg.list.size == 10
        when (refreshType) {
            1 -> refreshLayout.finishRefresh()
            else ->
                refreshLayout.finishRefresh()
        }

    }

    override fun setFollow(msg: String) {
        ToastUtils.showToast(activity, msg)
        mLoginPresenter.userInfo()
        mFansPresenter.fansList(type, uid, 1, 10)
    }

    override fun loginSuccess(msg: LoginBean) {

    }

    override fun getUserInfo(msg: UserInfo) {
        SPUtil.putObj("userinfo", msg)

        uid = msg.uid
        if (!TextUtils.isEmpty(msg.avatar)) {
            Glide.with(activity!!).load(msg.avatar)
                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .into(iv_avatar)
        }

        tv_name?.text = msg.nickname
        tv_fans?.text = msg.fans.toString()
        tv_guanzhu_num?.text = msg.follow.toString()
        tv_renqi?.text = msg.pageview.toString()
        when (msg.type) {
            1 -> tv_role?.text = getString(R.string.putong_role)
            2 -> tv_role?.text = getString(R.string.jiaoyiyuan)
            3 -> tv_role?.text = getString(R.string.jiqiren)
        }
        currentType = msg.check_status
        when (msg.check_status) {
            "none" -> tv_trader_state?.text = getString(R.string.weishenqing)
            "wait" -> tv_trader_state?.text = getString(R.string.wait)
            "pass" -> tv_trader_state?.text = getString(R.string.pass)
            "fail" -> tv_trader_state?.text = getString(R.string.fail)

        }
    }


    private var mTitle: String? = null
    private var mLoginPresenter: LoginPresenter = LoginPresenter(this)
    private var mFansPresenter: FansPresenter = FansPresenter(this)
    private var isfirstMine: Boolean = false
    private var bottomSheet: BottomSheetDialog? = null
    private var mOrderAdapter: FansAdapter? = null
    private var uid = ""
    private var currentType = ""

    companion object {
        fun getInstance(title: String): MineFragment {
            val fragment = MineFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }


    override fun getLayoutId(): Int = R.layout.fragment_mine

    override fun initView() {
        //状态栏透明和间距处理
        tv_moni.setOnClickListener(this)
        iv_set.setOnClickListener(this)
        tv_caopan.setOnClickListener(this)
        tv_wallet.setOnClickListener(this)

        tv_account.setOnClickListener(this)
        invite_friend.setOnClickListener(this)
        mine_trader.setOnClickListener(this)
        mine_subsiber.setOnClickListener(this)
        mine_collector.setOnClickListener(this)
        mine_jiaoyisuo.setOnClickListener(this)
        mine_message.setOnClickListener(this)
        mine_fade.setOnClickListener(this)
        coin_house_order.setOnClickListener(this)
        fans.setOnClickListener(this)
        tv_guanzhu.setOnClickListener(this)

//        if (isfirstMine) {
////            cl_guide.visibility = View.VISIBLE
//
//
//        }
////        cl_guide.setOnClickListener {
////            SPUtil.putBoolean("isfirstMine", true)
//////            cl_guide.visibility = View.GONE
////        }
        // var user=SPUtil.getObj("userinfo",UserInfo::class.java)

        RxBus.get().observerOnMain(this, Fans.ListBean::class.java) {
            it?.let {
                if (it.mutual) {// TODO: 2020/8/15 弹窗？
                    mFansPresenter.attentionCancle(it.follower_id)
                } else {
                    mFansPresenter.attention(it.follower_id)
                }
            }
        }

        GuideManager.showGuide(childFragmentManager, tv_moni, R.mipmap.guide_mine, BOTTOM, "guide_mine")//showGuide
    }

    override fun lazyLoad() {
    }

    override fun onResume() {
        super.onResume()
        mLoginPresenter.userInfo()
    }


    override fun onClick(v: View?) {
        when {
            v?.id == R.id.iv_avatar -> {
                val intent = Intent(activity, MineSubscribeActivity::class.java)
                startActivity(intent)
            }
            v?.id == R.id.iv_set -> {
                startActivity(Intent(activity, SettingActivity::class.java))
            }
            v?.id == R.id.coin_house_order -> startActivity(Intent(activity, ExchangeOrderActivity::class.java))
            v?.id == R.id.tv_caopan -> startActivity(Intent(activity, MonicpActivity::class.java))
            v?.id == R.id.tv_moni -> context?.let { RebotCategryActivity.start(it, "-1", "bot", "0") }
            v?.id == R.id.tv_wallet -> startActivity(Intent(activity, WaletActivity::class.java))
            v?.id == R.id.tv_account -> MyAccountActivity.start(context!!, uid, "bot", "0", "mine")
            v?.id == R.id.invite_friend -> startActivity(Intent(activity, InviteFriendsActivity::class.java))
            v?.id == R.id.mine_trader -> {
                when (currentType) {
                    "wait" -> ToastUtils.showToast(activity, getString(R.string.wait))
                    "pass" -> startActivity(Intent(activity, TraderSetActivity::class.java))
                    "none" -> startActivity(Intent(activity, SetTraderActivity::class.java))
                    "fail" -> startActivity(Intent(activity, SetTraderActivity::class.java))

                }


            }

            v?.id == R.id.mine_subsiber -> startActivity(Intent(activity, MineSubscribeActivity::class.java))
            v?.id == R.id.mine_collector -> startActivity(Intent(activity, CollectionActivity::class.java))
            v?.id == R.id.mine_jiaoyisuo -> startActivity(Intent(activity, CoinHouseActivity::class.java))

            v?.id == R.id.mine_message -> startActivity(Intent(activity, MessageActivity::class.java))
            v?.id == R.id.mine_fade -> startActivity(Intent(activity, HelpAndFeedActivity::class.java))
            v?.id == R.id.fans -> {
                type = "fans"
                initBottom()
            }
            v?.id == R.id.tv_guanzhu -> {
                type = "follow"
                initBottom()
            }

        }
    }

    lateinit var refreshLayout: SmartRefreshLayout
    private fun initBottom() {
        getFanData()

        bottomSheet = BottomSheetDialog(activity!!)
        val view = layoutInflater.inflate(R.layout.dialog_fans, null)
        bottomSheet!!.setContentView(view)
        refreshLayout = view.refreshLayout.setOnRefreshLoadmoreListener(this)

        view.recycleview.layoutManager = LinearLayoutManager(activity)
        mOrderAdapter = FansAdapter(R.layout.item_fans, null)
        view.tv_title.text = if (type == "fan") "粉丝${tv_fans?.text}" else "关注${tv_guanzhu_num.text}"
        view.recycleview.adapter = mOrderAdapter
        mOrderAdapter!!.bindToRecyclerView(view.recycleview)
        mOrderAdapter!!.setEmptyView(R.layout.empty_view)
        bottomSheet!!.show()
    }

    private fun getFanData() {
        mFansPresenter.fansList(type, uid, page, 10)
    }

    var page = 1
    var type = "fan"
    var refreshType = 0;
    override fun onLoadmore(refreshlayout: RefreshLayout?) {
        refreshType = 1
        page++
        getFanData()
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        refreshType = 0
        page = 1
        getFanData()
    }
}
