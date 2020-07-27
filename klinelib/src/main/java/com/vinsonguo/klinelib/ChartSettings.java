package com.vinsonguo.klinelib;

public class ChartSettings {

    private static String language;

    public static void setLanguage(String lang) {
        language = lang;
    }

    public static String getLanguage() {
        return language;
    }

    public static boolean isChinese() {
        return language.equals("chinese");
    }
}
