package com.hazz.aipick.mvp.model.bean;

import java.util.List;

public class Fans {


        /**
         * fans : 1
         * follow : 1
         * list : [{"followee_avatar":"","followee_id":2,"followee_nickname":"无名氏2","follower_avatar":"","follower_id":1,"follower_nickname":"无名氏1","mutual":true}]
         */

        public int fans;
        public int follow;
        public List<ListBean> list;

        public static class ListBean {
            /**
             * followee_avatar :
             * followee_id : 2
             * followee_nickname : 无名氏2
             * follower_avatar :
             * follower_id : 1
             * follower_nickname : 无名氏1
             * mutual : true
             */

            public String followee_avatar;
            public int followee_id;
            public String followee_nickname;
            public String follower_avatar;
            public int follower_id;
            public String follower_nickname;
            public boolean mutual;
        }

}
