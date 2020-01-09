package com.hazz.aipick.mvp.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PayBean implements Serializable {

        /**
         * userId : 1
         * exchangeId : 1
         * switch : on
         * followType : amount
         * followFactor : 23.00
         * timeRange : 30days
         */

        public String userId;
        public String exchangeId;
        @SerializedName("switch")
        public String switchX;
        public String followType;
        public String followFactor;
        public String timeRange;

    public PayBean(String userId, String exchangeId, String switchX, String followType, String followFactor, String timeRange) {
        this.userId = userId;
        this.exchangeId = exchangeId;
        this.switchX = switchX;
        this.followType = followType;
        this.followFactor = followFactor;
        this.timeRange = timeRange;
    }
}
