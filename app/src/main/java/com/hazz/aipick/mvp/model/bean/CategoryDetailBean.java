package com.hazz.aipick.mvp.model.bean;

public class CategoryDetailBean {

    /**
     * detail : 策略文字介绍
     * create_time : 创建时间
     * update_time : 修改时间
     */

    private String detail;
    private String create_time;
    private String update_time;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
