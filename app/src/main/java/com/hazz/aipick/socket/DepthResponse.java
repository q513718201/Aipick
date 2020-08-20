package com.hazz.aipick.socket;

import com.google.gson.annotations.SerializedName;

public class DepthResponse extends BaseResponse {

    @SerializedName("Latest")
    public String Latest;
    @SerializedName("Ask")
    public String Ask;
    @SerializedName("Bid")
    public String Bid;

    @Override
    public String toString() {
        return "DepthResponse{" +
                "Latest='" + Latest + '\'' +
                ", Ask='" + Ask + '\'' +
                ", Bid='" + Bid + '\'' +
                '}';
    }
}
