package com.hazz.aipick.socket;

import com.google.gson.annotations.SerializedName;

public class TickResponse extends BaseResponse{

    //{"Topic":"tick","Data":"ETC/USDT|1|1538815838|10.9678|3.8983"}

    @SerializedName("Data")
    public String data;

    public String symbol;

    public String atype;

    public String dt;

    public String price;

    public String qty;

    @Override
    public String toString() {
        return "TickResponse{" +
                "data='" + data + '\'' +
                ", symbol='" + symbol + '\'' +
                ", atype='" + atype + '\'' +
                ", dt='" + dt + '\'' +
                ", price='" + price + '\'' +
                ", qty='" + qty + '\'' +
                '}';
    }
}
