package com.hazz.aipick.mvp.presenter

import com.hazz.aipick.mvp.contract.SimulateContract
import com.hazz.aipick.mvp.model.bean.SimulateSummaryBean
import com.hazz.aipick.net.BasePresenter
import com.hazz.aipick.net.BaseResult
import com.hazz.aipick.net.Callback
import com.hazz.aipick.net.RetrofitManager

class SimulatorPresenter(view: SimulateContract.SimulateView) : BasePresenter<SimulateContract.SimulateView>(view) {
    fun getSummary() {
        val login = RetrofitManager.service.getSimulateSummary()

        doRequest(login, object : Callback<SimulateSummaryBean>(view) {
            override fun failed(tBaseResult: BaseResult<SimulateSummaryBean>): Boolean {
                return false
            }

            override fun success(tBaseResult: BaseResult<SimulateSummaryBean>) {
                tBaseResult.data?.let { view.setSummary(it) }
            }
        }, true)
    }
}