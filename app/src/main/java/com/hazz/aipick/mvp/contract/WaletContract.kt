package com.hazz.aipick.mvp.contract

import com.hazz.aipick.mvp.model.bean.*
import com.hazz.aipick.net.BaseView
import com.hazz.aipick.net.IBaseView


/**
 * Description:
 * Date：2019/4/9-14:03
 * Author: cwh
 */
interface WaletContract {

    interface waletView: BaseView {

        fun getWalet(msg: Walet)
        fun tixianSucceed(msg: String)
        fun tixianRecord(msg: List<TixianRecord>)
    }

    interface feedAndHelp: BaseView {

        fun completeResult(msg:String)
    }

    interface coinHouseView: BaseView {

        fun coinHouseList(msg:List<CoinHouse>)
        fun addCoinHouseSucceed(msg:String)
        fun coinHouseDesc(msg:CoinHouseDesc)
}

    interface messageView: BaseView {

        fun messageList(msg:List<Message>)
        fun readAll(msg:String)
    }

    interface myaccountView: BaseView {

        fun myaccount(msg:MyAccount)
        fun moniaccount(msg:MoniAccount)
        fun coinList(msg:BindCoinHouse)
        fun getPrice(msg:ChooseTime)
        fun setFollow(msg:String)
    }
    interface CoinView:BaseView{
        fun coinList(msg:BindCoinHouse)
    }

    interface orderView: BaseView {

        fun getOrder(msg:Order)
    }
}