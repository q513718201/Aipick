package com.hazz.aipick.widget

import android.content.Context
import android.util.AttributeSet
import com.hazz.aipick.R

class ColorTextView : android.support.v7.widget.AppCompatTextView {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    fun setText(text: CharSequence?, suffix: String) {

        if (text == null || text.isEmpty()) return
        var drawable = resources.getDrawable(R.mipmap.ic_zero)
        val toFloat = text.toString().toFloat()
        var txt = text
        var txtColor = resources.getColor(R.color.home_text)
        if (toFloat > 0) {
            drawable = resources.getDrawable(R.mipmap.up)
            txt = "+$text"
            txtColor = resources.getColor(R.color.main_color_green)
        } else if (toFloat < 0) {
            drawable = resources.getDrawable(R.mipmap.ic_down)
            txtColor = resources.getColor(R.color.main_color_red)

        }
        setText("$txt$suffix")
        setTextColor(txtColor)
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        setCompoundDrawables(null, null, drawable, null)
    }
}