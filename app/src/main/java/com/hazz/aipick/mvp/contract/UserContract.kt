package com.hazz.aipick.mvp.contract

import com.hazz.aipick.mvp.model.bean.InviteInfoBean
import com.hazz.aipick.net.BaseView


/**
 * 用户相关Contact
 */
interface UserContract {

    /**
     * 邀请好友
     */
    interface InviteView : BaseView {

        fun setInviteInfo(msg: InviteInfoBean)
    }
}