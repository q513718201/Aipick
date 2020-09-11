package com.hazz.aipick.mvp.model.bean;

public class IncomeBean {
    /**
     * create_at : 2019-01-02 12:00:00
     * amount : 34.22
     * from_name : 李二狗
     * from_avatar : https:
     * days : 30
     */

    private String create_at;
    private String amount;
    private String from_name;
    private String from_avatar;
    private String days;

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFrom_name() {
        return from_name;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    public String getFrom_avatar() {
        return from_avatar;
    }

    public void setFrom_avatar(String from_avatar) {
        this.from_avatar = from_avatar;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }
}
