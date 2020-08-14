package com.hazz.aipick.mvp.presenter


import android.util.Pair
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.model.bean.TraderSet
import com.hazz.aipick.net.*


class TraderAuthPresenter(view: LoginContract.tradeView) : BasePresenter<LoginContract.tradeView>(view) {

    fun traderAuth(countryCode: String, code: String, email: String, phone: String, cardNumber: String,
                   name: String, cardType: String, front: String, back: String, promise: String, coins: List<String>

    ) {


        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("countryCode", countryCode),
                Pair.create<Any, Any>("code", code),
                Pair.create<Any, Any>("email", email),
                Pair.create<Any, Any>("phone", phone),
                Pair.create<Any, Any>("cardNumber", cardNumber),
                Pair.create<Any, Any>("name", name),
                Pair.create<Any, Any>("cardType", cardType),
                Pair.create<Any, Any>("front", front),
                Pair.create<Any, Any>("back", back),
                Pair.create<Any, Any>("promise", promise),
                Pair.create<Any, Any>("coins", coins)


        )

        val login = RetrofitManager.service.traderAuth(body)

        doRequest(login, object : Callback<Any>(view) {
            override fun failed(tBaseResult: BaseResult<Any>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<Any>) {
                view.traderAuthSuccess(tBaseResult.msg)
            }

        }, true)

    }


    fun getCoinList() {


        val login = RetrofitManager.service.getCoin()

        doRequest(login, object : Callback<List<String>>(view) {
            override fun failed(tBaseResult: BaseResult<List<String>>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<List<String>>) {
                view.getCoin(tBaseResult.data!!)
            }

        }, true)

    }

    fun traderSet(days30: String, days90: String, days180:String,strategy: String ) {
        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("days30", days30),
                Pair.create<Any, Any>("days90", days90),
                Pair.create<Any, Any>("days180", days180),
                Pair.create<Any, Any>("strategy", strategy)
        )

        val login = RetrofitManager.service.traderSet(body)

        doRequest(login, object : Callback<Any>(view) {
            override fun failed(tBaseResult: BaseResult<Any>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<Any>) {
                view.traderSetSucceed(tBaseResult.msg)
            }

        }, true)

    }

    fun traderSetQuery( ) {

        val login = RetrofitManager.service.traderSetQuery()

        doRequest(login, object : Callback<TraderSet>(view) {
            override fun failed(tBaseResult: BaseResult<TraderSet>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<TraderSet>) {
                view.traderSetQuery(tBaseResult.data!!)
            }

        }, true)

    }

}