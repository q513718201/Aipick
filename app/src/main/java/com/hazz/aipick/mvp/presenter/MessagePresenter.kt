package com.hazz.aipick.mvp.presenter


import android.util.Base64
import android.util.Pair
import com.hazz.aipick.mvp.contract.LoginContract
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.model.bean.*
import com.hazz.aipick.net.*
import com.hazz.aipick.utils.RsaUtils
import java.nio.charset.Charset


class MessagePresenter(view: WaletContract.messageView) : BasePresenter<WaletContract.messageView>(view) {



    fun messageList(pageNumber:Int,pageSize:Int) {

        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("pageNumber", pageNumber),
                Pair.create<Any, Any>("pageSize", pageSize)


        )

        val login = RetrofitManager.service.messageList(body)

        doRequest(login, object : Callback<List<Message>>(view) {
            override fun failed(tBaseResult: BaseResult<List<Message>>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<List<Message>>) {
                view.messageList(tBaseResult.data!!)
            }

        }, true)

    }

    fun readAll(messageId:Int,readAll:String) {

        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("messageId", messageId),
                Pair.create<Any, Any>("readAll", readAll)


        )

        val login = RetrofitManager.service.messageRead(body)

        doRequest(login, object : Callback<Any>(view) {
            override fun failed(tBaseResult: BaseResult<Any>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<Any>) {
                view.readAll(tBaseResult.msg)
            }

        }, true)

    }
}