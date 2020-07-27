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
import com.hazz.aipick.ui.adapter.PhotoAdapter;

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

    public interface onMyClick {
        void onClick(String type);
    }

    public onMyClick onMyClick;

    public void setOnMyClick(onMyClick onMyClick) {
        this.onMyClick = onMyClick;
    }

    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            RadioButton radioButton = group.findViewById(checkedId);
            if (radioButton == null || !radioButton.isChecked()) {
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
                    if (onMyClick != null) {
                        onMyClick.onClick("1fen");
                    }

                    break;
                case R.id.fifteen:
                    if (onMyClick != null) {
                        onMyClick.onClick("15fen");
                    }
                    break;
                case R.id.hour:
                    if (onMyClick != null) {
                        onMyClick.onClick("1hour");
                    }
                    break;
                case R.id.k_day:
                    if (onMyClick != null) {
                        onMyClick.onClick("1day");
                    }
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
            mTimeOptionPopupWindow.setOnMyClick(new TimeOptionPopupWindow.onMyClick() {
                @Override
                public void onClick(String type) {
                    switch (type) {
                        case "1fen": {
                            if (onMyClick != null) {
                                onMyClick.onClick("1fenk");
                            }

                            break;
                        }
                        case "5fen": {
                            if (onMyClick != null) {
                                onMyClick.onClick("5fen");
                            }

                            break;
                        }
                        case "30fen": {
                            if (onMyClick != null) {
                                onMyClick.onClick("30fen");
                            }

                            break;
                        }
                        case "1week": {
                            if (onMyClick != null) {
                                onMyClick.onClick("1week");
                            }

                            break;
                        }
                    }
                }
            });
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
                    if (index.toString().equals("VOL")) {
                        if (onMyClick != null) {
                            onMyClick.onClick("vol");
                        }
                    }
                    if (index.toString().equals("MACD")) {
                        if (onMyClick != null) {
                            onMyClick.onClick("macd");
                        }
                    }
                    if (index.toString().equals("KDJ")) {
                        if (onMyClick != null) {
                            onMyClick.onClick("kdj");
                        }
                    }
                }

                @Override
                public void onMainIndexChange(Index index) {
                    if (onMyClick != null) {
                        onMyClick.onClick("main");
                    }
                }
            });
            mIndexOptionPopupWindow.setOnDismissListener(() -> checkLastRadioButton());
        }
        mIndexOptionPopupWindow.showAsDropDown(mRadioGroup);
    }


    private void checkLastRadioButton() {
        RadioButton radioButton = mRadioGroup.findViewById(mLastOptionId);
        if (radioButton == null) {
            return;
        }
        mUseLastSelected = true;
        radioButton.setChecked(true);
    }


}
