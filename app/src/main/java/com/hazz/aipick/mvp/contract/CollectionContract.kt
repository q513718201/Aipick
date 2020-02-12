package com.hazz.aipick.mvp.contract

import com.hazz.aipick.mvp.model.bean.*
import com.hazz.aipick.mvp.model.bean.Collection
import com.hazz.aipick.net.BaseView
import com.hazz.aipick.net.IBaseView


/**
 * Description:
 * Date：2019/4/9-14:03
 * Author: cwh
 */
interface CollectionContract {

    interface collectionView: BaseView {

        fun getCollection(msg: List<Collection>)
        fun addCollectionSucceed(msg: String)
    }

    interface subscribeView: BaseView {

        fun mySubscribe(msg: List<MySubscribe>)
        fun mySubscribeDesc(msg: SubscribeDesc)
        fun switchSucceed(msg:String)
    }

}