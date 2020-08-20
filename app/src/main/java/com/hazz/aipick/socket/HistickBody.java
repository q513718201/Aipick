package com.hazz.aipick.socket;

import com.google.gson.annotations.SerializedName;

public class HistickBody {

    @SerializedName("topic")
    private String topic;
    @SerializedName("symbol")
    private String symbol;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
