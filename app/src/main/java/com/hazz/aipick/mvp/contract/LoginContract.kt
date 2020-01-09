package com.hazz.aipick.mvp.contract

import com.hazz.aipick.mvp.model.bean.LoginBean
import com.hazz.aipick.mvp.model.bean.UserInfo
import com.hazz.aipick.net.BaseView
import com.hazz.aipick.net.IBaseView


/**
 * Description:
 * Date：2019/4/9-14:03
 * Author: cwh
 */
interface LoginContract {

    interface RegistView: BaseView {

        fun onRegistSuccess(msg:String)
        fun onSendMesSuccess(msg:String)
    }


    interface LoginView: BaseView {
        fun loginSuccess(msg:LoginBean)
        fun getUserInfo(msg: UserInfo)
    }


    interface bindCarView: BaseView {
        fun bindSuccess(msg:String)

    }

    interface updateView: BaseView {
        fun updateSuccess(msg:String)

    }
    interface tradeView: BaseView {
        fun traderAuthSuccess(msg:String)

    }
}