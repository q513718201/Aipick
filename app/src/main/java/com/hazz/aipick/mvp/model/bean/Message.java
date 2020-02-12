package com.hazz.aipick.mvp.model.bean;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {

        /**
         * message_id : 1
         * title : 一条消息
         * create_at : 2019-01-02 12:00:00
         * status : 0
         * content : 消息内容
         */

        public int message_id;
        public String title;
        public String create_at;
        public int status;
        public String content;

}
