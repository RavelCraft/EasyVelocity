package com.github.imdabigboss.easyvelocity.utils;

import java.text.SimpleDateFormat;

public class Utils {
    public static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        return (int)(Math.random() * ((max - min) + 1)) + min;
    }

    public static String epochToStringDate(long epoch) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(epoch)) + " UTC";
    }
}
