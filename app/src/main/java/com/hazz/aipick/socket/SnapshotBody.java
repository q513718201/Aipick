package com.hazz.aipick.socket;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SnapshotBody {


    /**
     * topic : snapshot
     * subscribe : ["USDT"]
     * unsubscribe : ["USDT"]
     */

    @SerializedName("topic")
    private String topic;
    @SerializedName("subscribe")
    private List<String> subscribe;
    @SerializedName("unsubscribe")
    private List<String> unsubscribe;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<String> getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(List<String> subscribe) {
        this.subscribe = subscribe;
    }

    public List<String> getUnsubscribe() {
        return unsubscribe;
    }

    public void setUnsubscribe(List<String> unsubscribe) {
        this.unsubscribe = unsubscribe;
    }
}
