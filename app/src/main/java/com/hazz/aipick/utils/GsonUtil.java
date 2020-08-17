package com.hazz.aipick.utils;

import com.google.gson.Gson;

public class GsonUtil {
    public static <T> T toBean(String json, Class<T> clazz) {
        return new Gson().fromJson(json, clazz);
    }

    public static String toJson(Object obj) {
        return new Gson().toJson(obj);
    }
}
