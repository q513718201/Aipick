package com.hazz.aipick.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.text.TextUtils
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import com.hazz.aipick.MyApplication
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.socket.WsManager
import com.hazz.aipick.utils.SPUtil
import kotlinx.android.synthetic.main.activity_splash.*
import pub.devrel.easypermissions.EasyPermissions


/**
 * Created by xuhao on 2017/12/5.
 * desc: 启动页
 */

class SplashActivity : BaseActivity() {


    private var textTypeface: Typeface?=null

    private var descTypeFace: Typeface?=null

    private var alphaAnimation:AlphaAnimation?=null


    override fun layoutId(): Int = R.layout.activity_splash

    override fun initData() {

    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        WsManager.getInstance().init()
        //渐变展示启动屏
        alphaAnimation= AlphaAnimation(0.3f, 1.0f)
        alphaAnimation?.duration = 2000
        alphaAnimation?.setAnimationListener(object : AnimationListener {
            override fun onAnimationEnd(arg0: Animation) {
                redirectTo()
            }

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationStart(animation: Animation) {}

        })
        if (alphaAnimation != null) {
            iv_web_icon.startAnimation(alphaAnimation)
        }

    }

    override fun start() {

    }


    fun redirectTo() {
        val string = SPUtil.getString("token")
        val boolean = SPUtil.getBoolean("isGuide")
              if(boolean){
            if(TextUtils.isEmpty(string)){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this, MainActivityNew::class.java)
                startActivity(intent)

            }
        }else{
            val intent = Intent(this, GuideActivity::class.java)
            startActivity(intent)

        }

//        val intent = Intent(this, MainActivityNew::class.java)
//        startActivity(intent)
       finish()
    }




}