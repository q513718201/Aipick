package com.hazz.aipick.mvp.model.bean;

public class UserSubscribeBean {
//    {
//        "subee_name": "李二狗",  // 被订阅者的昵称
//            "subee_avatar": "https://www.baidu.com/1.jpg",  // 被订阅者的头像
//            "suber_name": "李狗嗨",  // 订阅者的昵称
//            "suber_avatar": "https://www.baidu.com/1.jpg",  // 订阅者的头像
//            "amount": "23.00",  // 收益数量
//            "follow_at": "2020-01-10 12:00:00",  // 跟随时间

    //    }
    public String subee_name;
    public String subee_avatar;
    public String suber_name;
    public String suber_avatar;
    public String amount;
    public String follow_at;

    public static UserSubscribeBean demo(int n) {
        UserSubscribeBean bean = new UserSubscribeBean();
        bean.subee_name = "李二狗" + n;
        bean.subee_avatar = "https://www.baidu.com/1.jpg";
        bean.suber_name = "李狗嗨";
        bean.suber_avatar = "https://www.baidu.com/1.jpg";
        bean.amount = "-23.00";
        bean.follow_at = "2020-01-10 12:00:00";
        return bean;
    }
}
