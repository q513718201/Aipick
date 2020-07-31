package com.hazz.aipick.mvp.presenter


import android.util.Base64
import android.util.Pair
import com.hazz.aipick.mvp.contract.InComingContract
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.model.InComing
import com.hazz.aipick.mvp.model.bean.LoginBean
import com.hazz.aipick.mvp.model.bean.UserInfo
import com.hazz.aipick.net.*
import com.hazz.aipick.utils.RsaUtils
import java.nio.charset.Charset


class InComingPresenter(view: InComingContract.incomingView) : BasePresenter<InComingContract.incomingView>(view) {

    fun getIncoming(timeFilter: String) {



        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("timeFilter", timeFilter)


        )

        val login = RetrofitManager.service.incoming(body)

        doRequest(login, object : Callback<List<InComing>>(view) {
            override fun failed(tBaseResult: BaseResult<List<InComing>>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<List<InComing>>) {
                view.getIncoming(tBaseResult.data!!)
            }

        }, true)

    }



}