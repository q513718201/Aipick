package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.ui.adapter.TixianAdapter
import com.hazz.aipick.utils.ToolBarCustom
import kotlinx.android.synthetic.main.activity_my_incoming.*
import kotlinx.android.synthetic.main.activity_tixian_record.recycleview
import kotlinx.android.synthetic.main.activity_tixian_record.toolbar
import kotlinx.android.synthetic.main.dialog_choose.view.tv_cancle
import kotlinx.android.synthetic.main.dialog_choose_year.view.*
import java.util.*


class MyIncomingActivity : BaseActivity() {


    override fun layoutId(): Int = R.layout.activity_my_incoming


    override fun initData() {

    }

    private var mOrderAdapter: TixianAdapter? = null
    private val titleList = ArrayList<String>()

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(toolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.tixian_record))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { view -> finish() }


        titleList.add("正在持仓")
        titleList.add("历史持仓")

        recycleview.layoutManager = LinearLayoutManager(this)
        mOrderAdapter = TixianAdapter(R.layout.item_order, null)
        recycleview.adapter = mOrderAdapter
        mOrderAdapter!!.bindToRecyclerView(recycleview)
        mOrderAdapter!!.setEmptyView(R.layout.empty_view)

    }

    override fun start() {

        tv_year.setOnClickListener {
            showBottomYear()
        }
    }

    private fun showBottomYear() {
        var bottomSheet = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_choose_year, null)
        bottomSheet.setContentView(view)
        view.tv_cancle.setOnClickListener {
            bottomSheet.dismiss()
        }
        view.tv_one_month.setOnClickListener {
            bottomSheet.dismiss()
        }
        view.tv_three_month.setOnClickListener {
            bottomSheet.dismiss()
        }
        view.tv_one_year.setOnClickListener {
            bottomSheet.dismiss()
        }
        bottomSheet.show()
    }
}