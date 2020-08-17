package com.hazz.aipick.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hazz.aipick.R;
import com.hazz.aipick.mvp.model.bean.OnOrderBean;


public class MarketDepthItemViewUE extends RelativeLayout {

    private TextView mLeft;
    private TextView mRight;
    private OnOrderBean mItem;

    public MarketDepthItemViewUE(Context context) {
        this(context, null);
    }

    public MarketDepthItemViewUE(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_market_depth_item, this);
        mLeft = findViewById(R.id.left);
        mRight = findViewById(R.id.right);
    }

    public void bindView(OnOrderBean item) {
        mItem = item;
        int padding4 = (int) getResources().getDimension(R.dimen.dp_4);
        int padding16 = (int) getResources().getDimension(R.dimen.dp_16);

        if (item.type == OnOrderBean.ASK) {
            mRight.setText(item.quantity);
            mLeft.setText(item.price);
            mLeft.setTextColor(getContext().getResources().getColor(R.color.main_color_red));
            mLeft.setPadding(padding4, 0, 0, 0);
            mRight.setPadding(0, 0, padding16, 0);
            mRight.setTextColor(getContext().getResources().getColor(R.color.text_hint));
        } else if (item.type == OnOrderBean.BID) {
            mLeft.setText(item.quantity);
            mRight.setText(item.price);
            mRight.setTextColor(getContext().getResources().getColor(R.color.green));
            mLeft.setTextColor(getContext().getResources().getColor(R.color.text_hint));
            mLeft.setPadding(padding16, 0, 0, 0);
            mRight.setPadding(0, 0, padding4, 0);

        }
    }

    public void setDefaultType(int type) {
        int padding4 = (int) getResources().getDimension(R.dimen.dp_4);
        int padding16 = (int) getResources().getDimension(R.dimen.dp_16);
        if (type == OnOrderBean.ASK) {
            mLeft.setTextColor(getContext().getResources().getColor(R.color.main_color_red));
            mLeft.setPadding(padding4, 0, 0, 0);
            mRight.setPadding(0, 0, padding16, 0);
            mRight.setTextColor(getContext().getResources().getColor(R.color.text_hint));
        } else if (type == OnOrderBean.BID) {
            mRight.setTextColor(getContext().getResources().getColor(R.color.green));
            mLeft.setTextColor(getContext().getResources().getColor(R.color.text_hint));
            mLeft.setPadding(padding16, 0, 0, 0);
            mRight.setPadding(0, 0, padding4, 0);

        }
    }


    public void reset() {
        mLeft.setText("--");
        mRight.setText("--");
        mItem = null;
        postInvalidate();
    }
}
