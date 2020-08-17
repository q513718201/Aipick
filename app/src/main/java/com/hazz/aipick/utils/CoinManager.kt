package com.hazz.aipick.utils

import com.google.gson.Gson
import com.hazz.aipick.MyApplication
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.CoinBean
import java.nio.charset.Charset


object CoinManager {

    fun getCoin(coinName: String): CoinBean? {
        val assertsFileInBytes = AssetsUtil.getAssertsFileInBytes(MyApplication.context, "coin.json")
        val json = String(assertsFileInBytes, Charset.defaultCharset())

        var coins: List<CoinBean> = Gson().fromJson<Array<CoinBean>>(json, Array<CoinBean>::class.java).toMutableList()

        for (coin in coins) {
            if (coin.name == coinName) {
                return coin
            }
        }
        return null
    }

    fun getCoinIcon(coinName: String): Int {
        return when (coinName.toUpperCase()) {
            "BTC" -> R.mipmap.ic_btc
            "ETH" -> R.mipmap.ic_eth
            "HT" -> R.mipmap.ic_ht
            "XRP" -> R.mipmap.ic_xrp
            "LTC" -> R.mipmap.ic_ltc
            "BCH" -> R.mipmap.ic_bch
            "EOS" -> R.mipmap.ic_eos
            "ETC" -> R.mipmap.ic_etc
            "BSV" -> R.mipmap.ic_bsv
            "TRX" -> R.mipmap.ic_trx
            else -> R.mipmap.ic_aipick
        }
    }

    fun getCoinHouseIcon(house: String): Int {
        return when (house.toUpperCase()) {
            "huobi" -> R.mipmap.ic_huobi
// TODO: 2020/8/17
            else -> R.mipmap.ic_allcoin
        }
    }
}