package com.hazz.aipick.mvp.model.bean;

public class UserInfo {


        /**
         * avatar : https://www.baidu.com/1.jpg
         * check_status : none
         * fans : 0
         * follow : 0
         * location : 中国
         * nickname : 无名氏1
         * pageview : 0
         * security : {"bind_bankcard":false,"bind_exchange":false,"country_code":"+86","email":"","has_login_password":true,"has_trade_password":false,"phone":"18812345678"}
         * type : 1
         */

        public String avatar;
        public String check_status;
        public int fans;
        public int follow;
        public String location;
        public String nickname;
        public int pageview;
        public SecurityBean security;
        public int type;

        public static class SecurityBean {
            /**
             * bind_bankcard : false
             * bind_exchange : false
             * country_code : +86
             * email :
             * has_login_password : true
             * has_trade_password : false
             * phone : 18812345678
             */

            public boolean bind_bankcard;
            public boolean bind_exchange;
            public String country_code;
            public String email;
            public boolean has_login_password;
            public boolean has_trade_password;
            public String phone;
        }

}
