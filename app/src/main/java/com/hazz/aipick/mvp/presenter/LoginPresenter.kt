package com.hazz.aipick.mvp.presenter


import android.util.Pair
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.model.bean.LoginBean
import com.hazz.aipick.mvp.model.bean.UserInfo
import com.hazz.aipick.net.*
import com.hazz.aipick.utils.RsaUtils


class LoginPresenter(view: LoginContract.LoginView) : BasePresenter<LoginContract.LoginView>(view) {

    fun login(codeType: String, countryCode: String, credential: String, password: String
                ) {



        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("codeType", codeType),
                Pair.create<Any, Any>("countryCode", countryCode),
                Pair.create<Any, Any>("credential", RsaUtils.jiami(credential)),
                Pair.create<Any, Any>("password", RsaUtils.jiami(password))

        )

        val login = RetrofitManager.service.login(body)

        doRequest(login, object : Callback<LoginBean>(view) {
            override fun failed(tBaseResult: BaseResult<LoginBean>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<LoginBean>) {
                view.loginSuccess(tBaseResult.data!!)
            }

        }, true)

    }

    fun userInfo() {
        val login = RetrofitManager.service.userInfo()

        doRequest(login, object : Callback<UserInfo>(view) {
            override fun failed(tBaseResult: BaseResult<UserInfo>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<UserInfo>) {
                view.setUserInfo(tBaseResult.data!!)
            }

        }, false)

    }


}