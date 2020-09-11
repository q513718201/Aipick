package com.hazz.aipick.widget

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.hazz.aipick.R
import com.hazz.aipick.utils.ToastUtils
import kotlinx.android.synthetic.main.mine_dialog_type_chose.view.*

class DialogInsertCoinHouse(var context: Context) {



    init {
        onCreate()
    }

    lateinit var onConfirm: (String, String, String) -> Unit
    private lateinit var mDialog: Dialog
    private fun onCreate() {
        mDialog = Dialog(context)

        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = View.inflate(context, R.layout.mine_dialog_type_chose, null)
        view.mTvCancel.setOnClickListener {
            dismiss()
        }
        view.mTvConfirm.setOnClickListener {
            dismiss()
            if (::onConfirm.isInitialized) {
                when {
                    view.et1.text.isNullOrBlank() -> {
                        ToastUtils.showToast(context, "请输入交易所名称")
                    }
                    view.et2.text.isNullOrBlank() -> {
                        ToastUtils.showToast(context, "请输入交易所地址")
                    }
                    view.et3.text.isNullOrBlank() -> {
                        ToastUtils.showToast(context, "请输入API地址")
                    }
                    else -> onConfirm(view.et1.text.toString(), view.et2.text.toString(), view.et3.text.toString())
                }
            }

        }

        mDialog.setContentView(view)
        mDialog.setCanceledOnTouchOutside(true)
        mDialog.setCancelable(true)
        val window = mDialog.window
        //要加上设置背景，否则dialog宽高设置无作用
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        window.decorView.setPadding(0, 0, 0, 0) //消除边距
        val layoutParams = window.attributes
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        //layoutParams.y = DensityUtils.dip2px(context, 20);
        layoutParams.gravity = Gravity.BOTTOM
        window.attributes = layoutParams
    }

    fun show() {
        if (mDialog != null && !mDialog.isShowing) {
            mDialog.show()
        }
    }

    fun dismiss() {
        if (mDialog != null && mDialog.isShowing) {
            mDialog.dismiss()
        }
    }


}