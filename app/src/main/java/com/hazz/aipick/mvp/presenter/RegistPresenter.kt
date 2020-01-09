package com.hazz.aipick.mvp.presenter


import android.util.Base64
import android.util.Pair
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.net.*
import com.hazz.aipick.utils.RsaUtils
import java.nio.charset.Charset


class RegistPresenter(view: LoginContract.RegistView) : BasePresenter<LoginContract.RegistView>(view) {

    fun regist(codeType: String, countryCode: String, credential: String, loginPassword: String,
                 code: String) {

        val loadPublicKey = RsaUtils.loadPublicKey(RsaUtils.key)
        val encryptData = RsaUtils.encryptData(loginPassword.toByteArray(), loadPublicKey)
        val encode = Base64.encode(encryptData, Base64.NO_WRAP)
        val pwd = String(encode, Charset.forName("utf-8"))



        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("codeType", codeType),
                Pair.create<Any, Any>("countryCode", countryCode),
                Pair.create<Any, Any>("credential", RsaUtils.jiami(credential)),
                Pair.create<Any, Any>("loginPassword", RsaUtils.jiami(loginPassword)),
                Pair.create<Any, Any>("code", code)
        )

        val login = RetrofitManager.service.regist(body)

        doRequest(login, object : Callback<Any>(view) {
            override fun failed(tBaseResult: BaseResult<Any>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<Any>) {
                view.onRegistSuccess(tBaseResult.msg)
            }

        }, true)

    }


    fun sendCode(codeType: String, countryCode: String, credential: String, type: String){


        val loadPublicKey = RsaUtils.loadPublicKey(RsaUtils.key)
        val encryptData = RsaUtils.encryptData(credential.toByteArray(), loadPublicKey)
        val encode = Base64.encode(encryptData, Base64.NO_WRAP)
        val pwd = String(encode, Charset.forName("utf-8"))


        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("codeType", codeType),
                Pair.create<Any, Any>("countryCode", countryCode),
                Pair.create<Any, Any>("credential", RsaUtils.jiami(credential)),
                Pair.create<Any, Any>("type", type)

        )


        val login = RetrofitManager.service.sendMsg(body)

        doRequest(login, object : Callback<Any>(view) {
            override fun failed(tBaseResult: BaseResult<Any>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<Any>) {
                view.onSendMesSuccess(tBaseResult.msg)
            }

        }, true)

    }


    fun sendCodeLogin(codeType: String, countryCode: String, credential: String, type: String){


        val loadPublicKey = RsaUtils.loadPublicKey(RsaUtils.key)
        val encryptData = RsaUtils.encryptData(credential.toByteArray(), loadPublicKey)
        val encode = Base64.encode(encryptData, Base64.NO_WRAP)
        val pwd = String(encode, Charset.forName("utf-8"))


        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("codeType", codeType),
                Pair.create<Any, Any>("countryCode", countryCode),
                Pair.create<Any, Any>("credential", RsaUtils.jiami(credential)),
                Pair.create<Any, Any>("type", type)

        )


        val login = RetrofitManager.service.sendMsglogin(body)

        doRequest(login, object : Callback<Any>(view) {
            override fun failed(tBaseResult: BaseResult<Any>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<Any>) {
                view.onSendMesSuccess(tBaseResult.msg)
            }

        }, true)

    }
}