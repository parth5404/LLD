package stratergy;

import factory.ObstacleFactory;

public abstract class Obstacle {
    int src;
    int dest;
    Obstacle(int src,int dest){
        this.src=src;
        this.dest=dest;
    }
    public int getDest(){
        return dest;
    }

    public int getSrc() {
        return src;
    }
}
