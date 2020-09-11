package com.hazz.aipick.mvp.model.bean;

import com.google.gson.annotations.SerializedName;

public class MyAccount {

        /**
         * nickname : 李二狗
         * avatar : https://www.baidu.com/1.jpg
         * is_self : true
         * is_following : true
         * fans : 100
         * follow : 100.00
         * coins : BTC,EOS
         * pageview : 1000
         * last_update : 2019-01-02 12:00:00
         * total : 100.00
         * self : 100.00
         * gain_rate : 0.12
         * pullback : 0.34
         */

        public String nickname;
        public String avatar;
        public boolean is_self;
        public boolean is_following;
        public int fans;
        public String follow;
        public String coins;
        public int pageview;
        public String last_update;
        public String total;
        public String self;
        public String gain_rate;
        public String pullback;

        /**
         * end_time : 0
         * follow_times : 11
         * in_collection : false
         * is_sub : 0
         * pageview : 1000
         * trade_weeks : 1
         */

        public String end_time;
        public int follow_times;
        public boolean in_collection;
        public int is_sub;

        public int trade_weeks;
}
