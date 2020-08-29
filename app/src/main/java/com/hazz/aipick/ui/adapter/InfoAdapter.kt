package com.hazz.aipick.ui.adapter


import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.aipick.R
import com.hazz.aipick.mvp.model.bean.InfoBean
import com.hazz.aipick.ui.activity.InfoDetailActivity
import com.hazz.aipick.utils.DpUtils
import com.hazz.aipick.utils.GlideUtil
import com.werb.pickphotoview.adapter.SpaceItemDecoration

class InfoAdapter(layoutResId: Int, data: List<InfoBean>?) : BaseQuickAdapter<InfoBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: InfoBean) {
        item?.let {
            GlideUtil.showImage(it.head, helper.getView<ImageView>(R.id.iv))
            helper.setText(R.id.tv_name, it.name)
            helper.setText(R.id.tv_time, "${it.total_view}浏览 | ${it.time}")
            helper.setText(R.id.tv_content, it.content)
            val recyclerView = helper.getView<RecyclerView>(R.id.recyclerView)
            if (it.images.isEmpty()) {
                recyclerView.visibility = View.GONE
            } else {
                recyclerView.visibility = View.VISIBLE
                val gridLayoutManager = GridLayoutManager(mContext, 1)
                gridLayoutManager.spanCount = when (it.images.size) {
                    1 -> 1
                    2 -> 2
                    else -> 3
                }
                recyclerView.layoutManager = gridLayoutManager
                recyclerView.setHasFixedSize(false)
                recyclerView.addItemDecoration(SpaceItemDecoration(DpUtils.dip2px(mContext, 10f), gridLayoutManager.spanCount))
                recyclerView.adapter = ImageAdapter(R.layout.item_image, it.images)
            }
            helper.itemView.setOnClickListener {
                mContext.startActivity(Intent(mContext, InfoDetailActivity::class.java).putExtra("id", item.id))
            }
        }

    }
}
