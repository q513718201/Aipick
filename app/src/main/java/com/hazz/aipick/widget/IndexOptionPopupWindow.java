package com.hazz.aipick.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hazz.aipick.R;
import com.hazz.aipick.socket.Index;


public class IndexOptionPopupWindow extends PopupWindow implements RadioGroup.OnCheckedChangeListener{

    private OnIndexChangeListener mListener;
    private final RadioGroup mSecondGroup;
    private final RadioGroup mMainRadioGroup;


    public IndexOptionPopupWindow(Context context) {
        super(context);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                100, context.getResources().getDisplayMetrics());
        setHeight(height);

        View root = LayoutInflater.from(context).inflate(R.layout.pop_index_option, null);
        mMainRadioGroup = root.findViewById(R.id.main_rg);
        mMainRadioGroup.setOnCheckedChangeListener(this);
        mSecondGroup = root.findViewById(R.id.second_rg);
        mSecondGroup.setOnCheckedChangeListener(this);
        TextView mainHide = root.findViewById(R.id.main_hide);
        mainHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainRadioGroup.clearCheck();

            }
        });

        setBackgroundDrawable(new ColorDrawable());
        setContentView(root);
        setOutsideTouchable(true);
        setFocusable(true);
    }


    public interface OnIndexChangeListener {
        void onSecondIndexChange(Index index);
        void onMainIndexChange(Index index);
    }

    public void setOnIndexChangeListener(OnIndexChangeListener listener) {
        mListener = listener;
    }

    public Index getIndexById(int checkedId) {
        switch (checkedId) {
            case R.id.vol:
                return Index.VOL;
            case R.id.ma:
                return Index.MA;
            case R.id.boll:
                return Index.BOLL;
            case R.id.macd:
                return Index.MACD;
            case R.id.kdj:
                return Index.KDJ;
        }
        return null;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Index index = getIndexById(checkedId);
        if (group == mSecondGroup) {
            if (mListener != null && index != null) {
                mListener.onSecondIndexChange(index);
            }
        } else if (group == mMainRadioGroup) {
            RadioButton button = group.findViewById(checkedId);
            if (button == null && mListener != null) {
                mListener.onMainIndexChange(Index.NONE);
            } else if (mListener != null && button.isChecked()){
                mListener.onMainIndexChange(index);
            }
        }

    }
}
