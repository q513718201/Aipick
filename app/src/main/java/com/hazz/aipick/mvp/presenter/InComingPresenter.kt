package com.hazz.aipick.mvp.presenter


import android.util.Pair
import com.google.gson.Gson
import com.hazz.aipick.BuildConfig
import com.hazz.aipick.mvp.contract.InComingContract
import com.hazz.aipick.mvp.model.InComing
import com.hazz.aipick.net.*
import com.hazz.aipick.utils.AssetsUtil
import java.nio.charset.Charset


class InComingPresenter(view: InComingContract.incomingView) : BasePresenter<InComingContract.incomingView>(view) {

    fun getBotTradeIncoming(timeFilter: String) {
        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("timeFilter", timeFilter)
        )

        val login = RetrofitManager.service.botTradeIncoming(body)

        doRequest(login, object : Callback<List<InComing>>(view) {
            override fun failed(tBaseResult: BaseResult<List<InComing>>): Boolean {
                return false
            }

            override fun success(tBaseResult: BaseResult<List<InComing>>) {

                if (BuildConfig.DEBUG) {// TODO: 2020/8/29 test data
                    var assertsFileInBytes = AssetsUtil.getAssertsFileInBytes(context?.applicationContext, "income.json")
                    val json = String(assertsFileInBytes, Charset.defaultCharset())
                    var msg: List<InComing> = Gson().fromJson<Array<InComing>>(json, Array<InComing>::class.java).toMutableList()
                    view.getTradeIncoming(msg)
                } else
                    view.getTradeIncoming(tBaseResult.data!!)
            }

        }, true)

    }

    fun getBotIncoming(timeFilter: String) {

        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("timeFilter", timeFilter)
        )

        val login = RetrofitManager.service.botIncoming(body)

        doRequest(login, object : Callback<List<InComing>>(view) {
            override fun failed(tBaseResult: BaseResult<List<InComing>>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<List<InComing>>) {
                if (BuildConfig.DEBUG) {// TODO: 2020/8/29 test data
                    var assertsFileInBytes = AssetsUtil.getAssertsFileInBytes(context?.applicationContext, "income.json")
                    val json = String(assertsFileInBytes, Charset.defaultCharset())
                    var msg: List<InComing> = Gson().fromJson<Array<InComing>>(json, Array<InComing>::class.java).toMutableList()
                    view.getIncoming(msg)
                } else
                    view.getIncoming(tBaseResult.data!!)
            }

        }, true)

    }

    fun getUserTradeIncoming(id: String, timeFilter: String, unitType: String, isDemo: String) {

        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("objUserId", id),
                Pair.create<Any, Any>("timeFilter", timeFilter),
                Pair.create<Any, Any>("unitType", unitType),
                Pair.create<Any, Any>("isDemo", isDemo)
        )


        val login = RetrofitManager.service.botTradeIncoming(body)

        doRequest(login, object : Callback<List<InComing>>(view) {
            override fun failed(tBaseResult: BaseResult<List<InComing>>): Boolean {
                return false
            }

            override fun success(tBaseResult: BaseResult<List<InComing>>) {
                if (BuildConfig.DEBUG) {// TODO: 2020/8/29 test data
                    var assertsFileInBytes = AssetsUtil.getAssertsFileInBytes(context?.applicationContext, "income.json")
                    val json = String(assertsFileInBytes, Charset.defaultCharset())
                    var msg: List<InComing> = Gson().fromJson<Array<InComing>>(json, Array<InComing>::class.java).toMutableList()
                    view.getTradeIncoming(msg)
                } else
                    view.getTradeIncoming(tBaseResult.data!!)
            }

        }, true)

    }

    fun getUserIncoming(id: String, timeFilter: String, unitType: String, isDemo: String) {

        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("objUserId", id),
                Pair.create<Any, Any>("timeFilter", timeFilter),
                Pair.create<Any, Any>("unitType", unitType),
                Pair.create<Any, Any>("isDemo", isDemo)
        )

        val login = RetrofitManager.service.userIncoming(body)

        doRequest(login, object : Callback<List<InComing>>(view) {
            override fun failed(tBaseResult: BaseResult<List<InComing>>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<List<InComing>>) {
                if (BuildConfig.DEBUG) {// TODO: 2020/8/29 test data
                    var assertsFileInBytes = AssetsUtil.getAssertsFileInBytes(context?.applicationContext, "income.json")
                    val json = String(assertsFileInBytes, Charset.defaultCharset())
                    var msg: List<InComing> = Gson().fromJson<Array<InComing>>(json, Array<InComing>::class.java).toMutableList()
                    view.getIncoming(msg)
                } else
                    view.getIncoming(tBaseResult.data!!)
            }

        }, true)

    }

}