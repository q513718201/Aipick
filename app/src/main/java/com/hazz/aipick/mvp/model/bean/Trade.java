package com.hazz.aipick.mvp.model.bean;

import java.util.List;

public class Trade {


    /**
     * ch : market.btcusdt.trade.detail
     * ts : 1586954826286
     * tick : {"id":106262931823,"ts":1586954826278,"data":[{"id":"10626293182380908996560","ts":1586954826278,"tradeId":102113943502,"amount":0.039589,"price":6689.93,"direction":"buy"}]}
     */

    public String ch;
    public long ts;
    public TickBean tick;

    public static class TickBean {
        /**
         * id : 106262931823
         * ts : 1586954826278
         * data : [{"id":"10626293182380908996560","ts":1586954826278,"tradeId":102113943502,"amount":0.039589,"price":6689.93,"direction":"buy"}]
         */

        public long id;
        public long ts;
        public List<DataBean> data;

        public static class DataBean {
            /**
             * id : 10626293182380908996560
             * ts : 1586954826278
             * tradeId : 102113943502
             * amount : 0.039589
             * price : 6689.93
             * direction : buy
             */

            public String id;
            public long ts;
            public long tradeId;
            public String amount;
            public String price;
            public String direction;

            @Override
            public String toString() {
                return "DataBean{" +
                        "id='" + id + '\'' +
                        ", ts=" + ts +
                        ", tradeId=" + tradeId +
                        ", amount=" + amount +
                        ", price=" + price +
                        ", direction='" + direction + '\'' +
                        '}';
            }


        }

        @Override
        public String toString() {
            return "TickBean{" +
                    "id=" + id +
                    ", ts=" + ts +
                    ", data=" + data +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Trade{" +
                "ch='" + ch + '\'' +
                ", ts=" + ts +
                ", tick=" + tick +
                '}';
    }
}
