package com.hazz.aipick.mvp.presenter

import android.util.Pair
import com.google.gson.Gson
import com.hazz.aipick.mvp.contract.HomeContract
import com.hazz.aipick.mvp.model.bean.InfoBean
import com.hazz.aipick.net.*
import com.hazz.aipick.utils.AssetsUtil
import java.nio.charset.Charset

class InfoPresenter(view: HomeContract.InfoView) : BasePresenter<HomeContract.InfoView>(view) {
    fun getInfoData(pageIndex: Int, pageSize: Int) {

        val assertsFileInBytes = AssetsUtil.getAssertsFileInBytes(context, "info.json")
        val json = String(assertsFileInBytes, Charset.defaultCharset())
        var msg: MutableList<InfoBean> = Gson().fromJson<Array<InfoBean>>(json, Array<InfoBean>::class.java).toMutableList()
        view.setInfoList(msg)

        val body = RequestUtils.getBody(
                Pair.create<Any, Any>("pageNumber", pageIndex),
                Pair.create<Any, Any>("pageSize", pageSize)
        )

        val login = RetrofitManager.service.getInfoList(body)

        doRequest(login, object : Callback<List<InfoBean>>(view) {
            override fun failed(tBaseResult: BaseResult<List<InfoBean>>): Boolean {
                return false
            }

            override fun success(tBaseResult: BaseResult<List<InfoBean>>) {
                view.setInfoList(tBaseResult.data!!)
            }

        }, true)
    }
}