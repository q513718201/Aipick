package com.hazz.aipick.mvp.presenter


import android.util.Pair
import com.hazz.aipick.mvp.contract.CollectionContract
import com.hazz.aipick.mvp.model.bean.Collection
import com.hazz.aipick.net.*


class CollectionPresenter(view: CollectionContract.collectionView) : BasePresenter<CollectionContract.collectionView>(view) {


    fun getCollection(objType:String,pageNumber:Int,pageSize:Int) {

        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("objType", objType),
                Pair.create<Any, Any>("pageNumber", pageNumber),
                Pair.create<Any, Any>("pageSize", pageSize)


        )

        val login = RetrofitManager.service.getCollection(body)

        doRequest(login, object : Callback<List<Collection>>(view) {
            override fun failed(tBaseResult: BaseResult<List<Collection>>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<List<Collection>>) {
                view.getCollection(tBaseResult.data!!)
            }

        }, true)

    }

    fun addCollection(objType:String,objId:String,baseCoin:String,quoteCoin:String) {

        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("objType", objType),
                Pair.create<Any, Any>("objId", objId),
                Pair.create<Any, Any>("baseCoin", baseCoin),
                Pair.create<Any, Any>("quoteCoin", quoteCoin)


        )

        val login = RetrofitManager.service.addCollection(body)

        doRequest(login, object : Callback<Any>(view) {
            override fun failed(tBaseResult: BaseResult<Any>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<Any>) {
                view.addCollectionSucceed(tBaseResult.msg)
            }

        }, true)

    }
}