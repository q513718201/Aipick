package com.hazz.aipick.mvp.contract

import com.hazz.aipick.mvp.model.InComing
import com.hazz.aipick.mvp.model.bean.*
import com.hazz.aipick.net.BaseView


interface InComingContract {


    interface incomingView: BaseView {
        fun getIncoming(msg:List<InComing>)
        fun getTradeIncoming(msg:List<InComing>)
    }


}