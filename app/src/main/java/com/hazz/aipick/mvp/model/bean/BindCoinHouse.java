package com.hazz.aipick.mvp.model.bean;

import java.io.Serializable;
import java.util.List;

public class BindCoinHouse implements Serializable {


                public  List<SymbolsBean> symbols;
                public  List<ExchangesBean> exchanges;

                public static class SymbolsBean  implements Serializable{
                        /**
                         * id : 1
                         * owner_id : 1
                         * base_coin : BTC
                         * quote_coin : ETH
                         */

                        public String id;
                        public String owner_id;
                        public String base_coin;
                        public String quote_coin;
                }

                public static class ExchangesBean  implements Serializable{
                        /**
                         * exchange_id : 1
                         * exchange_code : huobi
                         */

                        public String exchange_id;
                        public String exchange_code;
                }

}
