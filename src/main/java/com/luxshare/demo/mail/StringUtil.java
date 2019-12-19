package com.luxshare.demo.mail;

public class StringUtil {
    public static String transBlank(String value) {
        StringBuffer tempStr = new StringBuffer();
        for (int i = 0; i < value.length(); i++) {
            if (' ' == value.charAt(i)) {
                tempStr.append("\040");
            } else {
                tempStr.append(value.charAt(i));
            }
        }
        return tempStr.toString();
    }
}
