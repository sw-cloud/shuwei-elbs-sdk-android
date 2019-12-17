package com.shuwei.elbssdk.demo.utils;

/**
 * 应用所需要的字符串工具类
 */
public class StringUtils {

    public static String checkNotNull(String string) {
        if (string == null) {
            return "";
        }
        return string;
    }

    /**
     * 多个字符串合并成一个字符串
     *
     * @param strings 多个字符串
     * @return 一个字符串
     * @author Jeremy
     * @date 10:37
     */
    public static String join(String... strings) {
        StringBuilder builder = new StringBuilder();
        for (String string : strings) {
            builder.append(string);
        }
        return builder.toString();
    }

}
