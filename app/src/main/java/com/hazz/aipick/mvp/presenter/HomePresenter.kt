package com.hazz.aipick.mvp.presenter


import android.util.Base64
import android.util.Pair
import com.hazz.aipick.mvp.contract.HomeContract
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.model.bean.Home
import com.hazz.aipick.mvp.model.bean.LoginBean
import com.hazz.aipick.mvp.model.bean.UserInfo
import com.hazz.aipick.net.*
import com.hazz.aipick.utils.RsaUtils
import java.nio.charset.Charset


class HomePresenter(view: HomeContract.homeView) : BasePresenter< HomeContract.homeView>(view) {

    fun getHomeMsg(subeeType: String, pageNumber: Int, pageSize: Int, rateStart: String,

                   rateEnd: String, timesStart: String, timesEnd: String, pullbackStart: String,
                   pullbackEnd: String
    ) {


        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("subeeType", subeeType),
                Pair.create<Any, Any>("pageNumber", pageNumber),
                Pair.create<Any, Any>("pageSize", pageSize),
                Pair.create<Any, Any>("rateStart", rateStart),
                Pair.create<Any, Any>("rateEnd", rateEnd),
                Pair.create<Any, Any>("timesStart", timesStart),
                Pair.create<Any, Any>("timesEnd", timesEnd),
                Pair.create<Any, Any>("pullbackStart", pullbackStart),
                Pair.create<Any, Any>("pullbackEnd", pullbackEnd)
                )

        val login = RetrofitManager.service.homeList(body)

        doRequest(login, object : Callback<List<Home>>(view) {
            override fun failed(tBaseResult: BaseResult<List<Home>>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<List<Home>>) {
                view.getHomeMsg(tBaseResult.data!!)
            }

        }, true)

    }


}