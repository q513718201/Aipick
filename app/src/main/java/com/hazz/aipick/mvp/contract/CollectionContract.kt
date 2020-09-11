package com.hazz.aipick.mvp.contract

import com.hazz.aipick.mvp.model.bean.Collection
import com.hazz.aipick.mvp.model.bean.MySubscribe
import com.hazz.aipick.mvp.model.bean.SubscribeDesc
import com.hazz.aipick.mvp.model.bean.UserSubscribeBean
import com.hazz.aipick.net.BaseView


/**
 * Description:
 * Date：2019/4/9-14:03
 * Author: cwh
 */
interface CollectionContract {

    interface collectionView : BaseView {

        fun getCollection(msg: List<Collection>)
        fun optionResult(msg: String)
    }

    interface subscribeView : BaseView {

        fun mySubscribe(msg: List<MySubscribe>)
        fun mySubscribeDesc(msg: SubscribeDesc)
        fun switchSucceed(msg: String)
    }

    interface userSubscribeView : BaseView {

        fun userSubscribe(msg: List<UserSubscribeBean>)
    }

}