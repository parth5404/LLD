package models;

import java.util.List;

public class Group {
    private int id;
    private String name;
    private List<Users> users;
    private BalanceMap bm;

    // can use nobserver notifier when expense is recorded or setteled
    public void addExpense(Users from, List<Users> to){
        
    }

}
