package com.hazz.aipick.mvp.presenter


import android.util.Base64
import android.util.Pair
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.*
import com.hazz.aipick.net.*
import com.hazz.aipick.utils.RsaUtils
import java.nio.charset.Charset


class CoinHousePresenter(view: WaletContract.coinHouseView) : BasePresenter<WaletContract.coinHouseView>(view) {

    fun coinHouseList() {


//
//        val body = RequestUtils.getBody(
//                Pair.create<Any, Any>("codeType", codeType),
//                Pair.create<Any, Any>("countryCode", countryCode),
//                Pair.create<Any, Any>("credential", RsaUtils.jiami(credential)),
//                Pair.create<Any, Any>("password", RsaUtils.jiami(password))
//
//        )

        val login = RetrofitManager.service.coinHouseList()

        doRequest(login, object : Callback<List<CoinHouse>>(view) {
            override fun failed(tBaseResult: BaseResult<List<CoinHouse>>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<List<CoinHouse>>) {
                view.coinHouseList(tBaseResult.data!!)
            }

        }, true)

    }


    fun addCoinHouse(name:String,url:String,apiUrl:String) {



        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("name", name),
                Pair.create<Any, Any>("url", url),
                Pair.create<Any, Any>("apiUrl",apiUrl)

        )

        val login = RetrofitManager.service.addCoinHouse(body)

        doRequest(login, object : Callback<Any>(view) {
            override fun failed(tBaseResult: BaseResult<Any>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<Any>) {
                view.addCoinHouseSucceed(tBaseResult.msg)
            }

        }, true)

    }


    fun bindCoinHouse(exchangeId:String,apiKey:String,secretKey:String,passphrase:String) {



        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("exchangeId", exchangeId),
                Pair.create<Any, Any>("apiKey", RsaUtils.jiami(apiKey)),
                Pair.create<Any, Any>("secretKey",RsaUtils.jiami(secretKey)),
                Pair.create<Any, Any>("passphrase",RsaUtils.jiami(passphrase))
        )

        val login = RetrofitManager.service.bindCoinHouse(body)

        doRequest(login, object : Callback<Any>(view) {
            override fun failed(tBaseResult: BaseResult<Any>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<Any>) {
                view.addCoinHouseSucceed(tBaseResult.msg)
            }

        }, true)

    }

    fun coinHouseDesc(exchangeId:Int) {



        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("exchangeId", exchangeId)

        )

        val login = RetrofitManager.service.coinHouseDesc(body)

        doRequest(login, object : Callback<CoinHouseDesc>(view) {
            override fun failed(tBaseResult: BaseResult<CoinHouseDesc>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<CoinHouseDesc>) {
                view.coinHouseDesc(tBaseResult.data!!)
            }

        }, true)

    }
}