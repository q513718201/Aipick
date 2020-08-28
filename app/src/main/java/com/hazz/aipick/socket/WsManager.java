package com.hazz.aipick.socket;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.hazz.aipick.MyApplication;
import com.hazz.aipick.managers.KManager;
import com.hazz.aipick.mvp.model.bean.BorSell;
import com.hazz.aipick.mvp.model.bean.BuyOrSell;
import com.hazz.aipick.mvp.model.bean.Ping;
import com.hazz.aipick.mvp.model.bean.Trade;
import com.hazz.aipick.utils.GZipUtil;
import com.hazz.aipick.utils.RxBus;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WsManager {
    private static WsManager mInstance;
    private final String TAG = this.getClass().getSimpleName();

    /**
     * WebSocket config
     */
    private static final int FRAME_QUEUE_SIZE = 5;
    private static final int CONNECT_TIMEOUT = 5000;
    private static final String DEF_URL = "ws://p_hb_ws.ratafee.nl/";
    private String url;

    private WsStatus mStatus;
    private WebSocket ws;
    private WsListener mListener;
    private String currentType = "kline";
    private Gson gson = new Gson();
    private HashMap<String, CoinDetail> mCoinDetailList = new HashMap<>();

    private WsManager() {
    }


    public static WsManager getInstance() {
        if (mInstance == null) {
            synchronized (WsManager.class) {
                if (mInstance == null) {
                    mInstance = new WsManager();
                }
            }
        }
        return mInstance;
    }


    public void init() {
        try {
            /**
             * configUrl其实是缓存在本地的连接地址
             * 这个缓存本地连接地址是app启动的时候通过http请求去服务端获取的,
             * 每次app启动的时候会拿当前时间与缓存时间比较,超过6小时就再次去服务端获取新的连接地址更新本地缓存
             */
            String configUrl = "";
            url = TextUtils.isEmpty(configUrl) ? DEF_URL : configUrl;
            ws = new WebSocketFactory().createSocket(url, CONNECT_TIMEOUT)
                    .setFrameQueueSize(FRAME_QUEUE_SIZE)//设置帧队列最大值为5
                    .setMissingCloseFrameAllowed(false)//设置不允许服务端关闭连接却未发送关闭帧
                    .addListener(mListener = new WsListener())//添加回调监听
                    .connectAsynchronously();//异步连接
            setStatus(WsStatus.CONNECTING);
            Logger.t(TAG).d("第一次连接");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface onMyClick {
        void onClick(HashMap<String, CoinDetail> coinDetails);
    }

    public onMyClick onMyClick;

    public void setOnMyClick(onMyClick onMyClick) {
        this.onMyClick = onMyClick;
    }

    public interface onPing {
        void onPing(CoinDetail coinDetails);
    }

    public onPing onPing;

    public void setOnPing(onPing onPing) {
        this.onPing = onPing;
    }


    public interface onBbo {
        void onBbo(BuyOrSell coinDetails);
    }

    public onBbo onBbo;

    public void setOnBbo(onBbo onPing) {
        this.onBbo = onPing;
    }


    public interface onBs {
        void onBs(BorSell borSell);
    }

    public onBs onBs;

    public void setOnBs(onBs onBs) {
        this.onBs = onBs;
    }


    public interface onTrade {
        void onTradeBack(Trade trade);
    }

    public onTrade mOnTrade;

    public void setOnTrade(onTrade mOnTrade) {
        this.mOnTrade = mOnTrade;
    }


    /**
     * 继承默认的监听空实现WebSocketAdapter,重写我们需要的方法
     * onTextMessage 收到文字信息
     * onConnected 连接成功
     * onConnectError 连接失败
     * onDisconnected 连接关闭
     */
    class WsListener extends WebSocketAdapter {
        @Override
        public void onTextMessage(WebSocket websocket, String text) throws Exception {
            super.onTextMessage(websocket, text);
            Logger.t(TAG).d(text);
        }

        @Override
        public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {
            super.onBinaryMessage(websocket, binary);
            String s = GZipUtil.uncompressToString(binary);
            if (s.contains("error")) {
                Log.e("daidai", s);
            } else {
                Log.d("daidai", s);
            }
            CoinDetail klineBean = gson.fromJson(s, CoinDetail.class);

            if (klineBean.tick != null) {
                mCoinDetailList.put(klineBean.ch, klineBean);
            }

            if (mCoinDetailList.size() == 10) {
                if (onMyClick != null) {
                    onMyClick.onClick(mCoinDetailList);
                }
            }

            KLineBean KLineBean1 = gson.fromJson(s, KLineBean.class);
            if (!KLineBean1.isEmpty()) {
                RxBus.get().send(KLineBean1);
            }


            BorSell borSell = gson.fromJson(s, BorSell.class);

            if (borSell != null) {
                if (borSell.tick != null) {
                    if (onBs != null) {
                        onBs.onBs(borSell);
                    }
                }

            }

            BuyOrSell buyOrSell = gson.fromJson(s, BuyOrSell.class);
            if (buyOrSell != null) {
                if (buyOrSell.tick != null) {
                    if (onBbo != null) {
                        onBbo.onBbo(buyOrSell);
                    }
                }

            }


            Trade trade = gson.fromJson(s, Trade.class);
            if (trade != null) {
                if (trade.tick != null) {
                    if (trade.tick.data != null) {
                        if (mOnTrade != null) {
                            mOnTrade.onTradeBack(trade);
                        }
                    }

                }

            }


            if (onPing != null) {
                if (klineBean.tick != null) {
                    onPing.onPing(klineBean);
                }
            }

            Ping ping = gson.fromJson(s, Ping.class);
            if (ping != null) {
                ws.sendText(s);
            }


        }

        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers)
                throws Exception {
            super.onConnected(websocket, headers);


            Logger.t(TAG).d("连接成功");
            setStatus(WsStatus.CONNECT_SUCCESS);
            cancelReconnect();//连接成功的时候取消重连,初始化连接次数
        }


        @Override
        public void onConnectError(WebSocket websocket, WebSocketException exception)
                throws Exception {
            super.onConnectError(websocket, exception);
            Logger.t(TAG).d("连接错误");
            setStatus(WsStatus.CONNECT_FAIL);
            reconnect();//连接错误的时候调用重连方法
        }


        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer)
                throws Exception {
            super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
            Logger.t(TAG).d("断开连接");
            setStatus(WsStatus.CONNECT_FAIL);
            reconnect();//连接断开的时候调用重连方法
        }
    }


    private void setStatus(WsStatus status) {
        this.mStatus = status;
    }


    private WsStatus getStatus() {
        return mStatus;
    }

    public void requestPing(String ping) {
        currentType = "ping";
        if (ws != null) {
            try {
                ws.sendText(ping);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void requestBbo(String market) {
        Gson mGson = new Gson();
        KLineBodyDetail kBody = new KLineBodyDetail();
        kBody.sub = market;
        kBody.id = "id1";
        String message = mGson.toJson(kBody);
        if (ws != null && message != null) {
            try {
                ws.sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void requestTrade(String sub) {
        Gson mGson = new Gson();
        KLineBodyDetail kBody = new KLineBodyDetail();
        kBody.sub = sub;
        kBody.id = "id1";
        String message = mGson.toJson(kBody);
        if (ws != null && message != null) {
            try {
                ws.sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void requestK(String req) {
        Gson mGson = new Gson();
        KLineBody kBody = new KLineBody();
        kBody.req = req;
        kBody.id = "id10";
        String message = mGson.toJson(kBody);
        if (ws != null && message != null) {
            try {
                ws.sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void requestCoinDetail(String sub, String type) {
        currentType = type;
        KLineBodyDetail kBody = new KLineBodyDetail();
        kBody.sub = sub;
        kBody.id = "id1";
        String message = gson.toJson(kBody);

        if (ws != null && message != null) {
            try {
                ws.sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void disconnect() {
        if (ws != null) {
            ws.disconnect();
        }
    }


    private Handler mHandler = new Handler();

    private int reconnectCount = 0;//重连次数
    private long minInterval = 3000;//重连最小时间间隔
    private long maxInterval = 60000;//重连最大时间间隔


    public void reconnect() {
        if (!isNetConnect()) {
            reconnectCount = 0;
            Logger.t(TAG).d("重连失败网络不可用");
            return;
        }

        //这里其实应该还有个用户是否登录了的判断 因为当连接成功后我们需要发送用户信息到服务端进行校验
        //由于我们这里是个demo所以省略了
        if (ws != null &&
                !ws.isOpen() &&//当前连接断开了
                getStatus() != WsStatus.CONNECTING) {//不是正在重连状态

            reconnectCount++;
            setStatus(WsStatus.CONNECTING);

            long reconnectTime = minInterval;
            if (reconnectCount > 3) {
                url = DEF_URL;
                long temp = minInterval * (reconnectCount - 2);
                reconnectTime = temp > maxInterval ? maxInterval : temp;
            }

            Logger.t(TAG).d("准备开始第%d次重连,重连间隔%d -- url:%s", reconnectCount, reconnectTime, url);
            mHandler.postDelayed(mReconnectTask, reconnectTime);
        }
    }


    private Runnable mReconnectTask = new Runnable() {

        @Override
        public void run() {
            try {
                ws = new WebSocketFactory().createSocket(url, CONNECT_TIMEOUT)
                        .setFrameQueueSize(FRAME_QUEUE_SIZE)//设置帧队列最大值为5
                        .setMissingCloseFrameAllowed(false)//设置不允许服务端关闭连接却未发送关闭帧
                        .addListener(mListener = new WsListener())//添加回调监听
                        .connectAsynchronously();//异步连接
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


    private void cancelReconnect() {
        reconnectCount = 0;
        mHandler.removeCallbacks(mReconnectTask);
    }


    private boolean isNetConnect() {
        ConnectivityManager connectivity = (ConnectivityManager) MyApplication.Companion.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }
}
