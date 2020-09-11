package com.hazz.aipick.managers;

import android.support.annotation.NonNull;

import com.hazz.aipick.socket.KLineBean;
import com.hazz.aipick.socket.KResponse;
import com.hazz.aipick.socket.WsManager;
import com.hazz.aipick.utils.RxBus;
import com.vinsonguo.klinelib.model.HisData;
import com.vinsonguo.klinelib.util.DataUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import io.reactivex.functions.Consumer;

public class KManager {

    public static final String PERIOD_ONE_MINUTE = "kline.1min";
    public static final String PERIOD_FIVE_MINUTE = "kline.5min";
    public static final String PERIOD_FIFTEEN_MINUTE = "kline.15min";
    public static final String PERIOD_THIRTY_MINUTE = "kline.30min";
    public static final String PERIOD_ONE_HOUR = "kline.60min";
    public static final String PERIOD_FOUR_HOUR = "kline.4hour";
    public static final String PERIOD_ONE_DAY = "kline.1day";
    public static final String PERIOD_ONE_WEEK = "kline.1week";
    public static final String PERIOD_ONE_MONTH = "kline.1mon";

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
        requestK(symbol + mPeriod);
    }

    public void requestFifteenMinuteK(String symbol) {
        mPeriod = PERIOD_FIFTEEN_MINUTE;
        requestK(symbol + mPeriod);
    }

    public void requestOneHourK(String symbol) {
        mPeriod = PERIOD_ONE_HOUR;
        requestK(symbol + mPeriod);
    }

    public void requestOneDayK(String symbol) {
        mPeriod = PERIOD_ONE_DAY;
        requestK(symbol + mPeriod);
    }

    public void requestWeekK(String symbol) {
        mPeriod = PERIOD_ONE_WEEK;
        requestK(symbol + mPeriod);
    }

    public void requestMonthK(String symbol) {
        mPeriod = PERIOD_ONE_MONTH;
        requestK(symbol + mPeriod);
    }

    public void requestFourHourK(String symbol) {
        mPeriod = PERIOD_FOUR_HOUR;
        requestK(symbol + mPeriod);
    }

    public void requestFiveMinuteK(String symbol) {
        mPeriod = PERIOD_FIVE_MINUTE;
        requestK(symbol + mPeriod);
    }


    public void requestThirtyMinuteK(String symbol) {
        mPeriod = PERIOD_THIRTY_MINUTE;
        requestK(symbol + mPeriod);
    }

    private String requestK = "";

    private void requestK(String period) {
        requestK = period;
        WsManager.getInstance().requestK(period);
        RxBus.get().observerOnMain(this, KLineBean.class, new Consumer<KLineBean>() {
            @Override
            public void accept(KLineBean bean) throws Exception {
                if (bean == null || bean.data == null || !bean.rep.equalsIgnoreCase(requestK))
                    return;
                List<HisData> dataList = new ArrayList<>(bean.data.size());
                for (KLineBean.DataBean a : bean.data) {

                    HisData data = new HisData(a.open, a.close, a.high, a.low, a.vol, a.id * 1000);
                    dataList.add(data);
                }
                notifyK(DataUtils.calculateHisData(dataList));
            }
        });
    }

}
