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

    public String unCheck() {
        if (TextUtils.isEmpty(email)) {
            return "请输入邮箱";
        }
        if (TextUtils.isEmpty(code)) {
            return "请输入验证码";
        }
        if (TextUtils.isEmpty(name)) {
            return "请输入持卡人姓名";
        }
        if (TextUtils.isEmpty(cardNumber)) {
            return "请输入证件号码";
        }
        if (TextUtils.isEmpty(front)) {
            return "请上传证件正面";
        }
        if (TextUtils.isEmpty(back)) {
            return "请上传证件背面";
        }
        return null;
    }
}
