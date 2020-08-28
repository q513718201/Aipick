package com.hazz.aipick.widget.kline;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import com.hazz.aipick.R;

public class TimeOptionPopupWindow extends PopupWindow {


    public enum Option {
        ONE_MINUTE,
        FIVE_MINUTE,
        THIRTY_MINUTE,
        WEEK
    }

    private Option mOption;
    private final RadioGroup mRadioGroup;

    public TimeOptionPopupWindow(Context context) {
        super(context);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                60, context.getResources().getDisplayMetrics());
        setHeight(height);

        View root = LayoutInflater.from(context).inflate(R.layout.pop_time_option, null);
        mRadioGroup = root.findViewById(R.id.radio_group);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                parseOption(checkedId);
                dismiss();

            }
        });
        setBackgroundDrawable(new ColorDrawable());
        setContentView(root);
        setOutsideTouchable(true);
        setFocusable(true);
    }

    private void parseOption(int checkedId) {
        switch (checkedId) {
            case R.id.one_minute:
                mOption = Option.ONE_MINUTE;
                break;
            case R.id.five_minute:
                mOption = Option.FIVE_MINUTE;
                break;
            case R.id.thirty_minute:
                mOption = Option.THIRTY_MINUTE;
                break;
            case R.id.week:
                mOption = Option.WEEK;
                break;
        }
    }

    public Option getOption() {
        return mOption;
    }

    public void reset() {
        mRadioGroup.clearCheck();
        mOption = null;
    }

}
