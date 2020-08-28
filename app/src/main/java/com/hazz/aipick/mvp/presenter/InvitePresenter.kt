package com.hazz.aipick.mvp.presenter

import com.hazz.aipick.mvp.contract.UserContract
import com.hazz.aipick.mvp.model.bean.InviteInfoBean
import com.hazz.aipick.net.BasePresenter
import com.hazz.aipick.net.BaseResult
import com.hazz.aipick.net.Callback
import com.hazz.aipick.net.RetrofitManager

class InvitePresenter(view: UserContract.InviteView) : BasePresenter<UserContract.InviteView>(view) {

    fun getInviteInfo() {
        val login = RetrofitManager.service.getInviteInfo()

        doRequest(login, object : Callback<InviteInfoBean>(view) {
            override fun failed(tBaseResult: BaseResult<InviteInfoBean>): Boolean {
                return false
            }

            override fun success(tBaseResult: BaseResult<InviteInfoBean>) {
                view.setInviteInfo(tBaseResult.data!!)
            }

        }, true)
    }
}