package com.hazz.aipick.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hazz.aipick.R;
import com.hazz.aipick.managers.DepthManager;
import com.hazz.aipick.managers.UserManager;
import com.hazz.aipick.mvp.model.DepthItem;
import com.hazz.aipick.mvp.model.Depths;
import com.hazz.aipick.mvp.model.bean.MarketItem;
import com.hazz.aipick.mvp.model.bean.UserInfo;
import com.hazz.aipick.ui.activity.LoginActivity;
import com.hazz.aipick.ui.adapter.AskDepthListAdapter;
import com.hazz.aipick.ui.adapter.BidDepthListAdapter;
import com.hazz.aipick.utils.MarketManager;
import com.hazz.aipick.utils.ToastUtils;
import com.hazz.aipick.view.DepthItemView;
import com.hazz.aipick.view.OrderBottomSheet;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class TradeView extends RelativeLayout implements TokenPriceNumberSelector.OnValueChangeListener {

    private static final String TAG = "TradeView";

    private ByexOptionSwitcher mByexOptionSwitcher;
    private RadioGroup mBuySellSelector;
    private Button mBuySell;
    private TradeType mTradeType = TradeType.BUY;
    private PriceType mPriceType = PriceType.CONSTRAINT;
    private TokenPriceNumberSelector mTokenNumberSelector;
    private TokenPriceNumberSelector mTokenPriceSelector;
    private SingleOptionSelector mPercentSelector;
    private TextView mCurrencyPrice;

    private RelativeLayout mAvailableBalanceLayout;
    private RelativeLayout mAvailableBuyLayout;
    private TextView mAvailableBalanceText;
    private TextView mAvailableBuyText;
    private RelativeLayout mSumLayout;
    private TextView mSumTitle;
    private TextView mSum;

    private TextView mPriceTitle;
    private TextView mQuantityTitle;

    private ListView mBuyListView;
    private ListView mSellListView;
    private BidDepthListAdapter mBuyListAdapter;
    private AskDepthListAdapter mSellListAdapter;

    //private String mTokenC = "AUC";
    private String mTokenA = "BTC";
    private String mTokenB = "USDT";
    private String mSymbol = "BTC/USDT";

    private TextView mMarketPriceInfo;
    private String mLatestPrice = "--";


    public void requestDepth() {
        DepthManager.shared().requestDepth(mSymbol, mDepthListener);
    }

    public void requestDepthAgain() {
        DepthManager.shared().requestDepthAgain();
    }


    public enum TradeType {
        BUY, SELL
    }

    public enum PriceType {
        CONSTRAINT, MARKET
    }


    public TradeView(Context context) {
        this(context, null);
    }

    public TradeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_trade, this);
        mByexOptionSwitcher = findViewById(R.id.buy_type);
        mTokenNumberSelector = findViewById(R.id.token_num_edit);
        mTokenPriceSelector = findViewById(R.id.token_price_edit);
        mPercentSelector = findViewById(R.id.percent_selector);
        mPercentSelector.setOnCheckedChangeListener(mOnCheckedChangeListener);

        mAvailableBalanceLayout = findViewById(R.id.available_balance_layout);
        mAvailableBuyLayout = findViewById(R.id.available_buy_layout);
        mSumLayout = findViewById(R.id.sum_layout);
        mSumTitle = findViewById(R.id.sum_title);
        mSum = findViewById(R.id.sum);
        mAvailableBuyText = findViewById(R.id.available_buy);
        mAvailableBalanceText = findViewById(R.id.available_balance);
        mPriceTitle = findViewById(R.id.price_title);
        mQuantityTitle = findViewById(R.id.quantity_title);

        mTokenPriceSelector.setOnValueChangeListener(this);
        mTokenNumberSelector.setOnValueChangeListener(this);

        int pricePoint = MarketManager.shared().getPricePoint(mSymbol);
        mTokenPriceSelector.setDecimalDigitsSum(pricePoint);
        int sizePoint = MarketManager.shared().getSizePoint(mSymbol);
        mTokenNumberSelector.setDecimalDigitsSum(sizePoint);

        mPercentSelector.setOnCheckedChangeListener(mOnCheckedChangeListener);
        mMarketPriceInfo = findViewById(R.id.market_price_info);
        mCurrencyPrice = findViewById(R.id.currency_price);
        mTokenNumberSelector.setHintText(String.format(getContext().getString(R.string.num_hint), mTokenA));
        mTokenPriceSelector.setHintText(String.format(getContext().getString(R.string.price_hint), mTokenB));
        mByexOptionSwitcher.setOnCheckedChangeListener(mOnCheckedChangeListener);
        mBuySell = findViewById(R.id.btn_buy_sell);
        mBuySell.setOnClickListener(mBuySellClickListener);
        updateBuySellButton();

        mBuySellSelector = findViewById(R.id.buy_sell_selector);
        mBuySellSelector.setOnCheckedChangeListener(mOnCheckedChangeListener);
        updateBuySellSelector();

        mBuyListView = findViewById(R.id.buy_list);
        mSellListView = findViewById(R.id.sell_list);

        mBuyListAdapter = new BidDepthListAdapter(getContext());
        mSellListAdapter = new AskDepthListAdapter(getContext());
        mBuyListView.setAdapter(mBuyListAdapter);
        mSellListView.setAdapter(mSellListAdapter);

        updateBuyOptionSwitcher();
        initBalanceInfo();
        initDepthList();

        MarketItem item = MarketManager.shared().getMarket(mSymbol);
        if (item != null && null != item.close) {
            mTokenPriceSelector.setText(item.close, pricePoint);
        }
        updateCurrencyPrice();
