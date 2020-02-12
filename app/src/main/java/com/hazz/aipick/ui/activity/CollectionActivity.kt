package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.CollectionContract
import com.hazz.aipick.mvp.model.bean.Collection
import com.hazz.aipick.mvp.presenter.CollectionPresenter
import com.hazz.aipick.ui.adapter.CollectionAdapter
import com.hazz.aipick.ui.adapter.OrderAdapter
import com.hazz.aipick.utils.ToolBarCustom
import kotlinx.android.synthetic.main.activity_mine_collection.*


import java.util.ArrayList


class CollectionActivity : BaseActivity(), TabLayout.OnTabSelectedListener, CollectionContract.collectionView {
    override fun addCollectionSucceed(msg: String) {

    }

    override fun getCollection(msg: List<Collection>) {

        mOrderAdapter?.setNewData(msg)
    }

    override fun onTabReselected(p0: TabLayout.Tab?) {

    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {
    }

    override fun onTabSelected(p0: TabLayout.Tab?) {
        val position = p0?.position
        when(position){
            0->{
                currentType="bot"
                mCollectionPresenter.getCollection(currentType,page,10)
            }
            1->{
                currentType="sentiment"
                mCollectionPresenter.getCollection(currentType,page,10)
            }
        }
    }




    override fun layoutId(): Int = R.layout.activity_mine_collection

    override fun initData() {
        mCollectionPresenter.getCollection(currentType,page,10)
    }

    private var mOrderAdapter: CollectionAdapter? = null
    private val titleList = ArrayList<String>()
    private  var bottomSheet:BottomSheetDialog?=null
    private var mCollectionPresenter: CollectionPresenter = CollectionPresenter(this)
    private var currentType="bot"
    private var page=1
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

                }


        titleList.add("机器人策略")
        titleList.add("市场情绪")
        for (i in titleList.indices) {
            tabLayout!!.addTab(tabLayout!!.newTab().setText(titleList[i]))
        }
        recycleview.layoutManager = LinearLayoutManager(this)
        mOrderAdapter= CollectionAdapter(R.layout.item_collection,null)

        recycleview.adapter = mOrderAdapter
        mOrderAdapter!!.bindToRecyclerView(recycleview)
        mOrderAdapter!!.setEmptyView(R.layout.empty_view)
        tabLayout.addOnTabSelectedListener(this)

    }

    override fun start() {

    }




}
