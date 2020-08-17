package com.hazz.aipick.ui.fragment

import com.hazz.aipick.R
import com.hazz.aipick.base.BaseFragment
import com.hazz.aipick.utils.CoinManager
import kotlinx.android.synthetic.main.fragment_about_coin.*


/**
 * 成交列表
 */
class AboutCoinFragment : BaseFragment() {

    private var coinName = ""
    private var key = ""

    companion object {
        fun getInstance(coinName: String): AboutCoinFragment {
            val fragment = AboutCoinFragment()
            fragment.coinName = coinName
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_about_coin
    }

    override fun initView() {
        val split = coinName?.split(".")
        val name = split[1].substring(0, split[1].length - 4)
        val coin = CoinManager.getCoin(name.toUpperCase())
        coin?.let {
            tv_coin_name.text = it.name
            tv_coin_name_en.text = it.nameen
            tv_about.text = it.desc
            tv_publish_time.text = getString(R.string.text_publish_time, it.time)
            tv_liutong.text = getString(R.string.text_liutong, it.circulation)
            tv_publish_amount.text = getString(R.string.text_publish, it.issue)
            tv_official_web.text = getString(R.string.text_office_web, it.cmplink)
            tv_web_view.text = getString(R.string.text_web_view, it.chainlink)
            icon.setImageResource(CoinManager.getCoinIcon(it.name))
        }
    }

    override fun lazyLoad() {
    }

}