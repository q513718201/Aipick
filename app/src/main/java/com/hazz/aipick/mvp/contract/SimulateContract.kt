package com.hazz.aipick.mvp.contract

import com.hazz.aipick.mvp.model.bean.CategoryDetailBean
import com.hazz.aipick.mvp.model.bean.SimulateSummaryBean
import com.hazz.aipick.mvp.model.bean.SubscribeBean
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

    interface CategoryDetailView : BaseView {
        fun setDetail(bean: CategoryDetailBean)
    }
    interface SubscribeView:BaseView{
        fun subscribeSuccess(bean: SubscribeBean)
    }

}