package com.hazz.aipick.mvp.model.bean;

import android.text.TextUtils;

import java.io.Serializable;

public class SetTrade implements Serializable {
    public String countryCode;
    public String code;
    public String email;
    public String phone;
    public String cardNumber;
    public String name;
    public String cardType;
    public String front;
    public String back;

    public SetTrade(String countryCode, String code, String email, String phone, String cardNumber, String name, String cardType, String front, String back) {
        this.countryCode = countryCode;
        this.code = code;
        this.email = email;
        this.phone = phone;
        this.cardNumber = cardNumber;
        this.name = name;
        this.cardType = cardType;
        this.front = front;
        this.back = back;
    }

    public boolean unCheck(){
        return TextUtils.isEmpty(email)||
                TextUtils.isEmpty(code)||
                TextUtils.isEmpty(name)||
                TextUtils.isEmpty(cardNumber)||
                TextUtils.isEmpty(front)||
                TextUtils.isEmpty(back);
    }
}
