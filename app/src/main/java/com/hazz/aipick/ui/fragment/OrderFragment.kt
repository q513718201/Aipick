package com.hazz.aipick.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseFragment
import com.hazz.aipick.ui.adapter.OrderAdapter
import kotlinx.android.synthetic.main.fragment_order.*
import java.util.ArrayList


@Suppress("DEPRECATION")
class OrderFragment : BaseFragment() {


//    private val mPresenter by lazy { HomePresenter() }

    private var mTitle: String? = null
    private val titleList = ArrayList<String>()

    private var mOrderAdapter: OrderAdapter? = null
    companion object {
        fun getInstance(title: String): OrderFragment {
            val fragment = OrderFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }


    override fun getLayoutId(): Int = R.layout.fragment_order


    /**
     * 初始化 ViewI
     */
    override fun initView() {
        titleList.add("正在持仓")
        titleList.add("历史持仓")
        for (i in titleList.indices) {
            tabLayout!!.addTab(tabLayout!!.newTab().setText(titleList[i]))
        }
        recycleview.layoutManager = LinearLayoutManager(activity)
        mOrderAdapter= OrderAdapter(R.layout.item_order,titleList)
        recycleview.adapter = mOrderAdapter
        mOrderAdapter!!.bindToRecyclerView(recycleview)
        mOrderAdapter!!.setEmptyView(R.layout.empty_view)
    }

    override fun lazyLoad() {
//
    }



}
