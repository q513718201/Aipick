package com.hazz.aipick.ui.activity


import android.annotation.SuppressLint
import android.graphics.Color
import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.CollectionContract
import com.hazz.aipick.mvp.model.bean.Collection
import com.hazz.aipick.mvp.presenter.CollectionPresenter
import com.hazz.aipick.ui.adapter.CollectionAdapter
import com.hazz.aipick.utils.ToastUtils
import com.hazz.aipick.utils.ToolBarCustom
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import kotlinx.android.synthetic.main.activity_mine_collection.*


class CollectionActivity : BaseActivity(), TabLayout.OnTabSelectedListener, CollectionContract.collectionView, OnRefreshLoadmoreListener {
    override fun optionResult(msg: String) {
        ToastUtils.showToast(this, msg)
        initData()
    }

    override fun getCollection(msg: List<Collection>) {
        refreshLayout.isEnableLoadmore = msg.size == 10
        when (page) {
            1 -> {
                refreshLayout.finishRefresh()
                mOrderAdapter?.setNewData(msg)
            }
            else -> {
                refreshLayout.finishLoadmore()
                mOrderAdapter?.addData(msg)
            }
        }

    }

    override fun onTabReselected(p0: TabLayout.Tab?) {

    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {
    }

    override fun onTabSelected(p0: TabLayout.Tab?) {
        when (p0?.position) {
            0 -> {
                currentType = "bot"
                mCollectionPresenter.getCollection(currentType, page, 10)
            }
            1 -> {
                currentType = "sentiment"
                mCollectionPresenter.getCollection(currentType, page, 10)
            }
        }
    }


    override fun layoutId(): Int = R.layout.activity_mine_collection

    override fun initData() {
        mCollectionPresenter.getCollection(currentType, page, 10)
    }

    private var mOrderAdapter: CollectionAdapter? = null
    private val titleList = ArrayList<String>()
    private var mCollectionPresenter: CollectionPresenter = CollectionPresenter(this)
    private var currentType = "bot"
    private var page = 1
    private var editable = false

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.collection))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }
                .setRightTextIsShow(true)
                .setRightText(getString(R.string.edit))
                .setRightTextColor(resources.getColor(R.color.dilaog_btn_color))
                .setOnRightClickListener {
                    editable = !editable
                    mOrderAdapter?.setEditable(editable)
                    rl_action.visibility = if (editable) View.VISIBLE else View.GONE
                    (it as TextView).text = if (editable) getString(R.string.text_finish) else getString(R.string.edit)

                }

        refreshLayout.setOnRefreshLoadmoreListener(this)
        refreshLayout.isEnableLoadmore = false
        titleList.add("机器人策略")
        // TODO: 2020/9/8 市场情绪
//        titleList.add("市场情绪")
        for (i in titleList.indices) {
            tabLayout!!.addTab(tabLayout!!.newTab().setText(titleList[i]))
        }
        recycleview.layoutManager = LinearLayoutManager(this)
        mOrderAdapter = CollectionAdapter(null)

        recycleview.adapter = mOrderAdapter
        mOrderAdapter!!.bindToRecyclerView(recycleview)
        mOrderAdapter!!.setEmptyView(R.layout.empty_view)
        tabLayout.addOnTabSelectedListener(this)
        tv_remove_all.setOnClickListener {
            mCollectionPresenter.deleteSelected(ArrayList(), currentType, "all")
        }
        tv_del.setOnClickListener {
            mOrderAdapter?.getSelectIds()?.let { mCollectionPresenter.deleteSelected(it, currentType, if (it.size > 1) "many" else "one") }
        }
    }

    override fun start() {

    }


    override fun onLoadmore(refreshlayout: RefreshLayout?) {
        page++
        initData()
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        page = 1
        initData()
    }


}
