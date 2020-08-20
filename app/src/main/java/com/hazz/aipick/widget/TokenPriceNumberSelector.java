package com.hazz.aipick.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hazz.aipick.R;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class TokenPriceNumberSelector extends LinearLayout {


    private int typeTrade;

    public enum Type {
        TOKEN, CURRENCY
    }

    private Type mType = Type.TOKEN;
    private ImageView mMinus;
    private ImageView mAdd;
    private EditText mEditText;
    private BigDecimal mStep;
    private BigDecimal mValueDecimal;
    private OnValueChangeListener mListener;
    private static final int integerDigits = 30;
    private int decimalDigitsSum = 8;// 小数的位数

    public TokenPriceNumberSelector(Context context) {
        this(context, null);
    }

    public TokenPriceNumberSelector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_token_price_number, this);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.regulation);
        typeTrade = array.getInt(R.styleable.regulation_type_trade, 0);
        mMinus = findViewById(R.id.minus);
        mAdd = findViewById(R.id.add);
        mEditText = findViewById(R.id.edit_text);
        mEditText.addTextChangedListener(mTextWatcher);

        mMinus.setOnClickListener(v -> {
            if (mEditText.getText() == null || mEditText.getText().length() == 0) {
                return;
            }
            mValueDecimal = mValueDecimal.subtract(mStep).setScale(mStep.scale(), BigDecimal.ROUND_HALF_UP);
            if (mValueDecimal.floatValue() < 0) {
                mValueDecimal = new BigDecimal(0).setScale(mStep.scale(), BigDecimal.ROUND_HALF_UP);
            }
            mEditText.setText(mValueDecimal.toPlainString());
            mEditText.setSelection(mEditText.getText().length());

        });
        mAdd.setOnClickListener(v -> {
            if (mEditText.getText() == null || mEditText.getText().length() == 0) {
                return;
            }
            mValueDecimal = mValueDecimal.add(mStep).setScale(mStep.scale(), BigDecimal.ROUND_HALF_UP);
            mEditText.setText(mValueDecimal.toPlainString());
            mEditText.setSelection(mEditText.getText().length());

        });
    }

    private void notifyValueChange() {
        if (mListener != null) {
            mListener.onValueChanged(TokenPriceNumberSelector.this, mValueDecimal);
        }
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                limitPointNum(s);
            } else {
                mValueDecimal = null;
                notifyValueChange();
            }
        }
    };


    private void limitPointNum(Editable editable) {
        String s = editable.toString();
        mEditText.removeTextChangedListener(mTextWatcher);
        boolean isReplaced = false;
        if (s.contains(".")) {
            if (integerDigits > 0) {
                mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(integerDigits + decimalDigitsSum + 1)});
            }
            if (s.length() - 1 - s.indexOf(".") > decimalDigitsSum) {
                s = s.substring(0, s.indexOf(".") + decimalDigitsSum + 1);
                editable.replace(0, editable.length(), s.trim());
                isReplaced = true;
            }
        } else {
            if (integerDigits > 0) {
                mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(integerDigits + 1)});
                if (s.length() > integerDigits) {
                    s = s.substring(0, integerDigits);
                    editable.replace(0, editable.length(), s.trim());
                    isReplaced = true;
                }
            }

        }
        if (s.trim().equals(".")) {
            s = "0" + s;
            editable.replace(0, editable.length(), s.trim());
            isReplaced = true;
        }
        if (s.startsWith("0") && s.trim().length() > 1) {
            if (!s.substring(1, 2).equals(".")) {
                editable.replace(0, editable.length(), "0");
                isReplaced = true;
            }
        }
        //If value not replace, then use user operation data and update value change.
        if (!isReplaced) {
            updateValue(s.trim());
            notifyValueChange();
        }

        mEditText.addTextChangedListener(mTextWatcher);
    }

    private void updateValue(String trim) {
        if (TextUtils.isEmpty(trim) || ".".equals(trim)) {
            trim = "0";
        }
        mValueDecimal = new BigDecimal(trim);
        updateStep(mValueDecimal.toPlainString());
    }

    public BigDecimal getValue() {
        return mValueDecimal;
    }

    public void setHintText(String hintText) {
        mEditText.setHint(hintText);
    }

    public void setText(@NonNull String text) {
        setText(text, 5);
    }

    public void setText(String text, int scale) {
        if (text == null || text.equals("--") || text.length() == 0) {
            return;
        }
        mValueDecimal = new BigDecimal(text);
        String trimText = mValueDecimal.setScale(scale, RoundingMode.FLOOR).stripTrailingZeros().toPlainString();
        mEditText.setText(trimText);
        updateStep(trimText);
    }

    private void updateStep(String trimText) {
        BigDecimal value = new BigDecimal(trimText);
        int scale = value.scale();
        if (scale == 0) {
            mStep = new BigDecimal(1);
        } else {
            StringBuilder builder = new StringBuilder("0.");
            for (int i = 0; i < scale - 1; i++) {
                builder.append(0);
            }
            builder.append(1);
            mStep = new BigDecimal(builder.toString());
        }
    }

    public void clear() {
        mEditText.getText().clear();
    }

    public interface OnValueChangeListener {
        void onValueChanged(TokenPriceNumberSelector selector, BigDecimal value);
    }

    public void setOnValueChangeListener(OnValueChangeListener listener) {
        mListener = listener;
    }

    public void setDecimalDigitsSum(int decimalDigitsSum) {
        this.decimalDigitsSum = decimalDigitsSum;
    }
}
