package com.hazz.aipick.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hazz.aipick.R;
import com.hazz.aipick.mvp.model.DepthItem;


public class DepthItemView extends RelativeLayout {
    private static final String TAG = "DepthItemView";

    private TextView mPrice;
    private TextView mQuantity;

    private Paint mPaint;
    private DepthItem mItem;

    private DepthItem.Type defaultType;

    public DepthItemView(Context context) {
        this(context, null);
    }

    public DepthItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        setWillNotDraw(false);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    public void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_depth_item, this);
        mPrice = findViewById(R.id.price);
        mQuantity = findViewById(R.id.quantity);
    }

    public void bindView(DepthItem item) {
        mItem = item;
        mPrice.setText(item.price);
        mQuantity.setText(item.quantity);
        if (item.type == DepthItem.Type.BID) {
            mPrice.setTextColor(getContext().getResources().getColor(R.color.main_color_green));
        } else if (item.type == DepthItem.Type.ASK) {
            mPrice.setTextColor(getContext().getResources().getColor(R.color.main_color_red));
        }
    }

    public void setDefaultType(DepthItem.Type defaultType) {
        this.defaultType = defaultType;
        if (defaultType  == DepthItem.Type.BID) {
            mPrice.setTextColor(getContext().getResources().getColor(R.color.main_color_green));
        } else if (defaultType == DepthItem.Type.ASK) {
            mPrice.setTextColor(getContext().getResources().getColor(R.color.main_color_red));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mItem == null) {
            return;
        }
        int left = (int) ((1 - mItem.quantityPercent) * getWidth());
        int top = 0;
        int right = getWidth();
        int bottom = getHeight() - 2;
        if (mItem.type == DepthItem.Type.BID) {
            mPaint.setColor(getResources().getColor(R.color.transparent_green));
        } else {
            mPaint.setColor(getResources().getColor(R.color.transparent_red));
        }
        canvas.drawRect(left, top, right, bottom, mPaint);
    }

    public void reset() {
        mPrice.setText("--");
        mQuantity.setText("--");
        mItem = null;
        postInvalidate();
    }

    public DepthItem getItem() {
        return mItem;
    }
}
