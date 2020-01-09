package com.hazz.aipick.mvp.model.bean;

import java.util.List;

public class Walet {


        /**
         * withdrawed : 12.00
         * withdrawing : 23.00
         * withdrawable : 100.00
         * total_amount : 200.00
         * us_rmb : 4.23
         * rmb_value : 9000
         * cond : {"min":"10.00","max":"100.00","start":"2019-01-02 09:00:00","end":"2019-01-02 18:00:00","fee_rate":"0.545"}
         * bankcard_list : [{"bank":"招商银行","number":"4321","id":"1"}]
         */

        public String withdrawed;
        public String withdrawing;
        public String withdrawable;
        public String total_amount;
        public String us_rmb;
        public String rmb_value;
        public CondBean cond;
        public List<BankcardListBean> bankcard_list;

        public static class CondBean {
            /**
             * min : 10.00
             * max : 100.00
             * start : 2019-01-02 09:00:00
             * end : 2019-01-02 18:00:00
             * fee_rate : 0.545
             */

            public String min;
            public String max;
            public String start;
            public String end;
            public String fee_rate;
        }

        public static class BankcardListBean {
            /**
             * bank : 招商银行
             * number : 4321
             * id : 1
             */

            public String bank;
            public String number;
            public String id;
        }

}
