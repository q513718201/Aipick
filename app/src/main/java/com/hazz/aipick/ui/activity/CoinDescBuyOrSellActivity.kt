package com.hazz.aipick.ui.activity


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.socket.KlineBean
import com.hazz.aipick.socket.WsManager
import com.hazz.aipick.ui.adapter.FragmentAdapter
import com.hazz.aipick.ui.fragment.AboutCoinFragment
import com.hazz.aipick.ui.fragment.OnOrderFragment
import com.hazz.aipick.ui.fragment.OnSellFragment
import com.hazz.aipick.utils.RxBus
import com.vinsonguo.klinelib.chart.KLineView
import com.vinsonguo.klinelib.model.HisData
import com.vinsonguo.klinelib.util.DataUtils
import kotlinx.android.synthetic.main.activity_coin_desc.mFrameLayout
import kotlinx.android.synthetic.main.activity_coin_desc_buy.*
import kotlinx.android.synthetic.main.activity_coin_desc_buy.chart
import kotlinx.android.synthetic.main.activity_coin_desc_buy.iv_back
import kotlinx.android.synthetic.main.activity_coin_desc_buy.tv_title
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView


class CoinDescBuyOrSellActivity : BaseActivity() {


    override fun layoutId(): Int = R.layout.activity_coin_desc_buy

    override fun initData() {

    }

    private var datas: MutableList<HisData>? = mutableListOf()

    private var mKLineView: KLineView? = null
    private var timeLineView: KLineView? = null

    private var isFen = true
    private var name = ""
    private var coinName = ""
    private var nameKine = ""

    @SuppressLint("SetTextI18n", "ResourceType")
    override fun initView() {
        coinName = intent.getStringExtra("name")
        val split = coinName.split(".")
        nameKine = split[0] + "." + split[1] + "."
        name = split[1].substring(0, split[1].length - 4).toUpperCase()
        tv_title.text = "$name/USDT"
        initBottom()

        switchFrame()
        WsManager.getInstance().requestK(nameKine + "kline.1min")

        iv_back.setOnClickListener {
            finish()
        }

        WsManager.getInstance().setOnPing {

            if (it.ch == coinName) {
                Log.d("junjun", "执行$it")
                market_info.bindView(it)
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
                "4hour" -> {
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

    private var mTitles: Array<String>? = null
    private var fragments: MutableList<Fragment> = ArrayList()

    private fun initBottom() {
        mTitles = resources.getStringArray(R.array.titles_sell_tab)
        fragments.add(OnOrderFragment.getInstance(coinName))
        fragments.add(OnSellFragment.getInstance(coinName))
        fragments.add(AboutCoinFragment.getInstance(coinName))


        view_pager.adapter = FragmentAdapter(supportFragmentManager, fragments, mTitles)
        ViewPagerHelper.bind(tab_layout, view_pager)
        val commonNavigator = CommonNavigator(this)
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return (mTitles as Array<String>).size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val clipPagerTitleView = ClipPagerTitleView(context)
                clipPagerTitleView.text = (mTitles as Array<String>)[index]
                clipPagerTitleView.textSize = UIUtil.dip2px(context, 14.0).toFloat()
                clipPagerTitleView.textColor = Color.parseColor("#a5b1c8")
                clipPagerTitleView.clipColor = resources.getColor(R.color.text_color_highlight)
                clipPagerTitleView.setOnClickListener { view_pager.currentItem = index }
                return clipPagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val linePagerIndicator = LinePagerIndicator(context)
                linePagerIndicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                linePagerIndicator.setColors(resources.getColor(R.color.text_color_highlight))
                return linePagerIndicator
            }
        }
        tab_layout.navigator = commonNavigator

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

    fun onBuy(view: View) {
        CoinBuyAndSellActivity.start(this, coinName, 0)
    }

    fun onSell(view: View) {
        CoinBuyAndSellActivity.start(this, coinName, 1)
    }
}
