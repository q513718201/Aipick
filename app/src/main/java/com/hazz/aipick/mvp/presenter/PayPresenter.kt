package com.hazz.aipick.mvp.presenter


import android.util.Pair
import com.hazz.aipick.mvp.contract.HomeContract
import com.hazz.aipick.mvp.model.bean.CreateId
import com.hazz.aipick.mvp.model.bean.PayBean
import com.hazz.aipick.mvp.model.bean.PayResultMine
import com.hazz.aipick.mvp.model.bean.PaySucceed
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
                tBaseResultMine.data?.let { view.payResult(it) }
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
    fun orderCancle(subId: String) {


        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("subId", subId)

        )

        val login = RetrofitManager.service.orderCancle(body)

        doRequest(login, object : Callback<Any>(view) {
            override fun failed(tBaseResult: BaseResult<Any>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<Any>) {
                view.payCancle(tBaseResult.msg)
            }

        }, true)

    }

    fun payCheck(subId: String,payResponse: String,sign: String,signType: String,statusCode: String) {

        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("subId", subId),
                Pair.create<Any, Any>("payResponse", payResponse),
                Pair.create<Any, Any>("sign", sign),
                Pair.create<Any, Any>("signType", signType),
                Pair.create<Any, Any>("statusCode", statusCode)
        )

        val login = RetrofitManager.service.PayCheck(body)

        doRequest(login, object : Callback<PaySucceed>(view) {
            override fun failed(tBaseResult: BaseResult<PaySucceed>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<PaySucceed>) {
                view.paySucceed(tBaseResult.data!!)
            }

        }, true)

    }
}