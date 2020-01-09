package com.hazz.aipick.mvp.model.bean;

import java.util.List;

public class CoinHouseDesc {


        /**
         * total_value : 12.00
         * rate : 0.21
         * week_rate : 2.22
         * gain : 12.00
         * recommends : [{"nickname":"策略名称","gain_rate":"12.00","max_pullback":"0.12","follower_gain":"123.00","follow_times":"1234","price":"12.00"}]
         */

        public String total_value;
        public String rate;
        public String week_rate;
        public String gain;
        public List<RecommendsBean> recommends;

        public static class RecommendsBean {
            /**
             * nickname : 策略名称
             * gain_rate : 12.00
             * max_pullback : 0.12
             * follower_gain : 123.00
             * follow_times : 1234
             * price : 12.00
             */

            public String nickname;
            public String gain_rate;
            public String max_pullback;
            public String follower_gain;
            public String follow_times;
            public String price;
        }

}
