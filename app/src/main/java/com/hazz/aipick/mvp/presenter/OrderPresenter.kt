package com.hazz.aipick.mvp.presenter


import android.util.Pair
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.Order
import com.hazz.aipick.net.*


class OrderPresenter(view: WaletContract.orderView) : BasePresenter<WaletContract.orderView>(view) {


    fun getOrder(id: String, action: String, pageNumber: Int, pageSize: Int, isDemo: String) {

        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("objUserId", id),
                Pair.create<Any, Any>("action", action),
                Pair.create<Any, Any>("pageNumber", pageNumber),
                Pair.create<Any, Any>("pageSize", pageSize),
                Pair.create<Any, Any>("isDemo", isDemo)
        )
        val login = RetrofitManager.service.getOrder(body)

        doRequest(login, object : Callback<Order>(view) {
            override fun failed(tBaseResult: BaseResult<Order>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<Order>) {
                view.getOrder(tBaseResult.data!!)
            }

        }, true)

    }

    fun getFakeOrder(action: String, pageNumber: Int, pageSize: Int) {
        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("action", action),
                Pair.create<Any, Any>("pageNumber", pageNumber),
                Pair.create<Any, Any>("pageSize", pageSize)
        )
        val login = RetrofitManager.service.getFakeOrder(body)
        doRequest(login, object : Callback<Order>(view) {
            override fun failed(tBaseResult: BaseResult<Order>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<Order>) {
                view.getOrder(tBaseResult.data!!)
            }

        }, true)
    }


}