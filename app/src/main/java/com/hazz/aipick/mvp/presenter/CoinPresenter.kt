package com.hazz.aipick.mvp.presenter


import android.util.Pair
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.BindCoinHouse
import com.hazz.aipick.mvp.model.bean.CategoryDetailBean
import com.hazz.aipick.net.*


class CoinPresenter(view:  WaletContract.CoinView) : BasePresenter< WaletContract.CoinView>(view) {


    fun coinList(subee: String) {

        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("subee", subee)


        )

        val login = RetrofitManager.service.coinList(body)

        doRequest(login, object : Callback<BindCoinHouse>(view) {
            override fun failed(tBaseResult: BaseResult<BindCoinHouse>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<BindCoinHouse>) {
                view.coinList(tBaseResult.data!!)
            }

        }, false)

    }




}