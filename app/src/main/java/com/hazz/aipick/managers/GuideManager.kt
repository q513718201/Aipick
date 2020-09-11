package com.hazz.aipick.managers

import android.support.v4.app.FragmentManager
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.RelativeLayout
import com.hazz.aipick.utils.SPUtil
import easily.tech.guideview.lib.GuideViewBundle
import easily.tech.guideview.lib.GuideViewBundle.TransparentOutline.TYPE_RECT
import easily.tech.guideview.lib.GuideViewFragment

object GuideManager {

    /**
     * targetView

    引导视图需要显示附着的目标视图

    hintView

    引导视图（不包含半透明浮层以及透明焦点区）

    transparentSpaceXXX

    默认的情况下，透明焦点区的大小跟目标视图的大小保持一致，如果需要加大透明区域的大小，可以通过设置这组属性，指定上下左右的额外的空白区域

    hintViewMarginXXX

    引导视图（hintView）相对于目标视图（targetView）的边距

    hasTransparentLayer

    是否显示透明焦点区域，默认显示。可以选择不绘制透明焦点区域而只有半透明的浮层

    hintViewDirection

    引导视图（hintView）相对于目标视图（targetView）的位置方向，目前可以定义上（上方左对齐）、下（下方左对齐）、左（左方上对齐）、右（右方上对齐）四个方向。如果需要在位置之余有不一样的对齐效果，可以使用hintViewMarginXXX属性

    outlineType

    透明焦点区的轮廓类型，有圆形（椭圆）轮廓和方形轮廓两种

    maskColor

    半透明遮罩浮层的颜色

    isDismissOnClicked

    全局点击可以关闭引导视图，默认为true。如果设置false，则需要手动设置点击hintView的特定位置关闭视图

    setHintViewParams

    如果不满意内置的，可以自己设置layoutParam
     */

    fun showGuide(manager: FragmentManager, targetView: View, imageSource: Int, direction: Int, tag: String) {
        val imageView = ImageView(targetView.context)
        imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        imageView.setImageResource(imageSource)


        var guide = GuideViewFragment.Builder().addGuidViewBundle(GuideViewBundle.Builder()
                .setTargetView(targetView)
                .setHintView(imageView)
                .setHintViewMargin(0, 0, 0, 0)
                .setTransparentSpace(20, 20, 20, 20)
                .setOutlineType(TYPE_RECT)
                .setDismissOnClicked(false)
                .setTargetViewClickable(true)
                .setGuideViewHideListener { SPUtil.putBoolean(tag, true) }
                .setHintViewDirection(direction)
                .build()
        ).build()

        guide?.let {
            if (!SPUtil.getBoolean(tag))
                it.show(manager, tag)
        }
    }

    fun showGuide(manager: FragmentManager, targetView: View, hintView: View, direction: Int, tag: String) {
        val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        var guide = GuideViewFragment.Builder().addGuidViewBundle(GuideViewBundle.Builder()
                .setTargetView(targetView)
                .setHintView(hintView)
                .setHintViewParams(params)
                .setHintViewMargin(0, 0, 0, 0)
                .setTransparentSpace(0, 0, 0, 0)
                .setOutlineType(TYPE_RECT)
                .setDismissOnClicked(true)
                .setGuideViewHideListener { SPUtil.putBoolean(tag, true) }
                .setHintViewDirection(direction)
                .build()
        ).build()
        hintView.setOnClickListener {
            guide?.let {
                guide.dismissAllowingStateLoss()
                SPUtil.putBoolean(tag, true)
            }
        }
        guide?.let {
            if (!SPUtil.getBoolean(tag))
                it.show(manager, tag)
        }
    }

    fun showGuide(manager: FragmentManager, targetView: View, hintView: View, hintView1: View, direction: Int, tag: String, tag1: String) {
        val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        var guide = GuideViewFragment.Builder().addGuidViewBundle(GuideViewBundle.Builder()
                .setTargetView(targetView)
                .setHintView(hintView)
                .setHintViewParams(params)
                .setHintViewMargin(0, 0, 0, 0)
                .setTransparentSpace(0, 0, 0, 0)
                .setOutlineType(TYPE_RECT)
                .setDismissOnClicked(true)
                .setGuideViewHideListener { SPUtil.putBoolean(tag, true) }
                .setHintViewDirection(direction)
                .build()
        ).addGuidViewBundle(
                GuideViewBundle.Builder()
                        .setTargetView(targetView)
                        .setHintView(hintView1)
                        .setHintViewParams(params)
                        .setHintViewMargin(0, 0, 0, 0)
                        .setTransparentSpace(0, 0, 0, 0)
                        .setOutlineType(TYPE_RECT)
                        .setDismissOnClicked(true)
                        .setGuideViewHideListener { SPUtil.putBoolean(tag1, true) }
                        .setHintViewDirection(direction)
                        .build()
        ).build()
        hintView.setOnClickListener {
            guide?.let {
                guide.onNext()
                SPUtil.putBoolean(tag, true)
            }
        }
        hintView1.setOnClickListener {
            guide.dismiss()
            SPUtil.putBoolean(tag1, true)
        }
        guide?.let {
            if (!SPUtil.getBoolean(tag))
                it.show(manager, tag)
        }
    }
}