package models;

import java.util.List;

import enums.SplitType;

public class Expense {
    private int id;
    private Users paidBY;
    private SplitType splitType;
    private int amount1;
    private int amount2;
    private List<Split> to;

    

}
