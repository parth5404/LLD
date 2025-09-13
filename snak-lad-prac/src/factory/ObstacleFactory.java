package factory;

import stratergy.Ladders;
import stratergy.Obstacle;
import stratergy.Snakes;

public  class ObstacleFactory {
    public static Obstacle create(String str,int src,int dest){
        if(str.equals("SNAKES")){
            return new Snakes(dest,src);
        }
        return new Ladders(src,dest);
    }
}
