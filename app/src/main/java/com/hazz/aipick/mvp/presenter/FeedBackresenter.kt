package com.hazz.aipick.mvp.presenter


import android.util.Pair
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.net.*

class FeedBackresenter(view: WaletContract.feedAndHelp) : BasePresenter<WaletContract.feedAndHelp>(view) {

    fun feedBack(content: String, contact: String, image1: String, image2: String, image3: String, image4: String) {


        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("content", content),
                Pair.create<Any, Any>("contact", contact),
                Pair.create<Any, Any>("image1", image1),
                Pair.create<Any, Any>("image2", image2),
                Pair.create<Any, Any>("image3", image3),
                Pair.create<Any, Any>("image4", image4)
        )

        val sendMsg = RetrofitManager.service.feedBack(body)

        doRequest(sendMsg, object : Callback<Any>(view) {
            override fun failed(tBaseResult: BaseResult<Any>): Boolean {

                return false
            }

            override fun success(tBaseResult: BaseResult<Any>) {
                view.completeResult(tBaseResult.msg)
            }

        }, true)

    }


}