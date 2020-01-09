package com.hazz.aipick.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseFragment
import com.hazz.aipick.events.ChangeIndex
import com.hazz.aipick.showToast
import com.hazz.aipick.ui.activity.*
import com.hazz.aipick.events.RxBus
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.model.bean.LoginBean
import com.hazz.aipick.mvp.model.bean.UserInfo
import com.hazz.aipick.mvp.presenter.LoginPresenter
import com.hazz.aipick.utils.StatusBarUtil
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.fragment_mine.iv_avatar
import kotlinx.android.synthetic.main.fragment_mine.toolbar

/**
 * Created by xuhao on 2017/11/9.
 * 我的
 */
class MineFragment : BaseFragment(),View.OnClickListener, LoginContract.LoginView {
    override fun loginSuccess(msg: LoginBean) {

    }

    override fun getUserInfo(msg: UserInfo) {
        if(!TextUtils.isEmpty(msg.avatar)){
            Glide.with(activity!!).load(msg.avatar)
                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .into(iv_avatar)
        }

        tv_name.text=msg.nickname
        tv_fans.text=msg.fans.toString()
        tv_guanzhu_num.text=msg.follow.toString()
        tv_renqi.text=msg.pageview.toString()
        when(msg.type){
            1->tv_role.text=getString(R.string.putong_role)
            2->tv_role.text=getString(R.string.jiaoyiyuan)
            3->tv_role.text=getString(R.string.jiqiren)
        }
    }


    private var mTitle:String? =null
    private var mLoginPresenter: LoginPresenter = LoginPresenter(this)

    companion object {
        fun getInstance(title:String): MineFragment {
            val fragment = MineFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }


    override fun getLayoutId(): Int= R.layout.fragment_mine

    override fun initView() {
        //状态栏透明和间距处理
        activity?.let { StatusBarUtil.darkMode(it) }
        activity?.let { StatusBarUtil.setPaddingSmart(it, toolbar) }

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

    }

    override fun lazyLoad() {
        mLoginPresenter.userInfo()
    }



    override fun onClick(v: View?) {
        when{
            v?.id==R.id.iv_avatar -> {
                val intent = Intent(activity, MineSubscribeActivity::class.java)
                startActivity(intent)
            }
            v?.id==R.id.iv_set ->{
                startActivity(Intent(activity,SettingActivity::class.java))
            }
            v?.id==R.id.tv_caopan -> RxBus.get().send(ChangeIndex())
            v?.id==R.id.tv_moni -> showToast("模拟跟随")
            v?.id==R.id.tv_wallet -> startActivity(Intent(activity,WaletActivity::class.java))
            v?.id==R.id.tv_account ->startActivity(Intent(activity,MyAccountActivity::class.java))
            v?.id==R.id.invite_friend -> startActivity(Intent(activity,InviteFriendsActivity::class.java))
            v?.id==R.id.mine_trader ->startActivity(Intent(activity,SetTraderActivity::class.java))

            v?.id==R.id.mine_subsiber ->startActivity(Intent(activity,MineSubscribeActivity::class.java))
            v?.id==R.id.mine_collector -> startActivity(Intent(activity,CollectionActivity::class.java))
            v?.id==R.id.mine_jiaoyisuo -> startActivity(Intent(activity,CoinHouseActivity::class.java))

            v?.id==R.id.mine_message -> startActivity(Intent(activity,MessageActivity::class.java))
            v?.id==R.id.mine_fade -> startActivity(Intent(activity,HelpAndFeedActivity::class.java))

        }
    }





}