package com.hazz.aipick.socket;

import android.util.Log;

import com.google.gson.Gson;

import io.crossbar.autobahn.websocket.WebSocketConnection;
import io.crossbar.autobahn.websocket.WebSocketConnectionHandler;
import io.crossbar.autobahn.websocket.types.WebSocketOptions;

public class MyWebSocketManager {
    private volatile static MyWebSocketManager webSocketManager;
    private WebSocketConnection wsc;

    private MyWebSocketManager() {
    }

    public static MyWebSocketManager getInstance() {
        if (webSocketManager == null) {
            synchronized (MyWebSocketManager.class) {
                if (webSocketManager == null) {
                    webSocketManager = new MyWebSocketManager();
                }
            }
        }
        return webSocketManager;
    }

    public void connReceiveWebSocketData() {
        if (null == wsc) {
            wsc = new WebSocketConnection();
        }
        WebSocketOptions mWebSocketOptions = new WebSocketOptions();
        mWebSocketOptions.setMaxFramePayloadSize(1024 * 1024 * 2);

        //重连间隔
        mWebSocketOptions.setReconnectInterval(10000);

        try {
            wsc.connect("ws://p_hb_ws.ratafee.nl/", new WebSocketConnectionHandler() {

                @Override
                public void onOpen() {
                    Log.d("junjun","open");
                }

                @Override
                public void onMessage(final String payload) {
                    Log.d("junjun",payload);
                }

                @Override
                public void onClose(int code, String reason) {
                    Log.d("junjun",reason);
                }

            }, mWebSocketOptions);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getJsonString() {
        Kbody mWebSocketParamsEnty = new Kbody();
        Gson gson = new Gson();
        String message = gson.toJson(mWebSocketParamsEnty);
        return message;
    }
    public void requestK(String symbol, String period) {
        Gson mGson = new Gson();
        Kbody kBody = new Kbody();
        kBody.sub = symbol;
        kBody.id = period;
        String message = mGson.toJson(kBody);
        if (wsc != null && message != null) {
            try {
                wsc.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 关闭WebSocket
     */
    public void closeWebSocket() {
        if (wsc != null) {
            wsc.sendClose();
        }
    }

}

