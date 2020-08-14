package com.hazz.aipick.mvp.presenter


import android.util.Pair
import com.hazz.aipick.mvp.contract.InComingContract
import com.hazz.aipick.mvp.model.InComing
import com.hazz.aipick.net.*


class InComingPresenter(view: InComingContract.incomingView) : BasePresenter<InComingContract.incomingView>(view) {

    fun getTradeIncoming(timeFilter: String) {



        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("timeFilter", timeFilter)


        )

        val login = RetrofitManager.service.tradeIncoming(body)

        doRequest(login, object : Callback<List<InComing>>(view) {
            override fun failed(tBaseResult: BaseResult<List<InComing>>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<List<InComing>>) {
                view.getTradeIncoming(tBaseResult.data!!)
            }

        }, true)

    }

    fun getIncoming(timeFilter: String) {



        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("timeFilter", timeFilter)


        )

        val login = RetrofitManager.service.incoming(body)

        doRequest(login, object : Callback<List<InComing>>(view) {
            override fun failed(tBaseResult: BaseResult<List<InComing>>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<List<InComing>>) {
                view.getIncoming(tBaseResult.data!!)
            }

        }, true)

    }



}