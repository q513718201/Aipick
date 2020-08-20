package com.hazz.aipick.socket;

import android.os.UserManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.hazz.aipick.MyApplication;
import com.hazz.aipick.managers.DepthManager;
import com.hazz.aipick.managers.KManager;
import com.hazz.aipick.mvp.model.OrderListItem;
import com.hazz.aipick.net.UrlPaths;
import com.hazz.aipick.utils.MarketManager;
import com.hazz.aipick.utils.RxBus;
import com.hazz.aipick.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;


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

    private LinkedBlockingQueue<Request> mRequestQueue = new LinkedBlockingQueue<>();

    private Gson mGson;

    private boolean mConnecting = false;

    private Map<String, ArrayList<SocketListener>> mListeners = new HashMap<>();
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
                        sWebSocket = new WebSocket(new URI(UrlPaths.WS_URL));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return sWebSocket;
    }


    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Logger.d("onOpen");
        if (mConnecting) {
            resendRequests();
            mConnecting = false;
        }
    }

    private void resendRequests() {
        Iterator<Request> iterator = mRequestQueue.iterator();
        while (iterator.hasNext()) {
            Request next = iterator.next();
            if (next.topic.equalsIgnoreCase(TOPIC_POSITION)) {
                requestDepth(next.params.get(0));
            } else if (next.topic.equalsIgnoreCase(TOPIC_SNAPSHOT)) {
                requestSnapshot();
            } else if (next.topic.equals(TOPIC_LOGIN)) {
                requestLogin();
            } else if (next.topic.equals(TOPIC_K)) {
                requestK(next.params.get(0), next.params.get(1), next.params.get(2), next.params.get(3));
            }
        }
    }

    @Override
    public void onMessage(String message) {
        BaseResponse baseResponse = mGson.fromJson(message, BaseResponse.class);
        switch (baseResponse.Topic) {
            case TOPIC_POSITION:
                DepthResponse depthResponse = mGson.fromJson(message, DepthResponse.class);
                DepthManager.shared().handleDepthResponse(depthResponse);
                break;
            case TOPIC_POSITION_PUSH:
                DepthResponse pushResponse = mGson.fromJson(message, DepthResponse.class);
                DepthManager.shared().handlePushPosition(pushResponse);
                break;
            case TOPIC_SNAPSHOT:
                SnapshotResponse snapshotResponse = mGson.fromJson(message, SnapshotResponse.class);
                MarketManager.shared().handleSnapshotResponse(snapshotResponse);
                break;
            case TOPIC_ORDER_STATUS:
                Logger.d(message);
                OrderStatusResponse orderStatusResponse = mGson.fromJson(message, OrderStatusResponse.class);
                handleOrderStatus(orderStatusResponse);
                break;
            case TOPIC_LOGIN:
                break;
            case TOPIC_TICK:
                TickResponse tickResponse = parseTick(message);
                MarketManager.shared().handleTickResponse(tickResponse);
                break;
            case TOPIC_K:
                KResponse kResponse = mGson.fromJson(message, KResponse.class);
                KManager.shared().handleKResponse(kResponse);
                break;
            case TOPIC_HISTICK:
                List<OrderListItem> orderListItems = parseHistick(message);
                notifyHistick(orderListItems);
                break;
            case TOPIC_ORDER_BOOK:
                OrderBookResponse orderBookResponse = mGson.fromJson(message, OrderBookResponse.class);
                DepthManager.shared().handleOrderBookResponse(orderBookResponse);
                break;

        }
    }

    private void notifyHistick(List<OrderListItem> orderListItems) {
        ArrayList<SocketListener> socketListeners = mListeners.get(TOPIC_HISTICK);
        if (socketListeners != null) {
            for (SocketListener listener : socketListeners) {
                listener.onTick(orderListItems);
            }
        }
    }

    private List<OrderListItem> parseHistick(String message) {
        HistickResponse histickResponse = mGson.fromJson(message, HistickResponse.class);
        List<OrderListItem> orderListItems = new ArrayList<>();
        for (String value : histickResponse.getData()) {
            OrderListItem orderListItem = new OrderListItem();
            String[] split = value.split("\\|");
            orderListItem.symbol = split[0];
            orderListItem.direction = Integer.valueOf(split[1]);
            long timestamp = Long.parseLong(split[2]);
            Date date = new Date(timestamp * 1000);
            DateFormat tf = new SimpleDateFormat("HH:mm:ss");
            orderListItem.tradeDate = tf.format(date);
            orderListItem.timestamp = timestamp;
            orderListItem.meanPrice = new BigDecimal(split[3]);
            orderListItem.amount = new BigDecimal(split[4]);
            orderListItems.add(orderListItem);
        }
        Collections.sort(orderListItems, new Comparator<OrderListItem>() {
            @Override
            public int compare(OrderListItem o1, OrderListItem o2) {
                return (int) (o2.timestamp - o1.timestamp);
            }
        });
        if (orderListItems.size() > 10) {
            orderListItems = orderListItems.subList(0, 10);
        }
        return orderListItems;

    }


    private TickResponse parseTick(String message) {
        TickResponse tickResponse = mGson.fromJson(message, TickResponse.class);
        String[] split = tickResponse.data.split("\\|");
        if (split.length > 0) {
            tickResponse.symbol = split[0];
            tickResponse.atype = split[1];
            tickResponse.dt = split[2];
            tickResponse.price = split[3];
            tickResponse.qty = split[4];
        }
        return tickResponse;
    }

    private void handleOrderStatus(OrderStatusResponse orderStatusResponse) {
        switch (orderStatusResponse.Status) {
            case OrderStatusResponse.STATUS_ACCEPTED:
            case OrderStatusResponse.STATUS_ALL_CANCELLED:
            case OrderStatusResponse.STATUS_ALL_FILLED:
            case OrderStatusResponse.STATUS_PARIAL_CANCELLED:
            case OrderStatusResponse.STATUS_PARTIAL_FILLED:
                RxBus.get().send("update");
                break;
            case OrderStatusResponse.STATUS_REJECTED:
                ToastUtils.showToast(MyApplication.Companion.getContext(), "订单被拒绝");
                break;
        }
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


    public void requestLogin() {
//        if (isOpen()) {
//            String refreshToken = UserManager.getInstance().getRefreshToken(BaseApp.shared());
//            if (refreshToken != null) {
//                LoginBody loginBody = new LoginBody(TOPIC_LOGIN, refreshToken);
//                String s = mGson.toJson(loginBody);
//                send(s);
//            }
//        } else {
//            queue(new Request(TOPIC_LOGIN));
//        }

    }

    public void requestDepth(String market) {
        mMarket = market;
        if (isOpen()) {
            DepthBody body = new DepthBody();
            body.setTopic(TOPIC_POSITION);
            body.setDepth(DEPTH_LENGTH);
            if (!TextUtils.isEmpty(mLastSubscribeDepth)) {
                body.setUnsubscribe(Collections.singletonList(mLastSubscribeDepth));
            }
            body.setSymbol(market);
            String jsonString = mGson.toJson(body);
            mLastSubscribeDepth = market;
            send(jsonString);
            Logger.d(body.toString());
        } else {
            queue(new Request(TOPIC_POSITION, market));
        }
    }

    public void requestSnapshot() {
        if (isOpen()) {
            SnapshotBody snapshotBody = new SnapshotBody();
            snapshotBody.setTopic(TOPIC_SNAPSHOT);
            List<String> subscribes = new ArrayList<String>();
            subscribes.add(USDT);
            subscribes.add(BC);
            subscribes.add(BTC);
            subscribes.add(AUC);
            snapshotBody.setSubscribe(subscribes);
            String jsonString = mGson.toJson(snapshotBody);
            send(jsonString);
        } else {
            queue(new Request(TOPIC_SNAPSHOT));
        }
    }

    public void requestK(String symbol, String period, String from, String to) {
        if (isOpen()) {
            KBody kBody = new KBody();
            kBody.setTopic(TOPIC_K);
            kBody.setSymbol(symbol);
            kBody.setTo(to);
            kBody.setFrom(from);
            kBody.setPeriod(period);
            String s = mGson.toJson(kBody);
            Logger.d(s);
            send(s);
        } else {
            queue(new Request(TOPIC_K, symbol, period, from, to));
        }
    }

    public void requestHistick(String symbol) {
        if (isOpen()) {
            HistickBody histickBody = new HistickBody();
            histickBody.setTopic(TOPIC_HISTICK);
            histickBody.setSymbol(symbol);
            String s = mGson.toJson(histickBody);
            send(s);
        } else {
            queue(new Request(TOPIC_HISTICK, symbol));
        }
    }

    private void queue(Request top) {
        try {
            mRequestQueue.put(top);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void addHistickListener(SocketListener listener) {
        ArrayList<SocketListener> socketListeners = mListeners.get(TOPIC_HISTICK);
        if (socketListeners == null) {
            socketListeners = new ArrayList<>();
            mListeners.put(TOPIC_HISTICK, socketListeners);
        }
        socketListeners.add(listener);
    }

    public void removeHistickListener(SocketListener listener) {
        ArrayList<SocketListener> socketListeners = mListeners.get(TOPIC_HISTICK);
        if (socketListeners != null) {
            socketListeners.remove(listener);
        }
    }


    /**
     * NOte:If call connect twice in a short period, it may cause IllegalStateException: WebSocketClient objects are not reuseable.
     * And if call reconnect first and then connect, the status will satisfy NOT_YET_CONNECTED, it wll cause the same problem.
     */
    public void tryToConnect() {
        Logger.d("tryToConnect " + getReadyState());
        if (getReadyState() == READYSTATE.NOT_YET_CONNECTED && !mConnecting) {
            mConnecting = true;
            connect();
        }
    }

    public void stopSnapshot() {
        if (isOpen()) {
            SnapshotBody snapshotBody = new SnapshotBody();
            snapshotBody.setTopic(TOPIC_SNAPSHOT);
            List<String> unsubscribes = new ArrayList<String>();
            unsubscribes.add(USDT);
            unsubscribes.add(BC);
            unsubscribes.add(BTC);
            snapshotBody.setUnsubscribe(unsubscribes);
            String jsonString = mGson.toJson(snapshotBody);
            send(jsonString);
        }
    }

    public void stopDepth() {
        if (isOpen() && mMarket != null) {
            DepthBody body = new DepthBody();
            body.setTopic(TOPIC_POSITION);
            body.setDepth(DEPTH_LENGTH);
            body.setUnsubscribe(Collections.singletonList(mMarket));
            String jsonString = mGson.toJson(body);
            send(jsonString);
            Logger.d(body.toString());
        }
    }

    public class Request {
        String topic;
        List<String> params = new ArrayList<>();

        public Request(String topic) {
            this.topic = topic;
        }

        public Request(String topic, String... params) {
            this.topic = topic;
            for (int i = 0; i < params.length; i++) {
                this.params.add(params[0]);
            }

        }
    }

}
