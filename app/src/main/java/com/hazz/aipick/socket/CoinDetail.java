package com.hazz.aipick.socket;

public class CoinDetail {


    /**
     * ch : market.trxusdt.detail
     * ts : 1585732004194
     * tick : {"id":202558060950,"low":0.01135,"high":0.01176,"open":0.011505,"close":0.011467,"vol":3857328.576689698,"amount":3.344424982039344E8,"version":202558060950,"count":24414}
     */

    public String ch;
    public long ts;
    public TickBean tick;
    public boolean isUp;

    public static class TickBean {
        /**
         * id : 202558060950
         * low : 0.01135
         * high : 0.01176
         * open : 0.011505
         * close : 0.011467
         * vol : 3857328.576689698
         * amount : 3.344424982039344E8
         * version : 202558060950
         * count : 24414
         */

        public String id;
        public String low;
        public String high;
        public String open;
        public String close;
        public String vol;
        public String amount;
        public String version;
        public String count;

        @Override
        public String toString() {
            return "TickBean{" +
                    "id='" + id + '\'' +
                    ", low='" + low + '\'' +
                    ", high='" + high + '\'' +
                    ", open='" + open + '\'' +
                    ", close='" + close + '\'' +
                    ", vol='" + vol + '\'' +
                    ", amount='" + amount + '\'' +
                    ", version='" + version + '\'' +
                    ", count='" + count + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CoinDetail{" +
                "ch='" + ch + '\'' +
                ", ts=" + ts +
                ", tick=" + tick +
                '}';
    }
}
