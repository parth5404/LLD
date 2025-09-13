package models;

import stratergy.Obstacle;

public class Cell {
    int position;
    Obstacle hasobstacle;

    Cell(int position){
        this.position=position;
    }
    public Obstacle getHasobstacle(){
        return hasobstacle;
    }

    public boolean isHasobstacle() {
        return hasobstacle!=null;
    }

    public void setHasobstacle(Obstacle hasobstacle) {
        this.hasobstacle = hasobstacle;
    }
}
