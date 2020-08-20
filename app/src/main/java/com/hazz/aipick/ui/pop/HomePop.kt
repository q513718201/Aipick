package com.hazz.aipick.ui.pop

import android.content.Context
import android.view.View
import com.hazz.aipick.R
import com.hazz.aipick.utils.DpUtils
import com.hazz.aipick.utils.SPUtil
import kotlinx.android.synthetic.main.pop_home.view.*
import razerdp.basepopup.BasePopupWindow

class HomePop(context: Context) : BasePopupWindow(context, DpUtils.getDeviceWidthAndHeight(context)[0], DpUtils.getDeviceWidthAndHeight(context)[1]) {

    override fun onCreateContentView(): View {
        val view = createPopupById(R.layout.pop_home)
        view.iv_close.setOnClickListener {
            dismiss()
        }
        view.iv_get_benefit.setOnClickListener {
            SPUtil.putBoolean("isfirstHome", false)
            mOnClickListener?.onClick(it)
            dismiss()
        }
        setOutSideDismiss(false)
        return view
    }

    var mOnClickListener: OnClickListener? = null

    interface OnClickListener {
        fun onClick(v: View)
    }

}