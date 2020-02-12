package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.widget.TextView
import com.github.fujianlian.klinechart.DataHelper
import com.github.fujianlian.klinechart.KLineChartAdapter
import com.github.fujianlian.klinechart.KLineEntity
import com.github.fujianlian.klinechart.formatter.DateFormatter
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.events.KineType
import com.hazz.aipick.socket.DataRequest
import com.hazz.aipick.socket.MyWebSocketManager
import com.hazz.aipick.socket.WebSocket
import com.hazz.aipick.utils.RxBus
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_coin_desc.*
import java.util.ArrayList


class CoinDescActivity : BaseActivity() {




    override fun layoutId(): Int = R.layout.activity_coin_desc

    override fun initData() {

    }

    private lateinit var datas: List<KLineEntity>

    private val adapter by lazy { KLineChartAdapter() }

    // 主图指标下标
    private var mainIndex = 0
    // 副图指标下标
    private var subIndex = -1

    @SuppressLint("SetTextI18n")
    override fun initView() {
        iv_back.setOnClickListener {
            finish()
        }
      //  WebSocket.getInstance().tryToConnect()
     //  WebSocket.getInstance().requestK("market.btcusdt.detail","id1")
        MyWebSocketManager.getInstance().connReceiveWebSocketData()
        MyWebSocketManager.getInstance().requestK("market.btcusdt.detail","id1")
        RxBus.get().observerOnMain(this,KineType::class.java) {

            when(it.type){
                "1fen"->{

                }
                "5fen"->{

                }
                "1hour"->{

                }
                "1day"->{

                }
                "1fen"->{

                }
            }
        }
    }

    override fun start() {
        kLineChartView.adapter = adapter
        kLineChartView.dateTimeFormatter = DateFormatter()
        kLineChartView.setGridRows(4)
        kLineChartView.setGridColumns(4)
        initDate()

        kLineChartView.hideSelectData()
        kLineChartView.setMainDrawLine(false)
    }


    private fun initDate() {
        kLineChartView.justShowLoading()

            datas = DataRequest.getALL(this).subList(0, 500)
            DataHelper.calculate(datas)
            runOnUiThread {
                adapter.addFooterData(datas)
                adapter.notifyDataSetChanged()
                kLineChartView.startAnimation()
                kLineChartView.refreshEnd()
            }
        }




}
