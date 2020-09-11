package com.hazz.aipick.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.hazz.aipick.BuildConfig
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseFragment
import com.hazz.aipick.events.ChangeEvent
import com.hazz.aipick.mvp.contract.CollectionContract
import com.hazz.aipick.mvp.model.bean.UserSubscribeBean
import com.hazz.aipick.mvp.presenter.UserSubscribePresenter
import com.hazz.aipick.ui.adapter.UserSubscribeAdapter
import com.hazz.aipick.utils.RxBus
import kotlinx.android.synthetic.main.fragment_subscribe.*


@Suppress("DEPRECATION")
class SubscribeFragment : BaseFragment(), CollectionContract.userSubscribeView {


    private var isRefresh: Boolean = false
    private var id: String? = ""
    private var mSubscribePresenter: UserSubscribePresenter = UserSubscribePresenter(this)
    private var page = 1
    private var subscribeAdapter: UserSubscribeAdapter? = null

    companion object {
        fun getInstance(id: String): SubscribeFragment {
            val fragment = SubscribeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.id = id
            return fragment
        }
    }

    var isDemo = "0"
    override fun getLayoutId(): Int = R.layout.fragment_subscribe


    /**
     * 初始化 ViewI
     */
    override fun initView() {

        //内容跟随偏移
        mRefreshLayout.setEnableHeaderTranslationContent(true)
        mRefreshLayout.setOnRefreshListener {
            isRefresh = true
            page = 1
            getData()
        }
        mRefreshLayout.isEnableLoadmore = false
        mRefreshLayout.setOnLoadmoreListener {
            isRefresh = false
            page++
            getData()
        }

        recycleview.layoutManager = LinearLayoutManager(activity)
        subscribeAdapter = UserSubscribeAdapter(R.layout.item_subscribe, null)

        recycleview.adapter = subscribeAdapter
        subscribeAdapter!!.bindToRecyclerView(recycleview)
        subscribeAdapter!!.setEmptyView(R.layout.empty_view)
        RxBus.get().observerOnMain(this, ChangeEvent::class.java) {
            isDemo = it.isDemo
            if (isVisible)
                getData()

        }
    }


    private fun getData() {
        if (id == "-1") {
            mSubscribePresenter.fakeFollows()
        } else {
            mSubscribePresenter.userFollows(id!!, page, 10, isDemo)
        }

    }

    override fun lazyLoad() {
        getData()
    }

    override fun userSubscribe(data: List<UserSubscribeBean>) {
        mRefreshLayout.finishRefresh()
        mRefreshLayout.isEnableLoadmore = data.size > 10
        if (data.isEmpty()) {
            if (BuildConfig.DEBUG) {
                var temp = ArrayList<UserSubscribeBean>()
//                temp.add(UserSubscribeBean.demo(0))
//                temp.add(UserSubscribeBean.demo(1))
//                temp.add(UserSubscribeBean.demo(2))
                if (isRefresh)
                    subscribeAdapter?.setNewData(temp)
                else {
                    subscribeAdapter?.addData(temp)
                }
            }
            return
        }
        if (isRefresh)
            subscribeAdapter?.setNewData(data)
        else {
            subscribeAdapter?.addData(data)
        }
    }
}
