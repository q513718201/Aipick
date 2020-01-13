package com.hazz.aipick.socket;

import android.util.Log;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;



public class WebSocket extends WebSocketClient {

    private static final String TAG = "WebSocket";

    private volatile static WebSocket sWebSocket;

    private static final String TOPIC_POSITION = "position";
    private static final String TOPIC_ORDER_BOOK = "orderbook";
    private static final String TOPIC_POSITION_PUSH = "position-push";
    private static final String TOPIC_SNAPSHOT = "snapshot";
    private static final String TOPIC_LOGIN = "login";
    private static final String TOPIC_ORDER_STATUS = "orderstatus";
    private static final String TOPIC_TICK = "tick";
    private static final String TOPIC_K = "k";
    private static final String TOPIC_HISTICK = "histick";

    private String mLastSubscribeDepth;
    private static final int DEPTH_LENGTH = 7;

    private static final String USDT = "USDT";
    private static final String BC = "BC";
    private static final String BTC = "BTC";
    private static final String AUC = "AUC";
    private static long lastClickTime = 0;

    public static final int MIN_CLICK_DELAY_TIME = 2000;

    private Gson mGson;

    private boolean mConnecting = false;

//    private Map<String, ArrayList<SocketListener>> mListeners = new HashMap<>();
//    private Map<String, ArrayList<SocketTickListener>> mListenerTick = new HashMap<>();

    private String mMarket;

    private WebSocket(URI serverUri) {
        super(serverUri);
        mGson = new Gson();
    }


    public static WebSocket getInstance() {
        if (sWebSocket == null) {
            synchronized (WebSocketClient.class) {
                if (sWebSocket == null) {
                    try {
                        sWebSocket = new WebSocket(new URI("wss://ws.coincap.io/trades/binance"));
                    } catch (URISyntaxException e) {
                        Log.d("junjun",e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
        return sWebSocket;
    }


    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.d("junjun","onOpen");
        if (mConnecting) {

            mConnecting = false;
        }
    }


    @Override
    public void onMessage(String message) {
        Log.d("junjun",message);
    }


    @Override
    public void onClose(int code, String reason, boolean remote) {
        Logger.d("onClose " + code + " " + reason + " " + remote);
        sWebSocket = null;
    }

    @Override
    public void onError(Exception ex) {
        Logger.d("onError: " + ex.getMessage());
    }

    public void tryToConnect() {
        Logger.d("tryToConnect " + getReadyState());
        if (getReadyState() == READYSTATE.NOT_YET_CONNECTED && !mConnecting) {
            mConnecting = true;
            connect();
        }
    }
}
