package com.hazz.aipick.mvp.contract

import com.hazz.aipick.mvp.model.bean.SimulateSummaryBean
import com.hazz.aipick.net.BaseView


/**
 * Description:
 * Date：2019/4/9-14:03
 * Author: cwh
 */
interface SimulateContract {

    interface SimulateView : BaseView {
        /**
         *
         */
        fun setSummary(bean: SimulateSummaryBean)
    }


}