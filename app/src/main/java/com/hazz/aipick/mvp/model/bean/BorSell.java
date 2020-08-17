package com.hazz.aipick.mvp.model.bean;

import java.util.List;

/**
 *
 */
public class BorSell {

    /**
     * ch : market.htusdt.depth.step0
     * ts : 1572362902027
     * tick : {"bids":[[3.7721,344.86],[3.7709,46.66]],"asks":[[3.7745,15.44],[3.7746,70.52]],"version":100434317651,"ts":1572362902012}
     */

    public String ch;
    public long ts;
    public TickBean tick;

    public static class TickBean {
        /**
         * bids : [[3.7721,344.86],[3.7709,46.66]]
         * asks : [[3.7745,15.44],[3.7746,70.52]]
         * version : 100434317651
         * ts : 1572362902012
         */

        public long version;
        public long ts;
        public List<List<Double>> bids;
        public List<List<Double>> asks;
    }
}
