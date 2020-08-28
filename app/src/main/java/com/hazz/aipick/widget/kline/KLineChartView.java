package com.hazz.aipick.widget.kline;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.hazz.aipick.R;
import com.hazz.aipick.managers.KManager;
import com.hazz.aipick.utils.GsonUtil;
import com.hazz.aipick.utils.SPUtil;
import com.hazz.aipick.widget.IndexOptionPopupWindow;
import com.vinsonguo.klinelib.ChartSettings;
import com.vinsonguo.klinelib.chart.KLineView;
import com.vinsonguo.klinelib.model.HisData;
import com.vinsonguo.klinelib.model.Index;

import java.util.List;

public class KLineChartView extends RelativeLayout implements KManager.KListener {

    private RadioGroup mRadioGroup;

    private FrameLayout mFrameLayout;

    private ProgressBar mProgressBar;

    private String mSymbol;

    private TimeOptionPopupWindow mTimeOptionPopupWindow;

    private IndexOptionPopupWindow mIndexOptionPopupWindow;

    private OptionRadioButton mMore;

    private OptionRadioButton mIndex;

    private KLineView mKLineView;

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


    public KLineChartView(Context context) {
        this(context, null);
    }

    public KLineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_k_chart, this);
        mRadioGroup = findViewById(R.id.radio_group);
        mRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
        mFrameLayout = findViewById(R.id.frame);
        mProgressBar = findViewById(R.id.progressBar);
        mMore = findViewById(R.id.more);
        mIndex = findViewById(R.id.index);
        mRadioGroup.check(R.id.time);
        ChartSettings.setLanguage(SPUtil.INSTANCE.getLanguage());
    }

    public void setSymbol(String symbol) {
        mSymbol = symbol;
        switchToTimeLineChart();
    }


    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (mSymbol == null) {
                return;
            }
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
                    switchToTimeLineChart();
                    break;
                case R.id.fifteen:
                    switchToFifteenChart();
                    break;
                case R.id.hour:
                    switchToHourChart();
                    break;
                case R.id.k_day:
                    switchToDayChart();
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
                        switchToOptionChart(option);
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
                    showSecondIndex(index);
                }

                @Override
                public void onMainIndexChange(Index index) {
                    showMainIndex(index);
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


    private void showSecondIndex(Index index) {
        View view = mFrameLayout.getChildAt(0);
        KLineView kLineView = (KLineView) view;
        kLineView.setSecondIndex(index);
    }

    private void showMainIndex(Index index) {
        View view = mFrameLayout.getChildAt(0);
        KLineView kLineView = (KLineView) view;
        kLineView.setMainIndex(index);
    }

    private void switchToOptionChart(TimeOptionPopupWindow.Option option) {
        mFrameLayout.removeAllViews();
        mProgressBar.setVisibility(View.VISIBLE);
        KLineView kLineView = getKLineView();
        kLineView.setId(getIdByOption(option));
        kLineView.setVisibility(View.GONE);
        mFrameLayout.addView(kLineView);
        switch (option) {
            case WEEK:
                KManager.shared().requestWeekK(mSymbol);
                break;
            case FIVE_MINUTE:
                KManager.shared().requestFiveMinuteK(mSymbol);
                break;
            case ONE_MINUTE:
                KManager.shared().requestOneMinuteK(mSymbol);
                break;
            case THIRTY_MINUTE:
                KManager.shared().requestThirtyMinuteK(mSymbol);
                break;
        }
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

    private void switchToDayChart() {
        mFrameLayout.removeAllViews();
        mProgressBar.setVisibility(View.VISIBLE);
        KLineView kLineView = getKLineView();
        kLineView.setId(CHART_DAY);
        kLineView.setVisibility(View.GONE);
        mFrameLayout.addView(kLineView);
        KManager.shared().requestOneDayK(mSymbol);
    }

    private void switchToHourChart() {
        mFrameLayout.removeAllViews();
        mProgressBar.setVisibility(View.VISIBLE);
        KLineView kLineView = getKLineView();
        kLineView.setId(CHART_HOUR);
        mKLineView.setVisibility(View.GONE);
        mFrameLayout.addView(kLineView);
        KManager.shared().requestOneHourK(mSymbol);
    }

    private void switchToFifteenChart() {
        mFrameLayout.removeAllViews();
        mProgressBar.setVisibility(View.VISIBLE);
        KLineView kLineView = getKLineView();
        kLineView.setId(CHART_FIFTEEN);
        kLineView.setVisibility(View.GONE);
        mFrameLayout.addView(kLineView);
        KManager.shared().requestFifteenMinuteK(mSymbol);
    }

    private void switchToTimeLineChart() {
        mFrameLayout.removeAllViews();
        mProgressBar.setVisibility(View.VISIBLE);
        KLineView timeLineView = getTimeLineView();
        timeLineView.setId(CHART_TIME);
        timeLineView.setDateFormat("HH:mm");
        timeLineView.setVisibility(View.GONE);
        mFrameLayout.addView(timeLineView);
        KManager.shared().requestOneMinuteK(mSymbol.toLowerCase());
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        KManager.shared().setKListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        KManager.shared().setKListener(null);
    }


    private void updateTimeLine(List<HisData> data) {
        if (data.size() == 0) {
            return;
        }
        KLineView timeLineView = getTimeLineView();
        mProgressBar.setVisibility(View.GONE);
        timeLineView.setLastClose(data.get(0).getClose());
        timeLineView.initData(data);
        timeLineView.showIndex();
        timeLineView.setVisibility(View.VISIBLE);
    }

    private void updateMinuteK(List<HisData> data) {
        if (data.size() == 0) {
            return;
        }
        mProgressBar.setVisibility(View.GONE);
        KLineView kLineView = getKLineView();
        kLineView.setDateFormat("HH:ss");
        kLineView.initData(data);
        kLineView.showIndex();
        kLineView.setVisibility(View.VISIBLE);
    }


    private void updateDayK(List<HisData> data) {
        if (data.size() == 0) {
            return;
        }
        mProgressBar.setVisibility(View.GONE);
        KLineView kLineView = getKLineView();
        kLineView.setDateFormat("yyyy-MM-dd");
        kLineView.initData(data);
        kLineView.showIndex();
        kLineView.setVisibility(View.VISIBLE);
    }

    private KLineView getKLineView() {
        if (mKLineView == null) {
            mKLineView = new KLineView(getContext());
        }
        mKLineView.setMode(KLineView.Mode.K);
        return mKLineView;
    }

    private KLineView getTimeLineView() {
        if (mKLineView == null) {
            mKLineView = new KLineView(getContext());
        }
        mKLineView.setMode(KLineView.Mode.LINE);
        return mKLineView;
    }

    @Override
    public void onK(List<HisData> data) {
        View childAt = mFrameLayout.getChildAt(0);
        switch (childAt.getId()) {
            case CHART_TIME:
                post(() -> updateTimeLine(data));
                break;
            case CHART_FIFTEEN:
            case CHART_HOUR:
            case CHART_MINUTE:
            case CHART_THRITY:
            case CHART_FIVE:
                post(() -> updateMinuteK(data));
                break;
            case CHART_DAY:
            case CHART_WEEK:
                post(() -> updateDayK(data));
                break;
        }
    }
}
