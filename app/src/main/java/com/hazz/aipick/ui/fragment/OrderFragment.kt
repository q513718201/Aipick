package com.hazz.aipick.ui.fragment

import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseFragment
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.Order
import com.hazz.aipick.mvp.presenter.OrderPresenter
import com.hazz.aipick.ui.adapter.OrderAdapter
import kotlinx.android.synthetic.main.dialog_buy_sell.view.*
import kotlinx.android.synthetic.main.fragment_order.*
import java.util.*


@Suppress("DEPRECATION")
class OrderFragment : BaseFragment(), WaletContract.orderView {

    override fun getOrder(msg: Order) {

        mOrderAdapter!!.setNewData(msg.list)
        tv1.text = msg.total_amount
        tv2.text = msg.total_times
        tv3.text = msg.total_gain
    }

    private var id: String? = ""
    private var bottomSheet: BottomSheetDialog? = null
    private var currentType = ""
    private var mOrderPresenter: OrderPresenter = OrderPresenter(this)
    private var page = 1
    private var mOrderAdapter: OrderAdapter? = null

    companion object {
        fun getInstance(id: String): OrderFragment {
            val fragment = OrderFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.id = id
            return fragment
        }
    }


    override fun getLayoutId(): Int = R.layout.fragment_order


    /**
     * 初始化 ViewI
     */
    override fun initView() {


        recycleview.layoutManager = LinearLayoutManager(activity)
        mOrderAdapter = OrderAdapter(R.layout.item_order, null)
        recycleview.adapter = mOrderAdapter
        mOrderAdapter!!.bindToRecyclerView(recycleview)
        mOrderAdapter!!.setEmptyView(R.layout.empty_view)

        tv_choose.setOnClickListener {
            bottomSheet = BottomSheetDialog(activity!!)
            val view = layoutInflater.inflate(R.layout.dialog_buy_sell, null)
            bottomSheet!!.setContentView(view)

            view.tv_cancle.setOnClickListener {
                bottomSheet!!.dismiss()
            }

            view.tv_buy.setOnClickListener {
                bottomSheet!!.dismiss()
                currentType = "buy"
                tv_choose.text = getString(R.string.buy)
                getDataSource()
            }

            view.tv_sell.setOnClickListener {
                bottomSheet!!.dismiss()
                currentType = "sell"
                tv_choose.text = getString(R.string.sell)
                getDataSource()
            }
            val viewById = bottomSheet!!.delegate.findViewById<View>(android.support.design.R.id.design_bottom_sheet)
            //设置布局背景透明
            viewById?.setBackgroundColor(resources.getColor(android.R.color.transparent))
            bottomSheet!!.show()
        }
    }


    private fun getDataSource() {
        mOrderPresenter.getOrder(id!!, currentType, page, 10, "0")
    }

    override fun lazyLoad() {
        mOrderPresenter.getOrder(id!!, "", page, 10, "0")
    }


}
