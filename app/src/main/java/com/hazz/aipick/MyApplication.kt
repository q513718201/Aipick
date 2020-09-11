package com.hazz.aipick

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.hazz.aipick.socket.ForegroundCallbacks
import com.hazz.aipick.socket.WsManager
import com.hazz.aipick.utils.*
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.*
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import com.tencent.bugly.crashreport.CrashReport
import kotlin.properties.Delegates


class MyApplication : Application(), DefaultRefreshFooterCreater, DefaultRefreshHeaderCreater {

    private var refWatcher: RefWatcher? = null

    companion object {

        private val TAG = "MyApplication"

        var context: Context by Delegates.notNull()
            private set

        fun getRefWatcher(context: Context): RefWatcher? {
            val myApplication = context.applicationContext as MyApplication
            return myApplication.refWatcher
        }

    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        if (BuildConfig.DEBUG)
//            refWatcher = setupLeakCanary()
        initConfig()
        DisplayManager.init(this)
        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
        DensityUtils.setDensity(this)
        SPUtil.init(this)
        SToast.initToast(this)
        initAppStatusListener()
        initRefresh()
        CrashReport.initCrashReport(this, Constants.BUGLY_APPID, false);
    }

    private fun initRefresh() {
        SmartRefreshLayout.setDefaultRefreshFooterCreater(this)
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(this)

    }

    private fun setupLeakCanary(): RefWatcher {
        return if (LeakCanary.isInAnalyzerProcess(this)) {
            RefWatcher.DISABLED
        } else LeakCanary.install(this)
    }


    /**
     * 初始化配置
     */
    private fun initConfig() {

        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(0)
                .methodOffset(7)
                .tag("hao_zz")
                .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }

    private val mActivityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

            DensityUtils.setDefault(activity)
            ActivityManager.getInstance().addActivity(activity)
        }

        override fun onActivityStarted(activity: Activity) {

        }

        override fun onActivityResumed(activity: Activity) {

        }

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {

        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }


        override fun onActivityDestroyed(activity: Activity) {
            Log.d(TAG, "onDestroy: " + activity.componentName.className)
            ActivityManager.getInstance().finishActivity(activity)
            RxBus.get().unSubscribe(activity)
        }
    }

    /**
     * 初始化应用前后台状态监听
     */
    private fun initAppStatusListener() {
        ForegroundCallbacks.init(this).addListener(object : ForegroundCallbacks.Listener {
            override fun onBecameForeground() {
                Logger.t("WsManager").d("应用回到前台调用重连方法")
                WsManager.getInstance().reconnect()
            }

            override fun onBecameBackground() {
                // WsManager.getInstance().disconnect()
            }
        })
    }

    override fun createRefreshFooter(context: Context?, layout: RefreshLayout?): RefreshFooter {
        layout?.setPrimaryColorsId(R.color.color_translucent, android.R.color.white) //全局设置主题颜色
        //指定为经典Footer
        var classicsFooter = ClassicsFooter(context)
        return classicsFooter.setSpinnerStyle(SpinnerStyle.Translate) //指定为经典Header
    }

    override fun createRefreshHeader(context: Context?, layout: RefreshLayout?): RefreshHeader {
        layout?.setPrimaryColorsId(R.color.color_translucent, android.R.color.white) //全局设置主题颜色
        val classicsHeader = ClassicsHeader(context)
        return classicsHeader.setSpinnerStyle(SpinnerStyle.Translate) //指定为经典Header
    }

}
