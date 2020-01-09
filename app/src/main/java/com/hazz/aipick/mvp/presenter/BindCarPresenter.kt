package com.hazz.aipick.mvp.presenter


import android.util.Base64
import android.util.Pair
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.model.bean.LoginBean
import com.hazz.aipick.mvp.model.bean.UserInfo
import com.hazz.aipick.net.*
import com.hazz.aipick.utils.RsaUtils
import java.nio.charset.Charset


class BindCarPresenter(view: LoginContract.bindCarView) : BasePresenter<LoginContract.bindCarView>(view) {

    fun bindCard(name: String, bank: String, branch: String, number: String,tradePassword:String
                ) {



        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("name", name),
                Pair.create<Any, Any>("bank", bank),
                Pair.create<Any, Any>("branch",branch),
                Pair.create<Any, Any>("number", RsaUtils.jiami(number)),
                Pair.create<Any, Any>("tradePassword", RsaUtils.jiami(tradePassword))

        )

        val login = RetrofitManager.service.bindCard(body)

        doRequest(login, object : Callback<Any>(view) {
            override fun failed(tBaseResult: BaseResult<Any>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<Any>) {
                view.bindSuccess(tBaseResult.msg)
            }

        }, true)

    }


}