//        EventBus.getDefault().register(this);
    }

    private void updateBuySellSelector() {
        if (mTradeType == TradeType.BUY) {
            mBuySellSelector.check(R.id.buy_option);
        } else {
            mBuySellSelector.check(R.id.sell_option);
        }
    }

    private DepthManager.DepthListener mDepthListener = new DepthManager.DepthListener() {
        @Override
        public void onDepthChange(Depths depths) {
            if (depths == null) {
                return;
            }
            post(new Runnable() {
                @Override
                public void run() {
                    updateDepths(depths);
                }
            });
        }
    };

    private void initDepthList() {
        String priceTitle = String.format(getResources().getString(R.string.price_hint), mTokenB);
        String quantityTitle = String.format(getResources().getString(R.string.num_hint), mTokenA);
        mPriceTitle.setText(priceTitle);
        mQuantityTitle.setText(quantityTitle);
        mBuyListAdapter.replaceData(DepthManager.shared().getDumpBidDepthList());
        mSellListAdapter.replaceData(DepthManager.shared().getDumpAskDepthList());
        mBuyListView.setOnItemClickListener(mOnItemClickListener);
        mSellListView.setOnItemClickListener(mOnItemClickListener);
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DepthItemView itemView = (DepthItemView) view;
            DepthItem depthItem = itemView.getItem();
            if (depthItem != null) {
                mTokenPriceSelector.setText(depthItem.price);
                refreshBalanceInfo(mTokenPriceSelector.getValue());
            }
        }
    };

    private void updateBuyOptionSwitcher() {
        List<String> options = new ArrayList<>();
        options.add(getResources().getString(R.string.limit_buy));
        options.add(getResources().getString(R.string.market_buy));
        mByexOptionSwitcher.setOptions(options, 12);
    }


    private void updateSellOptionSwitcher() {
        List<String> options = new ArrayList<>();
        options.add(getResources().getString(R.string.limit_sell));
        options.add(getResources().getString(R.string.market_sell));
        mByexOptionSwitcher.setOptions(options, 12);
    }

    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            if (group == mBuySellSelector) {
                handleBuySellSwitch(checkedId);
            } else if (group == mByexOptionSwitcher) {
                mByexOptionSwitcher.invalidate();
                handlePriceOptionSwitch(checkedId);
            } else if (group == mPercentSelector) {
                handlePercentChange(checkedId);
            }
            updateSumInfo();
        }
    };

    private void handlePercentChange(int checkedId) {
        BigDecimal totalAmount;
        if (mTradeType == TradeType.BUY) {
            String amountString = mAvailableBuyText.getText().toString().split(" ")[0];
            if (amountString.equals("--")) {
                return;
            }
            totalAmount = new BigDecimal(amountString);
        } else {
            String availableBalanceText = mAvailableBalanceText.getText().toString().split(" ")[0];
            if (availableBalanceText.equals("--")) {
                return;
            }
            totalAmount = new BigDecimal(availableBalanceText);
        }
        int sizePoint = MarketManager.shared().getSizePoint(mSymbol);
        switch (checkedId) {
            case 0:
                mTokenNumberSelector.setText(totalAmount.multiply(new BigDecimal(0.25)).toPlainString(), sizePoint);
                break;
            case 1:
                mTokenNumberSelector.setText(totalAmount.multiply(new BigDecimal(0.5)).toPlainString(), sizePoint);
                break;
            case 2:
                mTokenNumberSelector.setText(totalAmount.multiply(new BigDecimal(0.75)).toPlainString(), sizePoint);
                break;
            case 3:
                mTokenNumberSelector.setText(totalAmount.multiply(new BigDecimal(1)).toPlainString(), sizePoint);
                break;
        }
    }

    private void handlePriceOptionSwitch(int checkedId) {
        switch (checkedId) {
            case 0:
                mPriceType = PriceType.CONSTRAINT;
                mMarketPriceInfo.setVisibility(View.GONE);
                mCurrencyPrice.setVisibility(View.VISIBLE);
                mTokenPriceSelector.setVisibility(View.VISIBLE);
                break;
            case 1:
                mPriceType = PriceType.MARKET;
                mMarketPriceInfo.setVisibility(View.VISIBLE);
                mTokenPriceSelector.setVisibility(View.GONE);
                mCurrencyPrice.setVisibility(View.GONE);
                updateMarketPriceInfo();
                break;
        }
    }

    private void handleBuySellSwitch(int checkedId) {
        switch (checkedId) {
            case R.id.sell_option:
                updateSellOptionSwitcher();
                mTradeType = TradeType.SELL;
                break;
            case R.id.buy_option:
                updateBuyOptionSwitcher();
                mTradeType = TradeType.BUY;
        }
        updateBuySellButton();
    }

    public void updateBuySellButton() {
        if (mTradeType == TradeType.SELL) {
            mBuySell.setBackgroundResource(R.drawable.bg_red_corners);
            String text = String.format(getContext().getString(R.string.sell_token), mTokenA);
            mBuySell.setText(text);
        } else {
            mBuySell.setBackgroundResource(R.drawable.bg_green_corners);
            String text = String.format(getContext().getString(R.string.buy_token), mTokenA);
            mBuySell.setText(text);
        }
    }


    @Override
    public void onValueChanged(TokenPriceNumberSelector selector, BigDecimal value) {
        if (selector == mTokenPriceSelector) {
            updateCurrencyPrice();
            if (mPercentSelector.getCheckedRadioButtonId() != SingleOptionSelector.INVALID_ID) {
                handlePercentChange(mPercentSelector.getCheckedRadioButtonId());
            }
        }
        updateSumInfo();

    }

    private void updateSumInfo() {
        if (mPriceType == PriceType.CONSTRAINT) {
            BigDecimal price = mTokenPriceSelector.getValue();
            BigDecimal num = mTokenNumberSelector.getValue();
            if (price != null && num != null && price.floatValue() > 0 && num.floatValue() > 0) {
                MarketItem market = MarketManager.shared().getMarket(mSymbol);
                int scale = market.pricePoint + market.sizePoint;
                BigDecimal sum = price.multiply(num).setScale(scale, RoundingMode.FLOOR);
                updateSumText(sum);
            } else {
                initBalanceInfo();
            }
        } else if (!mLatestPrice.equals("--")) {
            refreshBalanceInfo(new BigDecimal(mLatestPrice));
        }
    }

    private BigDecimal calculateSum() {
        BigDecimal price = mTokenPriceSelector.getValue();
        BigDecimal num = mTokenNumberSelector.getValue();
        if (price != null && num != null && price.floatValue() > 0 && num.floatValue() > 0) {
            return price.multiply(num).setScale(8, RoundingMode.FLOOR);
        }
        return null;
    }

    private void initBalanceInfo() {
        mAvailableBuyLayout.setVisibility(View.VISIBLE);
        mAvailableBalanceLayout.setVisibility(View.VISIBLE);
        mSumLayout.setVisibility(View.GONE);
        refreshBalanceInfo(mTokenPriceSelector.getValue());
    }

    public void refreshBalanceInfo(BigDecimal value) {
        if (mTradeType == TradeType.BUY) {
            String availableCoinString = "--";
            BigDecimal amountB = UserManager.getInstance().getCoinFreeDecimal(mTokenB);
            BigDecimal availableCoin = null;
            if (amountB != null) {
                availableCoin = amountB.setScale(8, RoundingMode.FLOOR);
                availableCoinString = availableCoin.toPlainString();
            }
            availableCoinString = availableCoinString + " " + mTokenB;
            mAvailableBalanceText.setText(availableCoinString);

            String availableBuyString = "--";
            if (amountB != null && value != null && value.floatValue() != 0) {
                availableBuyString = availableCoin.divide(value, RoundingMode.FLOOR).setScale(8, RoundingMode.FLOOR).toPlainString();
            }
            availableBuyString = availableBuyString + " " + mTokenA;
            mAvailableBuyText.setText(availableBuyString);
        } else if (mTradeType == TradeType.SELL) {
            String availableCoinString = "--";
            BigDecimal amountA = UserManager.getInstance().getCoinFreeDecimal(mTokenA);
            BigDecimal availableCoin = null;
            if (amountA != null) {
                availableCoin = amountA.setScale(8, RoundingMode.FLOOR);
                availableCoinString = availableCoin.toPlainString();
            }
            availableCoinString = availableCoinString + " " + mTokenA;
            mAvailableBalanceText.setText(availableCoinString);
            String availableBuyString = "--";
            if (amountA != null && value != null) {
                availableBuyString = availableCoin.multiply(value).setScale(8, RoundingMode.FLOOR).toPlainString();
            }
            availableBuyString = availableBuyString + " " + mTokenB;
            mAvailableBuyText.setText(availableBuyString);

        }
    }

    private void updateSumText(BigDecimal sum) {
        mAvailableBalanceLayout.setVisibility(View.GONE);
        mAvailableBuyLayout.setVisibility(View.GONE);
        mSumLayout.setVisibility(View.VISIBLE);
        if (mTradeType == TradeType.BUY) {
            mSumTitle.setText(getResources().getString(R.string.buy_all));
        } else {
            mSumTitle.setText(getResources().getString(R.string.sell_all));
        }
        mSum.setText(sum.toPlainString());
    }

    private void updateDepths(Depths data) {
        if (data.bids != null) {
            mBuyListAdapter.replaceData(data.bids);
        }
        if (data.asks != null) {
            mSellListAdapter.replaceData(data.asks);
        }
    }

    private void updateCurrencyPrice() {
        BigDecimal value = mTokenPriceSelector.getValue();
        if (value != null) {
            String bcSymbol = mTokenB + "/AUC";
            MarketItem market = MarketManager.shared().getMarket(bcSymbol);
            if (mTokenB.equals("AUC")) {
                mCurrencyPrice.setText(getResources().getString(R.string.market_yuan) + value);
            } else if (market != null) {
                if (market.close == null) return;
                BigDecimal bc = new BigDecimal(market.close);
                String currency = value.multiply(bc).setScale(2, RoundingMode.FLOOR).toPlainString();
                mCurrencyPrice.setText(getResources().getString(R.string.market_yuan) + currency);
            }
        } else {
            mCurrencyPrice.setText(getResources().getString(R.string.market_yuan) + "--");
        }
    }


    private MarketManager.TickListener mTickListener = new MarketManager.TickListener() {
        @Override
        public void onTick(MarketItem item) {
            mLatestPrice = item.close;
            post(new Runnable() {
                @Override
                public void run() {
                    updateMarketPriceInfo();
                }
            });

        }
    };


    private void updateMarketPriceInfo() {
        if (mMarketPriceInfo.getVisibility() == View.VISIBLE) {
            String info = String.format(getContext().getString(R.string.market_price_info), mLatestPrice);
            mMarketPriceInfo.setText(info);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        MarketManager.shared().addMarketListener(mSymbol, mTickListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        DepthManager.shared().removeDepthListener(mSymbol);
        MarketManager.shared().removeMarketListener(mSymbol, mTickListener);
    }

    private OnClickListener mBuySellClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            boolean isLogin = UserManager.getInstance().isLogin(getContext());
            if (!isLogin) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                getContext().startActivity(intent);
                return;
            }
            UserInfo userInfo = UserManager.getInstance().getUserInfo(getContext());
            boolean preCertifi = preCertifi(userInfo);
            if (preCertifi) return;

            if (mTradeType == TradeType.BUY) {

                BigDecimal amountB = UserManager.getInstance().getCoinFreeDecimal(mTokenB);
                if (amountB == null) {
                    Toast.makeText(getContext(), R.string.no_found_funds, Toast.LENGTH_SHORT).show();
                    return;
                }
                BigDecimal sum = calculateSum();
                if (sum != null) {
                    int result = amountB.compareTo(sum);
                    if (result < 0) {
                        Toast.makeText(getContext(), R.string.not_sufficient_fund, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

            } else if (mTradeType == TradeType.SELL) {

                BigDecimal amountA = UserManager.getInstance().getCoinFreeDecimal(mTokenA);
                if (amountA == null) {
                    Toast.makeText(getContext(), R.string.no_found_funds, Toast.LENGTH_SHORT).show();
                    return;
                }
                BigDecimal value = mTokenNumberSelector.getValue();
                if (value != null) {
                    int result = amountA.compareTo(value);
                    if (result < 0) {
                        Toast.makeText(getContext(), R.string.not_sufficient_fund, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            boolean isShowBottomSheet = true/* SharedPreferencesUtils.getParam(getContext(), "show_next", true)*/;
            if (isShowBottomSheet) {
                if (checkUserInput()) {
                    OrderBottomSheet orderBottomSheet = new OrderBottomSheet(getContext());
                    if (mPriceType == PriceType.CONSTRAINT) {
                        orderBottomSheet.setPrice(mTokenPriceSelector.getValue());
                    } else {
                        orderBottomSheet.setPrice(new BigDecimal(mLatestPrice));
                    }
                    orderBottomSheet.setQuantity(mTokenNumberSelector.getValue());
                    orderBottomSheet.setPriceType(mPriceType);
                    orderBottomSheet.setTradeType(mTradeType);
                    String symbol = (mTokenA + "_" + mTokenB).toLowerCase();
                    orderBottomSheet.setSymbol(symbol);
                    orderBottomSheet.show();
                }
            } else {
                submit();
            }


        }
    };

    /**
     * 检查认证
     *
     * @param userInfo
     * @return
     */
    private boolean preCertifi(UserInfo userInfo) {
        return true/*CommonTipManager.shared().showCertificationDialog((Activity) getContext(), userInfo)*/;
    }


    private void submit() {
        ToastUtils.showToast(getContext(), "submit");
//        if (checkUserInput()) {
//            CoinOrderBody orderBody = new CoinOrderBody();
//            orderBody.consignType = getConsignType();
//            BigDecimal priceValue = mTokenPriceSelector.getValue();
//            orderBody.price = priceValue == null ? "0" : priceValue.toPlainString();
//            BigDecimal numValue = mTokenNumberSelector.getValue();
//            orderBody.qty = numValue == null ? "0" : numValue.toPlainString();
//            orderBody.symbol = (mTokenA + "_" + mTokenB).toLowerCase();
//            String accessToken = UserManager.getInstance().getAccessToken(getContext());
//            Singleton.get().dataSource.api().trade(accessToken, orderBody)
//                    .compose(ScheduleCompat.apply())
//                    .compose(BaseResultHelper.handleResult())
//                    .subscribe(new ByexSubscriber<String>() {
//                        @Override
//                        protected void success(String data) {
//                            Toast.makeText(getContext(), R.string.commit_success, Toast.LENGTH_SHORT).show();
//                            EventBus.getDefault().post(new UpdateOrderEvent());
//                        }
//
//                        @Override
//                        protected void failure(HLError error) {
//                            Logger.d(error.getMessage());
//                            Toast.makeText(getContext(), error.getGlobalMessage(getContext()), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }

    }


    private String getConsignType() {
        StringBuilder builder = new StringBuilder();
        if (mPriceType == TradeView.PriceType.CONSTRAINT) {
            builder.append("limit_");
        } else {
            builder.append("market_");
        }
        if (mTradeType == TradeView.TradeType.BUY) {
            builder.append("buy");
        } else {
            builder.append("sell");
        }
        return builder.toString();
    }


    private boolean checkUserInput() {
        if (mPriceType == PriceType.CONSTRAINT) {
            if (mTokenPriceSelector.getValue() == null) {
                Toast.makeText(getContext(), R.string.please_input_price, Toast.LENGTH_SHORT).show();
                return false;
            } else if (mTokenNumberSelector.getValue() == null) {
                Toast.makeText(getContext(), R.string.please_input_amount, Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (mPriceType == PriceType.MARKET) {
            if (mTokenNumberSelector.getValue() == null) {
                Toast.makeText(getContext(), R.string.please_input_amount, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
    // TODO: 2020/8/20 注册监听事件
//
//    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
//    public void onUpdateCoinEvent(UpdateCoinEvent event) {
//        if (mPriceType == PriceType.CONSTRAINT) {
//            refreshBalanceInfo(mTokenPriceSelector.getValue());
//        } else if (!mLatestPrice.equals("--")) {
//            refreshBalanceInfo(new BigDecimal(mLatestPrice));
//        }
//    }

    public void setMarket(MarketItem market) {
        setMarket(market, TradeType.BUY);
    }

    public void setMarket(MarketItem item, TradeType type) {
        mTokenB = item.tradeB;
        mTokenA = item.tradeA;
        mTradeType = type;
        mPriceType = PriceType.CONSTRAINT;
        MarketManager.shared().removeMarketListener(mSymbol, mTickListener);
        mSymbol = item.symbol;
        MarketManager.shared().addMarketListener(mSymbol, mTickListener);
        mTokenPriceSelector.clear();
        mTokenNumberSelector.clear();
        mTokenNumberSelector.setHintText(String.format(getContext().getString(R.string.num_hint), mTokenA));
        mTokenPriceSelector.setHintText(String.format(getContext().getString(R.string.price_hint), mTokenB));
        mBuySell.setText(String.format(getContext().getString(R.string.buy_token), mTokenA));

        int pricePoint = MarketManager.shared().getPricePoint(mSymbol);
        mTokenPriceSelector.setDecimalDigitsSum(pricePoint);
        int sizePoint = MarketManager.shared().getSizePoint(mSymbol);
        mTokenNumberSelector.setDecimalDigitsSum(sizePoint);

        updateBuySellSelector();
        if (mTradeType == TradeType.BUY) {
            updateBuyOptionSwitcher();
        } else {
            updateSellOptionSwitcher();
        }
        initDepthList();
        updateBuySellButton();
        mLatestPrice = item.close;
        mTokenPriceSelector.setText(item.close, pricePoint);
        updateCurrencyPrice();
        updateMarketPriceInfo();
        DepthManager.shared().requestDepth(mSymbol, mDepthListener);
        mPercentSelector.clearCheck();
        initBalanceInfo();


    }
}
