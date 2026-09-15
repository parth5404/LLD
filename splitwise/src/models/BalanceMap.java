package models;

import java.util.List;
import java.util.Optional;

public class BalanceMap {
    public int[][] getBm() {
        return bm;
    }
    private int[][] bm;

    /// a owes b means
    /// [a][b]=amt also a clears split let's say by x bm[a][b]-=x with guardrails
    /// for checking


    public BalanceMap(){
        this.bm = new int[100][100];
    }
    public void addExpense(Users from, List<Split> splits) {
        for(int i=0;i<splits.size();i++){
            if(splits.get(i).getUser().getId()==from.getId())continue;
            bm[splits.get(i).getUser().getId()][from.getId()]+=splits.get(i).amt();
        }
    }
}
