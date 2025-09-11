package Stratergy;
import enums.obstacleType;
import models.Cell;

public abstract class Obstacle {
    public int src;
    public int dest;
    public obstacleType type;
    public int n;

    public Obstacle(int src,int dest){
         this.src=src;
         this.dest=dest;
     }

    public obstacleType getType(){
        return type;
    }

    public void createObstacle(int type,int up,int down) {

    }
    public int getSrc(){
        return  src;
    }
    public int getDest(){
        return dest;
    }
    public int movePlayer(){
        return dest;
    }

}
