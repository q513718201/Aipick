package com.hazz.aipick.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.hazz.aipick.R;
import com.hazz.aipick.events.KineType;
import com.hazz.aipick.events.RxBus;
import com.hazz.aipick.socket.Index;

import io.reactivex.disposables.Disposable;

public class ChartView extends RelativeLayout {

    private RadioGroup mRadioGroup;

    private FrameLayout mFrameLayout;

    private ProgressBar mProgressBar;

    private String mSymbol;

    private TimeOptionPopupWindow mTimeOptionPopupWindow;

    private IndexOptionPopupWindow mIndexOptionPopupWindow;

    private OptionRadioButton mMore;

    private OptionRadioButton mIndex;


    private int mLastOptionId = R.id.time;

    private int mCurrentOptionId = R.id.time;

    private boolean mUseLastSelected = false;

    private Index mCurrentIndex;

    private static final int CHART_TIME = 0;
    private static final int CHART_FIFTEEN = 1;
    private static final int CHART_HOUR = 2;
    private static final int CHART_DAY = 3;
    private static final int CHART_MINUTE = 4;
    private static final int CHART_FIVE = 5;
    private static final int CHART_THRITY = 6;
    private static final int CHART_WEEK = 7;



    public ChartView(Context context) {
        this(context, null);
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_chart, this);
        mRadioGroup = findViewById(R.id.radio_group);
        mRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
      //  mFrameLayout = findViewById(R.id.frame);
        mProgressBar = findViewById(R.id.progressBar);
        mMore = findViewById(R.id.more);
        mIndex = findViewById(R.id.index);
        mRadioGroup.check(R.id.time);

    }

    public void setSymbol(String symbol) {
        mSymbol = symbol;

    }



    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            RadioButton radioButton = group.findViewById(checkedId);
            if (radioButton == null ||  !radioButton.isChecked()) {
                return;
            }
            mLastOptionId = mCurrentOptionId;
            mCurrentOptionId = checkedId;
            if (mUseLastSelected) {
                mUseLastSelected = false;
                return;
            }

            if (checkedId != R.id.more && checkedId != R.id.index) {
                mMore.resetOption();
                if (mTimeOptionPopupWindow != null) {
                    mTimeOptionPopupWindow.reset();
                }
            }
            switch (checkedId) {
                case R.id.time:
                    RxBus.get().send(new KineType("1fen"));

                    break;
                case R.id.fifteen:
                    RxBus.get().send(new KineType("5fen"));

                    break;
                case R.id.hour:
                    RxBus.get().send(new KineType("1hour"));

                    break;
                case R.id.k_day:
                    RxBus.get().send(new KineType("1day"));

                    break;
                case R.id.more:
                    showOptionPop();
                    break;
                case R.id.index:
                    showIndexPop();
                    break;
            }
        }
    };

    private void showOptionPop() {
        if (mTimeOptionPopupWindow == null) {
            mTimeOptionPopupWindow = new TimeOptionPopupWindow(getContext());
            mTimeOptionPopupWindow.setOnDismissListener(() -> {
                TimeOptionPopupWindow.Option option = mTimeOptionPopupWindow.getOption();
                if (option == null) {
                    checkLastRadioButton();
                } else {
                    if (mMore.getOption() != option) {
                        mMore.setSelectedOption(option);

                    }
                    mRadioGroup.clearCheck();
                }
            });
        }
        mTimeOptionPopupWindow.showAsDropDown(mRadioGroup);
    }

    private void showIndexPop() {
        if (mIndexOptionPopupWindow == null) {
            mIndexOptionPopupWindow = new IndexOptionPopupWindow(getContext());
            mIndexOptionPopupWindow.setOnIndexChangeListener(new IndexOptionPopupWindow.OnIndexChangeListener() {
                @Override
                public void onSecondIndexChange(Index index) {

                }

                @Override
                public void onMainIndexChange(Index index) {

                }
            });
            mIndexOptionPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    checkLastRadioButton();
                }
            });
        }
        mIndexOptionPopupWindow.showAsDropDown(mRadioGroup);
    }



    private int getIdByOption(TimeOptionPopupWindow.Option option) {
        switch (option) {
            case ONE_MINUTE:
                return CHART_MINUTE;
            case FIVE_MINUTE:
                return CHART_FIFTEEN;
            case THIRTY_MINUTE:
                return CHART_THRITY;
            case WEEK:
                return CHART_WEEK;
        }
        return -1;
    }

    private void checkLastRadioButton() {
        RadioButton radioButton = mRadioGroup.findViewById(mLastOptionId);
        if (radioButton == null) {
            return;
        }
        mUseLastSelected = true;
        radioButton.setChecked(true);
    }


    Disposable disposable;  //   全局变量

//    void testCount() {
//        disposable = Observable.interval(20, 20, TimeUnit.SECONDS)
//                .subscribe(count ->);
//    }

    void stop() {
        if (disposable != null) {
            disposable.dispose();
        }
    }


}
