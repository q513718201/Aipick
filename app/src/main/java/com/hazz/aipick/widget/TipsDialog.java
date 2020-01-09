package com.hazz.aipick.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.hazz.aipick.R;
import com.hazz.aipick.utils.DpUtils;


public class TipsDialog {

    Context context;
    Dialog dialog;
    TextView mTvTitle, mTvContent;
    Button mBtnCancle;
    Button mBtnConfirm;
    View mPartLine;
    private View mViewm;

    public void setCancleText() {
    }

    public interface OnConfirmListener {
        void onConfirm(View view);
    }

    public interface OnCancleListener {
        void onCancle(View view);
    }


    public TipsDialog(Context context) {
        this.context = context;
        createDialog();
    }

    public TipsDialog createDialog() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mViewm = View.inflate(context, R.layout.dialog_tips, null);
        mTvTitle = (TextView) mViewm.findViewById(R.id.title);
        mTvContent = mViewm.findViewById(R.id.mTvContent);
        mBtnConfirm = (Button) mViewm.findViewById(R.id.btn_confirm);
        mBtnCancle = (Button) mViewm.findViewById(R.id.btn_cancle);
        mPartLine = mViewm.findViewById(R.id.line_part);
        dialog.setContentView(mViewm);
        dialog.setCanceledOnTouchOutside(false);

        Window window = dialog.getWindow();
        //要加上设置背景，否则dialog宽高设置无作用
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = DpUtils.dip2px(context, 275);
       // layoutParams.height = DensityUtils.dip2px(context, 140);
        window.setAttributes(layoutParams);
        return this;
    }

    public TipsDialog setCancelable(boolean cancelable) {
        dialog.setCancelable(cancelable);
        return this;
    }

    public TipsDialog setTitle(String title) {
        mTvTitle.setText(title);
        return this;
    }

    public TipsDialog setContent(String content) {
        mTvContent.setText(content);
        return this;
    }

    public TipsDialog setTitleTextSize(float size) {
        mTvTitle.setTextSize(size);
        return this;
    }

    public TipsDialog setTitleColor(int color) {
        mTvTitle.setTextColor(color);
        return this;
    }

    public TipsDialog setConfirmText(String text) {
        mBtnConfirm.setText(text);
        return this;
    }

    public TipsDialog setCancleText(String text) {
        mBtnCancle.setText(text);
        return this;
    }

    public TipsDialog setConfirmColor(int color) {
        mBtnConfirm.setTextColor(color);
        return this;
    }

    public TipsDialog setCancleColor(int color) {
        mBtnCancle.setTextColor(color);
        return this;
    }

    public TipsDialog setConfirmListener(final OnConfirmListener onConfirmListener) {
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onConfirmListener != null) {
                    onConfirmListener.onConfirm(v);
                }
            }
        });
        return this;
    }

    public TipsDialog setCancleListener(final OnCancleListener onCancleListener) {
        mBtnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onCancleListener != null) {
                    onCancleListener.onCancle(v);
                }
            }
        });
        return this;
    }

    public void show() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
