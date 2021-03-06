package com.hazz.aipick.mvp.presenter


import android.util.Pair
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.model.bean.Fans
import com.hazz.aipick.net.*


class FansPresenter(view: LoginContract.FansView) : BasePresenter<LoginContract.FansView>(view) {

    fun fansList(type: String, userId: String, pageNumber: Int, pageSize: Int
    ) {

        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("type", type),
                Pair.create<Any, Any>("userId", userId),
                Pair.create<Any, Any>("pageNumber", pageNumber),
                Pair.create<Any, Any>("pageSize", pageSize)

        )

        val login = RetrofitManager.service.fansList(body)

        doRequest(login, object : Callback<Fans>(view) {
            override fun failed(tBaseResult: BaseResult<Fans>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<Fans>) {
                view.funsList(tBaseResult.data!!)
            }

        }, true)

    }

    fun attention(objUserId: String) {
        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("objUserId", objUserId)
        )

        val login = RetrofitManager.service.attention(body)

        doRequest(login, object : Callback<Any>(view) {
            override fun failed(tBaseResult: BaseResult<Any>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<Any>) {
                view.setFollow(tBaseResult.msg)
            }

        }, true)

    }

    fun attentionCancle(objUserId: String) {
        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("objUserId", objUserId)
        )

        val login = RetrofitManager.service.attentionCancle(body)

        doRequest(login, object : Callback<Any>(view) {
            override fun failed(tBaseResult: BaseResult<Any>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<Any>) {
                view.setFollow(tBaseResult.msg)
            }

        }, true)

    }


}