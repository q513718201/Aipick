package com.hazz.aipick.mvp.model.bean;

public class PayResultMine {


    /**
     * alipay : {"order_string":"dsdsdsdsd"}
     */

    public AlipayBean alipay;

    public WXPayBean wxpay;

    public static class WXPayBean {

    }

    public static class AlipayBean {
        /**
         * order_string : dsdsdsdsd
         */

        public String order_string;
    }


}
