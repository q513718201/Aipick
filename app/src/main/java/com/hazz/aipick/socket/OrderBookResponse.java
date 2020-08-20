package com.hazz.aipick.socket;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

public class OrderBookResponse extends BaseResponse {


    @SerializedName("asks")
    public List<List<BigDecimal>> asks;
    @SerializedName("bids")
    public List<List<BigDecimal>> bids;
}
