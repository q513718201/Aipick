package com.hazz.aipick.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseFragment
import com.hazz.aipick.events.ChangeEvent
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.Order
import com.hazz.aipick.mvp.presenter.OrderPresenter
import com.hazz.aipick.ui.adapter.OrderAdapter
import com.hazz.aipick.utils.BigDecimalUtil
import com.hazz.aipick.utils.RxBus
import kotlinx.android.synthetic.main.fragment_order.*


@Suppress("DEPRECATION")
class OrderFragment : BaseFragment(), WaletContract.orderView {

    override fun getOrder(msg: Order) {
        mOrderAdapter!!.setNewData(msg.list)
        tv1.text = BigDecimalUtil.format(msg.total_amount, 2)
        tv2.text = BigDecimalUtil.format(msg.total_times, 0)
        tv3.text = BigDecimalUtil.format(msg.total_gain, 2)
        tv_pos_now.text = "${getString(R.string.now_position)}(${msg.now_num})"
    }

    private var id: String? = "-1"
    private var currentType = "history"
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
        mOrderAdapter = OrderAdapter(null)
        recycleview.adapter = mOrderAdapter
        mOrderAdapter!!.bindToRecyclerView(recycleview)
        mOrderAdapter!!.setEmptyView(R.layout.empty_view)

        rg.setOnCheckedChangeListener { group, checkedId ->
            currentType = when (checkedId) {
                R.id.tv_pos_now -> "now"
                else -> "history"
            }
            getDataSource(currentType)
        }

        RxBus.get().observerOnMain(activity, ChangeEvent::class.java) {
            mOrderPresenter.getOrder(id!!, currentType, page, 10, it.isDemo)
        }
    }

    private var isDemo = "0"
    private fun getDataSource(dateType: String) {
        if (id == "-1") {
            mOrderPresenter.getFakeOrder(currentType, page, 10)
        } else {
            mOrderPresenter.getOrder(id!!, currentType, page, 10, isDemo)
        }
    }

    override fun lazyLoad() {
        getDataSource(currentType)
    }


}
