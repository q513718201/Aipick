package com.hazz.aipick.mvp.presenter


import android.util.Pair
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.*
import com.hazz.aipick.net.*


class AccountPresenter(view: WaletContract.myaccountView) : BasePresenter<WaletContract.myaccountView>(view) {



    fun myAccount(objUserId:String) {

        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("objUserId", objUserId)


        )

        val login = RetrofitManager.service.myAccount(body)

        doRequest(login, object : Callback<MyAccount>(view) {
            override fun failed(tBaseResult: BaseResult<MyAccount>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<MyAccount>) {
                view.myaccount(tBaseResult.data!!)
            }

        }, true)

    }


    fun coinList(subee:String) {

        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("subee", subee)


        )

        val login = RetrofitManager.service.coinList(body)

        doRequest(login, object : Callback<BindCoinHouse>(view) {
            override fun failed(tBaseResult: BaseResult<BindCoinHouse>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<BindCoinHouse>) {
                view.coinList(tBaseResult.data!!)
            }

        }, false)

    }


    fun getPrice(objUserId:String) {

        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("objUserId", objUserId)


        )

        val login = RetrofitManager.service.getPrice(body)

        doRequest(login, object : Callback<ChooseTime>(view) {
            override fun failed(tBaseResult: BaseResult<ChooseTime>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<ChooseTime>) {
                view.getPrice(tBaseResult.data!!)
            }

        }, true)

    }


    fun setFollow(subId:String,switch:String,followType:String,followFactor:String) {

        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("subId", subId),
                Pair.create<Any, Any>("switch", switch),
                Pair.create<Any, Any>("followType", followType),
                Pair.create<Any, Any>("followFactor", followFactor)

        )

        val login = RetrofitManager.service.setFollow(body)

        doRequest(login, object : Callback<Any>(view) {
            override fun failed(tBaseResult: BaseResult<Any>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<Any>) {
                view.setFollow(tBaseResult.msg)
            }

        }, true)

    }


    fun attention(objUserId:String) {
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
    fun attentionCancle(objUserId:String) {
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