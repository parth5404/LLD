package models;

import Stratergy.Obstacle;

import java.util.List;

public class Board {
    private int n;
    Cell [][] cell;

    Board(int n){
        this.n=n;
    }
    public int getSize(){
        return n;
    }
    public void InitializeCell(int n){
        int pos=0;
        boolean ltor=true;
        for (int i=0;i<n;i++){
            if(ltor) {
                for (int j = 0; j < n; j++) {
                    Cell cellobj = new Cell(pos++);
                    cell[i][j] = cellobj;
                }
            }
            else{
                for(int j=n-1;j>=0;j--){
                    Cell cellobj = new Cell(pos++);
                    cell[i][j] = cellobj;
                }
            }
            ltor=!ltor;
        }
    }
    private int getR(int pos){
        return pos/n;
    }
    private int getC(int pos){
        if(getR(pos)%2==0){
            return pos%n;
        }
        return n-pos%n-1;
    }

    private Cell getCell(int pos){
        return cell[getR(pos)][getC(pos)];
    }
    public Boolean addObstacle(Obstacle obstacle){
            Cell srccell=getCell(obstacle.getSrc());
            Cell destcell=getCell(obstacle.getDest());
            if(srccell.haveObstacle()|| destcell.haveObstacle()){
                return false;
            }
            srccell.setObs(obstacle);
            return true;
    }
    public void getNewPosition(Player player, int offset){
        Cell newPos=getCell(player.getPosition()+offset);
        int finalpos= newPos.getFinalpos();
        if(finalpos<player.getPosition()+offset){
            System.out.println("Snake Fucked");
        } else if (finalpos>player.getPosition()+offset) {
            System.out.println("Ladder on");
        }
        else{
            System.out.println("Moved");
        }
        player.setpos(finalpos);
    }

}
