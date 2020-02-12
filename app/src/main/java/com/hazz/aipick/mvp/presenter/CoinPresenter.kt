package com.hazz.aipick.mvp.presenter


import android.util.Base64
import android.util.Log
import android.util.Pair
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.model.bean.Coin
import com.hazz.aipick.mvp.model.bean.Fans
import com.hazz.aipick.mvp.model.bean.LoginBean
import com.hazz.aipick.mvp.model.bean.UserInfo
import com.hazz.aipick.net.*
import com.hazz.aipick.utils.RsaUtils
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


class CoinPresenter(view: LoginContract.CoinView) : BasePresenter<LoginContract.CoinView>(view) {

    fun coinList() {


//        val body = RequestUtils.getBody(
//                Pair.create<Any, Any>("type", type),
//                Pair.create<Any, Any>("userId", userId),
//                Pair.create<Any, Any>("pageNumber", pageNumber),
//                Pair.create<Any, Any>("pageSize", pageSize)
//
//        )

        val login = RetrofitManager.serviceCoin.getCoinDesc()

        doRequest1(login, object : Observer<Coin> {
            override fun onComplete() {
               view.hideLoading()
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: Coin) {
                Log.d("junjun", "请求数据")
                view.coinList(t)
            }

            override fun onError(e: Throwable) {
                Log.d("junjun",e.message)
            }

        }, true)
    }


}