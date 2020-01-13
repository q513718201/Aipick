package com.hazz.aipick.mvp.presenter


import android.util.Base64
import android.util.Pair
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.model.bean.Fans
import com.hazz.aipick.mvp.model.bean.LoginBean
import com.hazz.aipick.mvp.model.bean.UserInfo
import com.hazz.aipick.net.*
import com.hazz.aipick.utils.RsaUtils
import java.nio.charset.Charset


class FansPresenter(view: LoginContract.FansView) : BasePresenter<LoginContract.FansView>(view) {

    fun fansList(type: String, userId: String, pageNumber: Int, pageSize: Int
                ) {



        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("type", type),
                Pair.create<Any, Any>("userId", userId),
                Pair.create<Any, Any>("pageNumber", pageNumber),
                Pair.create<Any, Any>("pageSize", pageSize)

        )

        val login = RetrofitManager.service.fansList(body)

        doRequest(login, object : Callback<Fans>(view) {
            override fun failed(tBaseResult: BaseResult<Fans>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<Fans>) {
                view.funsList(tBaseResult.data!!)
            }

        }, true)

    }




}