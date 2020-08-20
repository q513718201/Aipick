package com.hazz.aipick.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.hazz.aipick.R

object GlideUtil {
    fun load(view: ImageView, url: String) {
        Glide.with(view).load(url).into(view)
    }
    fun load(view: ImageView, url: Int) {
        Glide.with(view).load(url).into(view)
    }
    fun loadAsHead(view: ImageView, res: Int) {
        Glide.with(view).load(res).centerCrop().fallback(R.mipmap.default_avatar).into(view)
    }

    fun loadAsHead(view: ImageView, res: String) {
        Glide.with(view).load(res).centerCrop().fallback(R.mipmap.img_avatar).into(view)
    }
}