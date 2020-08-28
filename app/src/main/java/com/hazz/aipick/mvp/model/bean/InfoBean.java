package com.hazz.aipick.mvp.model.bean;

import java.util.List;

public class InfoBean {

    /**
     * head : https://avatars3.githubusercontent.com/u/13379927?s=60&v=4
     * name : 周咚咚
     * total_view : 900
     * time : 25分钟前
     * content : 我们不必屠杀飞龙，只需躲避它们就可以做的很好；不做最暴利的投机者，只做最稳的投资人不做最暴利的投机者，只做最稳的投资人。
     * images : ["https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598265235227&di=8dae99fb2dae7b19f0643f9b1bb9b030&imgtype=0&src=http%3A%2F%2F06.imgmini.eastday.com%2Fmobile%2F20180327%2F41f86fa42353d97df2de76eb2e8f7176.jpeg","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598265235227&di=8828610017eb33e379277788a41f373c&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201403%2F16%2F20140316154321_2PSK8.thumb.700_0.jpeg","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598265235227&di=732344d688093a2d46d746732d8f1e69&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2Fbc693f297566124cec38aa8826598aa693fa49da4c5ad-0DmFks_fw658"]
     */

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String head;
    private String name;
    private String total_view;
    private String time;
    private String content;
    private List<String> images;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal_view() {
        return total_view;
    }

    public void setTotal_view(String total_view) {
        this.total_view = total_view;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
