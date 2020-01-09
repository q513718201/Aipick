package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.utils.ToolBarCustom
import kotlinx.android.synthetic.main.activity_bind_phone.*



class PhoneModifyActivity : BaseActivity() {


    override fun layoutId(): Int = R.layout.activity_bind_phone


    override fun initData() {

    }
    private var isBind=false

    private var num=""
    private var type=""
    private var onLeftIconClickListener:ToolBarCustom.Builder?=null

    @SuppressLint("SetTextI18n")
    override fun initView() {
          onLeftIconClickListener = ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)

                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view ->
                    finish()
                }

    }

    override fun start() {
        type= intent.getStringExtra("type")
        num= intent.getStringExtra("num")
        isBind= intent.getBooleanExtra("isBind",false)

        if(type == "phone"){
            if(isBind){
                onLeftIconClickListener?.setTitle(getString(R.string.modify_phone))
                tv1.text=getString(R.string.current_bind_phone,num)
            }else{
                rl.visibility= View.GONE
                onLeftIconClickListener?.setTitle(getString(R.string.bind_phone))
            }

        }else{
            if(isBind){
                onLeftIconClickListener?.setTitle(getString(R.string.modify_email))
                tv1.text=getString(R.string.current_bind_email,num)
            }else{
                rl.visibility= View.GONE
                onLeftIconClickListener?.setTitle(getString(R.string.bind_email))
            }

        }
    }


}
