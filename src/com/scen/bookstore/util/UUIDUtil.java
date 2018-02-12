package com.scen.bookstore.util;

import java.util.UUID;

/**
 * @author Scen
 * @date 2017/10/27
 */
public class UUIDUtil {
    public static String getUUID() {
        //得到随机字符串
        return UUID.randomUUID().toString();
    }
}
