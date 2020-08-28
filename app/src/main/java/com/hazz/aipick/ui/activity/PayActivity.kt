package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.HomeContract
import com.hazz.aipick.mvp.model.bean.*
import com.hazz.aipick.mvp.presenter.PayPresenter
import com.hazz.aipick.utils.PayUtil
import com.hazz.aipick.utils.SToast
import com.hazz.aipick.utils.ToastUtils
import com.hazz.aipick.utils.ToolBarCustom
import com.xgr.easypay.callback.IPayCallback
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_pay.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class PayActivity : BaseActivity(), HomeContract.payView, IPayCallback {

    override fun paySucceed(msg: PaySucceed) {
        tv_succeed.visibility = View.VISIBLE
        ll_succeed.visibility = View.VISIBLE
        ll_choose.visibility = View.GONE
        isPay = true

        tv1.text = msg.user_name

        tv2.text = msg.subee_name

        tv3.text = msg.price + "/" + msg.time_range_type

        tv4.text = msg.create_at

        when (msg.pay_method) {
            "alipay" -> tv5.text = getString(R.string.alipay)
            else -> tv5.text = getString(R.string.wechat_pay)
        }

        tv6.text = msg.expire_at
        tv_suscribe.text = "已完成"

    }


    override fun payCancle(msg: String) {
        ToastUtils.showToast(this, msg)
    }

    override fun createId(msg: CreateId) {
        id = msg.sub_id
        start(msg.timer.toLong())
    }

    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SDK_PAY_FLAG -> {
                    val payResult = PayResult(msg.obj as Map<String, String>)

                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    val resultInfo = payResult.getResult()// 同步返回需要验证的信息
                    Log.d("junjun", resultInfo)
                    val resultStatus = payResult.getResultStatus()
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        val gson = Gson()
                        val fromJson = gson.fromJson(resultInfo, ResultInfo::class.java)
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        mPayPresenter.payCheck(id, resultInfo, fromJson.sign, fromJson.sign_type,
                                "9000"
                        )
                        // SToast.showText("支付成功")


                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        SToast.showText("支付失败")
                    }
                }
                SDK_AUTH_FLAG -> {

                }
                else -> {
                }
            }
        }
    }


    override fun payResult(msg: PayResultMine) {
        if (msg.alipay != null)
            PayUtil.aliPay(this, msg.alipay.order_string, this)
        else {
            PayUtil.wxPay(this, msg.wxpay, this)
        }
    }


    override fun layoutId(): Int = R.layout.activity_pay

    override fun initData() {

    }

    private var id = ""
    private var price = ""
    private var mPayPresenter: PayPresenter = PayPresenter(this)
    private var role = ""
    private var createBean: CreateId? = null
    private var subscribe: Disposable? = null//保存订阅者
    private val SDK_PAY_FLAG = 1
    private val SDK_AUTH_FLAG = 2
    private var isPay = false

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.pay))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { finish() }

    }


    @SuppressLint("SetTextI18n")
    override fun start() {
        id = intent.getStringExtra("id")
        price = intent.getStringExtra("price")
        createBean = intent.getSerializableExtra("payBean") as CreateId
        role = intent.getStringExtra("role")
        tv_price.text = "$$price"

        tv_suscribe.setOnClickListener {

            mPayPresenter.pay(id, if (payType == 0) "alipay" else "wechat")
        }
        tv_alipay.setOnClickListener {
            tv_alipay.isSelected = true
            tv_wechat.isSelected = false
            payType = 0
        }
        tv_wechat.setOnClickListener {
            tv_alipay.isSelected = false
            tv_wechat.isSelected = true
            payType = 1
        }
        tv_alipay.isSelected = true

    }

    private var payType: Int = 0;//默认是支付宝支付


    private fun secondToDate(second: Long, patten: String): String {
        var calendar = Calendar.getInstance();
        calendar.timeInMillis = second * 1000//转换为毫秒
        var date = calendar.time
        var format = SimpleDateFormat(patten)
        var dateString = format.format(date)
        return dateString
    }

    fun start(count: Long) {
        subscribe = Flowable.interval(1, TimeUnit.SECONDS)//按时间间隔发送整数的Observable
                .observeOn(AndroidSchedulers.mainThread())//切换到主线程修改UI
                .subscribe {
                    val show = count - it
                    Log.d("junjun", show.toString())
                    tv_time.text = getString(R.string.pay_time, secondToDate(show, "mm:ss"))
                    if (show <= 0.toLong()) {//当倒计时小于0,计时结束
                        subscribe!!.dispose()
                        tv_time.text = "支付已超时"
                        return@subscribe//使用标记跳出方法
                    }

                }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (subscribe != null) {
            subscribe!!.dispose()
        }
        if (!isPay) {
            mPayPresenter.orderCancle(id)
        }

    }

    override fun failed(code: Int, message: String?) {
        TODO("Not yet implemented")
    }

    override fun cancel() {
        TODO("Not yet implemented")
    }

    override fun success() {
        TODO("Not yet implemented")
    }

}
