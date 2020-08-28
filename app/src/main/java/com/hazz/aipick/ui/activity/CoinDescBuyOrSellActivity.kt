package com.hazz.aipick.ui.activity


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v4.app.Fragment
import android.view.View
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.ui.adapter.FragmentAdapter
import com.hazz.aipick.ui.fragment.AboutCoinFragment
import com.hazz.aipick.ui.fragment.OnOrderFragment
import com.hazz.aipick.ui.fragment.OnSellFragment
import com.hazz.aipick.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_coin_desc_buy.*
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
        chart.setSymbol(nameKine)
        market_info.bindView(coinName)
        iv_back.setOnClickListener { finish() }
        initBottom()
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


    override fun start() {

    }

    fun onBuy(view: View) {
        ToastUtils.showToast(this, "正在开发，暂未开放")
//        CoinBuyAndSellActivity.start(this, coinName, 0)
    }

    fun onSell(view: View) {
        ToastUtils.showToast(this, "正在开发，暂未开放")
//        CoinBuyAndSellActivity.start(this, coinName, 1)
    }
}
