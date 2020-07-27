package com.vinsonguo.klinelib.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StringUtils {


    public static String amountToStringByTenThousand(float amount) {
        if (amount > 10000) {
            BigDecimal bigDecimal = new BigDecimal(amount / 10000);
            return bigDecimal.setScale(2, RoundingMode.FLOOR).toPlainString() + "万";
        }
        return String.valueOf(amount);
    }

    public static String amountToStringByThousand(float amount) {
        if (amount > 1000) {
            BigDecimal bigDecimal = new BigDecimal(amount / 1000);
            return bigDecimal.setScale(2, RoundingMode.FLOOR).toPlainString() + "K";
        }
        return String.valueOf(amount);
    }
}
