package com.hazz.aipick.socket;

import com.google.gson.annotations.SerializedName;

public class KBody {


    /**
     * topic : k
     * symbol : xxx/xxx
     * period : 3m
     * from : 20110101
     * to : 20120101
     */

    @SerializedName("topic")
    private String topic;
    @SerializedName("symbol")
    private String symbol;
    @SerializedName("period")
    private String period;
    @SerializedName("from")
    private String from;
    @SerializedName("to")
    private String to;

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

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
