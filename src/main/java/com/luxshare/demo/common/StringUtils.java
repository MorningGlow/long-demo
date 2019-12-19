package com.luxshare.demo.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 通用的 String 操作
 *
 * @author lion hua
 * @since 2019-12-04
 */
public class StringUtils {

    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

    private static final String ENGLISH_COMMA = ",";

    /**
     * 将英文逗号的字符串分隔拆成数组
     *
     * @param str 字符串
     * @return String[]
     */
    public static String[] commaDelimitedListToStringArray(String str) {
        return commaDelimitedToList(str, ENGLISH_COMMA).toArray(new String[0]);
    }

    /**
     * 将英文逗号的字符串分隔拆成字符串集合
     *
     * @param str 字符串
     * @return List<String>
     */
    public static List<String> commaDelimitedToList(String str) {
        return commaDelimitedToList(str, ENGLISH_COMMA);
    }


    /**
     * 由给定的分隔符对所给字符串进行分隔
     *
     * @param str       字符串非空,非空字符串
     * @param delimiter 分隔符不能为 null
     * @return 返回一个集合
     */
    public static List<String> commaDelimitedToList(String str, String delimiter) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(str) || Objects.isNull(delimiter)) {
            // 直接返回一个空字符串集合
            return new ArrayList<>();
        }
        return Stream.of(str.split(delimiter)).collect(Collectors.toList());
    }
}
