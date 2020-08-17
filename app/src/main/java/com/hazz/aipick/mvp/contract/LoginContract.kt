package com.hazz.aipick.mvp.contract

import com.hazz.aipick.mvp.model.bean.*
import com.hazz.aipick.net.BaseView


/**
 * Description:
 * Date：2019/4/9-14:03
 * Author: cwh
 */
interface LoginContract {

    interface RegistView : BaseView {

        fun onRegistSuccess(msg: String)
        fun onSendMesSuccess(msg: String)
    }


    interface LoginView : BaseView {
        fun loginSuccess(msg: LoginBean)
        fun getUserInfo(msg: UserInfo)
    }


    interface bindCarView : BaseView {
        fun bindSuccess(msg: String)

    }

    interface updateView : BaseView {
        fun updateSuccess(msg: String)

    }

    interface tradeView : BaseView {
        fun traderAuthSuccess(msg: String)
        fun getCoin(msg: List<String>)
        fun traderSetSucceed(msg: String)
        fun traderSetQuery(msg: TraderSet)
    }

    interface FansView : BaseView {
        fun funsList(msg: Fans)
        fun setFollow(msg: String)

    }

    interface CoinView : BaseView {
        fun coinList(msg: Coin)

    }

}