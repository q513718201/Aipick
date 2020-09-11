package com.hazz.aipick.mvp.presenter


import android.util.Pair
import com.hazz.aipick.mvp.contract.HomeContract
import com.hazz.aipick.mvp.model.bean.Home
import com.hazz.aipick.mvp.model.bean.PayResultMine
import com.hazz.aipick.mvp.model.bean.RateBean
import com.hazz.aipick.net.*
import com.hazz.aipick.utils.SPUtil


class HomePresenter(view: HomeContract.homeView) : BasePresenter<HomeContract.homeView>(view) {

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
                tBaseResult.data?.let { view.getHomeMsg(it) }
            }

        }, true)

    }

    fun getRate() {
        val rate = RetrofitManager.service.getRate()

        doRequest(rate, object : Callback<RateBean>(view) {

            override fun failed(tBaseResult: BaseResult<RateBean>): Boolean {
                return false
            }

            override fun success(tBaseResult: BaseResult<RateBean>) {
                tBaseResult.data?.let { view.setRate(it) }
            }
        }, false)

    }


}