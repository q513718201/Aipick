package com.hazz.aipick.mvp.presenter


import android.util.Base64
import android.util.Log
import android.util.Pair
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.*
import com.hazz.aipick.net.*
import com.hazz.aipick.utils.RsaUtils
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


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