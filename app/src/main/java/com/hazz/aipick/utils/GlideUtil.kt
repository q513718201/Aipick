package com.hazz.aipick.utils

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.hazz.aipick.R
import com.hazz.aipick.glide.GlideApp


object GlideUtil {
    var options: RequestOptions = RequestOptions()

    //设置占位符
    fun setPlaceholder(id: Int) {
        options.placeholder(id)
    }

    fun setPlaceholder(drawable: Drawable?) {
        options.placeholder(drawable)
    }

    //设置错误符
    fun setError(id: Int) {
        options.error(id)
    }

    fun setError(drawable: Drawable?) {
        options.error(drawable)
    }

    fun showImage(url: String?, imageView: ImageView) {
        GlideApp.with(imageView.context)
                .load(url)
                .apply(options)
                .into(imageView)
    }

    fun showImage(resId: Int, imageView: ImageView) {
        GlideApp.with(imageView.context)
                .load(resId)
                .apply(options)
                .into(imageView)
    }

    //设置图片固定的大小尺寸
    fun showImageWH(url: String?, imageView: ImageView, height: Int, width: Int) {
        options.override(width, height)
        GlideApp.with(imageView.context)
                .load(url)
                .apply(options)
                .into(imageView)
    }

    //设置图片圆角，以及弧度
    @SuppressLint("CheckResult")
    fun showImageRound(url: String?, imageView: ImageView, radius: Int) {
        options.transform(RoundedCorners(radius))
        //        options.transform(new GlideCircleTransform());
        GlideApp.with(imageView.context)
                .load(url)
                .apply(options)
                .into(imageView)
    }

    fun showImageRound(url: String?, imageView: ImageView, radius: Int, height: Int, width: Int) {
        //不一定有效，当原始图片为长方形时设置无效
        options.override(width, height)
        options.transform(RoundedCorners(radius))
        //        options.centerCrop(); //不能与圆角共存
        GlideApp.with(imageView.context)
                .load(url)
                .apply(options)
                .into(imageView)
    }

    fun showImageRound(url: String?, imageView: ImageView) {
        //自带圆角方法，显示圆形
        options.circleCrop()
        GlideApp.with(imageView.context)
                .load(url)
                .apply(options)
                .into(imageView)
    }

    fun showRound(url: String?, imageView: ImageView, res: Int) {
        options.circleCrop()
        options.error(res)
        GlideApp.with(imageView.context)
                .load(url)
                .apply(options)
                .into(imageView)
    }


    init {
        options.skipMemoryCache(false)
        options.diskCacheStrategy(DiskCacheStrategy.ALL)
        options.priority(Priority.HIGH)
        options.error(R.mipmap.load)
        //设置占位符,默认
        options.placeholder(R.mipmap.load)
        //设置错误符,默认
        options.error(R.mipmap.ic_error)
    }
}
