package com.hazz.aipick.mvp.model.bean;

public class ResultInfo {

    /**
     * alipay_trade_app_pay_response : {"code":"10000","msg":"Success","app_id":"2021000199651519","auth_app_id":"2021000199651519","charset":"utf-8","timestamp":"2020-01-15 15:48:06","out_trade_no":"057b509b-1336-427a-ac92-13cd6a23f40d","total_amount":"0.01","trade_no":"2020011522001426661410000797","seller_id":"2088731085581402"}
     * sign : HbO1Gfrf4YlLKya8fznjCG66U55kYNYvfjx90os5EE/wzty9oBPqQyNjjoQttlK47JDWwBH1Yu13TWPOSrAqiRcCpcg9WDQkTZOK1x3tVlSNITR3r95miZtZU4QtrXa544heAjr8q/qeOQxQX26m3ANXtGJofWtfuYgERUk0+KJKzz3xwtnabwkWXlBhCSSBt9xSBf2m+TsxFI/x/yZg2/d/6ja60C9mOxpuskiue0PzhhgB1QCi+bJVh37ujqnkJT+EEBjpCmj1lYDUSQzg9lgg3uqhUqgy5GlDaIeho2lZ7DBTQ+2KNzFqs6wAAkB0FelyKOSI9POhsXXXgzxE/A==
     * sign_type : RSA2
     */

    public AlipayTradeAppPayResponseBean alipay_trade_app_pay_response;
    public String sign;
    public String sign_type;

    public static class AlipayTradeAppPayResponseBean {
        /**
         * code : 10000
         * msg : Success
         * app_id : 2021000199651519
         * auth_app_id : 2021000199651519
         * charset : utf-8
         * timestamp : 2020-01-15 15:48:06
         * out_trade_no : 057b509b-1336-427a-ac92-13cd6a23f40d
         * total_amount : 0.01
         * trade_no : 2020011522001426661410000797
         * seller_id : 2088731085581402
         */

        public String code;
        public String msg;
        public String app_id;
        public String auth_app_id;
        public String charset;
        public String timestamp;
        public String out_trade_no;
        public String total_amount;
        public String trade_no;
        public String seller_id;

        @Override
        public String toString() {
            return "AlipayTradeAppPayResponseBean{" +
                    "code='" + code + '\'' +
                    ", msg='" + msg + '\'' +
                    ", app_id='" + app_id + '\'' +
                    ", auth_app_id='" + auth_app_id + '\'' +
                    ", charset='" + charset + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    ", out_trade_no='" + out_trade_no + '\'' +
                    ", total_amount='" + total_amount + '\'' +
                    ", trade_no='" + trade_no + '\'' +
                    ", seller_id='" + seller_id + '\'' +
                    '}';
        }
    }
}
