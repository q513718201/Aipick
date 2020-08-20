package com.hazz.aipick.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hazz.aipick.R;


public class SingleOptionSelector extends RadioGroup {

    public static final int INVALID_ID = -1;

    private int mDefaultPosition = INVALID_ID;
    private String[] mOptions = {"25%", "50%", "75%", "100%"};

    public SingleOptionSelector(Context context) {
        this(context, null);
    }

    public SingleOptionSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        addOptions();
    }

    public void setOptions(String[] options) {
        mOptions = options;
        addOptions();
    }

    private void addOptions() {
        for (int i = 0; i < mOptions.length; i++) {
            RadioButton radioButton = (RadioButton) LayoutInflater
                    .from(getContext()).inflate(R.layout.single_option_radio_btn, this, false);
            radioButton.setId(i);
            radioButton.setText(mOptions[i]);
            if (i == mDefaultPosition) {
                radioButton.setChecked(true);
            }
            addView(radioButton);
        }
    }

    /**
     * Must called before setOptions
     *
     * @param pos default selected position
     */
    public void setDefaultPosition(int pos) {
        mDefaultPosition = pos;
    }


    public String[] getOptions() {
        return mOptions;
    }
}
