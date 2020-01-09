package com.hazz.aipick.mvp.presenter


import android.util.Base64
import android.util.Pair
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.model.bean.LoginBean
import com.hazz.aipick.mvp.model.bean.UserInfo
import com.hazz.aipick.net.*
import com.hazz.aipick.utils.RsaUtils
import java.nio.charset.Charset


class TraderAuthPresenter(view: LoginContract.tradeView) : BasePresenter<LoginContract.tradeView>(view) {

    fun traderAuth(countryCode: String, code: String, email: String, phone: String,cardNumber:String,
                   name:String,cardType:String,front:String,back:String,promise:String

                ) {



        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("countryCode", countryCode),
                Pair.create<Any, Any>("code", code),
                Pair.create<Any, Any>("email",email),
                Pair.create<Any, Any>("phone", phone),
                Pair.create<Any, Any>("cardNumber", cardNumber),
                Pair.create<Any, Any>("name", name),
                Pair.create<Any, Any>("cardType", cardType),
                Pair.create<Any, Any>("front", front),
                Pair.create<Any, Any>("back", back),
                Pair.create<Any, Any>("promise", promise)




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


}