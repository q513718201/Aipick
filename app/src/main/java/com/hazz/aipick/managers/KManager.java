package com.hazz.aipick.managers;

import android.support.annotation.NonNull;

import com.hazz.aipick.socket.KBody;
import com.hazz.aipick.socket.KResponse;
import com.hazz.aipick.socket.WebSocket;
import com.vinsonguo.klinelib.model.HisData;
import com.vinsonguo.klinelib.util.DataUtils;
import com.vinsonguo.klinelib.util.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class KManager {

    public static final String PERIOD_ONE_MINUTE = "1m";
    public static final String PERIOD_FIVE_MINUTE = "5m";
    public static final String PERIOD_FIFTEEN_MINUTE = "15m";
    public static final String PERIOD_THIRTY_MINUTE = "30m";
    public static final String PERIOD_ONE_HOUR = "60m";
    public static final String PERIOD_ONE_DAY = "1d";
    public static final String PERIOD_ONE_WEEK = "1w";

    private String mPeriod = PERIOD_ONE_MINUTE;


    public static KManager shared() {
        return KManager.Holder.instance;
    }

    private KListener mKListener;

    private KManager() {
    }

    private static class Holder {
        private static final KManager instance = new KManager();
    }

    public void handleKResponse(KResponse response) {
        List<HisData> data = new ArrayList<>(response.getData().size());
        for (Map.Entry<String, String> pair : response.getData().entrySet()) {
            HisData hisData = createHisData(pair);
            data.add(hisData);
        }
        if (mPeriod.equals(PERIOD_ONE_DAY) || mPeriod.equals(PERIOD_ONE_WEEK)) {
            HisData hisData = data.get(0);
            long date = hisData.getDate();
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("gmt"));
            calendar.setTimeInMillis(date);
            String to = DateUtils.formatDayTime(calendar.getTime().getTime());
            calendar.add(Calendar.DAY_OF_YEAR, -365);
            String from = DateUtils.formatDayTime(calendar.getTime().getTime());
            KBody kBody = new KBody();
            kBody.setSymbol(response.symbol);
            kBody.setFrom(from);
            kBody.setTo(to);
            kBody.setPeriod(mPeriod);
            kBody.setTopic("k");
//            Singleton.get().dataSource
//                    .api().getTokenHisk(kBody)
//                    .compose(RxResultHelper.handleResult())
//                    .compose(ScheduleCompat.apply())
//                    .subscribe(new ByexSubscriber<HiskResponse>() {
//                        @Override
//                        protected void success(HiskResponse hiskResponse) {
//                            ArrayList<HisData> merge = new ArrayList<>(data.size() + hiskResponse.getData().size());
//                            ArrayList<HisData> oldData = new ArrayList<>(hiskResponse.getData().size());
//                            for (Map.Entry<String, String> pair : hiskResponse.getData().entrySet()) {
//                                HisData hisData = createHisData(pair);
//                                oldData.add(hisData);
//                            }
//                            merge.addAll(oldData);
//                            merge.addAll(data);
//                            notifyK(DataUtils.calculateHisData(merge));
//                        }
//
//                        @Override
//                        protected void failure(HLError error) {
//                            Logger.d(error.getMessage());
//                        }
//                    });

        } else {
            notifyK(DataUtils.calculateHisData(data));
        }

    }

    @NonNull
    private HisData createHisData(Map.Entry<String, String> pair) {
        HisData hisData = new HisData();
        String value = pair.getValue();
        String key = pair.getKey();
        //open|high|low|close|volume"
        String[] split = value.split("\\|");
        hisData.setOpen(Double.valueOf(split[0]));
        hisData.setHigh(Double.valueOf(split[1]));
        hisData.setLow(Double.valueOf(split[2]));
        hisData.setClose(Double.valueOf(split[3]));
        hisData.setVol(Float.valueOf(split[4]));
        try {
            if (key.length() == 8) {
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                format.setTimeZone(TimeZone.getTimeZone("gmt"));
                hisData.setDate(format.parse(key).getTime());
            } else {
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault());
                format.setTimeZone(TimeZone.getTimeZone("gmt"));
                hisData.setDate(format.parse(key).getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hisData;
    }


    private void notifyK(List<HisData> hisData) {
        if (mKListener != null) {
            mKListener.onK(hisData);
        }
    }

    public interface KListener {
        void onK(List<HisData> data);
    }

    public void setKListener(KListener listener) {
        mKListener = listener;
    }


    public void requestOneMinuteK(String symbol) {
        mPeriod = PERIOD_ONE_MINUTE;
        Date date = new Date();
        String to = DateUtils.formatPeriodTime(date.getTime());
        long time = date.getTime() - 8 * 60 * 60 * 1000;
        Date eight = new Date(time);
        String from = DateUtils.formatPeriodTime(eight.getTime());
        WebSocket.getInstance().requestK(symbol, PERIOD_ONE_MINUTE, from, to);
    }

    public void requestFifteenMinuteK(String symbol) {
        mPeriod = PERIOD_FIFTEEN_MINUTE;
        Date date = new Date();
        String to = DateUtils.formatPeriodTime(date.getTime());
        long time = date.getTime() - 120 * 60 * 60 * 1000;
        Date four = new Date(time);
        String from = DateUtils.formatPeriodTime(four.getTime());
        WebSocket.getInstance().requestK(symbol, PERIOD_FIFTEEN_MINUTE, from, to);
    }

    public void requestOneHourK(String symbol) {
        mPeriod = PERIOD_ONE_HOUR;
        Date date = new Date();
        String to = DateUtils.formatPeriodTime(date.getTime());
        long time = date.getTime() - 480 * 60 * 60 * 1000;
        Date twenty = new Date(time);
        String from = DateUtils.formatPeriodTime(twenty.getTime());
        WebSocket.getInstance().requestK(symbol, PERIOD_ONE_HOUR, from, to);
    }

    public void requestOneDayK(String symbol) {
        mPeriod = PERIOD_ONE_DAY;
        Date date = new Date();
        String to = DateUtils.formatDayTime(date.getTime());
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_YEAR, -360);
        String from = DateUtils.formatDayTime(instance.getTime().getTime());
        WebSocket.getInstance().requestK(symbol, PERIOD_ONE_DAY, from, to);
    }

    public void requestWeekK(String symbol) {
        mPeriod = PERIOD_ONE_WEEK;
        Date date = new Date();
        String to = DateUtils.formatDayTime(date.getTime());
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_YEAR, -360);
        String from = DateUtils.formatDayTime(instance.getTime().getTime());
        WebSocket.getInstance().requestK(symbol, PERIOD_ONE_WEEK, from, to);
    }


    public void requestFiveMinuteK(String symbol) {
        mPeriod = PERIOD_FIVE_MINUTE;
        Date date = new Date();
        String to = DateUtils.formatPeriodTime(date.getTime());
        long time = date.getTime() - 40 * 60 * 60 * 1000;
        Date four = new Date(time);
        String from = DateUtils.formatPeriodTime(four.getTime());
        WebSocket.getInstance().requestK(symbol, PERIOD_FIVE_MINUTE, from, to);
    }


    public void requestThirtyMinuteK(String symbol) {
        mPeriod = PERIOD_THIRTY_MINUTE;
        Date date = new Date();
        String to = DateUtils.formatPeriodTime(date.getTime());
        long time = date.getTime() - 240 * 60 * 60 * 1000;
        Date four = new Date(time);
        String from = DateUtils.formatPeriodTime(four.getTime());
        WebSocket.getInstance().requestK(symbol, PERIOD_THIRTY_MINUTE, from, to);
    }


}
