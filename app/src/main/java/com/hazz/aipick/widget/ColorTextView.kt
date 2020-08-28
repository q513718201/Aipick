package com.hazz.aipick.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import com.hazz.aipick.R

class ColorTextView : android.support.v7.widget.AppCompatTextView {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    @SuppressLint("ResourceAsColor")
    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        if (text == null || text.isEmpty()) return
        var drawable = resources.getDrawable(R.mipmap.up)
        if (text.startsWith("-")) {
            drawable = resources.getDrawable(R.mipmap.ic_down)
        }
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        setCompoundDrawables(null, null, drawable, null)
    }
}