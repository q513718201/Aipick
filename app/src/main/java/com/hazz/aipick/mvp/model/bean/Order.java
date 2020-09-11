package com.hazz.aipick.mvp.model.bean;

import java.util.List;

public class Order {


    /**
     * list : [{"base_coin":"BTC","buy_fee":"0.00000200","buy_price":"1.00000000","create_at":"2020-08-26 16:01:09","gain":"11.35362000","loss":0,"order_id":"518524944951296000","order_num":"0.00100000","quote_coin":"USDT","sell_fee":"0.00000000","sell_price":"11354.62000000","sell_time":"2020-08-27 13:29:36"},{"base_coin":"BTC","buy_fee":"0.00177840","buy_price":"11467.59000000","create_at":"2020-08-26 22:17:59","gain":0,"loss":"100.45292400","order_id":"518619779997646848","order_num":"0.88920000","quote_coin":"USDT","sell_fee":"0.00000000","sell_price":"11354.62000000","sell_time":"2020-08-27 13:15:31"},{"base_coin":"BTC","buy_fee":"0.00020000","buy_price":"11339.33000000","create_at":"2020-08-26 21:09:29","gain":"1.52900000","loss":0,"order_id":"518602542746189824","order_num":"0.10000000","quote_coin":"USDT","sell_fee":"0.00000000","sell_price":"11354.62000000","sell_time":"2020-08-27 13:15:31"}]
     * now_num : 1
     * total_amount : 3.87940000
     * total_gain : 12330122.93396402
     * total_times : 9.00000000
     */

    private int now_num;
    private String total_amount;
    private String total_gain;
    private String total_times;
    private List<ListBean> list;

    public int getNow_num() {
        return now_num;
    }

    public void setNow_num(int now_num) {
        this.now_num = now_num;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getTotal_gain() {
        return total_gain;
    }

    public void setTotal_gain(String total_gain) {
        this.total_gain = total_gain;
    }

    public String getTotal_times() {
        return total_times;
    }

    public void setTotal_times(String total_times) {
        this.total_times = total_times;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * base_coin : BTC
         * buy_fee : 0.00000200
         * buy_price : 1.00000000
         * create_at : 2020-08-26 16:01:09
         * gain : 11.35362000
         * loss : 0
         * order_id : 518524944951296000
         * order_num : 0.00100000
         * quote_coin : USDT
         * sell_fee : 0.00000000
         * sell_price : 11354.62000000
         * sell_time : 2020-08-27 13:29:36
         */

        private String base_coin;
        private String buy_fee;
        private String buy_price;
        private String create_at;
        private String gain;
        private String loss;

        public String getGain_loss() {
            return gain_loss;
        }

        public void setGain_loss(String gain_loss) {
            this.gain_loss = gain_loss;
        }

        private String gain_loss;
        private String order_id;
        private String order_num;
        private String quote_coin;
        private String sell_fee = "";
        private String sell_price = "";
        private String sell_time;
        public boolean isShowDetail;

        public String getBase_coin() {
            return base_coin;
        }

        public void setBase_coin(String base_coin) {
            this.base_coin = base_coin;
        }

        public String getBuy_fee() {
            return buy_fee;
        }

        public void setBuy_fee(String buy_fee) {
            this.buy_fee = buy_fee;
        }

        public String getBuy_price() {
            return buy_price;
        }

        public void setBuy_price(String buy_price) {
            this.buy_price = buy_price;
        }

        public String getCreate_at() {
            return create_at;
        }

        public void setCreate_at(String create_at) {
            this.create_at = create_at;
        }

        public String getGain() {
            return gain;
        }

        public void setGain(String gain) {
            this.gain = gain;
        }

        public String getLoss() {
            return loss;
        }

        public void setLoss(String loss) {
            this.loss = loss;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getOrder_num() {
            return order_num;
        }

        public void setOrder_num(String order_num) {
            this.order_num = order_num;
        }

        public String getQuote_coin() {
            return quote_coin;
        }

        public void setQuote_coin(String quote_coin) {
            this.quote_coin = quote_coin;
        }

        public String getSell_fee() {
            return sell_fee;
        }

        public void setSell_fee(String sell_fee) {
            this.sell_fee = sell_fee;
        }

        public String getSell_price() {
            return sell_price;
        }

        public void setSell_price(String sell_price) {
            this.sell_price = sell_price;
        }

        public String getSell_time() {
            return sell_time;
        }

        public void setSell_time(String sell_time) {
            this.sell_time = sell_time;
        }
    }
}
