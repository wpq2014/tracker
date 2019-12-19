package com.wpq.tracker;

/**
 * @author wpq
 * @version 1.0
 */
public class StringUtil {

    private StringUtil() {
        throw new AssertionError("cannot be instantiated");
    }

    static boolean isNullOrEmpty(String string) {
        return string == null || string.trim().equals("null") || string.trim().length() == 0;
    }

}
