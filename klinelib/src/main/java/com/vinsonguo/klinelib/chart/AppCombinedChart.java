package com.vinsonguo.klinelib.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.vinsonguo.klinelib.R;

/**
 * Created by dell on 2017/6/22.
 */

public class AppCombinedChart extends CombinedChart {

    private IMarker mXMarker;

    private float mYCenter;

    private boolean mDrawMADescription = false;

    private TextPaint mTextPaint;

    public AppCombinedChart(Context context) {
        this(context, null);
    }

    public AppCombinedChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppCombinedChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        mRenderer = new AppCombinedChartRenderer(this, mAnimator, mViewPortHandler);
        mTextPaint = new TextPaint();
    }

    public void setXMarker(IMarker marker) {
        mXMarker = marker;
    }

    @Override
    public void setData(CombinedData data) {
        try {
            super.setData(data);
        }catch (ClassCastException e) {
            // ignore
        }
        ((AppCombinedChartRenderer) mRenderer).createRenders();
        mRenderer.initBuffers();
    }

    @Override
    protected void drawMarkers(Canvas canvas) {
        if (mMarker == null || mXMarker == null || !isDrawMarkersEnabled() || !valuesToHighlight())
            return;

        for (int i = 0; i < mIndicesToHighlight.length; i++) {

            Highlight highlight = mIndicesToHighlight[i];

            IDataSet set = mData.getDataSetByIndex(highlight.getDataSetIndex());

            Entry e = mData.getEntryForHighlight(mIndicesToHighlight[i]);
            int entryIndex = set.getEntryIndex(e);

            // make sure entry not null
            if (e == null || entryIndex > set.getEntryCount() * mAnimator.getPhaseX())
                continue;

            float[] pos = getMarkerPosition(highlight);

            // check bounds
            if (!mViewPortHandler.isInBounds(pos[0], pos[1]))
                continue;

            // callbacks to update the content
            mMarker.refreshContent(e, highlight);
            mXMarker.refreshContent(e, highlight);

            // draw the marker
//            if (mMarker instanceof LineChartYMarkerView) {
            LineChartYMarkerView yMarker = (LineChartYMarkerView) mMarker;
            LineChartXMarkerView xMarker = (LineChartXMarkerView) mXMarker;
            int width = yMarker.getMeasuredWidth();
            mMarker.draw(canvas, getMeasuredWidth() - width * 1.05f, pos[1] - yMarker.getMeasuredHeight() / 2);

            mXMarker.draw(canvas, pos[0] - (xMarker.getMeasuredWidth() / 2), getMeasuredHeight());
//            } else {
//                mMarker.draw(canvas, pos[0], pos[1]);
//            }
        }
    }

    protected void drawDescription(Canvas c) {

        // check if description should be drawn
        if (mDescription != null && mDescription.isEnabled()) {

            MPPointF position = mDescription.getPosition();

            if (mDrawMADescription) {
                mTextPaint.setTypeface(mDescription.getTypeface());
                mTextPaint.setTextSize(mDescription.getTextSize());
                mTextPaint.setTextAlign(mDescription.getTextAlign());
                mTextPaint.setFakeBoldText(true);
                mTextPaint.setAntiAlias(true);
                Spannable spannable = new SpannableString(mDescription.getText());
                String[] split = mDescription.getText().split("  ");
                int position0 = 0;
                int position1 = split[0].length() + 2;
                int position2 = position1 + split[1].length() + 2;
                int position3 = position2 + split[2].length() + 2;
                spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.ma5)), position0, position1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.ma10)), position1, position2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.ma20)), position2, position3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.ma30)), position3, spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                StaticLayout staticLayout = new StaticLayout(spannable, mTextPaint,
                        getWidth(), Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
                float x = 10, y = 10;
                c.translate(x, y);
                staticLayout.draw(c);
            } else {
                mDescPaint.setTypeface(mDescription.getTypeface());
                mDescPaint.setTextSize(mDescription.getTextSize());
                mDescPaint.setColor(mDescription.getTextColor());
                mDescPaint.setTextAlign(mDescription.getTextAlign());
                mDescPaint.setAntiAlias(true);
                drawNormalDescription(c, position);
            }
        }
    }

    private void drawNormalDescription(Canvas c, MPPointF position) {
        float x, y;

        // if no position specified, draw on default position
        if (position == null) {
            x = mDescription.getXOffset();
//            y = mDescription.getTextSize() + mDescription.getYOffset();
            y = mDescription.getTextSize();
        } else {
            x = position.x;
            y = position.y;
        }

        c.drawText(mDescription.getText(), x, y, mDescPaint);
    }

    @Override
    protected void calcMinMax() {

        mXAxis.calculate(mData.getXMin(), mData.getXMax());

        if (mYCenter == 0) {
            // calculate axis range (min / max) according to provided data
            mAxisLeft.calculate(mData.getYMin(YAxis.AxisDependency.LEFT), mData.getYMax(YAxis.AxisDependency.LEFT));
            mAxisRight.calculate(mData.getYMin(YAxis.AxisDependency.RIGHT), mData.getYMax(YAxis.AxisDependency
                    .RIGHT));
        } else {
            float yLMin = mData.getYMin(YAxis.AxisDependency.LEFT);
            float yLMax = mData.getYMax(YAxis.AxisDependency.LEFT);
            float interval = (float) Math.max(Math.abs(mYCenter - yLMax), Math.abs(mYCenter - yLMin));
            yLMax = (float) Math.max(yLMax, (mYCenter + interval));
            yLMin = (float) Math.min(yLMin, (mYCenter - interval));
            mAxisLeft.calculate(yLMin, yLMax);

            float yRMin = mData.getYMin(YAxis.AxisDependency.RIGHT);
            float yRMax = mData.getYMax(YAxis.AxisDependency.RIGHT);
            float rinterval = (float) Math.max(Math.abs(mYCenter - yRMax), Math.abs(mYCenter - yRMin));
            yRMax = (float) Math.max(yRMax, (mYCenter + rinterval));
            yRMin = (float) Math.min(yRMin, (mYCenter - rinterval));
            mAxisRight.calculate(yRMin, yRMax);
        }
    }

    /**
     * 设置图表中Y居中的值
     */
    public void setYCenter(float YCenter) {
        mYCenter = YCenter;
    }

    public void setDrawMADescription(boolean drawMADescription) {
        mDrawMADescription = drawMADescription;
    }
}
