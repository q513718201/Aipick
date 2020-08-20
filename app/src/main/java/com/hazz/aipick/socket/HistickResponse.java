package com.hazz.aipick.socket;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistickResponse extends BaseResponse{

    //Data: ["BTC/BC|1|1539259483|42849.28|0.0446", "BTC/BC|1|1539332312|42849.28|0.22",…]

    @SerializedName("Symbol")
    private String symbol;


    @SerializedName("Data")
    private List<String> data;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public List<String> getData() {
        return data;
    }
}
