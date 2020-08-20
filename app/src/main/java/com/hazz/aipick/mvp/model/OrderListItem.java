package com.hazz.aipick.mvp.model;

import java.math.BigDecimal;

public class OrderListItem {

    public String symbol;

    public String orderId;

    public String tradeA;

    public String tradeB;

    public String tradeDate;

    public String tradePrice;

    public BigDecimal volume;

    public BigDecimal amount;

    public int direction;

    public BigDecimal meanPrice;

    public long timestamp;



    /**
     '已提交',   1
     '下单成功', 2
     '正在撮合', 3
     '部分成交', 4
     '全部成交', 5
     '部分撤单', 6
     '全部撤单', 7
     '下单失败'  8
     */

    public int status;

}
