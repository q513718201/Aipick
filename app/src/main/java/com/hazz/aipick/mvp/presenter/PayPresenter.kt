package com.hazz.aipick.mvp.presenter


import android.util.Pair
import com.hazz.aipick.mvp.contract.HomeContract
import com.hazz.aipick.mvp.model.bean.*
import com.hazz.aipick.net.*


class PayPresenter(view: HomeContract.payView) : BasePresenter< HomeContract.payView>(view) {

    fun pay(subId: String, payMethod: String) {


        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("subId", subId),
                Pair.create<Any, Any>("payMethod", payMethod)

                )

        val login = RetrofitManager.service.pay(body)

        doRequest(login, object : Callback<PayResultMine>(view) {
            override fun failed(tBaseResultMine: BaseResult<PayResultMine>): Boolean {

                return false
            }

            override fun success(tBaseResultMine: BaseResult<PayResultMine>) {
                view.payResult(tBaseResultMine.data!!)
            }

        }, true)

    }


    fun createId(subType: String, subData: PayBean) {


        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("subType", subType),
                Pair.create<Any, Any>("subData", subData)

        )

        val login = RetrofitManager.service.createId(body)

        doRequest(login, object : Callback<CreateId>(view) {
            override fun failed(tBaseResult: BaseResult<CreateId>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<CreateId>) {
                view.createId(tBaseResult.data!!)
            }

        }, true)

    }
}