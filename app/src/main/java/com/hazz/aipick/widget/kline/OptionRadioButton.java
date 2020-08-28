package com.hazz.aipick.widget.kline;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Layout;
import android.util.AttributeSet;

import com.hazz.aipick.R;


public class OptionRadioButton extends AppCompatRadioButton {


    private Bitmap mBitmap;
    private Paint mPaint;
    private TimeOptionPopupWindow.Option mOption;

    public OptionRadioButton(Context context) {
        this(context, null);
    }

    public OptionRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.h_more);
        mPaint = new Paint();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float desiredWidth = Layout.getDesiredWidth(getText(), getPaint());
        Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
        float mTextHeight = fontMetrics.descent - fontMetrics.ascent;//获取绘制字符的实际高度
        float bottom = getHeight() / 2 + mTextHeight / 2;
        int offset = (int) ((getWidth() - desiredWidth) / 2);
        float left = getWidth() - offset;
        float top = bottom - mBitmap.getHeight();
        canvas.drawBitmap(mBitmap, left, top, mPaint);
    }

    public void setSelectedOption(TimeOptionPopupWindow.Option selectedOption) {
        mOption = selectedOption;
        setTextColor(getResources().getColor(R.color.text_color_highlight));
        switch (selectedOption) {
            case WEEK:
                setText(getResources().getString(R.string.aweek));
                break;
            case ONE_MINUTE:
                setText(getResources().getString(R.string.one_min));
                break;
            case FIVE_MINUTE:
                setText(getResources().getString(R.string.five_min));
                break;
            case THIRTY_MINUTE:
                setText(getResources().getString(R.string.thirty_min));
                break;
        }
    }

    public void resetOption() {
        setTextColor(getResources().getColor(R.color.text_hint));
        setText(getResources().getString(R.string.more));
        mOption = null;
    }

    public TimeOptionPopupWindow.Option getOption() {
        return mOption;
    }
}
