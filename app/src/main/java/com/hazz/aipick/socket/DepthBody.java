package com.hazz.aipick.socket;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DepthBody {


    /**
     * topic : position
     * symbol : xxx/xxx
     * depth : 20
     */

    @SerializedName("topic")
    private String topic;
    @SerializedName("symbol")
    private String symbol;
    @SerializedName("depth")
    private int depth;
    @SerializedName("unsubscribe")
    private List<String> unsubscribe;

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

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public List<String> getUnsubscribe() {
        return unsubscribe;
    }

    public void setUnsubscribe(List<String> unsubscribe) {
        this.unsubscribe = unsubscribe;
    }

    @Override
    public String toString() {
        return "DepthBody{" +
                "topic='" + topic + '\'' +
                ", symbol='" + symbol + '\'' +
                ", depth=" + depth +
                ", unsubscribe='" + unsubscribe + '\'' +
                '}';
    }
}
