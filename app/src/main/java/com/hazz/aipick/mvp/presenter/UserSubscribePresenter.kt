package com.hazz.aipick.mvp.presenter


import android.util.Pair
import com.hazz.aipick.mvp.contract.CollectionContract
import com.hazz.aipick.mvp.model.bean.UserSubscribeBean
import com.hazz.aipick.net.*


class UserSubscribePresenter(view: CollectionContract.userSubscribeView) : BasePresenter<CollectionContract.userSubscribeView>(view) {


    /**
     *首页用户订阅
     *   "objUserId": "1",  // 目标用户id
     *  "pageNumber": "1",  // 页码
     *  "pageSize": "10",  // 页大小
     *  "isDemo": "0",  // 是否获取模拟账户的数据, 1 是 0 否
     */
    fun userFollows(id: String, pageNumber: Int, pageSize: Int, isDemo: Int) {
        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("objUserId", id),
                Pair.create<Any, Any>("pageNumber", pageNumber),
                Pair.create<Any, Any>("pageSize", pageSize),
                Pair.create<Any, Any>("isDemo", isDemo)
        )

        val login = RetrofitManager.service.userFollows(body)

        doRequest(login, object : Callback<List<UserSubscribeBean>>(view) {
            override fun failed(tBaseResult: BaseResult<List<UserSubscribeBean>>): Boolean {
                return false
            }

            override fun success(tBaseResult: BaseResult<List<UserSubscribeBean>>) {
                view.userSubscribe(tBaseResult.data!!)
            }

        }, true)
    }
}