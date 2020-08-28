package com.hazz.aipick.utils;

import com.blankj.utilcode.util.GsonUtils;

public class GsonUtil {
    public static <T> T toBean(String json, Class<T> clazz) {
        return GsonUtils.fromJson(json, clazz);
    }

    public static String toJson(Object obj) {
        return GsonUtils.toJson(obj);
    }
}
