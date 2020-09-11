package com.hazz.aipick.mvp.presenter


import android.util.Pair
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.IncomeBean
import com.hazz.aipick.net.*


class WalletIncomePresenter(view: WaletContract.IncomeView) : BasePresenter<WaletContract.IncomeView>(view) {

    fun getIncomeList(filterType: String, pageNumber: Int, pageSize: Int) {
        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("filterType", filterType),
                Pair.create<Any, Any>("pageNumber", pageNumber),
                Pair.create<Any, Any>("pageSize", pageSize)
        )

        val login = RetrofitManager.service.getIncomeList(body)

        doRequest(login, object : Callback<List<IncomeBean>>(view) {
            override fun failed(tBaseResult: BaseResult<List<IncomeBean>>): Boolean {
                return false
            }

            override fun success(tBaseResult: BaseResult<List<IncomeBean>>) {
                view.setIncomeList(tBaseResult.data!!)
            }

        }, true)

    }
}