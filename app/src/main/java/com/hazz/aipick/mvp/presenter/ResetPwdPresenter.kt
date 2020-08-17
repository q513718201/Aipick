package com.hazz.aipick.mvp.presenter


import android.util.Pair
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.net.*
import com.hazz.aipick.utils.RsaUtils


class ResetPwdPresenter(view: LoginContract.updateView) : BasePresenter<LoginContract.updateView>(view) {

    fun reset(codeType: String, countryCode: String?
               , credential: String , password: String , code: String) {


        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("codeType", codeType),
                Pair.create<Any, Any>("countryCode", countryCode),
                Pair.create<Any, Any>("credential",  RsaUtils.jiami(credential)),
                Pair.create<Any, Any>("password",  RsaUtils.jiami(password)),
                        Pair.create<Any, Any>("code", code)


        )

        val login = RetrofitManager.service.resetPwd(body)

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