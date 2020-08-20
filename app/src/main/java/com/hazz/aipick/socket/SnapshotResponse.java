package com.hazz.aipick.socket;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class SnapshotResponse extends BaseResponse {

    @SerializedName("Now")
    public long Now;

    @SerializedName("Data")
    public Map<String, String> Data;
}
