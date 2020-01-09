package com.hazz.aipick.mvp.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubscribeDesc {



        public String subee_name;
        public String start;
        public String end;
        public String pay_method;
        public String price;
        public boolean is_valid;
        @SerializedName("switch")
        public String switchX;
        public List<GainsBean> gains;

        public static class GainsBean {
            /**
             * amount : 12.00
             * create_at : 2019-01-02 12:00:00
             * exchange_code : huobi
             * base_coin : BTC
             * quote_coin : ETH
             */

            public String amount;
            public String create_at;
            public String exchange_code;
            public String base_coin;
            public String quote_coin;

    }
}
