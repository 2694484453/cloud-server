package com.ruoyi.common.utils;

/**
 * 用于格式化
 * @author gaopuguang_zz
 * @version 1.0
 * @description: TODO
 * @date 2024/8/20 9:19
 */
public class FormatterUtil {

    /**
     * 格式化cmd
     *
     * @param command cmd
     */
    public static String formatter(String... command) {
        StringBuilder stringBuilder = new StringBuilder();
        System.out.println("############################格式化命令##########################");
        for (String string : command) {
            stringBuilder.append(string).append(" ");
        }
        System.out.println(stringBuilder);
        return stringBuilder.toString();
    }
}
