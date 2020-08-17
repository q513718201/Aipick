package com.hazz.aipick.mvp.model.bean;

/**
 * market.btcusdt{***}.bbo
 */
public class BuyOrSell {

    /**
     * ch : market.btcusdt.bbo
     * ts : 1586509032146
     * tick : {"seqId":106094996670,"ask":6919.66,"askSize":0.002329,"bid":6919.35,"bidSize":1.636175,"quoteTime":1586509032145,"symbol":"btcusdt"}
     */

    public String ch;
    public long ts;
    public TickBean tick;

    public static class TickBean {
        /**
         * seqId : 106094996670
         * ask : 6919.66
         * askSize : 0.002329
         * bid : 6919.35
         * bidSize : 1.636175
         * quoteTime : 1586509032145
         * symbol : btcusdt
         */

        public long seqId;
        public String ask;
        public String askSize;
        public String bid;
        public String bidSize;
        public long quoteTime;
        public String symbol;

        @Override
        public String toString() {
            return "TickBean{" +
                    "seqId=" + seqId +
                    ", ask=" + ask +
                    ", askSize=" + askSize +
                    ", bid=" + bid +
                    ", bidSize=" + bidSize +
                    ", quoteTime=" + quoteTime +
                    ", symbol='" + symbol + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "BorSell{" +
                "ch='" + ch + '\'' +
                ", ts=" + ts +
                ", tick=" + tick +
                '}';
    }
}
