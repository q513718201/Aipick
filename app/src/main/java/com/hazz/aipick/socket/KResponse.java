package com.hazz.aipick.socket;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class KResponse extends BaseResponse {

    //{"Topic":"k","Symbol":"BTC/USDT","Result":"","Msg":"","First":"201810010848","Nearest":"201810140746","Data":{"201810140745":"6385.84|6389.32|6384.54|6386.04|0.7271",
    @SerializedName("Symbol")
    public String symbol;

    @SerializedName("First")
    public String first;

    @SerializedName("Nearest")
    public String nearest;


    @SerializedName("Data")
    public Map<String, String> data;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getNearest() {
        return nearest;
    }

    public void setNearest(String nearest) {
        this.nearest = nearest;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}