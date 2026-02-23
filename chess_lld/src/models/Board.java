package models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Board {
    private int numR;
    private int numC;
    private Cell [][]cells;

//    public void genrate(){
//        int i=0;
//        while(i<=1){
//            for(int j=0;j<numR;j++){
//                cells[i][j]=new Cell(i,j,new )
//            }
//        }
//    }
}
