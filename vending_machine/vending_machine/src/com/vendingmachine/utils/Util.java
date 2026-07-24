package com.vendingmachine.utils;

public class Util {
    public int getRowFromStr(String s) {
        if (s == null || s.isEmpty())
            return -1;
        return Character.toUpperCase(s.charAt(0)) - 'A';
    }

    public int getColFromStr(String s) {
        if (s == null || s.length() < 2)
            return -1;
        return s.charAt(1) - '0';
    }
}
