package com.hazz.aipick.mvp.model;

public class DepthItem {


    public enum Type {
        ASK, BID
    }

    public String price;

    public String quantity;

    public Type type;

    public float quantityPercent;

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
}
