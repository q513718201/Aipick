package com.hazz.aipick.mvp.presenter


import android.util.Base64
import android.util.Pair
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.LoginBean
import com.hazz.aipick.mvp.model.bean.TixianRecord
import com.hazz.aipick.mvp.model.bean.UserInfo
import com.hazz.aipick.mvp.model.bean.Walet
import com.hazz.aipick.net.*
import com.hazz.aipick.utils.RsaUtils
import java.nio.charset.Charset


class WaletPresenter(view: WaletContract.waletView) : BasePresenter<WaletContract.waletView>(view) {

    fun waletDesc() {


//
//        val body = RequestUtils.getBody(
//                Pair.create<Any, Any>("codeType", codeType),
//                Pair.create<Any, Any>("countryCode", countryCode),
//                Pair.create<Any, Any>("credential", RsaUtils.jiami(credential)),
//                Pair.create<Any, Any>("password", RsaUtils.jiami(password))
//
//        )

        val login = RetrofitManager.service.walet()

        doRequest(login, object : Callback<Walet>(view) {
            override fun failed(tBaseResult: BaseResult<Walet>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<Walet>) {
                view.getWalet(tBaseResult.data!!)
            }

        }, true)

    }

    fun tixian(amount:String,tradePassword:String) {

        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("amount", amount),
                Pair.create<Any, Any>("tradePassword", RsaUtils.jiami(tradePassword))


        )

        val login = RetrofitManager.service.tixian(body)

        doRequest(login, object : Callback<Any>(view) {
            override fun failed(tBaseResult: BaseResult<Any>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<Any>) {
                view.tixianSucceed(tBaseResult.msg)
            }

        }, true)

    }
    fun tixianRecord(pageNumber:Int,pageSize:Int) {

        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("pageNumber", pageNumber),
                Pair.create<Any, Any>("pageSize", pageSize)


        )

        val login = RetrofitManager.service.tixianRecord(body)

        doRequest(login, object : Callback<List<TixianRecord>>(view) {
            override fun failed(tBaseResult: BaseResult<List<TixianRecord>>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<List<TixianRecord>>) {
                view.tixianRecord(tBaseResult.data!!)
            }

        }, true)

    }

}