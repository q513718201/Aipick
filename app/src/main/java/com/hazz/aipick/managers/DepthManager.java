package com.hazz.aipick.managers;

import android.support.annotation.NonNull;

import com.hazz.aipick.mvp.model.DepthItem;
import com.hazz.aipick.mvp.model.Depths;
import com.hazz.aipick.mvp.model.bean.MarketItem;
import com.hazz.aipick.socket.DepthResponse;
import com.hazz.aipick.socket.OrderBookResponse;
import com.hazz.aipick.socket.WebSocket;
import com.hazz.aipick.utils.MarketManager;
import com.orhanobut.logger.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DepthManager {

    private List<DepthItem> mDumpBidList;
    private List<DepthItem> mDumpAskList;

    private List<DepthItem> mAskDepths = new LinkedList<>();
    private List<DepthItem> mBidDepths = new LinkedList<>();
    private float mMostAskQuantity = 0;
    private float mMostBidQuantity = 0;
    private static final int DEPTH_LENGTH = 7;
    private HashMap<String, DepthListener>  mListeners = new HashMap<>();

    private String mCurrentSymbol = "";

    private Depths mDepths;

    public static DepthManager shared() {
        return DepthManager.Holder.instance;
    }

    private DepthManager() {
    }

    public void stopPosition() {
        Logger.d("stopPosition");
        WebSocket.getInstance().stopDepth();
        mDepths = null;
    }



    private static class Holder {
        private static final DepthManager instance = new DepthManager();
    }

    public void handleOrderBookResponse(OrderBookResponse orderBookResponse) {
        //默认深度都是降序排列
        mMostAskQuantity = 0;
        mMostBidQuantity = 0;
        if (mDepths == null) {
            mDepths = new Depths();
            mDepths.asks = new ArrayList<>();
            mDepths.bids = new ArrayList<>();
        }
        mDepths.asks.clear();
        mDepths.bids.clear();
        if (orderBookResponse.asks != null) {
            for (List<BigDecimal> item: orderBookResponse.asks) {
                DepthItem depthItem = createDepthItem(item, DepthItem.Type.ASK);
                float quantity = item.get(1).floatValue();
                if (quantity > mMostAskQuantity) {
                    mMostAskQuantity = quantity;
                }
                mDepths.asks.add(0, depthItem);
            }
        }
        updateQuantityPercent(mDepths.asks, mMostAskQuantity);
        if (orderBookResponse.bids != null) {
            for (List<BigDecimal> item: orderBookResponse.bids) {
                DepthItem depthItem = createDepthItem(item, DepthItem.Type.BID);
                float quantity = item.get(1).floatValue();
                if (quantity > mMostBidQuantity) {
                    mMostBidQuantity = quantity;
                }
                mDepths.bids.add(depthItem);
            }
        }
        updateQuantityPercent(mDepths.bids, mMostBidQuantity);
        if (mListeners.get(mCurrentSymbol) != null) {
            mListeners.get(mCurrentSymbol).onDepthChange(mDepths);
        }

    }


    public void handleDepthResponse(DepthResponse depthResponse) {
        if (depthResponse.Ask == null && depthResponse.Bid == null) {
            return;
        }
        mAskDepths.clear();
        mBidDepths.clear();
        mMostAskQuantity = 0;
        mMostBidQuantity = 0;
        String[] asks = depthResponse.Ask.split("\\|");
        String[] bids = depthResponse.Bid.split("\\|");
        Depths depths = new Depths();
        depths.asks = new ArrayList<>();
        depths.bids = new ArrayList<>();
        depths.latest = depthResponse.Latest;
        if (asks.length % 2 == 0) {
            for (int i = 0; i < asks.length; i = i + 2) {
                DepthItem depthItem = createDepthItem(asks, i);
                depthItem.type = DepthItem.Type.ASK;
                if (Float.valueOf(depthItem.quantity) > mMostAskQuantity) {
                    mMostAskQuantity = Float.valueOf(depthItem.quantity);
                }
                depths.asks.add(0, depthItem);
            }
        }
        updateQuantityPercent(depths.asks, mMostAskQuantity);
        mAskDepths.addAll(depths.asks);
        paddingAsks(depths.asks);
        if (bids.length % 2 == 0) {
            for (int i = 0; i < bids.length; i = i + 2) {
                DepthItem depthItem = createDepthItem(bids, i);
                depthItem.type = DepthItem.Type.BID;
                if (Float.valueOf(depthItem.quantity) > mMostBidQuantity) {
                    mMostBidQuantity = Float.valueOf(depthItem.quantity);
                }
                depths.bids.add(depthItem);
            }
        }
        updateQuantityPercent(depths.bids, mMostBidQuantity);
        mBidDepths.addAll(depths.bids);
        paddingBids(depths.bids);

        mDepths = depths;

        if (mListeners.get(mCurrentSymbol) != null) {
            mListeners.get(mCurrentSymbol).onDepthChange(depths);
        }
    }

    @NonNull
    private DepthItem createDepthItem(String[] data, int i) {
        DepthItem depthItem = new DepthItem();
        MarketItem market = MarketManager.shared().getMarket(mCurrentSymbol);
        depthItem.price = new BigDecimal(data[i]).setScale(market.pricePoint, RoundingMode.FLOOR).toPlainString();
        depthItem.quantity = new BigDecimal(data[i + 1]).setScale(market.sizePoint, RoundingMode.FLOOR).toPlainString();
        return depthItem;
    }

    @NonNull
    private DepthItem createDepthItem(List<BigDecimal> data, DepthItem.Type type) {
        DepthItem depthItem = new DepthItem();
        MarketItem market = MarketManager.shared().getMarket(mCurrentSymbol);
        depthItem.price = data.get(0).setScale(market.pricePoint, RoundingMode.FLOOR).toPlainString();
        depthItem.quantity = data.get(1).setScale(market.sizePoint, RoundingMode.FLOOR).toPlainString();
        depthItem.type = type;
        return depthItem;
    }


    public void updateQuantityPercent(List<DepthItem> list, float max) {
        for (DepthItem item : list) {
            item.quantityPercent = Float.valueOf(item.quantity) / max;
        }
    }

    public void handlePushPosition(DepthResponse depthResponse) {
        String[] asks = depthResponse.Ask.split("\\|");
        String[] bids = depthResponse.Bid.split("\\|");
        Depths depths = new Depths();
        depths.asks = new ArrayList<>();
        depths.bids = new ArrayList<>();

        boolean isAskHandled = false;
        if (asks.length != 0 && asks.length % 2 == 0) {
            for (int i = 0; i < asks.length; i = i + 2) {
                DepthItem depthItem = createDepthItem(asks, i);
                depthItem.type = DepthItem.Type.ASK;
                if (mAskDepths.size() == 0) {
                    mAskDepths.add(depthItem);
                } else {
                    int index;
                    for (index = 0; index < mAskDepths.size(); index++) {
                        BigDecimal newPrice = new BigDecimal(depthItem.price);
                        BigDecimal oldPrice = new BigDecimal(mAskDepths.get(index).price);
                        int result = newPrice.compareTo(oldPrice);
                        //match the same price
                        if (result == 0) {
                            //if quantity is zero
                            if (isQuantityZero(depthItem.quantity)) {
                                //remove from depths
                                mAskDepths.remove(index);
                            } else {
                                //replace amount
                                DepthItem oldDepthItem = mAskDepths.get(index);
                                oldDepthItem.quantity = depthItem.quantity;
                            }
                            isAskHandled = true;
                            break;

                        } else if (result > 0) {
                            //insert new depth
                            mAskDepths.add(index, depthItem);
                            isAskHandled = true;
                            break;
                        }
                    }
                    if (!isAskHandled) {
                        mAskDepths.add(depthItem);
                    }
                }
                if (Float.valueOf(depthItem.quantity) > mMostAskQuantity) {
                    mMostAskQuantity = Float.valueOf(depthItem.quantity);
                    updateQuantityPercent(mAskDepths, mMostAskQuantity);
                } else {
                    depthItem.quantityPercent = Float.valueOf(depthItem.quantity) / mMostAskQuantity;
                }

            }
        }
        depths.asks.addAll(mAskDepths);
        paddingAsks(depths.asks);


        boolean isBidHandled = false;
        if (bids.length != 0 && bids.length % 2 == 0) {
            for (int i = 0; i < bids.length; i = i + 2) {
                DepthItem depthItem = createDepthItem(bids, i);
                depthItem.type = DepthItem.Type.BID;
                if (mBidDepths.size() == 0) {
                    mBidDepths.add(depthItem);
                } else {
                    int index;
                    for (index = 0; index < mBidDepths.size(); index++) {
                        BigDecimal newPrice = new BigDecimal(depthItem.price);
                        BigDecimal oldPrice = new BigDecimal(mBidDepths.get(index).price);
                        int result = newPrice.compareTo(oldPrice);
                        if (result == 0) {
                            if (isQuantityZero(depthItem.quantity)) {
                                //remove from depths
                                mBidDepths.remove(index);
                            } else {
                                //replace amount
                                DepthItem oldDepthItem = mBidDepths.get(index);
                                oldDepthItem.quantity = depthItem.quantity;
                            }
                            isBidHandled = true;
                            break;
                        } else if (result > 0) {
                            //insert new depth
                            mBidDepths.add(index, depthItem);
                            isBidHandled = true;
                            break;
                        }
                    }
                    if (!isBidHandled) {
                        mBidDepths.add(depthItem);
                    }
                }
                if (Float.valueOf(depthItem.quantity) > mMostBidQuantity) {
                    mMostBidQuantity = Float.valueOf(depthItem.quantity);
                    updateQuantityPercent(mAskDepths, mMostBidQuantity);
                } else {
                    depthItem.quantityPercent = Float.valueOf(depthItem.quantity) / mMostBidQuantity;
                }
            }
        }
        depths.bids.addAll(mBidDepths);
        paddingBids(depths.bids);

        //trim list
        depths.asks = depths.asks.subList(depths.asks.size() - DEPTH_LENGTH, depths.asks.size());
        depths.bids = depths.bids.subList(0, DEPTH_LENGTH);

        mDepths = depths;
        if (mListeners.get(mCurrentSymbol) != null) {
            mListeners.get(mCurrentSymbol).onDepthChange(depths);
        }
    }

    private boolean isQuantityZero(String quantity) {
        return Float.valueOf(quantity) == 0f;
    }

    private void paddingBids(List<DepthItem> bids) {
        paddingDepth(bids, false);
    }

    private void paddingAsks(List<DepthItem> asks) {
        paddingDepth(asks, true);
    }

    private void paddingDepth(List<DepthItem> depthItems, boolean paddingStart) {
        if (depthItems.size() < DEPTH_LENGTH) {
            int sizeOfPadding = DEPTH_LENGTH - depthItems.size();
            for (int i = 0; i < sizeOfPadding; i++) {
                DepthItem depthItem = new DepthItem();
                depthItem.price = "--";
                depthItem.quantity = "--";
                if (paddingStart) {
                    depthItem.type = DepthItem.Type.ASK;
                    depthItems.add(0, depthItem);
                } else {
                    depthItem.type = DepthItem.Type.BID;
                    depthItems.add(depthItem);
                }
            }
        }
    }

    public interface DepthListener {
        void onDepthChange(Depths depths);
    }


    public void removeDepthListener(String symbol) {
        mListeners.remove(symbol);
    }

    public List<DepthItem> getDumpAskDepthList() {
        if (mDumpAskList != null) {
            return mDumpAskList;
        }
        mDumpAskList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            DepthItem depthItem = new DepthItem();
            depthItem.price = "--";
            depthItem.quantity = "--";
            depthItem.type = DepthItem.Type.ASK;
            mDumpAskList.add(depthItem);
        }
        return mDumpAskList;
    }


    public List<DepthItem> getDumpBidDepthList() {
        if (mDumpBidList != null) {
            return mDumpBidList;
        }
        mDumpBidList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            DepthItem depthItem = new DepthItem();
            depthItem.price = "--";
            depthItem.quantity = "--";
            depthItem.type = DepthItem.Type.BID;
            mDumpBidList.add(depthItem);
        }
        return mDumpBidList;
    }

    public void requestDepth(String symbol, DepthListener listener) {
        Logger.d("requestDepth");
        mListeners.put(symbol, listener);
        if (symbol.equals(mCurrentSymbol) && mDepths != null) {
            listener.onDepthChange(mDepths);
        } else {
            mCurrentSymbol = symbol;
            WebSocket.getInstance().tryToConnect();
            WebSocket.getInstance().requestDepth(symbol);
        }
    }

    public void requestDepthAgain() {
        WebSocket.getInstance().tryToConnect();
        WebSocket.getInstance().requestDepth(mCurrentSymbol);
    }

}
