package com.hazz.aipick.mvp.model.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class OnOrderBean implements MultiItemEntity {

    public String price;

    public String quantity;

    public int type;

    public float quantityPercent;

    public static int ASK = 0;
    public static int BID = 1;

    public boolean isValid() {
        return !price.equals("--");
    }

    @Override
    public String toString() {
        return "DepthItem{" +
                "price='" + price + '\'' +
                ", quantity='" + quantity + '\'' +
                ", type=" + type +
                ", quantityPercent=" + quantityPercent +
                '}';
    }

    @Override
    public int getItemType() {
        return type;
    }

}
