package com.hazz.aipick.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hazz.aipick.R;

import java.util.List;


public class ByexOptionSwitcher extends RadioGroup {
    private static final String TAG = "ByexOptionSwitcher";

    private List<String> mOptions;
    private Paint mBarPaint;
    private float mBarHeight = TypedValue
            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
    private float mBarTextGap = TypedValue
            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());

    private float mTextGap = TypedValue
            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());


    public ByexOptionSwitcher(Context context) {
        this(context, null);
    }


    public ByexOptionSwitcher(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        mBarPaint = new Paint();
        mBarPaint.setAntiAlias(true);
        mBarPaint.setColor(getResources().getColor(R.color.colorAccent));
        mBarPaint.setStrokeWidth(mBarHeight);
    }

    public void setOptions(List<String> options) {
        setOptions(options, 0);
    }


    public void setOptions(List<String> options, float textSize) {
        removeAllViews();
        mOptions = options;
        for (int i = 0; i < options.size(); i++) {
            RadioButton radioButton = (RadioButton) LayoutInflater.from(getContext())
                    .inflate(R.layout.btn_option_switcher, this, false);
            LayoutParams layoutParams = (LayoutParams) radioButton.getLayoutParams();
            if (i != options.size() - 1) {
                //To fix sdk below 17
                layoutParams.setMargins(0, 0, (int) mTextGap, 0);
            }
            if (textSize > 0) {
                radioButton.setTextSize(textSize);
            }
            if (i == 0) {
                radioButton.setChecked(true);
            }
            radioButton.setId(i);
            radioButton.setText(options.get(i));
            addView(radioButton);
            if (i == 0) {
                check(i);
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mOptions == null || mOptions.size() == 0) {
            super.onDraw(canvas);
            return;
        }
        int checkedRadioButtonId = getCheckedRadioButtonId();
        RadioButton childAt = (RadioButton) getChildAt(checkedRadioButtonId);
        float desiredWidth = Layout.getDesiredWidth(childAt.getText(), childAt.getPaint());
        int offset = (int) ((childAt.getWidth() - desiredWidth) / 2);
        int barLeft = childAt.getLeft() + offset;
        int barBottom = childAt.getBottom();
        int barRight = childAt.getRight() - offset;
        int barTop = (int) (barBottom - mBarHeight);
        canvas.drawRect(barLeft, barTop , barRight, barBottom, mBarPaint);
    }


}
