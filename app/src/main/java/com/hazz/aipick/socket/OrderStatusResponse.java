package com.hazz.aipick.socket;

import com.google.gson.annotations.SerializedName;

public class OrderStatusResponse extends BaseResponse{

    public static final int STATUS_SUBMITTED = 1;
    public static final int STATUS_ACCEPTED = 2;
    public static final int STATUS_QUEUED = 3;
    public static final int STATUS_PARTIAL_FILLED = 4;
    public static final int STATUS_ALL_FILLED = 5;
    public static final int STATUS_PARIAL_CANCELLED = 6;
    public static final int STATUS_ALL_CANCELLED = 7;
    public static final int STATUS_REJECTED = 8;

    @SerializedName("Orderid")
    public String Orderid;
    @SerializedName("Status")
    public int Status;
}
