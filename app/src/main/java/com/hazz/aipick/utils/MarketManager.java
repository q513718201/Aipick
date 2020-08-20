package com.hazz.aipick.utils;

import android.content.Context;
import android.util.Log;

import com.hazz.aipick.mvp.model.bean.MarketItem;
import com.hazz.aipick.socket.SnapshotResponse;
import com.hazz.aipick.socket.TickResponse;
import com.orhanobut.logger.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarketManager {

    //根本就没有custom这个选项啊
    private static final String TOKEN_CUSTOM = "CUSTOM";
    private static final String TOKEN_AUC = "AUC";
    private static final String TOKEN_BTC = "BTC";
    private static final String TOKEN_USDT = "USDT";

    private HashMap<String, MarketItem> mAllMarkets = new HashMap<>();
    private HashMap<String, List<MarketItem>> mPairHashMap = new HashMap<>();

    private List<SnapshotListener> mSnapshotListeners = new ArrayList<>();
    private List<TickListener> mTickListeners = new ArrayList<>();
    private HashMap<String, List<TickListener>> mMarketListenerHashMap = new HashMap<>();
    private static final String USER_SELECT_TOKEN_PAIR = "user_select_token_pair";
    private static final String USER_SELECT_TOKEN_PAIR_NO_LOGIN = "user_select_token_pair_no_login";
    private static final String TOKEN_PAIR = "user_select_token_pair";
    private static final String TOKEN_PAIR_NO_LOGIN = "user_select_token_pair_no_login";

    private static final int DEFAULT_MARKET_SIZE = 20;
    private List<MarketItem> allTokenPair = new ArrayList<>();
    private List<MarketItem> userTokenPair = new ArrayList<>();
//    protected Api mApi = Singleton.get().dataSource.api();

    private boolean isReload = false;


    /**
     * 初始化币对列表
     * 这里显示是获取到了 AUC 市场数据
     * @param
     */
    public void initialTokenPair() {
        mPairHashMap.clear();
        mAllMarkets.clear();
        Calendar calendar = Calendar.getInstance();
        // TODO: 2020/8/20
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        Singleton.get().dataSource.api().symbolList(String.valueOf(day))
//                .compose(RxResultHelper.handleResult())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new ByexSubscriber<List<Symbol>>() {
//                    @Override
//                    protected void success(List<Symbol> data) {
//                        Log.d("YUDAN","list<Symbol> is "+data.toString());
//                        mPairHashMap.put(TOKEN_CUSTOM, new ArrayList<>(DEFAULT_MARKET_SIZE));
//                        mPairHashMap.put(TOKEN_BTC, new ArrayList<>(DEFAULT_MARKET_SIZE));
//                        mPairHashMap.put(TOKEN_USDT, new ArrayList<>(DEFAULT_MARKET_SIZE));
//                        mPairHashMap.put(TOKEN_AUC, new ArrayList<>(DEFAULT_MARKET_SIZE));
//                        if (data != null && !data.isEmpty()) {
//                            userTokenPair.clear();
//                            for (Symbol symbol : data) {
//                                String[] pair = symbol.symbol.split("/");
//                                MarketItem marketItem = new MarketItem();
//                                marketItem.tradeA = pair[0];
//                                marketItem.tradeB = pair[1];
//                                marketItem.symbol = symbol.symbol;
//                                marketItem.pricePoint = symbol.pricePoint;
//                                marketItem.weight = symbol.weight;
//                                marketItem.sizePoint = symbol.sizePoint;
//                                mAllMarkets.put(marketItem.symbol, marketItem);//所有的
//                                mPairHashMap.get(marketItem.tradeB).add(marketItem);//分类
//                                userTokenPair.add(marketItem);
//                            }
//
//                            //获取自选列表
//                            refreshSelectToken();
//                            weightSort(mPairHashMap.get(TOKEN_BTC));
//                            weightSort(mPairHashMap.get(TOKEN_USDT));
//                            weightSort(mPairHashMap.get(TOKEN_AUC));
//                            notifySnapshot();
//                            saveSymbolList(mPairHashMap);
//
//                            if (isReload) {
//                                isReload = false;
//                                startSnapshot();
//                            }
//                        }
//                    }
//
//                    @Override
//                    protected void failure(HLError error) {
//                        Logger.d(error.getMessage());
//                    }
//                });
    }

//    /**
//     * 更新自选列表币对
//     */
//    public void refreshSelectToken() {
//        List<MarketItem> userToken = getUserSelectToken();
//        List<MarketItem> marketItems = new ArrayList<>();
//        for (MarketItem item : userToken) {
//            if (item.userSelect) {
//                String symbol = item.symbol;
//                MarketItem marketItem = mAllMarkets.get(symbol);
//                if (marketItem != null) {
//                    marketItem.userSelect = true;
//                    marketItems.add(marketItem);
//                }
//            }
//        }
//        mPairHashMap.put(TOKEN_CUSTOM, marketItems);
//    }

    public void tryToReloadData() {
        if (mAllMarkets.isEmpty() || mPairHashMap.isEmpty()) {
            isReload = true;
            initialTokenPair();
        } else {
            startSnapshot();
        }
    }

    public void startSnapshot() {
        Logger.d("startSnapshot");
//        WsManager.getInstance().
//        WebSocket.getInstance().requestSnapshot();
    }

    public void stopSnapshot() {
        Logger.d("stopSnapshot");
//        WebSocket.getInstance().stopSnapshot();
    }

//    public void startWebSocket() {
//        WebSocket.getInstance().requestSnapshot();
//    }

    public MarketItem getMarket(String symbol) {
        return mAllMarkets.get(symbol);
    }

    public static void changeDownSort(List<MarketItem> markets) {
        Collections.sort(markets, (o1, o2) -> {
            float result = o2.change - o1.change;
            if (result > 0) {
                return 1;
            } else if (result < 0) {
                return -1;
            } else {
                return 0;
            }
        });
    }

    public static void weightSort(List<MarketItem> markets) {
        Collections.sort(markets, (o1, o2) -> {
            float result = o1.weight - o2.weight;
            if (result > 0) {
                return 1;
            } else if (result < 0) {
                return -1;
            } else {
                return 0;
            }
        });
    }


    public static void changeUpSort(List<MarketItem> markets) {
        Collections.sort(markets, (o1, o2) -> {
            float result = o1.change - o2.change;
            if (result > 0) {
                return 1;
            } else if (result < 0) {
                return -1;
            } else {
                return 0;
            }
        });
    }


    public static void dateUpSort(List<MarketItem> markets) {
        Collections.sort(markets, (MarketItem o1, MarketItem o2) -> {
            long result = o1.date - o2.date;
            if (result > 0) {
                return 1;
            } else if (result < 0) {
                return -1;
            } else {
                return 0;
            }
        });
    }

    public static void dateDownSort(List<MarketItem> markets) {
        Collections.sort(markets, (o1, o2) -> {
            long result = o2.date - o1.date;
            if (result > 0) {
                return 1;
            } else if (result < 0) {
                return -1;
            } else {
                return 0;
            }
        });
    }


    public static void volumeDownSort(List<MarketItem> markets) {
        Collections.sort(markets, (o1, o2) -> Float.valueOf(o2.volume).compareTo(Float.valueOf(o1.volume)));
    }

    public static void volumeUpSort(List<MarketItem> markets) {
        Collections.sort(markets, (o1, o2) -> Float.valueOf(o1.volume).compareTo(Float.valueOf(o2.volume)));
    }


    private void saveSymbolList(HashMap<String, List<MarketItem>> pairHashMap) {
//        ACache aCache = ACache.get(BaseApp.shared(), "symbol_list");
//        if (pairHashMap != null && !pairHashMap.isEmpty()) {
//            aCache.put("symbol_map", pairHashMap);
//        }
    }

    public int getSizePoint(String symbol) {
        MarketItem s = mAllMarkets.get(symbol);
        int result = 8;
        if (s != null) {
            result = s.sizePoint;
        }
        return result;
    }

    public int getPricePoint(String symbol) {
        MarketItem s = mAllMarkets.get(symbol);
        int result = 4;
        if (s != null) {
            result = s.pricePoint;
        }
        return result;
    }

    public int getWeight(String symbol) {
        MarketItem s = mAllMarkets.get(symbol);
        int weight = 0;
        if (s != null) {
            weight = s.weight;
        }
        return weight;
    }

    public HashMap<String, List<MarketItem>> getAllMarketList() {
        if (mPairHashMap == null) {
            Log.d("YUDAN", "getAllMarketList in MarketManager  is null");
            return new HashMap<>();
        }
        if (mPairHashMap.isEmpty()) {
            Log.d("YUDAN", "getAllMarketList is empty");
//            ACache aCache = ACache.get(BaseApp.shared(), "symbol_list");
//            HashMap<String, List<MarketItem>> hashMap = (HashMap<String, List<MarketItem>>) aCache.getAsObject("symbol_map");
//            if (hashMap == null) {
//                return mPairHashMap;
//            } else {
//                mPairHashMap = hashMap;
//            }
//            if (!mPairHashMap.isEmpty() && mAllMarkets.isEmpty()) {
//                updateDefaultMarketItem(TOKEN_BTC);
//                updateDefaultMarketItem(TOKEN_USDT);
//                updateDefaultMarketItem(TOKEN_AUC);
//            }
        }
        Log.d("YUDAN", "getAllMarketList is not empty or null : "+mPairHashMap);
        return mPairHashMap;
    }

    private void updateDefaultMarketItem(String token) {
        List<MarketItem> bcMarketItems = mPairHashMap.get(token);
        for (int i = 0; i < bcMarketItems.size(); i++) {
            MarketItem marketItem = bcMarketItems.get(i);
            mAllMarkets.put(marketItem.symbol, marketItem);
        }
    }

    public List<MarketItem> getMarketList(String token) {
        return mPairHashMap.get(token);
    }


    public MarketItem getCoinPriceCny(String token) {
        return mAllMarkets.get(token);
    }

    public void handleSnapshotResponse(SnapshotResponse snapshotResponse) {
        for (Map.Entry<String, String> pair : snapshotResponse.Data.entrySet()) {
            MarketItem marketItem = mAllMarkets.get(pair.getKey());
            if (marketItem == null) {
                Logger.w("Not market exist");
                return;
            }

            String value = pair.getValue();
            String[] split = value.split("\\|");
            marketItem.date = Long.valueOf(split[0]);
            marketItem.open = split[1];
            marketItem.high = split[2];
            marketItem.low = split[3];
            marketItem.close = split[4];
            marketItem.volume = split[5];
            if (TOKEN_AUC.equals(marketItem.tradeB)) {
                marketItem.cnyPrice = marketItem.close;
            }
            BigDecimal close = new BigDecimal(marketItem.close);
            BigDecimal open = new BigDecimal(marketItem.open);
            BigDecimal percent = close.subtract(open).multiply(BigDecimal.valueOf(100)).divide(open, RoundingMode.FLOOR)
                    .setScale(2, RoundingMode.HALF_UP);
            marketItem.change = percent.floatValue();
            marketItem.isUp = percent.floatValue() >= 0;
            String percentString = marketItem.isUp ? "+" + percent.toPlainString() : percent.toPlainString();
            marketItem.changeString = percentString + "%";
        }
        calculateCNYPrice();
        notifySnapshot();
    }

    public void handleTickResponse(TickResponse tickResponse) {
        if (tickResponse == null) {
            return;
        }
        MarketItem marketItem = mAllMarkets.get(tickResponse.symbol);
        if (marketItem == null) {
            Logger.w("no market item exist");
            return;
        }
        marketItem.close = tickResponse.price;
        BigDecimal close = new BigDecimal(marketItem.close);
        BigDecimal open = new BigDecimal(marketItem.open);
        BigDecimal percent = close.subtract(open).multiply(BigDecimal.valueOf(100)).divide(open, RoundingMode.FLOOR)
                .setScale(2, RoundingMode.HALF_UP);
        marketItem.change = percent.floatValue();
        marketItem.isUp = percent.floatValue() >= 0;
        String percentString = marketItem.isUp ? "+" + percent.toPlainString() : percent.toPlainString();
        marketItem.changeString = percentString + "%";
        BigDecimal volumeBigDecimal = new BigDecimal(marketItem.volume);
        BigDecimal qtyBigDecimal = new BigDecimal(tickResponse.qty);
        marketItem.volume = volumeBigDecimal.add(qtyBigDecimal).stripTrailingZeros().toEngineeringString();
        marketItem.date = Long.valueOf(tickResponse.dt);
        BigDecimal high = new BigDecimal(marketItem.high);
        BigDecimal low = new BigDecimal(marketItem.low);
        if (close.compareTo(high) > 0) {
            marketItem.high = tickResponse.price;
        } else if (close.compareTo(low) < 0) {
            marketItem.low = tickResponse.price;
        }
        if (TOKEN_AUC.equals(marketItem.tradeB)) {
            marketItem.cnyPrice = marketItem.close;
        } else {
            String basicSymbol = marketItem.tradeB + "/AUC";
            MarketItem basicMarketItem = mAllMarkets.get(basicSymbol);
            marketItem.cnyPrice = BigDecimalUtil.mul(basicMarketItem.close, marketItem.close, marketItem.pricePoint);
        }
        notifyTick(marketItem);
    }

    private void notifyTick(MarketItem item) {
        List<TickListener> tickListeners = mMarketListenerHashMap.get(item.symbol);
        if (tickListeners != null) {
            for (TickListener tickListener : tickListeners) {
                tickListener.onTick(item);
            }
        }
        for (TickListener tickListener : mTickListeners) {
            tickListener.onTick(item);
        }
    }

    private void notifySnapshot() {
        for (SnapshotListener snapshotListener : mSnapshotListeners) {
            snapshotListener.onSnapshot(mPairHashMap);
        }
    }

    private void calculateCNYPrice() {
        List<MarketItem> btcItems = mPairHashMap.get(TOKEN_BTC);
        MarketItem btcBC = mAllMarkets.get("BTC/AUC");
        for (MarketItem market : btcItems) {
            market.cnyPrice = BigDecimalUtil.mul(btcBC.close, market.close, market.pricePoint);
        }
        List<MarketItem> usdtItems = mPairHashMap.get(TOKEN_USDT);
        MarketItem usdtBC = mAllMarkets.get("USDT/AUC");
        for (MarketItem market : usdtItems) {
            market.cnyPrice = BigDecimalUtil.mul(usdtBC.close, market.close, market.pricePoint);
        }
    }


    public interface SnapshotListener {
        void onSnapshot(HashMap<String, List<MarketItem>> snapshot);
    }

    public interface TickListener {
        void onTick(MarketItem item);
    }

    public void addSnapshotListener(SnapshotListener listener) {
        mSnapshotListeners.add(listener);
    }

    public void removeSnapshotListener(SnapshotListener listener) {
        mSnapshotListeners.remove(listener);
    }


    /**
     * 监听单个币对的Tick
     *
     * @param symbol   要监听的币对
     * @param listener 监听器
     */
    public void addMarketListener(String symbol, TickListener listener) {
        List<TickListener> tickListeners = mMarketListenerHashMap.get(symbol);
        if (tickListeners == null) {
            tickListeners = new ArrayList<>();
            mMarketListenerHashMap.put(symbol, tickListeners);
        }
        tickListeners.add(listener);
    }

    public void removeMarketListener(String symbol, TickListener listener) {
        List<TickListener> tickListeners = mMarketListenerHashMap.get(symbol);
        if (tickListeners != null) {
            tickListeners.remove(listener);
        }
    }

    public void addTickListener(TickListener listener) {
        mTickListeners.add(listener);
    }

    public void removeTickListener(TickListener listener) {
        mTickListeners.remove(listener);
    }


    public void addTokenPair(Context context, MarketItem item, boolean login) {
        if (allTokenPair != null) {
            optTokenPair(context, item, 1, login);
        }
    }

    public void delTokenPair(Context context, MarketItem item, boolean login) {
        if (!allTokenPair.isEmpty()) {
            optTokenPair(context, item, 0, login);
        }
    }

    private void optTokenPair(Context context, MarketItem item, int select, boolean login) {
//        if (!login) {
//            localOptTokenpair(select, context, item, false);
//            return;
//        }
//        optTokenPairByNet(context, item, select);
    }
//
//    private void optTokenPairByNet(Context context, MarketItem item, int select) {
//        mApi.optUserSelectTokenPair(UserManager.getInstance().getAccessToken(context), new TokenPairSelectBody(select, item.symbol))
//                .compose(ScheduleCompat.apply())
//                .compose(BaseResultHelper.handleResult())
//                .subscribe(new ByexSubscriber<String>() {
//                    @Override
//                    protected void success(String data) {
//                        localOptTokenpair(select, context, item, true);
//                    }
//
//                    @Override
//                    protected void failure(HLError error) {
//                        int opt = R.string.add;
//                        if (select == 1) {
//                            opt = R.string.add;
//                        } else {
//                            opt = R.string.delete;
//                        }
//                        Toast.makeText(context, context.getResources().getString(opt) + context.getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    private void localOptTokenpair(int select, Context context, MarketItem item, boolean isLogin) {
//        int opt = R.string.add;
//        if (select == 1) {
//            opt = R.string.add;
//            localAddTokenPair(opt, context, item, isLogin);
//        } else {
//            opt = R.string.delete;
//            localDelTokenpair(opt, context, item, isLogin);
//        }
//    }

//    private void localDelTokenpair(int opt, Context context, MarketItem item, boolean isLogin) {
//        Toast.makeText(context, context.getResources().getString(opt) + context.getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
//        List<MarketItem> userSelectToken = getUserSelectToken(context);
//        if (userSelectToken.isEmpty()) {
//            return;
//        }
//        delToken(context, item, isLogin);
//    }
//
//    private void localAddTokenPair(int opt, Context context, MarketItem item, boolean isLogin) {
//        Toast.makeText(context, context.getResources().getString(opt) + context.getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
//        for (MarketItem marketItem : allTokenPair) {
//            if (item.symbol.equalsIgnoreCase(marketItem.symbol)) {
//                marketItem.userSelect = true;
//                break;
//            }
//        }
//        refreshSelectToken();//刷新列表
//        if (!isLogin) {
//            saveNoLoginSelectTokenToFile(context, allTokenPair);
//        }
//        saveUserSelectTokenToFile(context, allTokenPair);
//        //更新界面
//        EventBus.getDefault().post(new RefreshEvent());
//    }

//
//    /**
//     * 后台数据同步到本地
//     *
//     * @param context
//     * @param list
//     */
//    public void syncTokenPair(Context context, List<String> list) {
//        if (allTokenPair != null) {
//            for (MarketItem market : allTokenPair) {
//                if (list.contains(market.symbol)) {
//                    market.userSelect = true;
//                } else {
//                    market.userSelect = false;
//                }
//            }
//            saveUserSelectTokenToFile(context, allTokenPair);
//            refreshSelectToken();
//            EventBus.getDefault().post(new RefreshEvent());
//        }
//    }
//
//    /**
//     * 更新用户自选
//     */
//    public void reqUserSelect(Context context) {
//        if (UserManager.getInstance().isLogin(context)) {
//            mApi.getUserSelectTokenPair(UserManager.getInstance().getAccessToken(context))
//                    .compose(ScheduleCompat.apply())
//                    .compose(RxResultHelper.handleResult())
//                    .subscribe(new ByexSubscriber<List<UserSelectPair>>() {
//                        @Override
//                        protected void success(List<UserSelectPair> data) {
//                            List<String> list = new ArrayList<>();
//                            for (UserSelectPair pair : data) {
//                                int collectStatus = pair.collectStatus;
//                                if (collectStatus == 1) {//收藏
//                                    list.add(pair.symbol);
//                                }
//                            }
//                            syncTokenPair(context, list);
//                        }
//
//                        @Override
//                        protected void failure(HLError error) {
//
//                        }
//                    });
//        }
//    }
//
//
//    private void delToken(Context context, MarketItem item, boolean isLogin) {
//        if (allTokenPair != null) {
//            for (MarketItem market : allTokenPair) {
//                if (market.symbol.equalsIgnoreCase(item.symbol)) {
//                    market.userSelect = false;
//                    break;
//                }
//            }
//            refreshSelectToken();//刷新列表
//            EventBus.getDefault().post(new RefreshEvent());
//            saveUserSelectTokenToFile(context, allTokenPair);
//            if (!isLogin) {
//                saveNoLoginSelectTokenToFile(context, allTokenPair);
//            }
//        }
//    }
//
//    /**
//     * 缓存/文件
//     *
//     * @return
//     */
//    public List<MarketItem> getUserSelectToken(Context context) {
//        if (allTokenPair == null || allTokenPair.isEmpty()) {
//            List<MarketItem> userSelectTokenFromFile = getUserSelectTokenFromFile(context);
//            if (userSelectTokenFromFile == null) {
//                if (!userTokenPair.isEmpty()) {
//                    allTokenPair.addAll(userTokenPair);
//                    saveUserSelectTokenToFile(context, userTokenPair);
//                }
//                return allTokenPair;
//            }
//            allTokenPair.clear();
//            allTokenPair.addAll(userSelectTokenFromFile);
//        } else { //todo 去掉
//            List<MarketItem> userSelectTokenFromFile = getUserSelectTokenFromFile(context);
//            if (userSelectTokenFromFile == null) {
//                saveUserSelectTokenToFile(context, userTokenPair);
//            }
//        }
//        return allTokenPair;
//    }


    public void saveUserSelectTokenToFile(Context context, List<MarketItem> list) {
//        ACache aCache = ACache.get(context, USER_SELECT_TOKEN_PAIR);
//        ArrayList<MarketItem> supportTokenArrayList = new ArrayList<>(list);
//        aCache.put(TOKEN_PAIR, supportTokenArrayList);
    }

//    public List<MarketItem> getUserSelectTokenFromFile(Context context) {
//        ACache aCache = ACache.get(context, USER_SELECT_TOKEN_PAIR);
//        List<MarketItem> itemList = (List<MarketItem>) aCache.getAsObject(TOKEN_PAIR);
//        return itemList;
//    }
//
//    public void saveNoLoginSelectTokenToFile(Context context, List<MarketItem> list) {
//        ACache aCache = ACache.get(context, USER_SELECT_TOKEN_PAIR);
//        ArrayList<MarketItem> supportTokenArrayList = new ArrayList<>(list);
//        aCache.put(TOKEN_PAIR_NO_LOGIN, supportTokenArrayList);
//    }
//
//    public List<MarketItem> getNoLoginSelectTokenFromFile(Context context) {
//        ACache aCache = ACache.get(context, USER_SELECT_TOKEN_PAIR);
//        List<MarketItem> itemList = (List<MarketItem>) aCache.getAsObject(TOKEN_PAIR_NO_LOGIN);
//        return itemList;
//    }

//
//    public void syncNoLoginData(Context context) {
//        List<MarketItem> noLoginFromFile = getNoLoginSelectTokenFromFile(context);
//        if (noLoginFromFile == null || noLoginFromFile.isEmpty()) {
//            saveUserSelectTokenToFile(context, new ArrayList<>());
//            if (allTokenPair != null) {
//                for (MarketItem marketItem : allTokenPair) {
//                    marketItem.userSelect = false;
//                }
//            }
//        } else {
//            saveUserSelectTokenToFile(context, noLoginFromFile);
//            if (allTokenPair != null) {
//                for (MarketItem marketItem : allTokenPair) {
//                    for (MarketItem item : noLoginFromFile) {
//                        if (item.symbol.equalsIgnoreCase(marketItem.symbol)) {
//                            marketItem.userSelect = item.userSelect;
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//        //同时更新界面
//        refreshSelectToken();
//        EventBus.getDefault().post(new RefreshEvent());
//    }


//    public boolean isUserSelect(Context context, String symbol) {
//        List<MarketItem> userSelectToken = getUserSelectToken(context);
//        if (userSelectToken != null && !userSelectToken.isEmpty()) {
//            for (MarketItem item : userSelectToken) {
//                String symbol1 = item.symbol;
//                if (symbol1.equalsIgnoreCase(symbol)) {
//                    return item.userSelect;
//                }
//            }
//        }
//        return false;
//    }

    //-----------------------------------

    public static MarketManager shared() {
        return MarketManager.Holder.instance;
    }

    private MarketManager() {
    }


    private static class Holder {
        private static final MarketManager instance = new MarketManager();
    }
}
