package com.hazz.aipick.widget.kline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.hazz.aipick.R;

public class BottomLineRadioButton extends AppCompatRadioButton {


    private Paint mBarPaint;
    private float mBarHeight;


    public BottomLineRadioButton(Context context) {
        this(context, null);
    }

    public BottomLineRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBarHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        mBarPaint = new Paint();
        mBarPaint.setAntiAlias(true);
        mBarPaint.setColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isChecked()) {
            float desiredWidth = Layout.getDesiredWidth(getText(), getPaint());
            int offset = (int) ((getWidth() - desiredWidth) / 2);
            int barLeft = offset;
            int barBottom = getHeight();
            int barRight = getWidth() - offset;
            int barTop = (int) (barBottom - mBarHeight);
            canvas.drawRect(barLeft, barTop, barRight, barBottom, mBarPaint);
        }
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        invalidate();
    }
}
