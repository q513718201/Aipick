package com.hazz.aipick.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class MarketItem implements Parcelable, Serializable {

    public String symbol;

    public String tradeA;

    public String tradeB;

    public long date;

    public String open;

    public String high;

    public String low;

    public String close;

    public String volume = "--";

    public String changeString = "--";

    public float change;

    public boolean isUp = true;

    public boolean isSelected = false;

    public String cnyPrice = "--";

    public int pricePoint = 2;

    public int sizePoint = 4;

    public int weight;


    public boolean userSelect = false;//自选

    public int price_point;

    public int size_point;

    public MarketItem() {

    }

    public MarketItem(Parcel in) {
        symbol = in.readString();
        tradeA = in.readString();
        tradeB = in.readString();
        date = in.readLong();
        open = in.readString();
        high = in.readString();
        low = in.readString();
        close = in.readString();
        volume = in.readString();
        changeString = in.readString();
        change = in.readFloat();
        isUp = in.readByte() != 0;
        cnyPrice = in.readString();
        pricePoint = in.readInt();
        sizePoint = in.readInt();
        weight = in.readInt();
        userSelect = in.readByte() != 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(symbol);
        parcel.writeString(tradeA);
        parcel.writeString(tradeB);
        parcel.writeLong(date);
        parcel.writeString(open);
        parcel.writeString(high);
        parcel.writeString(low);
        parcel.writeString(close);
        parcel.writeString(volume);
        parcel.writeString(changeString);
        parcel.writeFloat(change);
        parcel.writeByte((byte) (isUp ? 1 : 0));
        parcel.writeString(cnyPrice);
        parcel.writeInt(pricePoint);
        parcel.writeInt(weight);
        parcel.writeInt(sizePoint);
        parcel.writeByte((byte) (userSelect ? 1 : 0));
    }

    public static final Parcelable.Creator<MarketItem> CREATOR = new Parcelable.Creator<MarketItem>() {
        @Override
        public MarketItem createFromParcel(Parcel in) {
            return new MarketItem(in);
        }

        @Override
        public MarketItem[] newArray(int size) {
            return new MarketItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public String toString() {
        return "MarketItem{" +
                "symbol='" + symbol + '\'' +
                ", tradeA='" + tradeA + '\'' +
                ", tradeB='" + tradeB + '\'' +
                ", date=" + date +
                ", open='" + open + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", close='" + close + '\'' +
                ", volume='" + volume + '\'' +
                ", changeString='" + changeString + '\'' +
                ", change=" + change +
                ", isUp=" + isUp +
                ", isSelected=" + isSelected +
                ", cnyPrice='" + cnyPrice + '\'' +
                ", pricePoint=" + pricePoint +
                ", sizePoint=" + sizePoint +
                ", weight=" + weight +
                ", userSelect=" + userSelect +
                ", price_point=" + price_point +
                ", size_point=" + size_point +
                '}';
    }
}
