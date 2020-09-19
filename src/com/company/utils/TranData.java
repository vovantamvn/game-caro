package com.company.utils;

import java.awt.*;

public class TranData {

    public static Point convertToPoint(String message){
        String[] items = message.split("-");

        try {
            return new Point(
                    Integer.parseInt(items[0]),
                    Integer.parseInt(items[1])
            );
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static String convertToString(int x, int y) {
        return x + "-" + y;
    }
}
