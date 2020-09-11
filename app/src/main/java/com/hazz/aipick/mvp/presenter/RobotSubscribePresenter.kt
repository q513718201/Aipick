package com.hazz.aipick.mvp.presenter


import com.hazz.aipick.mvp.contract.SimulateContract
import com.hazz.aipick.mvp.model.bean.SubscribeBean
import com.hazz.aipick.net.BasePresenter
import com.hazz.aipick.net.BaseResult
import com.hazz.aipick.net.Callback
import com.hazz.aipick.net.RetrofitManager


class RobotSubscribePresenter(view: SimulateContract.SubscribeView) : BasePresenter<SimulateContract.SubscribeView>(view) {

    fun subscribe() {


        val login = RetrofitManager.service.robotSub()

        doRequest(login, object : Callback<SubscribeBean>(view) {
            override fun failed(tBaseResult: BaseResult<SubscribeBean>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<SubscribeBean>) {
                view.subscribeSuccess(tBaseResult.data!!)
            }

        }, true)

    }

}