package com.basics.utils;

/**
 * @Description 切换索引
 * @Author hyk
 * @Date 2019/1/25 11:52
 **/
public class EsIndexChange {

    private static String suffix;

    public static void setSuffix(String suffix) {
        EsIndexChange.suffix = suffix;
    }

    public static String getSuffix() {
        return suffix;
    }

}
