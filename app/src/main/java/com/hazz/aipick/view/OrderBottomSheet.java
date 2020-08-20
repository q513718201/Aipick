package com.hazz.aipick.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hazz.aipick.R;
import com.hazz.aipick.utils.SPUtil;
import com.hazz.aipick.widget.TradeView;
import com.orhanobut.logger.Logger;


import java.math.BigDecimal;


public class OrderBottomSheet extends BottomSheetDialog {

    private static final String TAG = "OrderBottomSheet";

    private TextView mPriceText;
    private TextView mQuantityText;
    private TextView mQuantityTitleText;
    private TextView mTitleText;
    private CheckBox mNotShowNext;

    private ImageButton mClose;


    private BigDecimal mPriceValue;
    private BigDecimal mQuantityValue;
    private TradeView.PriceType mPriceType;
    private TradeView.TradeType mTradeType;
    private String mSymbol;
    private Button mSubmit;
    private Context mContext;

    public OrderBottomSheet(@NonNull Context context) {
        this(context, 0);
    }

    public OrderBottomSheet(@NonNull Context context, int theme) {
        super(context, theme);
        //Don't do this in
        mContext = context;
        View root = LayoutInflater.from(context).inflate(R.layout.view_order_bottom_sheet, null);
        setContentView(root);
        mPriceText = root.findViewById(R.id.price);
        mQuantityText = root.findViewById(R.id.quantity);
        mQuantityTitleText = root.findViewById(R.id.quantity_title);
        mTitleText = root.findViewById(R.id.title);
        mSubmit = root.findViewById(R.id.submit);
        mClose = root.findViewById(R.id.close);
        mNotShowNext = root.findViewById(R.id.not_show_next);
        mNotShowNext.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Logger.d(isChecked);
                SPUtil.INSTANCE.putBoolean( "show_next", !isChecked);
            }
        });
        View.OnClickListener onClickListener = v -> {
            switch (v.getId()) {
                case R.id.submit:
                    submit();
                    break;
                case R.id.close:
                    dismiss();
                    break;
            }

        };
        mSubmit.setOnClickListener(onClickListener);
        mClose.setOnClickListener(onClickListener);
    }

    public void setPrice(BigDecimal price) {
        mPriceValue = price;
        mPriceText.setText(String.valueOf(price));
    }

    public void setQuantity(BigDecimal quantity) {
        mQuantityValue = quantity;
        mQuantityText.setText(String.valueOf(mQuantityValue));
    }

    public void setPriceType(TradeView.PriceType priceType) {
        mPriceType = priceType;

    }

    public void setTradeType(TradeView.TradeType tradeType) {
        mTradeType = tradeType;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StringBuilder builder = new StringBuilder();
        if (mPriceType == TradeView.PriceType.CONSTRAINT) {
            builder.append(mContext.getResources().getString(R.string.limit_order));
        } else {
            builder.append(mContext.getResources().getString(R.string.market_order));
        }
        if (mTradeType == TradeView.TradeType.BUY) {
            builder.append(mContext.getResources().getString(R.string.buy));
            mQuantityTitleText.setText(R.string.buy_amounts);
        } else {
            builder.append(mContext.getResources().getString(R.string.sell));
            mQuantityTitleText.setText(R.string.sell_amounts);
        }
        mTitleText.setText(builder.toString());
    }

    public void setSymbol(String symbol) {
        mSymbol = symbol;
    }

    private void submit() {
        // TODO: 2020/8/20
//        CoinOrderBody orderBody = new CoinOrderBody();
//        orderBody.consignType = getConsignType();
//        orderBody.price = mPriceValue.toPlainString();
//        orderBody.qty = mQuantityValue.toPlainString();
//        orderBody.symbol = mSymbol;
//        String accessToken = UserManager.getInstance().getAccessToken(getContext());
//        Singleton.get().dataSource.api().trade(accessToken, orderBody)
//                .compose(ScheduleCompat.apply())
//                .compose(BaseResultHelper.handleResult())
//                .subscribe(new ByexSubscriber<String>() {
//                    @Override
//                    protected void success(String data) {
//                        Toast.makeText(getContext(), R.string.commit_success, Toast.LENGTH_SHORT).show();
//                        OrderBottomSheet.this.dismiss();
//                        EventBus.getDefault().post(new UpdateOrderEvent());
//                    }
//
//                    @Override
//                    protected void failure(HLError error) {
//                        Logger.d(error.getMessage());
//                        Toast.makeText(getContext(), error.getGlobalMessage(getContext()), Toast.LENGTH_SHORT).show();
//                        OrderBottomSheet.this.dismiss();
//                    }
//                });
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
}
