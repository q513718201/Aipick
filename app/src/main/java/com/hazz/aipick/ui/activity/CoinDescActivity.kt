package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.socket.KlineBean
import com.hazz.aipick.socket.WsManager
import com.hazz.aipick.utils.BigDecimalUtil
import com.hazz.aipick.utils.RxBus
import com.vinsonguo.klinelib.chart.KLineView
import com.vinsonguo.klinelib.model.HisData
import com.vinsonguo.klinelib.util.DataUtils
import kotlinx.android.synthetic.main.activity_coin_desc.*


class CoinDescActivity : BaseActivity() {


    override fun layoutId(): Int = R.layout.activity_coin_desc

    override fun initData() {

    }

    private var datas: MutableList<HisData>? = mutableListOf()


    private var mKLineView: KLineView? = null
    private var timeLineView: KLineView? = null

    // 主图指标下标
    private var mainIndex = 0

    // 副图指标下标
    private var subIndex = -1
    private var isFen = true
    private var name = ""
    private var coinName = ""
    private var market = ""
    private var nameKine = ""

    @SuppressLint("SetTextI18n", "ResourceType")
    override fun initView() {
        coinName = intent.getStringExtra("name")
        market = intent.getStringExtra("market")
        val split = coinName.split(".")
        nameKine = split[0] + "." + split[1] + "."
        name = split[1].substring(0, split[1].length - 4).toUpperCase()

        tv_title.text = "$name/USDT"
        tv_sub_title.text = market
        switchFrame()
        WsManager.getInstance().requestK(nameKine + "kline.1min")

        iv_back.setOnClickListener {
            finish()
        }

        WsManager.getInstance().setOnPing {
            if (it.ch == coinName) {
                val tick = it.tick
                val compare = BigDecimalUtil.compare(tick.close, tick.open)
                it.isUp = compare != -1
                val rate = BigDecimalUtil.mul(BigDecimalUtil.div(BigDecimalUtil.sub(tick.close, tick.open, 6), tick.open, 6), "100", 2) + "%"
                runOnUiThread {
                    tv_rate.text = if (it.isUp) "+$rate" else rate
                    if (!it.isUp) {
                        tv_name.setTextColor(resources.getColor(R.color.redF4))
                        tv_rate.setTextColor(resources.getColor(R.color.redF4))
                        tv_zj.setTextColor(resources.getColor(R.color.redF4))

                        tv_zj.text = "-" + BigDecimalUtil.sub(tick.open, tick.close, 2)
                    } else {
                        tv_zj.text = "+" + BigDecimalUtil.sub(tick.close, tick.open, 2)
                    }
                    tv_name.text = tick.close
                    tv_hight.text = tick.high
                    tv_low.text = tick.low
                    tv_vol.text = tick.vol

                }
            }


        }

        chart.setOnMyClick {
            WsManager.getInstance().reconnect()

            when (it) {
                "1fen" -> {
                    isFen = true
                    WsManager.getInstance().requestK(nameKine + "kline.1min")
                }
                "1fenk" -> {
                    isFen = false
                    switchFrame()
                    WsManager.getInstance().requestK(nameKine + "kline.1min")
                }
                "5fen" -> {
                    isFen = false
                    switchFrame()
                    WsManager.getInstance().requestK(nameKine + "kline.5min")
                }
                "30fen" -> {
                    isFen = false
                    switchFrame()
                    WsManager.getInstance().requestK(nameKine + "kline.30min")
                }

                "15fen" -> {
                    isFen = false
                    switchFrame()
                    WsManager.getInstance().requestK(nameKine + "kline.15min")
                }
                "1hour" -> {
                    isFen = false
                    switchFrame()
                    WsManager.getInstance().requestK(nameKine + "kline.60min")
                }
                "1day" -> {
                    isFen = false
                    switchFrame()
                    WsManager.getInstance().requestK(nameKine + "kline.1day")
                }
                "1week" -> {
                    isFen = false
                    switchFrame()
                    WsManager.getInstance().requestK(nameKine + "kline.1week")
                }
                "vol" -> {
//                    kLineChartView.changeMainDrawType(Status.MA)
                }
                "macd" -> {
                    //  kLineChartView.setChildDraw(0)
                }
                "kdj" -> {
                    // kLineChartView.setChildDraw(1)
                }
                "main" -> {
                    // kLineChartView.hideChildDraw()

                }
            }
        }


        RxBus.get().observerOnMain(this, KlineBean::class.java) {

            val data = it.data
            if (data != null) {
                mDialog!!.dismiss()
            }
            if (!data.isNullOrEmpty()) {
                Log.d("junjun", data.toString())
                datas?.clear()
                for (a in data) {
                    val kLineEntity = HisData()
                    kLineEntity.close = a.close
                    kLineEntity.open = a.open
                    kLineEntity.high = a.high
                    kLineEntity.vol = a.vol.toFloat()
                    kLineEntity.low = a.low

                    datas?.add(kLineEntity)
                }
                val calculateHisData = DataUtils.calculateHisData(datas)
                mProgressBar.visibility = View.GONE
                if (isFen) {
                    timeLineView = getTimeLineView()
                    mProgressBar.visibility = View.GONE
                    timeLineView!!.setLastClose(calculateHisData!![0].close)
                    timeLineView!!.initData(calculateHisData)
                    timeLineView!!.id = 0
                    timeLineView!!.showIndex()
                    timeLineView!!.visibility = View.VISIBLE
                } else {
                    val kLineView = getKLineView()
                    kLineView.setDateFormat("yyyy-MM-dd")
                    kLineView.initData(calculateHisData)
                    kLineView.id = 1
                    kLineView.showIndex()
                    kLineView.visibility = View.VISIBLE
                }

            }

        }
    }

    private fun switchFrame() {
        mFrameLayout.removeAllViews()
        mProgressBar.visibility = View.VISIBLE
        val kLineView = getKLineView()
        //  kLineView.id = 1
        kLineView.visibility = View.GONE
        mFrameLayout.addView(kLineView)
    }

    override fun start() {

    }

    private fun getKLineView(): KLineView {
        if (mKLineView == null) {
            mKLineView = KLineView(applicationContext)
        }
        mKLineView!!.setMode(KLineView.Mode.K)
        return mKLineView!!
    }

    private fun getTimeLineView(): KLineView {
        if (mKLineView == null) {
            mKLineView = KLineView(applicationContext)
        }
        mKLineView!!.setMode(KLineView.Mode.LINE)
        return mKLineView!!
    }
}
