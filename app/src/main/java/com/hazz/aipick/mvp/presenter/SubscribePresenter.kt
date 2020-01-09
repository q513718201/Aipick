package com.hazz.aipick.mvp.presenter


import android.util.Base64
import android.util.Pair
import com.hazz.aipick.mvp.contract.CollectionContract
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.*
import com.hazz.aipick.mvp.model.bean.Collection
import com.hazz.aipick.net.*
import com.hazz.aipick.utils.RsaUtils
import java.nio.charset.Charset


class SubscribePresenter(view: CollectionContract.subscribeView) : BasePresenter<CollectionContract.subscribeView>(view) {


    fun getCollection(objType:String,subDirect:String,timeRange:String,
                      pageNumber:Int,pageSize:Int) {

        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("objType", objType),
                Pair.create<Any, Any>("subDirect", subDirect),
                Pair.create<Any, Any>("timeRange", timeRange),
                Pair.create<Any, Any>("pageNumber", pageNumber),
                Pair.create<Any, Any>("pageSize", pageSize)




        )

        val login = RetrofitManager.service.mySubscribe(body)

        doRequest(login, object : Callback<List<MySubscribe>>(view) {
            override fun failed(tBaseResult: BaseResult<List<MySubscribe>>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<List<MySubscribe>>) {
                view.mySubscribe(tBaseResult.data!!)
            }

        }, true)

    }


    fun mySubscribeDesc(subId:String,
                      pageNumber:Int,pageSize:Int) {

        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("subId", subId),
                Pair.create<Any, Any>("pageNumber", pageNumber),
                Pair.create<Any, Any>("pageSize", pageSize)

        )

        val login = RetrofitManager.service.mySubscribeDesc(body)

        doRequest(login, object : Callback<SubscribeDesc>(view) {
            override fun failed(tBaseResult: BaseResult<SubscribeDesc>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<SubscribeDesc>) {
                view.mySubscribeDesc(tBaseResult.data!!)
            }

        }, true)

    }
    fun mySubscribeSwitch(subId:String) {

        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("subId", subId)

        )

        val login = RetrofitManager.service.mySubscribeSwitch(body)

        doRequest(login, object : Callback<Any>(view) {
            override fun failed(tBaseResult: BaseResult<Any>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<Any>) {
                view.switchSucceed(tBaseResult.msg)
            }

        }, true)

    }
}