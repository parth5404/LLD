package com.vendingmachine.model;

public enum Denomination {
    ONE_DOLLAR(1.0),
    TWO_DOLLARS(2.0),
    FIVE_DOLLARS(5.0),
    TEN_DOLLARS(10.0),
    TWENTY_DOLLARS(20.0),
    FIFTY_DOLLARS(50.0);

    private final double value;

    Denomination(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
    
    public static Denomination fromValue(double val) {
        for (Denomination d : values()) {
            if (d.getValue() == val) {
                return d;
            }
        }
        return null;
    }
}
