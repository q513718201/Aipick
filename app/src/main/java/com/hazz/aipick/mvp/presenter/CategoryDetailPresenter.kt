package com.hazz.aipick.mvp.presenter

import android.util.Pair
import com.hazz.aipick.mvp.contract.SimulateContract
import com.hazz.aipick.mvp.model.bean.CategoryDetailBean
import com.hazz.aipick.net.*

class CategoryDetailPresenter(view: SimulateContract.CategoryDetailView) : BasePresenter<SimulateContract.CategoryDetailView>(view) {
    fun getDetail(id: String) {
        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("objUserId", id)
        )

        val login = RetrofitManager.service.getStrategyDetail(body)

        doRequest(login, object : Callback<CategoryDetailBean>(view) {
            override fun failed(tBaseResult: BaseResult<CategoryDetailBean>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<CategoryDetailBean>) {
                tBaseResult.data?.let { view.setDetail(it) }
            }

        }, false)
    }
}