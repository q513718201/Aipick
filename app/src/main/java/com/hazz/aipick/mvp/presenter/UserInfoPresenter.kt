package com.hazz.aipick.mvp.presenter


import android.util.Pair
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.net.*
import com.hazz.aipick.utils.RsaUtils
import com.hazz.aipick.utils.SPUtil
import org.json.JSONObject


class UserInfoPresenter(view: LoginContract.updateView) : BasePresenter<LoginContract.updateView>(view) {

    fun update(updateType: String, location: String, code: String, avatar: String,
               nickname: String, countryCode: String, credential: String, codeType: String, password: String
               , newPassword: String
               , tradePassword: String
               , newTradePassword: String) {


        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("updateType", updateType),
                Pair.create<Any, Any>("location", location),
                Pair.create<Any, Any>("code", code),
                Pair.create<Any, Any>("avatar", avatar),
                Pair.create<Any, Any>("nickname", nickname),
                Pair.create<Any, Any>("countryCode", countryCode),
                Pair.create<Any, Any>("credential", RsaUtils.jiami(credential)),
                Pair.create<Any, Any>("codeType", codeType),
                Pair.create<Any, Any>("password", RsaUtils.jiami(password)),
                Pair.create<Any, Any>("newPassword", RsaUtils.jiami(newPassword)),
                Pair.create<Any, Any>("tradePassword", RsaUtils.jiami(tradePassword)),
                Pair.create<Any, Any>("newTradePassword", RsaUtils.jiami(newTradePassword))


        )

        val login = RetrofitManager.service.updata(body)

        doRequest(login, object : Callback<Any>(view) {
            override fun failed(tBaseResult: BaseResult<Any>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<Any>) {
                view.updateSuccess(tBaseResult.msg)
            }

        }, true)

    }

}