package com.hazz.aipick.ui.pop

import android.content.Context
import android.view.View
import com.hazz.aipick.R
import com.hazz.aipick.utils.DpUtils
import razerdp.basepopup.BasePopupWindow

class AlertPop(context: Context) : BasePopupWindow(context, DpUtils.getDeviceWidthAndHeight(context)[0], DpUtils.getDeviceWidthAndHeight(context)[1]) {
    override fun onCreateContentView(): View {
        val view = createPopupById(R.layout.pop_home)

        return view
    }
}