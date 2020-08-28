package com.hazz.aipick.mvp.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubscribeDesc {


    /**
     * subee_name : 被订阅者
     * start : 2019-02-02 12:00:00
     * end : 2019-03-02 12:00:00
     * pay_method : wechat
     * price : 12.00
     * is_valid : true
     * switch : on
     * gains : [{"amount":"10.00","create_at":"2019-01-02 12:00:00","exchange_code":"huobi","base_coin":"BTC","quote_coin":"ETH"},{"amount":"112.00","create_at":"2019-01-02 12:00:00","exchange_code":"huobi","base_coin":"BTC","quote_coin":"ETH"},{"amount":"1211.00","create_at":"2019-01-02 12:00:00","exchange_code":"huobi","base_coin":"BTC","quote_coin":"ETH"},{"amount":"9012.00","create_at":"2019-06-02 12:00:00","exchange_code":"huobi","base_coin":"LTC","quote_coin":"ETH"}]
     */

    private String subee_name;
    private String start;
    private String end;
    private String pay_method;
    private String price;
    private boolean is_valid;
    @SerializedName("switch")
    private String switchX;
    private List<GainsBean> gains;

    public String getSubee_name() {
        return subee_name;
    }

    public void setSubee_name(String subee_name) {
        this.subee_name = subee_name;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getPay_method() {
        return pay_method;
    }

    public void setPay_method(String pay_method) {
        this.pay_method = pay_method;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isIs_valid() {
        return is_valid;
    }

    public void setIs_valid(boolean is_valid) {
        this.is_valid = is_valid;
    }

    public String getSwitchX() {
        return switchX;
    }

    public void setSwitchX(String switchX) {
        this.switchX = switchX;
    }

    public List<GainsBean> getGains() {
        return gains;
    }

    public void setGains(List<GainsBean> gains) {
        this.gains = gains;
    }

    public static class GainsBean {
        /**
         * amount : 10.00
         * create_at : 2019-01-02 12:00:00
         * exchange_code : huobi
         * base_coin : BTC
         * quote_coin : ETH
         */

        private String amount;
        private String create_at;
        private String exchange_code;
        private String base_coin;
        private String quote_coin;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCreate_at() {
            return create_at;
        }

        public void setCreate_at(String create_at) {
            this.create_at = create_at;
        }

        public String getExchange_code() {
            return exchange_code;
        }

        public void setExchange_code(String exchange_code) {
            this.exchange_code = exchange_code;
        }

        public String getBase_coin() {
            return base_coin;
        }

        public void setBase_coin(String base_coin) {
            this.base_coin = base_coin;
        }

        public String getQuote_coin() {
            return quote_coin;
        }

        public void setQuote_coin(String quote_coin) {
            this.quote_coin = quote_coin;
        }
    }
}
