package com.hazz.aipick.mvp.presenter


import android.util.Base64
import android.util.Pair
import com.hazz.aipick.mvp.contract.CollectionContract
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.Collection
import com.hazz.aipick.mvp.model.bean.LoginBean
import com.hazz.aipick.mvp.model.bean.TixianRecord
import com.hazz.aipick.mvp.model.bean.UserInfo
import com.hazz.aipick.mvp.model.bean.Walet
import com.hazz.aipick.net.*
import com.hazz.aipick.utils.RsaUtils
import java.nio.charset.Charset


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

}