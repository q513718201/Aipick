package com.hazz.aipick.mvp.model.bean;

import java.util.List;

public class Order {


        /**
         * total_amount : 23.00
         * total_times : 23.00
         * total_gain : 12.00
         * list : [{"base_coin":"BTC","quote_coin":"ETH","create_at":"2020-01-10 12:00:00","price":"23.00","amount":"23.00","action":"buy"}]
         */

        public String total_amount;
        public String total_times;
        public String total_gain;
        public List<ListBean> list;

        public static class ListBean {
            /**
             * base_coin : BTC
             * quote_coin : ETH
             * create_at : 2020-01-10 12:00:00
             * price : 23.00
             * amount : 23.00
             * action : buy
             */

            public String base_coin;
            public String quote_coin;
            public String create_at;
            public String price;
            public String amount;
            public String action;
        }

}
