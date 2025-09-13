package models;

import stratergy.Obstacle;

import java.util.LinkedList;
import java.util.Queue;

public class Board {
    Cell[][] cell;
    int size;

    Board(int size) {
        this.size = size;
        this.cell = new Cell[size][size];
        initializeCell(size);
    }
    public void initializeCell(int n) {
        int pos = 0;
        boolean ltor = true;
        for (int i = 0; i < n; i++) {
            if (ltor) {
                for (int j = 0; j < n; j++) {
                    Cell cellobj = new Cell(pos++);
                    cell[i][j] = cellobj;
                }
            } else {
                for (int j = n - 1; j >= 0; j--) {
                    Cell cellobj = new Cell(pos++);
                    cell[i][j] = cellobj;
                }
            }
            ltor = !ltor;
        }
    }


    public int getRow(int pos) {
        return pos / size;
    }

    public int getCol(int pos) {
        if (getRow(pos) % 2 == 0)
            return pos % size;
        return size - 1 - pos % size;
    }

    public boolean addObstacle(Obstacle obstacle){
        int src=obstacle.getSrc();
        int dest= obstacle.getDest();

        Cell srcCell=cell[getRow(src)][getCol(src)];
        Cell destCell=cell[getRow(dest)][getCol(dest)];
        if(srcCell.isHasobstacle()|| destCell.isHasobstacle()){
            return false;
        }
        srcCell.setHasobstacle(obstacle);
        return true;
    }

    public void playerpos(Players p, int diceval) {
        int pos = p.getPosition();
        int nextpos = p.getPosition() + diceval;
        int finalpos = nextpos;
        if (cell[getRow(nextpos)][getCol(nextpos)].isHasobstacle()) {
            finalpos += cell[getRow(nextpos)][getCol(nextpos)].getHasobstacle().getDest();
        }
//        if(finalpos>size){
//            System.out.println("Invalid Move");
//            return ;
//        }
        if(nextpos<finalpos){
            System.out.println("Sedhi to "+ finalpos);
        }
        else if(nextpos==finalpos){
            System.out.println(p.getName()+ " Just Moved "+ finalpos);
        }
        else{
            System.out.println("Saap");
        }
        p.setPosition(finalpos);
    }

}
