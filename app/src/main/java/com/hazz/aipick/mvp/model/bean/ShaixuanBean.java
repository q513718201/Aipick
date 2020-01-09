package com.hazz.aipick.mvp.model.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class ShaixuanBean implements MultiItemEntity {
    public String title;
    public int type;

    @Override
    public int getItemType() {
        return type;
    }

    public ShaixuanBean(String title, int type) {
        this.title = title;
        this.type = type;
    }
}
