package com.hazz.aipick.mvp.contract

import com.hazz.aipick.mvp.model.bean.*
import com.hazz.aipick.net.BaseView


interface HomeContract {


    interface homeView : BaseView {
        fun getHomeMsg(msg: List<Home>)
        fun setRate(bean: RateBean)
    }
    interface InfoView:BaseView{
        fun setInfoList(data:List<InfoBean>)
    }

    interface payView : BaseView {
        fun payResult(msg: PayResultMine)
        fun createId(msg: CreateId)
        fun payCancle(msg: String)
        fun paySucceed(msg: PaySucceed)
    }

}